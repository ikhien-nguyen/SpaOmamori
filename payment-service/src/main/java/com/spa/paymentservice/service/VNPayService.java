package com.spa.paymentservice.service;

import com.spa.paymentservice.config.VNPayConfig;
import com.spa.paymentservice.entity.Invoice;
import com.spa.paymentservice.entity.InvoiceStatus;
import com.spa.paymentservice.entity.PaymentMethod;
import com.spa.paymentservice.exception.AppException;
import com.spa.paymentservice.exception.ErrorCode;
import com.spa.paymentservice.messaging.InvoicePaidPublisher;
import com.spa.paymentservice.repository.InvoiceRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Toan bo logic ky/xac thuc chu ky VNPay dat rieng trong service nay, tach
 * khoi InvoiceService de InvoiceService khong phai biet chi tiet ky thuat
 * cua tung cong thanh toan (sau nay them MoMo/ZaloPay chi can them 1 Service
 * tuong tu, khong dung cham vao InvoiceService).
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VNPayService {

    VNPayConfig vnPayConfig;
    InvoiceRepository invoiceRepository;
    InvoicePaidPublisher invoicePaidPublisher;

    // UC_11 (mo rong - khach hang tu thanh toan online): sinh URL thanh toan
    // VNPay cho 1 hoa don dang "Cho thanh toan". Khach duoc redirect toi day,
    // VNPay tu hien giao dien quet QR / nhap the.
    public VNPayUrlResult createPaymentUrl(Invoice invoice, HttpServletRequest httpRequest) {
        if (invoice.getStatus() != InvoiceStatus.PENDING_PAYMENT) {
            throw new AppException(ErrorCode.VNPAY_INVOICE_NOT_PENDING);
        }

        String txnRef = generateTxnRef();
        invoice.setVnpTxnRef(txnRef);
        invoiceRepository.save(invoice);

        // VNPay quy dinh so tien la so nguyen, don vi = VND * 100 (khong co
        // phan thap phan) - xem tai lieu tich hop VNPay.
        long amount = invoice.getTotalAmount()
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", "Thanh toan hoa don " + invoice.getId());
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        params.put("vnp_IpAddr", extractClientIp(httpRequest));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        params.put("vnp_CreateDate", formatter.format(calendar.getTime()));
        calendar.add(Calendar.MINUTE, 15); // het han sau 15p neu khach khong thanh toan
        params.put("vnp_ExpireDate", formatter.format(calendar.getTime()));

        String hashData = buildHashData(params);
        String secureHash = hmacSHA512(vnPayConfig.getHashSecret(), hashData);
        String paymentUrl = vnPayConfig.getPayUrl() + "?" + hashData + "&vnp_SecureHash=" + secureHash;

        return new VNPayUrlResult(paymentUrl, txnRef);
    }

    // Xu ly IPN (server-to-server) VNPay goi ve sau khi khach thanh toan
    // xong - day la nguon xac nhan DUY NHAT duoc tin tuong, KHONG dua vao
    // vnp_ReturnUrl (trinh duyet khach co the bi tat/mat mang truoc khi ve
    // toi return-url). Tra ve dung dinh dang {RspCode, Message} VNPay yeu
    // cau, KHONG boc trong ApiResponse chuan cua he thong.
    public Map<String, String> processIpn(Map<String, String> receivedParams) {
        Map<String, String> fields = new HashMap<>(receivedParams);
        String receivedHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String computedHash = hmacSHA512(vnPayConfig.getHashSecret(), buildHashData(new TreeMap<>(fields)));
        if (receivedHash == null || !computedHash.equalsIgnoreCase(receivedHash)) {
            log.warn("VNPay IPN chu ky khong hop le, txnRef={}", fields.get("vnp_TxnRef"));
            return ipnResponse("97", "Invalid Signature");
        }

        String txnRef = fields.get("vnp_TxnRef");
        Invoice invoice = invoiceRepository.findByVnpTxnRef(txnRef).orElse(null);
        if (invoice == null) {
            return ipnResponse("01", "Order not found");
        }

        long expectedAmount = invoice.getTotalAmount()
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();
        long receivedAmount = Long.parseLong(fields.getOrDefault("vnp_Amount", "0"));
        if (expectedAmount != receivedAmount) {
            log.warn("VNPay IPN sai so tien, txnRef={}, expected={}, received={}", txnRef, expectedAmount, receivedAmount);
            return ipnResponse("04", "Invalid amount");
        }

        // VNPay co the goi lai IPN nhieu lan (retry) cho cung 1 giao dich -
        // neu da xu ly PAID roi thi bao "da xac nhan", KHONG cong don/xu ly
        // lai lan 2 (tranh tru kho 2 lan sau nay khi noi voi Cosmetic Service).
        if (invoice.getStatus() != InvoiceStatus.PENDING_PAYMENT) {
            return ipnResponse("02", "Order already confirmed");
        }

        String responseCode = fields.get("vnp_ResponseCode");
        String transactionStatus = fields.get("vnp_TransactionStatus");
        if ("00".equals(responseCode) && "00".equals(transactionStatus)) {
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
            invoice.setPaidAt(LocalDateTime.now());
            invoice.setVnpTransactionNo(fields.get("vnp_TransactionNo"));
            invoiceRepository.save(invoice);

            invoicePaidPublisher.publish(invoice);
        } else {
            log.info("VNPay bao thanh toan khong thanh cong, txnRef={}, responseCode={}", txnRef, responseCode);
        }

        return ipnResponse("00", "Confirm Success");
    }

    private Map<String, String> ipnResponse(String rspCode, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("RspCode", rspCode);
        response.put("Message", message);
        return response;
    }

    // Dung TreeMap (sap xep theo key) o noi goi ham nay de dam bao thu tu
    // tham so luon nhat quan giua luc ky va luc VNPay ky lai de doi chieu.
    private String buildHashData(Map<String, String> sortedParams) {
        StringBuilder hashData = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = sortedParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                continue;
            }
            hashData.append(entry.getKey())
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));
            if (it.hasNext()) {
                hashData.append('&');
            }
        }
        return hashData.toString();
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            hmac512.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * bytes.length);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("Ky HMAC-SHA512 that bai: {}", e.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        // vnp_IpAddr yeu cau dang IPv4 - "0:0:0:0:0:0:0:1" (localhost IPv6)
        // se bi VNPay tu choi, tam quy ve 127.0.0.1 khi test local.
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    private String generateTxnRef() {
        return System.currentTimeMillis() + String.valueOf(new SecureRandom().nextInt(900) + 100);
    }

    public record VNPayUrlResult(String paymentUrl, String txnRef) {
    }
}

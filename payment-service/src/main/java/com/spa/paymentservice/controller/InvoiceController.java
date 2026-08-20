package com.spa.paymentservice.controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import com.spa.paymentservice.dto.request.ConfirmPaymentRequest;
import com.spa.paymentservice.dto.request.CreateInvoiceRequest;
import com.spa.paymentservice.dto.response.ApiResponse;
import com.spa.paymentservice.dto.response.InvoiceResponse;
import com.spa.paymentservice.dto.response.VnPayPaymentUrlResponse;
import com.spa.paymentservice.service.InvoiceService;
import com.spa.paymentservice.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvoiceController {

    InvoiceService invoiceService;
    VNPayService vnPayService; // MOI THEM

    // UC_11 - buoc 1-5: Admin lap hoa don (tu lich hen da hoan thanh va/hoac
    // ban le my pham).
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<InvoiceResponse> createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return ApiResponse.<InvoiceResponse>builder()
                .result(invoiceService.createInvoice(request))
                .build();
    }

    // UC_11 - buoc 6-7: Admin xac nhan thanh toan.
    @PatchMapping("/{id}/confirm-payment")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<InvoiceResponse> confirmPayment(
            @PathVariable String id, @Valid @RequestBody ConfirmPaymentRequest request) {
        return ApiResponse.<InvoiceResponse>builder()
                .result(invoiceService.confirmPayment(id, request))
                .build();
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<InvoiceResponse> cancelInvoice(@PathVariable String id) {
        return ApiResponse.<InvoiceResponse>builder()
                .result(invoiceService.cancelInvoice(id))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<InvoiceResponse> getInvoice(@PathVariable String id) {
        return ApiResponse.<InvoiceResponse>builder()
                .result(invoiceService.getInvoiceById(id))
                .build();
    }

    // Use case khach hang tra cuu lich su hoa don/giao dich cua chinh minh.
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<List<InvoiceResponse>> getByCustomer(@PathVariable String customerId) {
        return ApiResponse.<List<InvoiceResponse>>builder()
                .result(invoiceService.getInvoicesByCustomer(customerId))
                .build();
    }
// Customer xem danh sách hóa đơn/giao dịch của chính mình.
// customerId lấy từ JWT subject, không nhận từ client.
@GetMapping("/me")
@PreAuthorize("hasAuthority('CUSTOMER')")
public ApiResponse<List<InvoiceResponse>> getMyInvoices(
        @AuthenticationPrincipal Jwt jwt) {

    String customerId = jwt.getSubject();

    return ApiResponse.<List<InvoiceResponse>>builder()
            .result(invoiceService.getInvoicesByCustomer(customerId))
            .build();
}

// Customer chỉ được xem chi tiết hóa đơn thuộc về chính mình.
@GetMapping("/me/{id}")
@PreAuthorize("hasAuthority('CUSTOMER')")
public ApiResponse<InvoiceResponse> getMyInvoice(
        @PathVariable String id,
        @AuthenticationPrincipal Jwt jwt) {

    String customerId = jwt.getSubject();

    return ApiResponse.<InvoiceResponse>builder()
            .result(invoiceService.getInvoiceForCustomer(id, customerId))
            .build();
}

    // Khach hang bam "Thanh toan online" tren hoa don -> BE tra ve URL VNPay,
    // FE redirect trinh duyet/webview toi day de khach quet QR hoac nhap the.
    @PostMapping("/{id}/vnpay/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<VnPayPaymentUrlResponse> createVnPayPaymentUrl(
            @PathVariable String id, HttpServletRequest httpRequest) {
        return ApiResponse.<VnPayPaymentUrlResponse>builder()
                .result(invoiceService.createVnPayPaymentUrl(id, httpRequest))
                .build();
    }

    // VNPay goi server-to-server ve day sau khi khach thanh toan xong (IPN =
    // Instant Payment Notification) - day la nguon xac nhan CHINH THUC duy
    // nhat, khong phai vnpay/return ben duoi. Endpoint nay PHAI dat duoi
    // "/internal/**" ve mat y nghia (khong danh cho FE goi) nhung VNPay goi
    // truc tiep tu server ho nen khong the dat sau Gateway - can mo firewall/
    // whitelist IP VNPay that ky khi len production. Tra ve dung format
    // {"RspCode":..,"Message":..} theo yeu cau cua VNPay, KHONG boc
    // ApiResponse chuan.
    @GetMapping("/vnpay/ipn")
    public ResponseEntity<Map<String, String>> vnpayIpn(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(vnPayService.processIpn(params));
    }

    // Trinh duyet khach hang duoc VNPay redirect ve day sau khi thanh toan -
    // CHI de hien thi ket qua cho khach xem, KHONG dung de cap nhat trang
    // thai hoa don (vi khach co the dong tab truoc khi ve toi day, hoac gia
    // mao request truc tiep vao URL nay). Trang thai that su luon lay tu
    // vnpay/ipn ben tren.
    @GetMapping("/vnpay/return")
    public ApiResponse<String> vnpayReturn(@RequestParam Map<String, String> params) {
        String responseCode = params.get("vnp_ResponseCode");
        String message = "00".equals(responseCode)
                ? "Thanh toán thành công, hệ thống đang xác nhận."
                : "Thanh toán không thành công hoặc đã bị hủy.";
        return ApiResponse.<String>builder().message(message).build();
    }
}

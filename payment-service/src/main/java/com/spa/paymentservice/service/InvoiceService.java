package com.spa.paymentservice.service;

import com.spa.paymentservice.client.AppointmentClient;
import com.spa.paymentservice.client.CosmeticClient;
import com.spa.paymentservice.dto.request.ConfirmPaymentRequest;
import com.spa.paymentservice.dto.request.CreateInvoiceRequest;
import com.spa.paymentservice.dto.request.InvoiceItemRequest;
import com.spa.paymentservice.dto.response.AppointmentResponse;
import com.spa.paymentservice.dto.response.CosmeticResponse;
import com.spa.paymentservice.dto.response.InvoiceResponse;
import com.spa.paymentservice.dto.response.VnPayPaymentUrlResponse;
import com.spa.paymentservice.entity.Invoice;
import com.spa.paymentservice.entity.InvoiceItem;
import com.spa.paymentservice.entity.InvoiceItemType;
import com.spa.paymentservice.entity.InvoiceStatus;
import com.spa.paymentservice.exception.AppException;
import com.spa.paymentservice.exception.ErrorCode;
import com.spa.paymentservice.mapper.InvoiceMapper;
import com.spa.paymentservice.messaging.InvoicePaidPublisher;
import com.spa.paymentservice.repository.InvoiceRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvoiceService {

    InvoiceRepository invoiceRepository;
    InvoiceMapper invoiceMapper;
    AppointmentClient appointmentClient;
    VNPayService vnPayService;
    InvoicePaidPublisher invoicePaidPublisher;
    CosmeticClient cosmeticClient;

    // UC_11 - luong chinh buoc 1-5: gom chi phi dich vu (tu lich hen) + chi
    // phi my pham (nhap tay/ke don), tinh tong tien, luu voi trang thai
    // "Cho thanh toan".
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        Invoice invoice = Invoice.builder().status(InvoiceStatus.PENDING_PAYMENT).build();

        if (!request.isRetailSale()) {
            attachAppointmentItems(invoice, request.getAppointmentId());
        } else {
            if (request.getCustomerId() == null || request.getCustomerId().isBlank()) {
                throw new AppException(ErrorCode.INVOICE_EMPTY);
            }
            invoice.setCustomerId(request.getCustomerId());
        }

        attachCosmeticItems(invoice, request.getCosmeticItems());

        if (invoice.getItems().isEmpty()) {
            throw new AppException(ErrorCode.INVOICE_EMPTY);
        }

        BigDecimal totalAmount = invoice.getItems().stream()
                .map(InvoiceItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        invoice.setTotalAmount(totalAmount);

        // existsByAppointmentId() o tren chi chan duoc phan lon truong hop;
        // van co the co race condition khi 2 request tao hoa don cung luc
        // cho 1 lich hen. Unique constraint o DB (appointment_id) la lop
        // bao ve cuoi cung - bat loi vi pham va tra ve dung ErrorCode cu.
        try {
            invoiceRepository.save(invoice);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.INVOICE_ALREADY_EXISTS_FOR_APPOINTMENT);
        }
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    // UC_11 - luong chinh buoc 6-7: xac nhan hinh thuc thanh toan, chuyen
    // trang thai hoa don thanh "Da thanh toan".
    public InvoiceResponse confirmPayment(String id, ConfirmPaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if (invoice.getStatus() != InvoiceStatus.PENDING_PAYMENT) {
            throw new AppException(ErrorCode.INVOICE_NOT_PENDING);
        }

        invoice.setPaymentMethod(request.getPaymentMethod());
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(LocalDateTime.now());
        invoiceRepository.save(invoice);

        invoicePaidPublisher.publish(invoice);

        return invoiceMapper.toInvoiceResponse(invoice);
    }

    // Cho phep Admin huy hoa don lap nham, chi khi con Cho thanh toan (chua
    // thu tien, chua tru kho).
    public InvoiceResponse cancelInvoice(String id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if (invoice.getStatus() != InvoiceStatus.PENDING_PAYMENT) {
            throw new AppException(ErrorCode.INVOICE_NOT_PENDING);
        }

        invoice.setStatus(InvoiceStatus.CANCELLED);
        invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    public InvoiceResponse getInvoiceById(String id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    // Khach hang tu thanh toan online: sinh URL VNPay (chua QR + cac hinh
    // thuc thanh toan khac) cho hoa don dang "Cho thanh toan".
    public VnPayPaymentUrlResponse createVnPayPaymentUrl(String invoiceId, HttpServletRequest httpRequest) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        var result = vnPayService.createPaymentUrl(invoice, httpRequest);
        return VnPayPaymentUrlResponse.builder()
                .paymentUrl(result.paymentUrl())
                .txnRef(result.txnRef())
                .build();
    }

    // Dung cho use case khach hang tra cuu lich su hoa don/giao dich cua
    // chinh minh.
    public List<InvoiceResponse> getInvoicesByCustomer(String customerId) {
        return invoiceRepository.findByCustomerId(customerId).stream()
                .map(invoiceMapper::toInvoiceResponse)
                .toList();
    }

    private void attachAppointmentItems(Invoice invoice, String appointmentId) {
        if (invoiceRepository.existsByAppointmentId(appointmentId)) {
            throw new AppException(ErrorCode.INVOICE_ALREADY_EXISTS_FOR_APPOINTMENT);
        }

        AppointmentResponse appointment = fetchAppointment(appointmentId);

        // Chi lap hoa don khi ca tri lieu da thuc su hoan thanh - tranh thu
        // tien cho dich vu chua lam xong.
        if (!"COMPLETED".equals(appointment.getStatus())) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_COMPLETED);
        }

        invoice.setAppointmentId(appointment.getId());
        invoice.setCustomerId(appointment.getCustomerId()); // lay tu appointment, khong tin client tu truyen

        invoice.addItem(InvoiceItem.builder()
                .itemType(InvoiceItemType.SERVICE)
                .referenceId(appointment.getServiceId())
                .itemName(appointment.getServiceName())
                .unitPrice(appointment.getServicePrice())
                .quantity(1)
                .subtotal(appointment.getServicePrice())
                .build());

        invoice.addItem(InvoiceItem.builder()
                .itemType(InvoiceItemType.ROOM)
                .referenceId(appointment.getRoomId())
                .itemName(appointment.getRoomName())
                .unitPrice(appointment.getRoomPrice())
                .quantity(1)
                .subtotal(appointment.getRoomPrice())
                .build());
    }

    private void attachCosmeticItems(Invoice invoice, List<InvoiceItemRequest> cosmeticItems) {
        if (cosmeticItems == null) {
            return;
        }

        for (InvoiceItemRequest item : cosmeticItems) {
            // cosmeticId bat buoc (dung ERD: MaMatHang khong duoc null) - luon
            // lay gia/ten that tu cosmetic-service, KHONG tin client tu goi
            // len (tranh gia mao gia). Da bo nhanh "nhap tay thu cong" truoc
            // day vi vi pham rang buoc NOT NULL cua ERD.
            if (item.getCosmeticId() == null || item.getCosmeticId().isBlank()) {
                throw new AppException(ErrorCode.COSMETIC_ID_REQUIRED);
            }

            CosmeticResponse cosmetic = fetchCosmetic(item.getCosmeticId());
            BigDecimal subtotal = cosmetic.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            invoice.addItem(InvoiceItem.builder()
                    .itemType(InvoiceItemType.COSMETIC)
                    .referenceId(item.getCosmeticId())
                    .itemName(cosmetic.getName())
                    .unitPrice(cosmetic.getPrice())
                    .quantity(item.getQuantity())
                    .subtotal(subtotal)
                    .build());
        }
    }

    private CosmeticResponse fetchCosmetic(String cosmeticId) {
        try {
            var response = cosmeticClient.getCosmetic(cosmeticId);
            if (response == null || response.getResult() == null) {
                throw new AppException(ErrorCode.COSMETIC_NOT_EXISTED);
            }
            return response.getResult();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Gọi cosmetic-service thất bại: {}", e.getMessage());
            throw new AppException(ErrorCode.COSMETIC_NOT_EXISTED);
        }
    }

    private AppointmentResponse fetchAppointment(String appointmentId) {
        try {
            var response = appointmentClient.getAppointment(appointmentId);
            if (response == null || response.getResult() == null) {
                throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
            }
            return response.getResult();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Gọi appointment-service thất bại: {}", e.getMessage());
            throw new AppException(ErrorCode.APPOINTMENT_NOT_FOUND);
        }
    }
}

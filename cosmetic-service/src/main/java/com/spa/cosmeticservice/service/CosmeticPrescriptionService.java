package com.spa.cosmeticservice.service;

import com.spa.cosmeticservice.dto.request.CosmeticPrescriptionRequest;
import com.spa.cosmeticservice.dto.response.CosmeticPrescriptionResponse;
import com.spa.cosmeticservice.entity.Cosmetic;
import com.spa.cosmeticservice.entity.CosmeticPrescription;
import com.spa.cosmeticservice.exception.AppException;
import com.spa.cosmeticservice.exception.ErrorCode;
import com.spa.cosmeticservice.mapper.CosmeticPrescriptionMapper;
import com.spa.cosmeticservice.repository.CosmeticPrescriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CosmeticPrescriptionService {

    CosmeticPrescriptionRepository prescriptionRepository;
    CosmeticPrescriptionMapper prescriptionMapper;
    CosmeticService cosmeticService;
    CosmeticInventoryService cosmeticInventoryService;

    public CosmeticPrescriptionResponse create(CosmeticPrescriptionRequest request) {
        Cosmetic cosmetic = cosmeticService.getCosmeticOrThrow(request.getCosmeticId());

        // Theo đúng use case: chỉ KIỂM TRA tồn kho khả dụng ở bước kê đơn, KHÔNG trừ
        // kho ngay — kho chỉ thực sự bị trừ khi Payment Service xác nhận thanh toán
        // (xem CosmeticInventoryService.deductStock, gọi từ InvoicePaidConsumer).
        if (!cosmeticInventoryService.hasEnoughStock(cosmetic.getId(), request.getQuantity())) {
            throw new AppException(ErrorCode.INVENTORY_NOT_ENOUGH);
        }

        CosmeticPrescription prescription = CosmeticPrescription.builder()
                .appointmentId(request.getAppointmentId())
                .technicianId(request.getTechnicianId())
                .cosmetic(cosmetic)
                .quantity(request.getQuantity())
                // Chốt giá tại thời điểm kê đơn (xem giải thích ở entity).
                .unitPrice(cosmetic.getPrice())
                .prescribedAt(LocalDateTime.now())
                .build();

        prescriptionRepository.save(prescription);
        return toResponseWithTotal(prescription);
    }

    // Admin dùng danh sách này để tổng hợp "tiền mỹ phẩm kê đơn" khi lập hóa đơn
    // dịch vụ (Use Case Lập hóa đơn, trường hợp 1).
    public List<CosmeticPrescriptionResponse> getByAppointmentId(String appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId).stream()
                .map(this::toResponseWithTotal)
                .toList();
    }

    private CosmeticPrescriptionResponse toResponseWithTotal(CosmeticPrescription prescription) {
        CosmeticPrescriptionResponse response = prescriptionMapper.toResponse(prescription);
        response.setTotalPrice(prescription.getUnitPrice().multiply(
                BigDecimal.valueOf(prescription.getQuantity())));
        return response;
    }
}

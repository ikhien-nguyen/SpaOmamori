package com.spa.cosmeticservice.service;

import com.spa.cosmeticservice.dto.request.CosmeticOrderRequest;
import com.spa.cosmeticservice.dto.response.CosmeticOrderResponse;
import com.spa.cosmeticservice.entity.Cosmetic;
import com.spa.cosmeticservice.entity.CosmeticOrder;
import com.spa.cosmeticservice.entity.CosmeticOrderItem;
import com.spa.cosmeticservice.exception.AppException;
import com.spa.cosmeticservice.exception.ErrorCode;
import com.spa.cosmeticservice.mapper.CosmeticOrderMapper;
import com.spa.cosmeticservice.repository.CosmeticOrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CosmeticOrderService {

    CosmeticOrderRepository orderRepository;
    CosmeticOrderMapper orderMapper;
    CosmeticService cosmeticService;
    CosmeticInventoryService cosmeticInventoryService;

    @Transactional
    public CosmeticOrderResponse create(CosmeticOrderRequest request) {
        CosmeticOrder order = CosmeticOrder.builder()
                .appointmentId(request.getAppointmentId())
                .technicianId(request.getTechnicianId())
                .note(request.getNote())
                .build();

        // Theo đúng use case: chỉ KIỂM TRA tồn kho khả dụng ở bước kê đơn, KHÔNG trừ
        // kho ngay — kho chỉ thực sự bị trừ khi Payment Service xác nhận thanh toán
        // (xem CosmeticInventoryService.deductStock, gọi từ InvoicePaidConsumer).
        for (CosmeticOrderRequest.Item itemRequest : request.getItems()) {
            Cosmetic cosmetic = cosmeticService.getCosmeticOrThrow(itemRequest.getCosmeticId());

            if (!cosmeticInventoryService.hasEnoughStock(cosmetic.getId(), itemRequest.getQuantity())) {
                throw new AppException(ErrorCode.INVENTORY_NOT_ENOUGH);
            }

            order.addItem(CosmeticOrderItem.builder()
                    .cosmetic(cosmetic)
                    .quantity(itemRequest.getQuantity())
                    .usageInstruction(itemRequest.getUsageInstruction())
                    .build());
        }

        orderRepository.save(order);
        return toResponseWithPricing(order);
    }

    // Admin dùng để tổng hợp "tiền mỹ phẩm kê đơn" khi lập hóa đơn dịch vụ
    // (Use Case Lập hóa đơn, trường hợp 1). Mỗi lịch hẹn tối đa 1 đơn kê (unique).
    public CosmeticOrderResponse getByAppointmentId(String appointmentId) {
        CosmeticOrder order = orderRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.PRESCRIPTION_NOT_FOUND));
        return toResponseWithPricing(order);
    }

    public CosmeticOrderResponse getById(String id) {
        CosmeticOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRESCRIPTION_NOT_FOUND));
        return toResponseWithPricing(order);
    }

    // Ghép response: lấy giá SỐNG từ Cosmetic tại thời điểm gọi (xem ghi chú ở
    // CosmeticOrderItem vì sao không lưu unitPrice trong bảng chi tiết).
    private CosmeticOrderResponse toResponseWithPricing(CosmeticOrder order) {
        CosmeticOrderResponse response = orderMapper.toResponse(order);

        List<CosmeticOrderResponse.Item> itemResponses = order.getItems().stream()
                .map(item -> {
                    CosmeticOrderResponse.Item itemResponse = orderMapper.toItemResponse(item);
                    BigDecimal unitPrice = item.getCosmetic().getPrice();
                    itemResponse.setUnitPrice(unitPrice);
                    itemResponse.setLineTotal(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
                    return itemResponse;
                })
                .toList();

        BigDecimal totalAmount = itemResponses.stream()
                .map(CosmeticOrderResponse.Item::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setItems(itemResponses);
        response.setTotalAmount(totalAmount);
        return response;
    }
}

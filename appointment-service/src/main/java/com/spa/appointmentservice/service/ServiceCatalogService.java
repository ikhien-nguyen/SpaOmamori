package com.spa.appointmentservice.service;

import com.spa.appointmentservice.dto.request.ServiceCreationRequest;
import com.spa.appointmentservice.dto.response.ServiceResponse;
import com.spa.appointmentservice.entity.SpaService;
import com.spa.appointmentservice.exception.AppException;
import com.spa.appointmentservice.exception.ErrorCode;
import com.spa.appointmentservice.mapper.ServiceMapper;
import com.spa.appointmentservice.repository.ServiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Đặt tên "ServiceCatalogService" (thay vì "ServiceService") để tránh trùng
 * với tên entity SpaService và dễ đọc hơn.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ServiceCatalogService {

    ServiceRepository serviceRepository;
    ServiceMapper serviceMapper;

    public ServiceResponse createService(ServiceCreationRequest request) {
        SpaService service = serviceMapper.toSpaService(request);
        service.setIsActive(true);
        serviceRepository.save(service);
        return serviceMapper.toServiceResponse(service);
    }

    public List<ServiceResponse> getAllActiveServices() {
        return serviceRepository.findAll().stream()
                .filter(SpaService::getIsActive)
                .map(serviceMapper::toServiceResponse)
                .toList();
    }

    public ServiceResponse getServiceById(String id) {
        SpaService service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXISTED));
        return serviceMapper.toServiceResponse(service);
    }

    public void deactivateService(String id) {
        SpaService service = serviceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXISTED));
        service.setIsActive(false);
        serviceRepository.save(service);
    }
}
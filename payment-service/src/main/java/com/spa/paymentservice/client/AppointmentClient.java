package com.spa.paymentservice.client;

import com.spa.paymentservice.dto.response.ApiResponse;
import com.spa.paymentservice.dto.response.AppointmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// url="${appointment-service.url}" la giai phap tam vi appointment-service
// chua dang ky Eureka that su (xem ghi chu trong application.yaml). Sau nay
// xoa thuoc tinh url de dung discovery qua Eureka.
//
// Luu y: phai goi "/appointments/internal/{id}" (service-to-service, khong
// co @PreAuthorize, khong qua Gateway) thay vi "/appointments/{id}" vi
// AppointmentController.GET /{id} gio chi cho phep ADMIN/THERAPIST. Endpoint
// /internal/** bi API Gateway block (InternalPathBlockFilter), chi Feign
// trong cluster moi goi duoc.
@FeignClient(name = "appointment-service", url = "${appointment-service.url}")
public interface AppointmentClient {

    @GetMapping("/appointments/internal/{id}")
    ApiResponse<AppointmentResponse> getAppointment(@PathVariable("id") String id);
}

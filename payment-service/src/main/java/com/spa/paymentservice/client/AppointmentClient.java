package com.spa.paymentservice.client;

import com.spa.paymentservice.dto.response.ApiResponse;
import com.spa.paymentservice.dto.response.AppointmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// url="${appointment-service.url}" la giai phap tam vi appointment-service
// chua dang ky Eureka that su (xem ghi chu trong application.yaml). Sau nay
// xoa thuoc tinh url de dung discovery qua Eureka.
@FeignClient(name = "appointment-service", url = "${appointment-service.url}")
public interface AppointmentClient {

    @GetMapping("/appointments/{id}")
    ApiResponse<AppointmentResponse> getAppointment(@PathVariable("id") String id);
}

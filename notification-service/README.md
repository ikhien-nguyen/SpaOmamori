# notification-service

Microservice gửi/lưu thông báo cho khách hàng (đặt lịch, xác nhận, thanh toán...),
viết theo đúng convention của các service khác trong SpaOmamori (Eureka client,
`ApiResponse<T>`, `AppException`/`ErrorCode`/`GlobalExceptionHandler`, MapStruct).

## Cổng & context-path

`http://localhost:8088/notifications` — port 8088 chưa service nào trong dự án
dùng (đã kiểm tra: 8080 user, 8081 profile, 8082 appointment, 8083 media,
8084 treatment, 8085 room, 8086 payment, 8087 cosmetic, 8761 eureka, 8888 gateway).

## Trước khi chạy

1. Tạo database MySQL: `CREATE DATABASE omamori_notification;`
2. Cần Kafka chạy ở `localhost:9092` (dùng chung Kafka với payment-service/cosmetic-service).
3. `eureka-server` phải chạy trước.
4. Mặc định `notification.email.enabled: false` → email chỉ log ra console
   (`[MOCK EMAIL] to=... subject=... content=...`), **không cần** tài khoản SMTP
   thật để chạy demo. Khi có SMTP thật, điền lại `spring.mail.*` trong
   `application.yaml` và đổi `enabled: true`.

## API

| Method | Path | Mô tả | Ai gọi |
|---|---|---|---|
| POST | `/notifications` | Tạo + gửi 1 thông báo | Service khác gọi nội bộ (Feign) |
| GET | `/notifications/me` | Danh sách thông báo của tôi | FE, qua Gateway (header `X-User-Id`) |
| GET | `/notifications/me/unread-count` | Số thông báo chưa đọc | FE, qua Gateway |
| PATCH | `/notifications/{id}/read` | Đánh dấu đã đọc | FE, qua Gateway |

## Đã tích hợp sẵn: thông báo khi thanh toán thành công

`InvoicePaidConsumer` lắng nghe topic Kafka `invoice-paid` — **cùng topic**
`cosmetic-service` đang nghe để trừ kho, khác `group-id` nên cả 2 service đều
nhận được đầy đủ, không tranh nhau message.

**Cần sửa 1 chỗ nhỏ bên `payment-service`** để việc này chạy được: hiện
`InvoicePaidPublisher`/`InvoicePaidEvent` bên đó chỉ có `invoiceId` + `items`
(đủ cho cosmetic-service trừ kho, nhưng thiếu `customerId` nên
notification-service không biết gửi cho ai). Thêm 2 field vào
`payment-service/.../dto/event/InvoicePaidEvent.java`:

```java
public class InvoicePaidEvent {
    private String invoiceId;
    private String customerId;      // MOI THEM
    private java.math.BigDecimal totalAmount; // MOI THEM
    private List<Item> items;
    ...
}
```

và khi build event trong `InvoicePaidPublisher.publish(Invoice invoice)`, set thêm:

```java
InvoicePaidEvent event = InvoicePaidEvent.builder()
        .invoiceId(invoice.getId())
        .customerId(invoice.getCustomerId())      // MOI THEM
        .totalAmount(invoice.getTotalAmount())     // MOI THEM
        .items(items)
        .build();
```

`cosmetic-service` không cần sửa gì — nó chỉ đọc `invoiceId`/`items` nên 2 field
thêm vào sẽ tự bị bỏ qua khi deserialize.

## Tích hợp từ service khác (ví dụ appointment-service)

Copy pattern `AppointmentClient`/`CosmeticClient` đã có sẵn trong project:

```java
// appointment-service/src/main/java/com/spa/appointmentservice/client/NotificationClient.java
@FeignClient(name = "notification-service")
public interface NotificationClient {
    @PostMapping("/notifications")
    void create(@RequestBody CreateNotificationRequest request);
}
```

rồi gọi ở chỗ đổi trạng thái lịch hẹn, ví dụ khi Admin `CONFIRMED`:

```java
notificationClient.create(CreateNotificationRequest.builder()
        .recipientId(appointment.getCustomerId())
        .type(NotificationType.APPOINTMENT_CONFIRMED)
        .channel(NotificationChannel.IN_APP)
        .title("Lịch hẹn đã được xác nhận")
        .content("Lịch hẹn ngày " + appointment.getAppointmentTime() + " đã được xác nhận.")
        .build());
```

(cần định nghĩa lại `CreateNotificationRequest`/`NotificationType`/`NotificationChannel`
dạng DTO riêng bên appointment-service, giống cách `AppointmentResponse` được
copy sang `payment-service` — mỗi service tự giữ bản DTO của mình, không share
module chung, đúng convention hiện tại của dự án).

## Cần thêm ở API Gateway

Thêm route trong `api-gateway/application.yaml` (đã đưa ở lần trước, nhắc lại):

```yaml
        - id: notification_service
          uri: lb://notification-service
          predicates:
            - Path=${app.api-prefix}/notifications/**
          filters:
            - StripPrefix=2
```

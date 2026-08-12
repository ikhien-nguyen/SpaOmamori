package com.spa.roomservice.dto.request;

import com.spa.roomservice.entity.RoomType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreationRequest {

    @NotBlank(message = "Tên phòng không được để trống")
    private String name;

    @NotNull(message = "Loại phòng không được để trống")
    private RoomType type;

    @NotNull(message = "Giá phòng không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phòng phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Sức chứa không được để trống")
    @Min(value = 1, message = "Sức chứa phải lớn hơn 0")
    private Integer capacity;

    private String note;
}

package com.spa.roomservice.dto.response;

import com.spa.roomservice.entity.RoomStatus;
import com.spa.roomservice.entity.RoomType;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private String id;
    private String name;
    private RoomType type;
    private BigDecimal price;
    private Integer capacity;
    private String note;
    private Boolean isActive;
    private RoomStatus status;
}
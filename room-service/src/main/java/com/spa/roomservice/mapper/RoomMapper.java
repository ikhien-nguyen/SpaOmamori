package com.spa.roomservice.mapper;

import com.spa.roomservice.dto.request.RoomCreationRequest;
import com.spa.roomservice.dto.response.RoomResponse;
import com.spa.roomservice.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "status", ignore = true)
    Room toRoom(RoomCreationRequest request);

    RoomResponse toRoomResponse(Room room);
}
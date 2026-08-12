package com.spa.roomservice.service;

import com.spa.roomservice.dto.request.RoomCreationRequest;
import com.spa.roomservice.dto.response.RoomResponse;
import com.spa.roomservice.entity.Room;
import com.spa.roomservice.entity.RoomStatus;
import com.spa.roomservice.exception.AppException;
import com.spa.roomservice.exception.ErrorCode;
import com.spa.roomservice.mapper.RoomMapper;
import com.spa.roomservice.repository.RoomRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoomService {

    RoomRepository roomRepository;
    RoomMapper roomMapper;

    public RoomResponse createRoom(RoomCreationRequest request) {
        // UC_10 - dieu kien dac biet: "Ten dich vu/phong/my pham khong duoc
        // trung lap" -> check truoc khi luu, khong phan biet hoa/thuong.
        if (roomRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AppException(ErrorCode.ROOM_NAME_EXISTED);
        }

        Room room = roomMapper.toRoom(request);
        room.setIsActive(true);
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);
        return roomMapper.toRoomResponse(room);
    }

    public List<RoomResponse> getAllActiveRooms() {
        return roomRepository.findAll().stream()
                .filter(Room::getIsActive)
                .map(roomMapper::toRoomResponse)
                .toList();
    }

    public RoomResponse getRoomById(String id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        return roomMapper.toRoomResponse(room);
    }

    public void deactivateRoom(String id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        room.setIsActive(false);
        roomRepository.save(room);
    }

    // Goi noi bo boi appointment-service khi lich hen chuyen IN_PROGRESS
    // (chiem phong) hoac COMPLETED/CANCELLED (tra phong lai) - UC_07.
    public RoomResponse updateRoomStatus(String id, RoomStatus status) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        if (!Boolean.TRUE.equals(room.getIsActive())) {
            throw new AppException(ErrorCode.ROOM_INACTIVE);
        }

        room.setStatus(status);
        roomRepository.save(room);
        return roomMapper.toRoomResponse(room);
    }
}
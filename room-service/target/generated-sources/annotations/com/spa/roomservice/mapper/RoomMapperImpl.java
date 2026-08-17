package com.spa.roomservice.mapper;

import com.spa.roomservice.dto.request.RoomCreationRequest;
import com.spa.roomservice.dto.response.RoomResponse;
import com.spa.roomservice.entity.Room;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-17T20:07:03+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23-valhalla (Oracle Corporation)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public Room toRoom(RoomCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        Room.RoomBuilder room = Room.builder();

        room.name( request.getName() );
        room.type( request.getType() );
        room.price( request.getPrice() );
        room.capacity( request.getCapacity() );
        room.note( request.getNote() );

        return room.build();
    }

    @Override
    public RoomResponse toRoomResponse(Room room) {
        if ( room == null ) {
            return null;
        }

        RoomResponse.RoomResponseBuilder roomResponse = RoomResponse.builder();

        roomResponse.id( room.getId() );
        roomResponse.name( room.getName() );
        roomResponse.type( room.getType() );
        roomResponse.price( room.getPrice() );
        roomResponse.capacity( room.getCapacity() );
        roomResponse.note( room.getNote() );
        roomResponse.isActive( room.getIsActive() );
        roomResponse.status( room.getStatus() );

        return roomResponse.build();
    }
}

package com.spa.roomservice.repository;

import com.spa.roomservice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    boolean existsByNameIgnoreCase(String name);
}
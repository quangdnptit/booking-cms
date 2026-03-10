package com.demo.booking_cms.repository;

import com.demo.booking_cms.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    List<Room> findByTheaterId(UUID theaterId);

    boolean existsByTheaterIdAndName(UUID theaterId, String name);
}

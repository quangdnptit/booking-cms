package com.demo.booking_cms.service;

import com.demo.booking_cms.dto.request.RoomRequest;
import com.demo.booking_cms.dto.response.RoomResponse;
import com.demo.booking_cms.entity.Room;
import com.demo.booking_cms.entity.Theater;
import com.demo.booking_cms.repository.RoomRepository;
import com.demo.booking_cms.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final TheaterRepository theaterRepository;
    private final SeatGenerationService seatGenerationService;

    public List<RoomResponse> findAll() {
        return roomRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RoomResponse> findByTheater(UUID theaterId) {
        return roomRepository.findByTheaterId(theaterId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RoomResponse findById(UUID id) {
        return roomRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public RoomResponse create(RoomRequest request) {
        Theater theater = theaterRepository.findById(request.getTheaterId()).orElse(null);
        if (theater == null) {
            throw new IllegalArgumentException("Theater not found");
        }
        if (roomRepository.existsByTheaterIdAndName(request.getTheaterId(), request.getName())) {
            throw new IllegalStateException("Room with this name already exists in this theater");
        }
        
        Room room = Room.builder()
                .theater(theater)
                .name(request.getName())
                .totalSeats(request.getTotalSeats())
                .totalRows(request.getTotalRows())
                .seatsPerRow(request.getSeatsPerRow())
                .totalSeats(request.getTotalRows() * request.getSeatsPerRow())
                .build();
        
        Room savedRoom = roomRepository.save(room);
        seatGenerationService.generateSeatsForRoom(savedRoom);
        return mapToResponse(savedRoom);
    }

    public RoomResponse update(UUID id, RoomRequest request) {
        Theater theater = theaterRepository.findById(request.getTheaterId()).orElse(null);
        if (theater == null) {
            throw new IllegalArgumentException("Theater not found");
        }
        
        return roomRepository.findById(id).map(room -> {
            room.setTheater(theater);
            room.setName(request.getName());
            room.setTotalSeats(request.getTotalSeats());
            return mapToResponse(roomRepository.save(room));
        }).orElse(null);
    }

    public boolean delete(UUID id) {
        if (!roomRepository.existsById(id)) {
            return false;
        }
        roomRepository.deleteById(id);
        return true;
    }

    private RoomResponse mapToResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .theaterId(room.getTheater() != null ? room.getTheater().getId() : null)
                .theaterName(room.getTheater() != null ? room.getTheater().getName() : null)
                .name(room.getName())
                .totalSeats(room.getTotalSeats())
                .totalRows(room.getTotalRows())
                .seatsPerRow(room.getSeatsPerRow())
                .createdAt(room.getCreatedAt())
                .build();
    }
}

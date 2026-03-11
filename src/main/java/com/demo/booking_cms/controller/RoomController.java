package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.RoomRequest;
import com.demo.booking_cms.entity.Room;
import com.demo.booking_cms.entity.Theater;
import com.demo.booking_cms.repository.RoomRepository;
import com.demo.booking_cms.repository.TheaterRepository;
import com.demo.booking_cms.service.SeatGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin
public class RoomController {

    private final RoomRepository roomRepository;
    private final TheaterRepository theaterRepository;
    private final SeatGenerationService seatGenerationService;

    @GetMapping
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @GetMapping("/theater/{theaterId}")
    public List<Room> findByTheater(@PathVariable UUID theaterId) {
        return roomRepository.findByTheaterId(theaterId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable UUID id) {
        return roomRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody RoomRequest request) {
        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElse(null);
        if (theater == null) {
            return ResponseEntity.badRequest().build();
        }
        if (roomRepository.existsByTheaterIdAndName(request.getTheaterId(), request.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Room room = Room.builder()
                .theater(theater)
                .name(request.getName())
                .totalSeats(request.getTotalSeats())
                .totalRows(request.getTotalRows())
                .seatsPerRow(request.getSeatsPerRow())
                .totalSeats(request.getTotalRows() * request.getSeatsPerRow())
                .build();
        Room roomEntity = roomRepository.save(room);
        seatGenerationService.generateSeatsForRoom(roomEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable UUID id, @RequestBody RoomRequest request) {
        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElse(null);
        if (theater == null) {
            return ResponseEntity.badRequest().build();
        }
        return roomRepository.findById(id)
                .map(room -> {
                    room.setTheater(theater);
                    room.setName(request.getName());
                    room.setTotalSeats(request.getTotalSeats());
                    return ResponseEntity.ok(roomRepository.save(room));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!roomRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roomRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

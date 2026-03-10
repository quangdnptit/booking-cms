package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.SeatRequest;
import com.demo.booking_cms.entity.Room;
import com.demo.booking_cms.entity.Seat;
import com.demo.booking_cms.repository.RoomRepository;
import com.demo.booking_cms.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@CrossOrigin
public class SeatController {

    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;

    @GetMapping
    public List<Seat> findAll() {
        return seatRepository.findAll();
    }

    @GetMapping("/room/{roomId}")
    public List<Seat> findByRoom(@PathVariable UUID roomId) {
        return seatRepository.findByRoomId(roomId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> findById(@PathVariable UUID id) {
        return seatRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Seat> create(@RequestBody SeatRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElse(null);
        if (room == null) {
            return ResponseEntity.badRequest().build();
        }
        Seat seat = Seat.builder()
                .room(room)
                .seatRow(request.getSeatRow())
                .seatNumber(request.getSeatNumber())
                .seatType(request.getSeatType() != null ? request.getSeatType() : "STANDARD")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(seatRepository.save(seat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seat> update(@PathVariable UUID id, @RequestBody SeatRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElse(null);
        if (room == null) {
            return ResponseEntity.badRequest().build();
        }
        return seatRepository.findById(id)
                .map(seat -> {
                    seat.setRoom(room);
                    seat.setSeatRow(request.getSeatRow());
                    seat.setSeatNumber(request.getSeatNumber());
                    if (request.getSeatType() != null) {
                        seat.setSeatType(request.getSeatType());
                    }
                    return ResponseEntity.ok(seatRepository.save(seat));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!seatRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        seatRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

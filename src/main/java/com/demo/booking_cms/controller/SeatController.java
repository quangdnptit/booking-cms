package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.request.SeatRequest;
import com.demo.booking_cms.dto.response.SeatResponse;
import com.demo.booking_cms.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@CrossOrigin
public class SeatController {

    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<List<SeatResponse>> findAll() {
        return ResponseEntity.ok(seatService.findAll());
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<SeatResponse>> findByRoom(@PathVariable UUID roomId) {
        return ResponseEntity.ok(seatService.findByRoomId(roomId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatResponse> findById(@PathVariable UUID id) {
        SeatResponse response = seatService.findById(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatResponse> update(@PathVariable UUID id, @RequestBody SeatRequest request) {
        SeatResponse response = seatService.updateSeat(id, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!seatService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}

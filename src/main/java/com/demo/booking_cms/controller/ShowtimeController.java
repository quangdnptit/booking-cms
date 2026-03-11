package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.request.ShowtimeRequest;
import com.demo.booking_cms.dto.response.ShowtimeResponse;
import com.demo.booking_cms.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
@CrossOrigin
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @GetMapping
    public ResponseEntity<List<ShowtimeResponse>> findAll() {
        return ResponseEntity.ok(showtimeService.findAll());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowtimeResponse>> findByMovie(@PathVariable UUID movieId) {
        return ResponseEntity.ok(showtimeService.findByMovieId(movieId));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ShowtimeResponse>> findByRoom(@PathVariable UUID roomId) {
        return ResponseEntity.ok(showtimeService.findByRoomId(roomId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> findById(@PathVariable UUID id) {
        ShowtimeResponse response = showtimeService.findById(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ShowtimeRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(showtimeService.create(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ShowtimeRequest request) {
        try {
            ShowtimeResponse response = showtimeService.update(id, request);
            return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!showtimeService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}

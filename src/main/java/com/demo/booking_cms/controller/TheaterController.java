package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.request.TheaterRequest;
import com.demo.booking_cms.dto.response.TheaterResponse;
import com.demo.booking_cms.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
@CrossOrigin
public class TheaterController {

    private final TheaterService theaterService;

    @GetMapping
    public ResponseEntity<List<TheaterResponse>> findAll() {
        return ResponseEntity.ok(theaterService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterResponse> findById(@PathVariable UUID id) {
        TheaterResponse response = theaterService.findById(id);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TheaterResponse> create(@RequestBody TheaterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(theaterService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheaterResponse> update(@PathVariable UUID id, @RequestBody TheaterRequest request) {
        TheaterResponse response = theaterService.update(id, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!theaterService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}

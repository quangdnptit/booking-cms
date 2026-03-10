package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.TheaterRequest;
import com.demo.booking_cms.entity.Theater;
import com.demo.booking_cms.repository.TheaterRepository;
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

    private final TheaterRepository theaterRepository;

    @GetMapping
    public List<Theater> findAll() {
        return theaterRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theater> findById(@PathVariable UUID id) {
        return theaterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Theater> create(@RequestBody TheaterRequest request) {
        Theater theater = Theater.builder()
                .name(request.getName())
                .location(request.getLocation())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(theaterRepository.save(theater));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theater> update(@PathVariable UUID id, @RequestBody TheaterRequest request) {
        return theaterRepository.findById(id)
                .map(theater -> {
                    theater.setName(request.getName());
                    theater.setLocation(request.getLocation());
                    return ResponseEntity.ok(theaterRepository.save(theater));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!theaterRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        theaterRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

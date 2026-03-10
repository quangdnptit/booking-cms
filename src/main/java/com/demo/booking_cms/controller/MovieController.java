package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.MovieRequest;
import com.demo.booking_cms.entity.Movie;
import com.demo.booking_cms.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@CrossOrigin
public class MovieController {

    private final MovieRepository movieRepository;

    @GetMapping
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> findById(@PathVariable UUID id) {
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody MovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .genre(request.getGenre())
                .ageRating(request.getAgeRating())
                .posterUrl(request.getPosterUrl())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(movieRepository.save(movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> update(@PathVariable UUID id, @RequestBody MovieRequest request) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(request.getTitle());
                    movie.setDescription(request.getDescription());
                    movie.setDurationMinutes(request.getDurationMinutes());
                    movie.setGenre(request.getGenre());
                    movie.setAgeRating(request.getAgeRating());
                    movie.setPosterUrl(request.getPosterUrl());
                    return ResponseEntity.ok(movieRepository.save(movie));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        movieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.demo.booking_cms.controller;

import com.demo.booking_cms.dto.ShowtimeRequest;
import com.demo.booking_cms.entity.Movie;
import com.demo.booking_cms.entity.Room;
import com.demo.booking_cms.entity.Showtime;
import com.demo.booking_cms.repository.MovieRepository;
import com.demo.booking_cms.repository.RoomRepository;
import com.demo.booking_cms.repository.ShowtimeRepository;
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

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    @GetMapping
    public List<Showtime> findAll() {
        return showtimeRepository.findAll();
    }

    @GetMapping("/movie/{movieId}")
    public List<Showtime> findByMovie(@PathVariable UUID movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    @GetMapping("/room/{roomId}")
    public List<Showtime> findByRoom(@PathVariable UUID roomId) {
        return showtimeRepository.findByRoomId(roomId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Showtime> findById(@PathVariable UUID id) {
        return showtimeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Showtime> create(@RequestBody ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElse(null);
        Room room = roomRepository.findById(request.getRoomId())
                .orElse(null);
        if (movie == null || room == null) {
            return ResponseEntity.badRequest().build();
        }
        Showtime showtime = Showtime.builder()
                .movie(movie)
                .room(room)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .basePrice(request.getBasePrice())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(showtimeRepository.save(showtime));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Showtime> update(@PathVariable UUID id, @RequestBody ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElse(null);
        Room room = roomRepository.findById(request.getRoomId())
                .orElse(null);
        if (movie == null || room == null) {
            return ResponseEntity.badRequest().build();
        }
        return showtimeRepository.findById(id)
                .map(showtime -> {
                    showtime.setMovie(movie);
                    showtime.setRoom(room);
                    showtime.setStartTime(request.getStartTime());
                    showtime.setEndTime(request.getEndTime());
                    showtime.setBasePrice(request.getBasePrice());
                    return ResponseEntity.ok(showtimeRepository.save(showtime));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!showtimeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        showtimeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

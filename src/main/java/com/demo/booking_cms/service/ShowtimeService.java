package com.demo.booking_cms.service;

import com.demo.booking_cms.dto.request.ShowtimeRequest;
import com.demo.booking_cms.dto.response.ShowtimeResponse;
import com.demo.booking_cms.entity.Movie;
import com.demo.booking_cms.entity.Room;
import com.demo.booking_cms.entity.Showtime;
import com.demo.booking_cms.repository.MovieRepository;
import com.demo.booking_cms.repository.RoomRepository;
import com.demo.booking_cms.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public List<ShowtimeResponse> findAll() {
        return showtimeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ShowtimeResponse findById(UUID id) {
        return showtimeRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public ShowtimeResponse create(ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId()).orElse(null);
        Room room = roomRepository.findById(request.getRoomId()).orElse(null);
        
        if (movie == null || room == null) {
            throw new IllegalArgumentException("Movie or Room not found");
        }
        
        Showtime showtime = Showtime.builder()
                .movie(movie)
                .room(room)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .basePrice(request.getBasePrice())
                .isPublished(request.isPublished())
                .build();
                
        return mapToResponse(showtimeRepository.save(showtime));
    }

    public ShowtimeResponse update(UUID id, ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId()).orElse(null);
        Room room = roomRepository.findById(request.getRoomId()).orElse(null);
        
        if (movie == null || room == null) {
            throw new IllegalArgumentException("Movie or Room not found");
        }
        
        return showtimeRepository.findById(id).map(showtime -> {
            showtime.setMovie(movie);
            showtime.setRoom(room);
            showtime.setStartTime(request.getStartTime());
            showtime.setEndTime(request.getEndTime());
            showtime.setBasePrice(request.getBasePrice());
            return mapToResponse(showtimeRepository.save(showtime));
        }).orElse(null);
    }

    public boolean delete(UUID id) {
        if (!showtimeRepository.existsById(id)) {
            return false;
        }
        showtimeRepository.deleteById(id);
        return true;
    }

    private ShowtimeResponse mapToResponse(Showtime showtime) {
        return ShowtimeResponse.builder()
                .id(showtime.getId())
                .movieId(showtime.getMovie() != null ? showtime.getMovie().getId() : null)
                .movieTitle(showtime.getMovie() != null ? showtime.getMovie().getTitle() : null)
                .roomId(showtime.getRoom() != null ? showtime.getRoom().getId() : null)
                .roomName(showtime.getRoom() != null ? showtime.getRoom().getName() : null)
                .startTime(showtime.getStartTime())
                .endTime(showtime.getEndTime())
                .publishedAt(showtime.getPublishedAt())
                .isPublished(showtime.getIsPublished())
                .basePrice(showtime.getBasePrice())
                .createdAt(showtime.getCreatedAt())
                .build();
    }
}

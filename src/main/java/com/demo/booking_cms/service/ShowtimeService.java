package com.demo.booking_cms.service;

import com.demo.booking_cms.dto.request.GenerateSeatsRequest;
import com.demo.booking_cms.dto.request.GenerateSeatsRequestWrapper;
import com.demo.booking_cms.dto.request.ShowtimeRequest;
import com.demo.booking_cms.dto.response.ShowtimeResponse;
import com.demo.booking_cms.entity.*;
import com.demo.booking_cms.gateway.GoBookingClient;
import com.demo.booking_cms.repository.MovieRepository;
import com.demo.booking_cms.repository.RoomRepository;
import com.demo.booking_cms.repository.SeatRepository;
import com.demo.booking_cms.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final GoBookingClient goBookingClient;
    private final SeatRepository seatRepository;

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

    @Transactional
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
                .isPublished(request.getIsPublished())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        Showtime showtimeEntity = showtimeRepository.save(showtime);
        publishShowtime(room.getId(), showtimeEntity);
        return mapToResponse(showtimeEntity);
    }

    @Transactional
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
            showtime.setIsPublished(request.getIsPublished());
            Showtime saved = showtimeRepository.save(showtime);

            publishShowtime(room.getId(), saved);
            return mapToResponse(saved);

        }).orElseThrow(() -> new IllegalArgumentException("Showtime not found"));
    }

    public void publishShowtime(UUID roomId, Showtime showtime) {
        if(!showtime.getIsPublished()) {
            return;
        }
        List<Seat> seats = seatRepository.findByRoomId(roomId);
        List<GenerateSeatsRequest> generateSeatsRequest = seats.stream().map(seatEntity -> GenerateSeatsRequest.builder()
                .seatKey(String.format("%s#%d", seatEntity.getSeatRow(), seatEntity.getSeatNumber()))
                .price(showtime.getBasePrice().floatValue())
                .showtimeId(showtime.getId().toString())
                .seatType(seatEntity.getSeatType().name())
                .seatStatus(seatEntity.getIsActive() ? "AVAILABLE" : "UNAVAILABLE")
                .roomId(roomId)
                .createdAt(seatEntity.getCreatedAt().toString())
                .updatedAt(seatEntity.getUpdatedAt().toString())
                .build()).toList();

        goBookingClient.generateSeats(new GenerateSeatsRequestWrapper(generateSeatsRequest));
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

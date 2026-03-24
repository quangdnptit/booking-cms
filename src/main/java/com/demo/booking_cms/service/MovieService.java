package com.demo.booking_cms.service;

import com.demo.booking_cms.dto.request.MovieRequest;
import com.demo.booking_cms.dto.response.MovieResponse;
import com.demo.booking_cms.entity.Movie;
import com.demo.booking_cms.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private static final Logger log = LoggerFactory.getLogger(MovieService.class);

    public List<MovieResponse> findAll() {
        log.info("find all movie");
        return movieRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MovieResponse findById(UUID id) {
        log.atInfo()
                .addKeyValue("movieID", id)
                .log("find by id");
        return movieRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public MovieResponse create(MovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .genre(request.getGenre())
                .ageRating(request.getAgeRating())
                .posterUrl(request.getPosterUrl())
                .build();
        return mapToResponse(movieRepository.save(movie));
    }

    public MovieResponse update(UUID id, MovieRequest request) {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(request.getTitle());
            movie.setDescription(request.getDescription());
            movie.setDurationMinutes(request.getDurationMinutes());
            movie.setGenre(request.getGenre());
            movie.setAgeRating(request.getAgeRating());
            movie.setPosterUrl(request.getPosterUrl());
            return mapToResponse(movieRepository.save(movie));
        }).orElse(null);
    }

    public boolean delete(UUID id) {
        if (!movieRepository.existsById(id)) {
            return false;
        }
        movieRepository.deleteById(id);
        return true;
    }

    private MovieResponse mapToResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationMinutes(movie.getDurationMinutes())
                .genre(movie.getGenre())
                .ageRating(movie.getAgeRating())
                .posterUrl(movie.getPosterUrl())
                .createdAt(movie.getCreatedAt())
                .build();
    }
}

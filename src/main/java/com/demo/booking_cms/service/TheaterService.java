package com.demo.booking_cms.service;

import com.demo.booking_cms.dto.request.TheaterRequest;
import com.demo.booking_cms.dto.response.TheaterResponse;
import com.demo.booking_cms.entity.Theater;
import com.demo.booking_cms.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public List<TheaterResponse> findAll() {
        return theaterRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TheaterResponse findById(UUID id) {
        return theaterRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public TheaterResponse create(TheaterRequest request) {
        Theater theater = Theater.builder()
                .name(request.getName())
                .location(request.getLocation())
                .build();
        return mapToResponse(theaterRepository.save(theater));
    }

    public TheaterResponse update(UUID id, TheaterRequest request) {
        return theaterRepository.findById(id).map(theater -> {
            theater.setName(request.getName());
            theater.setLocation(request.getLocation());
            return mapToResponse(theaterRepository.save(theater));
        }).orElse(null);
    }

    public boolean delete(UUID id) {
        if (!theaterRepository.existsById(id)) {
            return false;
        }
        theaterRepository.deleteById(id);
        return true;
    }

    private TheaterResponse mapToResponse(Theater theater) {
        return TheaterResponse.builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .createdAt(theater.getCreatedAt())
                .build();
    }
}

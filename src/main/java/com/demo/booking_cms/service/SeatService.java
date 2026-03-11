package com.demo.booking_cms.service;

import com.demo.booking_cms.dto.request.SeatRequest;
import com.demo.booking_cms.dto.response.SeatResponse;
import com.demo.booking_cms.entity.Seat;
import com.demo.booking_cms.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public List<SeatResponse> findAll() {
        return seatRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SeatResponse> findByRoomId(UUID roomId) {
        return seatRepository.findByRoomId(roomId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SeatResponse findById(UUID id) {
        return seatRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public SeatResponse updateSeatType(UUID id, SeatRequest request) {
        return seatRepository.findById(id).map(seat -> {
            if (request.getSeatType() != null) {
                seat.setSeatType(request.getSeatType());
            }
            return mapToResponse(seatRepository.save(seat));
        }).orElse(null);
    }

    public boolean delete(UUID id) {
        if (!seatRepository.existsById(id)) {
            return false;
        }
        seatRepository.deleteById(id);
        return true;
    }

    private SeatResponse mapToResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .roomId(seat.getRoom() != null ? seat.getRoom().getId() : null)
                .roomName(seat.getRoom() != null ? seat.getRoom().getName() : null)
                .seatRow(seat.getSeatRow())
                .seatNumber(seat.getSeatNumber())
                .seatType(seat.getSeatType())
                .createdAt(seat.getCreatedAt())
                .build();
    }
}

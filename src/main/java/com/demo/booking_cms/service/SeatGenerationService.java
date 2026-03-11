package com.demo.booking_cms.service;

import com.demo.booking_cms.entity.Room;
import com.demo.booking_cms.entity.Seat;
import com.demo.booking_cms.enums.SeatType;
import com.demo.booking_cms.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatGenerationService {

    private final SeatRepository seatRepository;

    @Transactional
    public void generateSeatsForRoom(Room room) {
        List<Seat> seats = new ArrayList<>();
        for (int row = 0; row < room.getTotalRows(); row++) {
            char rowLetter = (char) ('A' + row);

            for (int number = 1; number <= room.getSeatsPerRow(); number++) {
                Seat seat = Seat.builder()
                        .room(room)
                        .seatRow(String.valueOf(rowLetter))
                        .seatNumber(number)
                        .seatType(SeatType.STANDARD)
                        .build();

                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }
}
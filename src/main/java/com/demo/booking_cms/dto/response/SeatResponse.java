package com.demo.booking_cms.dto.response;

import com.demo.booking_cms.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponse {
    private UUID id;
    private UUID roomId;
    private String roomName;
    private String seatRow;
    private Integer seatNumber;
    private SeatType seatType;
    private Instant createdAt;
}

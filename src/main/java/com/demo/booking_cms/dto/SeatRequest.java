package com.demo.booking_cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatRequest {
    private UUID roomId;
    private String seatRow;
    private Integer seatNumber;
    private String seatType;
}

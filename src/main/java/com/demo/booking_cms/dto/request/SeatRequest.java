package com.demo.booking_cms.dto.request;

import com.demo.booking_cms.enums.SeatType;
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
    private SeatType seatType;
}

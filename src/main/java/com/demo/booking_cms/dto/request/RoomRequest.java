package com.demo.booking_cms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {
    private UUID theaterId;
    private String name;
    private Integer totalSeats;
    private Integer totalRows;
    private Integer seatsPerRow;
}

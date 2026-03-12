package com.demo.booking_cms.dto.response;

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
public class RoomResponse {
    private UUID id;
    private UUID theaterId;
    private String theaterName;
    private String name;
    private Integer totalSeats;
    private Integer totalRows;
    private Integer seatsPerRow;
    private Instant createdAt;
    private Instant updatedAt;
}

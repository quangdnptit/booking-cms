package com.demo.booking_cms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeResponse {
    private UUID id;
    private UUID movieId;
    private String movieTitle;
    private UUID roomId;
    private String roomName;
    private Instant startTime;
    private Instant endTime;
    private Instant publishedAt;
    private Boolean isPublished;
    private BigDecimal basePrice;
    private Instant createdAt;
    private Instant updatedAt;
}

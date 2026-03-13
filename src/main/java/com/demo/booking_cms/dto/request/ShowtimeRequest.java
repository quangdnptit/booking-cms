package com.demo.booking_cms.dto.request;

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
public class ShowtimeRequest {
    private UUID movieId;
    private UUID roomId;
    private Instant startTime;
    private Instant endTime;
    private BigDecimal basePrice;
    private Boolean isPublished;
}

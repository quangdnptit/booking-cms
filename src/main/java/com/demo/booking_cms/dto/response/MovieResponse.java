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
public class MovieResponse {
    private UUID id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private String genre;
    private String ageRating;
    private String posterUrl;
    private Instant createdAt;
    private Instant updatedAt;
}

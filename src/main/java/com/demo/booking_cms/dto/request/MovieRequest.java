package com.demo.booking_cms.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {
    private String title;
    private String description;
    private Integer durationMinutes;
    private String genre;
    private String ageRating;
    private String posterUrl;
}

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
public class TheaterResponse {
    private UUID id;
    private String name;
    private String location;
    private Instant createdAt;
    private Instant updatedAt;
}

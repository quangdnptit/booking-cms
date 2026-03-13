package com.demo.booking_cms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class GenerateSeatsRequest {
    @JsonProperty("showtime_id")
    private String showtimeId;

    @JsonProperty("seat_key")
    private String seatKey;

    @JsonProperty("room_id")
    private UUID roomId;

    @JsonProperty("seat_type")
    private String seatType;

    @JsonProperty("is_active")
    private String isActive;

    private float price;

    @JsonProperty("seat_status")
    private String seatStatus;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;
}


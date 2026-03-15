package com.demo.booking_cms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerateSeatsRequestWrapper {
    private List<GenerateSeatsRequest> seats;
    @JsonProperty("base_price")
    private float basePrice;
}

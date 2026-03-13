package com.demo.booking_cms.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenerateSeatsRequestWrapper {
    public List<GenerateSeatsRequest> seats;
}

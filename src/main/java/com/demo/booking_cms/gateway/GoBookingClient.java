package com.demo.booking_cms.gateway;

import com.demo.booking_cms.dto.request.GenerateSeatsRequest;
import com.demo.booking_cms.dto.request.GenerateSeatsRequestWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class GoBookingClient {

    private final WebClient webClient;

    public GoBookingClient(@Value("${go.api.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void generateSeats(GenerateSeatsRequestWrapper req) {
        webClient.post()
                .uri("/seats/generate-seats")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
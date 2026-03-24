package com.demo.booking_cms.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Component
public class HttpRequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Instant start = Instant.now();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long ms = Duration.between(start, Instant.now()).toMillis();
            String qs = request.getQueryString();
            String uri = qs == null || qs.isBlank() ? request.getRequestURI() : request.getRequestURI() + "?" + qs;

            log.info("HTTP {} {} -> {} ({}ms)",
                    request.getMethod(),
                    uri,
                    response.getStatus(),
                    ms
            );
        }
    }
}


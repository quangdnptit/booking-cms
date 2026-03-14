package com.demo.booking_cms.gateway;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Issues HS256 JWTs compatible with user-booking {@code auth.SignAccessToken}:
 * RegisteredClaims only — sub, iat, exp (same as golang-jwt/v5).
 */
@Service
public class GoBookingJwtService {

    private final SecretKey signingKey;
    private final String subject;
    private final long ttlSeconds;

    public GoBookingJwtService(
            @Value("${go.api.jwt-secret:your-secret}") String secret,
            @Value("${go.api.jwt-subject:cms-gateway}") String subject,
            @Value("${go.api.jwt-ttl-seconds:3600}") long ttlSeconds
    ) {
        // Raw bytes like Go's []byte(secret); SecretKeySpec avoids jjwt's 256-bit min on Keys.hmacShaKeyFor
        this.signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        this.subject = subject;
        this.ttlSeconds = ttlSeconds > 0 ? ttlSeconds : 3600;
    }

    /**
     * Short-lived service token for user-booking protected routes (e.g. generate-seats).
     */
    public String createAccessToken() {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttlSeconds);
        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }
}

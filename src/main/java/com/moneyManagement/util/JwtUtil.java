package com.moneyManagement.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "O9H2KEUoeha0Yiei2a+oKtwiviiI3Q4gZcnUBNcI9lY=";

    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    private SecretKey getSigningKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public String extractUsername(String jwt) {
        return extractClaims(jwt).getSubject();
    }

    public boolean isTokenExpired(String jwt) {
        return extractClaims(jwt).getExpiration().before(new Date());
    }

    public boolean validateToken(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }
}



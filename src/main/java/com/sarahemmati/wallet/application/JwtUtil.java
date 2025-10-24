package com.sarahemmati.wallet.application;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long ttlMs;

    public JwtUtil(
            @Value("${app.jwt.secret:}") String base64Secret,
            @Value("${app.jwt.ttl-hours:6}") long ttlHours) {
        if (base64Secret == null || base64Secret.isBlank()) {
            throw new IllegalStateException("Missing property 'app.jwt.secret'. Set it in application-<profile>.properties or pass -Dapp.jwt.secret=...");
        }
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(base64Secret);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret must be >= 32 bytes after Base64 decode.");
        }
        this.key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
        this.ttlMs = java.time.Duration.ofHours(ttlHours).toMillis();
    }

    public String generate(String username){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ttlMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isValid(String token){
        try {
            getUsername(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex){
            return false;
        }
    }
}

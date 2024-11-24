package com.dear_tree.dear_tree.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    private static SecretKey key;

    @PostConstruct
    public void init() {
        if (JWT_SECRET != null) {
            byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } else {
            throw new IllegalStateException("JWT_SECRET is not set");
        }
    }

    private static SecretKey getSigningKey() {
        if (key == null) {
            throw new IllegalStateException("Signing key is not initialized");
        }
        return key;
    }

    public static String createToken(String username, Long expiredMs) {

        String jwt = Jwts.builder()
                .claim("username", username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(getSigningKey())
                .compact();
        return jwt;
    }

    public static boolean isExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public static boolean validateToken(String token, String username) {
        return getUsername(token).equals(username) && !isExpired(token);
    }

    public static String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    //토큰 만료 시간 추출
    public static Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    //토큰 남은 유효 시간 계산
    public static long getRemainingExpiration(String token) {
        Date expirationDate = extractExpiration(token);
        return (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
    }
}

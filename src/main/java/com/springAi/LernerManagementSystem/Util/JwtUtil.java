package com.springAi.LernerManagementSystem.Util;

import com.springAi.LernerManagementSystem.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {
    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(
                    "mySecretKeyForJwtAuthentication123456789".getBytes(StandardCharsets.UTF_8)
            );

    public static String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getUserName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .claim("EmailVerified", user.isEnabled())
                .claim("roles",user.getRole())
                .signWith(KEY)
                .compact();
    }

    public static Claims VerifyToken(String token) {
        Claims clams = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
//                .parseEncryptedClaims(token)
                .getPayload();
        return clams;
    }
}

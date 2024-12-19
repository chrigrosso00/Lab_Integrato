package com.lab.utils;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class MetabaseJwtUtil {

    //key user metabase
    @Value("${jwt.secret.key}")
    public static final String SECRET_KEY = "f747f6709863374827e64617f3d3e63ab8326aa1da4f09c29b694b3e723351e3";

    public static String generateEmbedToken(String resourceType, Long resourceId, Map<String, Object> params) {
        long now = System.currentTimeMillis();

        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 10 * 60 * 1000))
                .claim("resource", Map.of("type", resourceType, "id", resourceId))
                .claim("params", params)
                .signWith(key)
                .compact();
    }
}
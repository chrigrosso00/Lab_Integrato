package com.lab.utils;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class MetabaseJwtUtil {

    private static final String METABASE_SECRET_KEY = "f747f6709863374827e64617f3d3e63ab8326aa1da4f09c29b694b3e723351e3";

    public static String generateEmbedToken(Long dashboardId, Map<String, Object> params) {
        long now = System.currentTimeMillis();
    
        Key key = new SecretKeySpec(METABASE_SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 60 * 60 * 1000))  // Token valido per 1 ora
                .claim("resource", Map.of("dashboard", dashboardId))
                .claim("params", params)  // Passa i parametri come userId
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

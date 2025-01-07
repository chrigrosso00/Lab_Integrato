package com.lab.utils;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// @Component
// public class MetabaseJwtUtil {

//     //key user metabase
//     public static final String SECRET_KEY = "f747f6709863374827e64617f3d3e63ab8326aa1da4f09c29b694b3e723351e3";

//     public static String generateEmbedToken(Long dashboardId, Map<String, Object> filters) {
//         long now = System.currentTimeMillis();

//         // Creazione della chiave a partire dalla SECRET_KEY
//         Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");

//         return Jwts.builder()
//                 .setHeaderParam("typ", "JWT")
//                 .setIssuedAt(new Date(now))
//                 .setExpiration(new Date(now + 10 * 60 * 1000))
//                 .claim("resource", Map.of("dashboard", dashboardId))
//                 .claim("params", filters)
//                 .signWith(key, io.jsonwebtoken.SignatureAlgorithm.HS256)
//                 .compact();
//     }
// }

// @Component
// public class MetabaseJwtUtil {

//     // Chiave segreta Metabase 
//     private static final String METABASE_SECRET_KEY = "f747f6709863374827e64617f3d3e63ab8326aa1da4f09c29b694b3e723351e3";

//     public static String generateEmbedToken(Long dashboardId, Map<String, Object> params) {
//         long now = System.currentTimeMillis();

//         // Creazione della chiave utilizzando HmacSHA256
//         Key key = new SecretKeySpec(METABASE_SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

//         //Costruzione del token JWT
//         return Jwts.builder()
//                 .setIssuedAt(new Date(now))                         
//                 .setExpiration(new Date(now + 10 * 60 * 1000))     
//                 .claim("resource", Map.of("dashboard", dashboardId)) // Risorsa (dashboard)
//                 .claim("params", params)                       // Parametri di filtro
//                 .signWith(key, SignatureAlgorithm.HS256)    // Firma con HmacSHA256
//                 .compact();
        
//     }
// }

@Component
public class MetabaseJwtUtil {

    // Chiave segreta Metabase 
    private static final String METABASE_SECRET_KEY = "f747f6709863374827e64617f3d3e63ab8326aa1da4f09c29b694b3e723351e3";

    public static String generateEmbedToken(Long dashboardId, Map<String, Object> params) {
        long now = System.currentTimeMillis();
    
        // Creazione della chiave utilizzando HmacSHA256
        Key key = new SecretKeySpec(METABASE_SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    
        // Costruzione del token JWT
        return Jwts.builder()
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 10 * 60 * 1000))
                .claim("resource", Map.of("dashboard", dashboardId)) // Risorsa (dashboard)
                .claim("params", params) // Parametri di filtro
                .signWith(key, SignatureAlgorithm.HS256) // Firma con HmacSHA256
                .compact();
    }
    

}
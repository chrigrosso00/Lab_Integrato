package com.lab.services;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class MetabaseJwtService {

    @Value("${metabase.secret.key}")  // Carica la chiave segreta dal file di configurazione
    private String secretKey;

    // Metodo per generare il token JWT
    public String generateEmbedToken(Long dashboardId, Long userId, Map<String, Object> params) {
        long now = System.currentTimeMillis();

        // Creazione della chiave segreta per JWT
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        // Generazione del token JWT con dashboardId, userId e parametri
        return Jwts.builder()
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 60 * 60 * 1000))  // Scadenza di 1 ora
                .claim("resource", Map.of("dashboard", dashboardId))  // Impostazione della risorsa (dashboard)
                .claim("params", params)  // Parametri aggiuntivi, se necessari
                .claim("userId", userId)  // Aggiungi userId come claim
                .signWith(key, SignatureAlgorithm.HS256)  // Firma con la chiave segreta
                .compact();
    }
}

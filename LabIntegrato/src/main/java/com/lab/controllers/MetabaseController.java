package com.lab.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.services.MetabaseJwtService;

//base FUNZIONA

// @RestController
// @RequestMapping("/metabase")
// public class MetabaseController {

//     @GetMapping("/embed-url")
//     public ResponseEntity<String> getEmbedUrl() {
//         Long dashboardId = 73L; // ID della dashboard
//         Map<String, Object> params = Map.of(); // Parametri di filtro (se necessario)

//         String token = MetabaseJwtUtil.generateEmbedToken(dashboardId, params);

//         String iframeUrl = "http://localhost:3000/embed/dashboard/" + token +
//                            "#background=false&bordered=false&titled=true";
//         return ResponseEntity.ok(iframeUrl);
//     }

// }


//versione def funziona
@RestController
@RequestMapping("/metabase")
public class MetabaseController {

    private final MetabaseJwtService jwtTokenService;

    public MetabaseController(MetabaseJwtService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/embed-url")
    public ResponseEntity<String> getEmbedUrl() {
        Long dashboardId = 73L;  // ID della dashboard Metabase
        Long userId = 12L;       // L'userId associato alla sessione dell'utente
        
        // Parametri di esempio per l'embed
        Map<String, Object> params = Map.of();
        
        // Genera il token JWT usando il servizio separato
        String token = jwtTokenService.generateEmbedToken(dashboardId, userId, params);

        // Crea l'URL per l'iframe da utilizzare nel frontend
        String iframeUrl = "http://localhost:3000/embed/dashboard/" + token + "?user_id=" + userId + "#background=false&bordered=false&titled=true";
        return ResponseEntity.ok().body(iframeUrl);
    }
}

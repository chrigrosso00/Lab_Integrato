package com.lab.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.services.MetabaseJwtService;
import com.lab.utils.JwtUtil;

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
    
    @Autowired
	private JwtUtil jwtUtil;

    @GetMapping("/embed-url")
    public ResponseEntity<String> getEmbedUrl(@RequestHeader("Authorization") String authorizationHeader) {
        Long dashboardId = 11L;  // ID della dashboard Metabase
        String tokenUser = authorizationHeader.replace("Bearer ", "");
        
        // Estrai l'id del cliente dal token JWT
        Long userId = jwtUtil.extractId(tokenUser);     // L'userId associato alla sessione dell'utente
        
        // Parametri di esempio per l'embed
        Map<String, Object> params = Map.of();
        
        // Genera il token JWT usando il servizio separato
        String token = jwtTokenService.generateEmbedToken(dashboardId, userId, params);

        // Crea l'URL per l'iframe da utilizzare nel frontend
        // https://its.metabaseapp.com/dashboard/11-dashboard-cliente?user_id=
        // https://its.metabaseapp.com/public/dashboard/7021df49-6e88-42f4-b017-33f0a96082aa
        String iframeUrl = "https://its.metabaseapp.com/public/dashboard/7021df49-6e88-42f4-b017-33f0a96082aa?user_id=" + userId + "#background=false&bordered=false&titled=false";
        
        System.out.println(iframeUrl);
        
        return ResponseEntity.ok().body(iframeUrl);
    }

    @GetMapping("/embed-url/admin")
    public ResponseEntity<String> getEmbedUrlAdmin(@RequestHeader("Authorization") String authorizationHeader) {
        Long dashboardId = 11L;  // ID della dashboard Metabase
        String tokenUser = authorizationHeader.replace("Bearer ", "");
        
        // Estrai l'id del cliente dal token JWT
        Long userId = jwtUtil.extractId(tokenUser);     // L'userId associato alla sessione dell'utente
        
        // Parametri di esempio per l'embed
        Map<String, Object> params = Map.of();
        
        // Genera il token JWT usando il servizio separato
        String token = jwtTokenService.generateEmbedToken(dashboardId, userId, params);

        String iframeUrl = "https://its.metabaseapp.com/public/dashboard/2a576b22-47b0-4842-8985-98f1ce865e7f";
        
        System.out.println(iframeUrl);
        
        return ResponseEntity.ok().body(iframeUrl);
    }
}

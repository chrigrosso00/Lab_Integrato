package com.lab.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import metabase.metabaseembedded.utils.MetabaseJwtUtil;

// http://localhost:8080/api/metabase/dashboard?dashboardId=73&userId=12 &status=active

// @RestController
// @RequestMapping("/api/metabase")
// public class MetabaseController {

//     @GetMapping("/dashboard")
//     public ResponseEntity<Map<String, String>> getDashboardEmbedLink(@RequestParam Long dashboardId) {
//         String embedToken = MetabaseJwtUtil.generateEmbedToken(dashboardId, Map.of(
//             "userId", 12, // Filtro personalizzato per l'utente
//             "status", "active"
//         ));

//         String embedUrl = "http://localhost:3000/embed/dashboard/" + embedToken + "#bordered=false&titled=true";

//         return ResponseEntity.ok(Map.of("embedUrl", embedUrl));
//     }

// }

//COPIA BASE
@RestController
@RequestMapping("/api/metabase")
public class MetabaseController{
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, String>> getDashboardEmbedLink(@RequestParam Long dashboardId, @RequestParam(required = false) Long userId, @RequestParam(required = false) String status) {

            if (dashboardId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "DashboardId is required"));
            }
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "UserId is required"));
            }
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("userId", userId);

            if (status != null) {
                parameters.put("status", status);
            }
            
            String embedToken = MetabaseJwtUtil.generateEmbedToken(dashboardId, parameters);
            String embedUrl = "http://localhost:3000/embed/dashboard/" + embedToken + "#bordered=false&titled=true";

        return ResponseEntity.ok(Map.of("embedUrl", embedUrl));
    }
    

}

// @RestController
// @RequestMapping("/api/metabase")
// public class MetabaseController {

// @GetMapping("/dashboard")
// public ResponseEntity<Map<String, String>> getDashboardEmbedLink(@RequestParam(required = true) Long dashboardId,
//                                                                  @RequestParam(required = false) Long userId,
//                                                                  @RequestParam(required = false) String status) {
//     try {
//         if (dashboardId == null) {
//             return ResponseEntity.badRequest().body(Map.of("error", "DashboardId is required"));
//         }
//         if (userId == null) {
//             return ResponseEntity.badRequest().body(Map.of("error", "UserId is required"));
//         }

//         Map<String, Object> parameters = new HashMap<>();
//         parameters.put("userId", userId);

//         if (status != null) {
//             parameters.put("status", status);
//         }

//         String embedToken = MetabaseJwtUtil.generateEmbedToken(dashboardId, userId, parameters);
//         String embedUrl = "http://localhost:3000/embed/dashboard/" + embedToken + "#bordered=false&titled=true";

//         return ResponseEntity.ok(Map.of("embedUrl", embedUrl));
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                 .body(Map.of("error", "Internal Server Error: " + e.getMessage()));
//     }
// }

// }

// @RestController
// @RequestMapping("/api/metabase")
// public class MetabaseController {

//     @GetMapping("/dashboard")
//     public ResponseEntity<?> getDashboardEmbedWithParams(@RequestParam Long dashboardId, @RequestParam Long userId) {
//         // Validazione degli input
//         if (dashboardId == null || userId == null) {
//             return ResponseEntity.badRequest().body(Map.of("error", "Both dashboardId and userId are required"));
//         }

//         try {
//             // Configurazione dei parametri per il token JWT
//             Map<String, Object> parameters = Map.of("userId", userId);

//             // Genera il token JWT utilizzando il metodo aggiornato
//             String embedToken = MetabaseJwtUtil.generateEmbedToken(dashboardId, parameters);

//             // Costruisce l'URL per l'iframe della dashboard
//             String embedUrl = "http://localhost:3000/embed/dashboard/" + embedToken + "#bordered=false&titled=true";

//             // Costruisce l'URL dell'API per verificare i parametri
//             String paramKey = "userId";
//             String apiUrl = String.format("http://localhost:3000/api/embed/dashboard/%s/params/%s/values", embedToken, paramKey);

//             // Esegue la richiesta HTTP per verificare il parametro
//             HttpRequest request = HttpRequest.newBuilder()
//                     .uri(URI.create(apiUrl))
//                     .GET()
//                     .build();

//             HttpResponse<String> response = HttpClient.newHttpClient()
//                     .send(request, HttpResponse.BodyHandlers.ofString());

//             // Analizza la risposta dell'API
//             if (response.statusCode() == 200) {
//                 // Ritorna l'embedUrl e la risposta dei parametri
//                 return ResponseEntity.ok(Map.of(
//                         "embedUrl", embedUrl,
//                         "paramsResponse", response.body()
//                 ));
//             } else {
//                 // Gestisce eventuali errori dall'API
//                 return ResponseEntity.status(response.statusCode()).body(Map.of(
//                         "error", "Failed to fetch parameter values",
//                         "details", response.body()
//                 ));
//             }
//         } catch (Exception e) {
//             // Gestione degli errori generali
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
//                     "error", "Internal Server Error",
//                     "message", e.getMessage()
//             ));
//         }
//     }
// }



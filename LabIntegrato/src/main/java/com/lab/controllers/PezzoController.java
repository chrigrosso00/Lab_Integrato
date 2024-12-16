package com.lab.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.lab.DTO.PezzoDTO;
import com.lab.entities.Pezzo;
import com.lab.services.MagazzinoService;
import com.lab.services.PezzoService;
import com.lab.utils.JwtUtil;

@Controller
public class PezzoController {
	
	@Autowired
	private PezzoService pezzoService;
	
	@Autowired
	private MagazzinoService magazzinoService;
	
	@Autowired
    private JwtUtil jwtUtil;
	
    @GetMapping("/cliente/pezzi")
    public ResponseEntity<?> getPezzi(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Autenticazione richiesta.");
            }

            String token = authorizationHeader.substring(7); // Rimuove "Bearer "

            // Decodifica il token
            List<String> roles = jwtUtil.extractRoles(token);

            // Controlla se il ruolo è ROLE_CLIENTE
            if (roles == null || !roles.contains("ROLE_CLIENTE")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Accesso negato. Ruolo non autorizzato.");
            }

            // Recupera tutti i pezzi
            List<Pezzo> pezzi = pezzoService.getAllPezzi();
            List<Integer> idPezzi = new ArrayList<>();

            for (Pezzo pezzo : pezzi) {
                idPezzi.add(pezzo.getCodicePezzo());
            }

            // Recupera le quantità per ogni pezzo
            List<Integer> quantita = magazzinoService.getQuantita(idPezzi);

            // Combina Pezzo e Quantità in PezzoDTO
            List<PezzoDTO> pezzoDTOs = new ArrayList<>();
            for(int i = 0; i < pezzi.size(); i++) {
                Pezzo pezzo = pezzi.get(i);
                int q = quantita.get(i);
                PezzoDTO dto = new PezzoDTO(
                        pezzo.getCodicePezzo(),
                        pezzo.getNome(),
                        pezzo.getImmagineUrl(),
                        pezzo.getPrezzo(),
                        q
                );
                pezzoDTOs.add(dto);
            }

            return ResponseEntity.ok(pezzoDTOs);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore interno al server.");
        }
    }
	

}

package com.lab.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.DTO.CreazioneOrdineDTO;
import com.lab.entities.Cliente;
import com.lab.repos.ClienteDAO;
import com.lab.services.OrdineService;
import com.lab.utils.JwtUtil;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
	
	@Autowired
	private ClienteDAO clienteDAO;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private OrdineService ordineService;

	@PostMapping("/endpoint")
	public ResponseEntity<String> getIdFromToken(@RequestHeader("Authorization") String authorizationHeader) {
	    String token = authorizationHeader.replace("Bearer ", "");
	    Long id = jwtUtil.extractId(token);
	    return ResponseEntity.ok("ID utente estratto: " + id);
	}

	@PostMapping("/crea/ordine")
	public ResponseEntity<Map<String, Object>> creaOrdine(@RequestBody List<CreazioneOrdineDTO> ordineDTO, 
	                                         @RequestHeader("Authorization") String authorizationHeader) {
	    try {
	        // Estrai il token JWT dall'header
	        String token = authorizationHeader.replace("Bearer ", "");
	        
	        // Estrai l'id del cliente dal token JWT
	        Long clienteId = jwtUtil.extractId(token);
	        
	        // Recupera il cliente dal database
	        Optional<Cliente> clienteOptional = clienteDAO.findById(clienteId);
	        if (clienteOptional.isEmpty()) {
	        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Utente non trovato"));
	        }

	        Cliente cliente = clienteOptional.get();
	        
	        // Crea il nuovo ordine
	        ordineService.creaOrdine(ordineDTO, cliente);
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("message", "Creazione dell'ordine completata con successo!");
	        response.put("ordineId", 1);

	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Errore nella creazione dell'ordine");
	        errorResponse.put("message", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}

}

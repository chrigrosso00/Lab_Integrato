package com.lab.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.DTO.CreazioneOrdineDTO;
import com.lab.entities.Cliente;
import com.lab.entities.Ordine;
import com.lab.repos.ClienteDAO;
import com.lab.services.OrdineService;
import com.lab.utils.JwtUtil;

import jakarta.validation.Valid;

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
	public ResponseEntity<String> creaOrdine(@RequestBody @Valid CreazioneOrdineDTO ordineDTO, 
	                                         @RequestHeader("Authorization") String authorizationHeader) {
	    try {
	        // Estrai il token JWT dall'header
	        String token = authorizationHeader.replace("Bearer ", "");
	        
	        // Estrai l'id del cliente dal token JWT
	        Long clienteId = jwtUtil.extractId(token);
	        
	        // Recupera il cliente dal database
	        Optional<Cliente> clienteOptional = clienteDAO.findById(clienteId);
	        if (clienteOptional.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente non trovato con id: " + clienteId);
	        }

	        Cliente cliente = clienteOptional.get();
	        
	        // Crea il nuovo ordine
	        Ordine ordine = ordineService.creaOrdine(ordineDTO, cliente);
	        
	        return ResponseEntity.ok("Creazione Ordine avvenuta con successo! ID Ordine: " + ordine.getId());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante la creazione dell'ordine: " + e.getMessage());
	    }
	}


}

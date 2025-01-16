package com.lab.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab.DTO.CreazioneOrdineDTO;
import com.lab.DTO.PezzoOrdineDTO;
import com.lab.DTO.StatisticheDTO;
import com.lab.DTO.StoricoOrdineDTO;
import com.lab.customException.ForbiddenException;
import com.lab.customException.NotFoundException;
import com.lab.customException.UnauthorizedException;
import com.lab.entities.Cliente;
import com.lab.entities.Ordine;
import com.lab.entities.PezziOrdine;
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

	@GetMapping("/storico/ordini")
	public ResponseEntity<?> getPezzi(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("Autenticazione richiesta.");
			}

			String token = authorizationHeader.substring(7); // Rimuove "Bearer "

			// Decodifica il token
			List<String> roles = jwtUtil.extractRoles(token);

			Integer idCliente = jwtUtil.extractIdUtente(token);

			// Controlla se il ruolo è ROLE_CLIENTE
			if (roles == null || !roles.contains("ROLE_CLIENTE")) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("Accesso negato. Ruolo non autorizzato.");
			}

			// Recupera tutti gli ordini per il cliente
			List<Ordine> ordini = ordineService.getOrdiniByCliente(idCliente);

			// Combina Pezzo e Quantità in PezzoDTO
			List<StoricoOrdineDTO> pezzoDTOs = new ArrayList<>();
			for (Ordine ordine : ordini) {
				int percentualeAvanzamento = calcolaPercentualeAvanzamento(ordine);

				// Trasformiamo ogni PezziOrdine in PezzoOrdineDTO
				List<PezzoOrdineDTO> pezziOrdineDTO = ordine.getPezziOrdine().stream()
						.map(pezzoOrdine -> new PezzoOrdineDTO(
								pezzoOrdine.getPezzo().getCodicePezzo(),  // Codice del pezzo
								pezzoOrdine.getPezzo().getNome(),         // Nome del pezzo
								pezzoOrdine.getPezzo().getImmagineUrl(),  // URL immagine
								pezzoOrdine.getQuantitaTotale(),          // Quantità totale richiesta
								pezzoOrdine.getQuantita(),                // Quantità rimanente
								pezzoOrdine.getPezzo().getPrezzo(),        // Prezzo singolo
								pezzoOrdine.getPezzo().getPrezzo() * pezzoOrdine.getQuantitaTotale() // Prezzo totale
						))
						.collect(Collectors.toList());

				// Crea il DTO principale
				StoricoOrdineDTO dto = new StoricoOrdineDTO(
						ordine.getId(),                  // ID dell'ordine
						ordine.getDataInizio(),          // Data di inizio
						ordine.getDataFine(),            // Data di fine
						pezziOrdineDTO,                  // Lista di PezzoOrdineDTO
						ordine.getStato(),               // Stato dell'ordine
						percentualeAvanzamento           // Percentuale di avanzamento
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

	public int calcolaPercentualeAvanzamento(Ordine ordine) {
		// Se non ci sono pezzi, consideriamo l'ordine completato al 100%
		if (ordine.getPezziOrdine() == null || ordine.getPezziOrdine().isEmpty()) {
			return 100;
		}

		// Variabili per il calcolo
		int totaleQuantitaCompletata = 0;
		int totaleQuantita = 0;

		for (PezziOrdine pezziOrdine : ordine.getPezziOrdine()) {
			int quantitaTotale = pezziOrdine.getQuantitaTotale();
			int quantitaRimanente = pezziOrdine.getQuantita();

			// Se la quantità totale è 0, saltiamo il calcolo per questo pezzo
			if (quantitaTotale == 0) continue;

			// Calcolo della quantità completata
			int quantitaCompletata = quantitaTotale - quantitaRimanente;

			// Aggiungiamo le quantità completate e totali al conteggio complessivo
			totaleQuantitaCompletata += quantitaCompletata;
			totaleQuantita += quantitaTotale;
		}

		// Evitiamo la divisione per zero
		if (totaleQuantita == 0) {
			return 100; // Se non ci sono pezzi, consideriamo completato
		}

		// Calcolo della percentuale
		int percentuale = (int) Math.round(((double) totaleQuantitaCompletata / totaleQuantita) * 100);

		// Limitiamo il massimo al 100%
		return Math.min(percentuale, 100);
	}

	@DeleteMapping("/elimina/ordine/{ordineId}")
	public ResponseEntity<?> eliminaOrdine(
			@RequestHeader("Authorization") String authorizationHeader,
			@PathVariable Long ordineId) {
		try {
			System.out.println("ID Ordine ricevuto: " + ordineId);

			// Controllo autenticazione e autorizzazione
			Integer idCliente = validaToken(authorizationHeader);
			System.out.println("ID Cliente dal token: " + idCliente);

			List<String> roles = jwtUtil.extractRoles(authorizationHeader.substring(7));
			System.out.println("Ruoli utente loggato: " + roles);

			if (roles == null || !roles.contains("ROLE_CLIENTE")) {
				System.out.println("Accesso negato. Ruolo non autorizzato.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("Accesso negato. Ruolo non autorizzato.");
			}

			if (!ordineService.verificaProprietarioOrdine(ordineId, idCliente)) {
				System.out.println("Accesso negato. L'ordine non appartiene all'utente loggato.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("Accesso negato. Questo ordine non ti appartiene.");
			}

			// Annulla l'ordine
			ordineService.setStatoOrdineAnnullato(ordineId);

			return ResponseEntity.noContent().build();

		} catch (UnauthorizedException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (ForbiddenException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Errore interno al server.");
		}
	}

	@GetMapping("/statistiche")
	public ResponseEntity<?> getStatisticheCliente(@RequestHeader("Authorization") String authorizationHeader){

		try {

			// Controllo autenticazione e autorizzazione
			Integer idCliente = validaToken(authorizationHeader);
			System.out.println("ID Cliente dal token: " + idCliente);

			List<String> roles = jwtUtil.extractRoles(authorizationHeader.substring(7));
			System.out.println("Ruoli utente loggato: " + roles);

			if (roles == null || !roles.contains("ROLE_CLIENTE")) {
				System.out.println("Accesso negato. Ruolo non autorizzato.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("Accesso negato. Ruolo non autorizzato.");
			}

			// Annulla l'ordine
			List<Ordine> ordini = ordineService.getOrdiniByCliente(idCliente);

			int ordiniTotali = ordini.size();
			int ordiniCompletati = ordineService.getNumeroOrdiniCompletati(ordini);
			int ordiniInAttesa = ordineService.getNumeroOrdiniInAttesa(ordini);
			int ordiniAnnullati = ordineService.getNumeroOrdiniAnnullati(ordini);

			StatisticheDTO statistiche = new StatisticheDTO(ordiniTotali, ordiniCompletati, ordiniInAttesa, ordiniAnnullati);

			return ResponseEntity.ok(statistiche);

		} catch (UnauthorizedException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (ForbiddenException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Errore interno al server.");
		}
	}

	private Integer validaToken(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new UnauthorizedException("Autenticazione richiesta.");
		}
		String token = authorizationHeader.substring(7); // Rimuove "Bearer "
		return jwtUtil.extractIdUtente(token); // Estrai l'ID dell'utente dal token
	}

}

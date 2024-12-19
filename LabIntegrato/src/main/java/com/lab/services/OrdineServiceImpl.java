package com.lab.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab.DTO.CreazioneOrdineDTO;
import com.lab.customException.NotFoundException;
import com.lab.entities.Cliente;
import com.lab.entities.Ordine;
import com.lab.entities.PezziOrdine;
import com.lab.entities.PezziOrdineId;
import com.lab.entities.Pezzo;
import com.lab.repos.OrdineDAO;
import com.lab.repos.PezzoDAO;

@Service
public class OrdineServiceImpl implements OrdineService{
	
	@Autowired
	private OrdineDAO ordineDAO;
	
	@Autowired
    private PezzoDAO pezzoDAO;
	
	@Autowired
	private MagazzinoService magazzinoService;
	
	public OrdineServiceImpl(OrdineDAO ordineDAO, PezzoDAO pezzoDAO) {
        this.ordineDAO = ordineDAO;
        this.pezzoDAO = pezzoDAO;
    }
	
	@Override
	@Transactional(rollbackFor = RuntimeException.class) 
    public void creaOrdine(List<CreazioneOrdineDTO> prodottiCarrello, Cliente cliente) {
        // 1. Crea l'oggetto Ordine
        Ordine ordine = new Ordine();
        ordine.setCliente(cliente);
        ordine.setDataInizio(LocalDate.now());
        ordine.setStato("IN ATTESA");

        // 2. Salva l'ordine e ottiene l'ID generato
        ordineDAO.save(ordine);
        
        int totaleProdotti = 0;
        
        // 3. Per ogni prodotto nel carrello, controlla la disponibilità e calcola la quantità da produrre
        for (CreazioneOrdineDTO prodotto : prodottiCarrello) {
            // 3.1. Usa il MagazzinoService per controllare la disponibilità
            Map<String, Integer> disponibilita = magazzinoService.controllaDisponibilitaECalcolaProduzione(prodotto.getCodicePezzo(), prodotto.getQuantita());
            
            Pezzo pezzo = pezzoDAO.findById(prodotto.getCodicePezzo())
                    .orElseThrow(() -> new RuntimeException("Pezzo con codice " + prodotto.getCodicePezzo() + " non trovato"));
            
            int quantitaDaMagazzino = disponibilita.get("quantitaDaMagazzino");
            int quantitaDaProdurre = disponibilita.get("quantitaDaProdurre");
            
            totaleProdotti += quantitaDaProdurre + quantitaDaMagazzino;

            // 3.2. Aggiorna la quantità disponibile nel magazzino
            if (quantitaDaMagazzino > 0) {
                magazzinoService.aggiornaQuantitaDisponibile(prodotto.getCodicePezzo(), quantitaDaMagazzino);
            }

            // 3.3. Se la quantità da produrre è maggiore di 0, salva nella tabella PezziOrdine
            PezziOrdineId pezziOrdineId = new PezziOrdineId(ordine.getId(), prodotto.getCodicePezzo());

            PezziOrdine pezziOrdine = new PezziOrdine();
            pezziOrdine.setId(pezziOrdineId);
            pezziOrdine.setOrdine(ordine);
            pezziOrdine.setPezzo(pezzo);
            pezziOrdine.setQuantita(quantitaDaProdurre);
            pezziOrdine.setQuantitaTotale(quantitaDaProdurre + quantitaDaMagazzino);// Salva solo la quantità effettivamente da produrre

            ordine.getPezziOrdine().add(pezziOrdine);
        }
        
        ordine.setTotalePezzi(totaleProdotti);

        // 4. Salva l'ordine e i pezzi associati
        ordineDAO.save(ordine);
    }
	
	@Override
    public List<Ordine> getOrdiniByCliente(Integer idCliente) {
        return ordineDAO.findByClienteId(idCliente);
    }

	@Override
	public void setStatoOrdineAnnullato(Long idOrdine) {
	    // Controllo che l'ID dell'ordine sia valido
	    if (idOrdine == null || idOrdine <= 0) {
	        throw new IllegalArgumentException("ID Ordine non valido.");
	    }

	    // Controllo se l'ordine esiste
	    Ordine ordine = ordineDAO.findById(idOrdine)
				.orElseThrow(() -> new NotFoundException("Ordine con id " + idOrdine + " non trovato"));

	    // Cambia lo stato dell'ordine
	    ordine.setStato("ANNULLATO");

	    // Salva l'ordine aggiornato nel database
	    ordineDAO.save(ordine);
	}
	
	@Override
	public boolean verificaProprietarioOrdine(Long ordineId, Integer idCliente) {
	    Ordine ordine = ordineDAO.findById(ordineId)
	        .orElseThrow(() -> new NotFoundException("Ordine non trovato."));
	    return ordine.getCliente().getId().equals(idCliente.longValue()); // Verifica che l'ordine appartenga al cliente loggato
	}

	@Override
	public int getNumeroOrdiniCompletati(List<Ordine> ordini) {
		int count = 0;
		for (Ordine ordine : ordini) {
			if (ordine.getStato().equals("COMPLETATO")){ // CORRETTO) {
				count ++;
			}
		}
		return count;
	}

	@Override
	public int getNumeroOrdiniInAttesa(List<Ordine> ordini) {
		int count = 0;
		for (Ordine ordine : ordini) {
			if (ordine.getStato().equals("IN ATTESA")) {
				count ++;
			}
		}
		return count;
	}

	@Override
	public int getNumeroOrdiniAnnullati(List<Ordine> ordini) {
		int count = 0;
		for (Ordine ordine : ordini) {
			if (ordine.getStato().equals("ANNULLATO")) {
				count ++;
			}
		}
		return count;
	}


}

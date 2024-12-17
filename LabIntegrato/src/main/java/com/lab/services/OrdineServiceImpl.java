package com.lab.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab.DTO.CreazioneOrdineDTO;
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
        
        // 3. Per ogni prodotto nel carrello, controlla la disponibilità e calcola la quantità da produrre
        for (CreazioneOrdineDTO prodotto : prodottiCarrello) {
            // 3.1. Usa il MagazzinoService per controllare la disponibilità
            Map<String, Integer> disponibilita = magazzinoService.controllaDisponibilitaECalcolaProduzione(prodotto.getCodicePezzo(), prodotto.getQuantita());
            
            Pezzo pezzo = pezzoDAO.findById(prodotto.getCodicePezzo())
                    .orElseThrow(() -> new RuntimeException("Pezzo con codice " + prodotto.getCodicePezzo() + " non trovato"));
            
            int quantitaDaMagazzino = disponibilita.get("quantitaDaMagazzino");
            int quantitaDaProdurre = disponibilita.get("quantitaDaProdurre");

            // 3.2. Aggiorna la quantità disponibile nel magazzino
            if (quantitaDaMagazzino > 0) {
                magazzinoService.aggiornaQuantitaDisponibile(prodotto.getCodicePezzo(), quantitaDaMagazzino);
            }

            // 3.3. Se la quantità da produrre è maggiore di 0, salva nella tabella PezziOrdine
            if (quantitaDaProdurre > 0) {
                PezziOrdineId pezziOrdineId = new PezziOrdineId(ordine.getId(), prodotto.getCodicePezzo());

                PezziOrdine pezziOrdine = new PezziOrdine();
                pezziOrdine.setId(pezziOrdineId);
                pezziOrdine.setOrdine(ordine);
                pezziOrdine.setPezzo(pezzo);
                pezziOrdine.setQuantita(quantitaDaProdurre); // Salva solo la quantità effettivamente da produrre

                ordine.getPezziOrdine().add(pezziOrdine);
            } else {
            }
        }

        // 4. Salva l'ordine e i pezzi associati
        ordineDAO.save(ordine);
    }

}

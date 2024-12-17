package com.lab.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab.DTO.CreazioneOrdineDTO;
import com.lab.entities.Cliente;
import com.lab.entities.Ordine;
import com.lab.entities.PezziOrdine;
import com.lab.entities.PezziOrdineId;
import com.lab.entities.Pezzo;
import com.lab.repos.OrdineDAO;
import com.lab.repos.PezzoDAO;

import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdineServiceImpl implements OrdineService{
	
	@Autowired
	private OrdineDAO ordineDAO;
	
	@Autowired
    private PezzoDAO pezzoDAO;
	
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

        // 3. Per ogni prodotto nel carrello, crea l'oggetto PezziOrdine
        for (CreazioneOrdineDTO prodotto : prodottiCarrello) {
            Pezzo pezzo = pezzoDAO.findById(prodotto.getCodicePezzo())
                    .orElseThrow(() -> new RuntimeException("Pezzo con codice " + prodotto.getCodicePezzo() + " non trovato"));

            PezziOrdineId pezziOrdineId = new PezziOrdineId(ordine.getId(), prodotto.getCodicePezzo());
            
            PezziOrdine pezziOrdine = new PezziOrdine();
            pezziOrdine.setId(pezziOrdineId);
            pezziOrdine.setOrdine(ordine);
            pezziOrdine.setPezzo(pezzo);
            pezziOrdine.setQuantita(prodotto.getQuantita());

            ordine.getPezziOrdine().add(pezziOrdine);
        }

        // 4. Salva l'ordine e i pezzi associati
        ordineDAO.save(ordine);
    }

}

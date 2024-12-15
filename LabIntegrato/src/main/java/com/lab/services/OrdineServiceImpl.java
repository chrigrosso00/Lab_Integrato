package com.lab.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab.DTO.CreazioneOrdineDTO;
import com.lab.entities.Cliente;
import com.lab.entities.Operazione;
import com.lab.entities.Ordine;
import com.lab.entities.PezziOrdine;
import com.lab.repos.OrdineDAO;

import jakarta.validation.Valid;

@Service
public class OrdineServiceImpl implements OrdineService{
	
	@Autowired
	private OrdineDAO ordineDAO;

	@Override
	public Ordine creaOrdine(@Valid CreazioneOrdineDTO ordineDTO, Cliente cliente) {
	    // Crea l'oggetto ordine e imposta i valori di base
	    Ordine ordine = new Ordine();
	    ordine.setCliente(cliente);
	    ordine.setDataInizio(LocalDate.now()); // Imposta la data di inizio come oggi

	    // Associa i PezziOrdine all'Ordine
	    List<PezziOrdine> pezziOrdine = ordineDTO.getPezziOrdine();
	    if (pezziOrdine != null && !pezziOrdine.isEmpty()) {
	        for (PezziOrdine pezzo : pezziOrdine) {
	            pezzo.setOrdine(ordine); // Associa l'ordine al pezzo
	            ordine.addPezzoOrdine(pezzo); // Metodo di supporto per la relazione
	        }
	    }

	    // Associa le Operazioni all'Ordine
	    List<Operazione> operazioni = ordineDTO.getOperazioni();
	    if (operazioni != null && !operazioni.isEmpty()) {
	        for (Operazione operazione : operazioni) {
	            operazione.setOrdine(ordine); // Associa l'operazione all'ordine
	            ordine.addOperazione(operazione); // Metodo di supporto per la relazione
	        }
	    }

	    // Salva l'ordine nel database
	    ordineDAO.save(ordine);

	    return ordine;
	}


}

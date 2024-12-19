package com.lab.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lab.DTO.CreazioneOrdineDTO;
import com.lab.entities.Cliente;
import com.lab.entities.Ordine;

import jakarta.validation.Valid;

@Service
public interface OrdineService {

	void creaOrdine(@Valid List<CreazioneOrdineDTO> ordineDTO, Cliente cliente);
	
	List<Ordine> getOrdiniByCliente(Integer idCliente);
	
	void setStatoOrdineAnnullato(Long idOrdine);
	
	boolean verificaProprietarioOrdine(Long ordineId, Integer idCliente);

	int getNumeroOrdiniCompletati(List<Ordine> ordini);

	int getNumeroOrdiniInAttesa(List<Ordine> ordini);

	int getNumeroOrdiniAnnullati(List<Ordine> ordini);

}

package com.lab.services;

import org.springframework.stereotype.Service;

import com.lab.DTO.CreazioneOrdineDTO;
import com.lab.entities.Cliente;
import com.lab.entities.Ordine;

import jakarta.validation.Valid;

@Service
public interface OrdineService {

	Ordine creaOrdine(@Valid CreazioneOrdineDTO ordineDTO, Cliente cliente);

}

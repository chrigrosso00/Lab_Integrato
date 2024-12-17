package com.lab.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lab.DTO.CreazioneOrdineDTO;
import com.lab.entities.Cliente;

import jakarta.validation.Valid;

@Service
public interface OrdineService {

	void creaOrdine(@Valid List<CreazioneOrdineDTO> ordineDTO, Cliente cliente);

}

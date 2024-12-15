package com.lab.services;

import org.springframework.stereotype.Service;

import com.lab.DTO.UtenteRegistrationDTO;

@Service
public interface ClienteService {

	void registraCliente(UtenteRegistrationDTO utenteDto);

}

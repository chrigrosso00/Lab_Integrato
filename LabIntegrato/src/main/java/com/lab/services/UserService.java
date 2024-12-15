package com.lab.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.lab.DTO.UtenteRegistrationDTO;
import com.lab.entities.User;

@Service
public interface UserService extends JpaRepository<User, Long> {

	void registraUtente(UtenteRegistrationDTO utenteDto);

}

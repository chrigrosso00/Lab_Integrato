package com.lab.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lab.entities.Pezzo;

@Service
public interface PezzoService {
	
	List<Pezzo> getAllPezzi();

}

package com.lab.services;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface MagazzinoService {

	List<Integer> getQuantita(List<Integer> idPezzi);

}

package com.lab.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab.repos.MagazzinoDAO;

@Service
public class MagazzinoServiceImpl implements MagazzinoService{
	
	@Autowired
	MagazzinoDAO magazzinoDAO;

    @Override
    public List<Integer> getQuantita(List<Integer> idPezzi) {
        List<Integer> quantita = new ArrayList<>();
        for (Integer id : idPezzi) {
            int q = magazzinoDAO.findQuantitaDisponibileByPezzoId(id);
            quantita.add(q);
        }
        return quantita;
    }

}

package com.lab.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab.repos.MagazzinoDAO;

import jakarta.transaction.Transactional;

@Service
public class MagazzinoServiceImpl implements MagazzinoService{
	
	@Autowired
	MagazzinoDAO magazzinoDAO;

	@Override
    public List<Integer> getQuantita(List<Integer> idPezzi) {
        List<Integer> quantita = new ArrayList<>();
        for (Integer id : idPezzi) {
            Integer q = magazzinoDAO.findQuantitaDisponibileByPezzoId(id);
            quantita.add(q);
        }
        return quantita;
    }
    
    @Override
    public Integer getDisponibilitaPezzo(Integer codicePezzo) {
        Integer quantitaDisponibile = magazzinoDAO.findQuantitaDisponibileByPezzoId(codicePezzo);
        if (quantitaDisponibile == null) {
            return 0;
        }
        return quantitaDisponibile;
    }
    
    @Override
    public Map<String, Integer> controllaDisponibilitaECalcolaProduzione(Integer codicePezzo, Integer quantitaRichiesta) {
        Integer disponibilitaAttuale = magazzinoDAO.findQuantitaDisponibileByPezzoId(codicePezzo);
        if (disponibilitaAttuale == null) {
            disponibilitaAttuale = 0;
        }

        int quantitaDaMagazzino = Math.min(disponibilitaAttuale, quantitaRichiesta);
        int quantitaDaProdurre = quantitaRichiesta - quantitaDaMagazzino;

        Map<String, Integer> risultato = new HashMap<>();
        risultato.put("quantitaDaMagazzino", quantitaDaMagazzino);
        risultato.put("quantitaDaProdurre", quantitaDaProdurre);
        return risultato;
    }
    
    @Override
    @Transactional
    public void aggiornaQuantitaDisponibile(Integer codicePezzo, Integer quantitaDaSottrarre) {
        magazzinoDAO.decrementaDisponibilita(codicePezzo, quantitaDaSottrarre);
    }

}

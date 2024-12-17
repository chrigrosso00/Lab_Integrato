package com.lab.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface MagazzinoService {

    List<Integer> getQuantita(List<Integer> idPezzi);
    
    Integer getDisponibilitaPezzo(Integer codicePezzo);

    Map<String, Integer> controllaDisponibilitaECalcolaProduzione(Integer codicePezzo, Integer quantita);

    void aggiornaQuantitaDisponibile(Integer codicePezzo, Integer quantitaDaSottrarre);
}

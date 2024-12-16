package com.lab.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab.entities.Pezzo;
import com.lab.repos.PezzoDAO;

@Service
public class PezzoServiceImpl implements PezzoService{
	
	@Autowired
	private PezzoDAO pezzoDAO;

	@Override
    public List<Pezzo> getAllPezzi() {
        return pezzoDAO.findAll();
    }

}

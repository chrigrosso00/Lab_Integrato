package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Pezzo;

@Repository
public interface PezzoDAO extends JpaRepository<Pezzo, Integer> {

}

package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Stampo;

@Repository
public interface StampoDAO extends JpaRepository<Stampo, String> {

}

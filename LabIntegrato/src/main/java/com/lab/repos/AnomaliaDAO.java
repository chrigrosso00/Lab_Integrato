package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Anomalia;

@Repository
public interface AnomaliaDAO extends JpaRepository<Anomalia, Long> {

}

package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.AnomaliaOperazione;

@Repository
public interface AnomaliaOperazioneDAO extends JpaRepository<AnomaliaOperazione, Long> {

}

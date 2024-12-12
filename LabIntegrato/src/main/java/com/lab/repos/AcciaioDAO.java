package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Acciaio;

@Repository
public interface AcciaioDAO extends JpaRepository<Acciaio, Long> {

}

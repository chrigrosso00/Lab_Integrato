package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Operatore;

@Repository
public interface OperatoreDAO extends JpaRepository<Operatore, String> {

}

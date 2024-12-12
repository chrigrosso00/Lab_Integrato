package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Forgiatura;

@Repository
public interface ForgiaturaDAO extends JpaRepository<Forgiatura, Long> {

}

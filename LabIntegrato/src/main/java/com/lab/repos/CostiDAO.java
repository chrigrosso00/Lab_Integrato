package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Costi;

@Repository
public interface CostiDAO extends JpaRepository<Costi, Long> {

}

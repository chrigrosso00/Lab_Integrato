package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Magazzino;

@Repository
public interface MagazzinoDAO extends JpaRepository<Magazzino, Long> {

}

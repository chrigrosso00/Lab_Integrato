package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Acciaio;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AcciaioDAO extends JpaRepository<Acciaio, Long> {

    List<Acciaio> findByPrezzoAlKgBetween(BigDecimal x,BigDecimal y);

}

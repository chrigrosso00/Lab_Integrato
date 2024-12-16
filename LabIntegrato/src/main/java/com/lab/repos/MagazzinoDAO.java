package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.entities.Magazzino;

@Repository
public interface MagazzinoDAO extends JpaRepository<Magazzino, Long> {
	
	// Query personalizzata per ottenere quantitaDisponibile per un dato pezzoId
    @Query("SELECT m.quantitaDisponibile FROM Magazzino m WHERE m.pezzo.id = :pezzoId")
    Integer findQuantitaDisponibileByPezzoId(@Param("pezzoId") Integer pezzoId);

}

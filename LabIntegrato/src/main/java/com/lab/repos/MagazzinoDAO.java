package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lab.entities.Magazzino;
import com.lab.entities.Pezzo;

@Repository
public interface MagazzinoDAO extends JpaRepository<Magazzino, Long> {
    
    @Query("SELECT COALESCE(SUM(m.quantitaDisponibile), 0) FROM Magazzino m WHERE m.pezzo = :pezzo")
    Integer findQuantitaDisponibileByPezzo(@Param("pezzo") Pezzo pezzo);
    
 // Trova la quantità disponibile di un pezzo tramite il suo ID
    @Query("SELECT COALESCE(SUM(m.quantitaDisponibile), 0) FROM Magazzino m WHERE m.pezzo.id = :codicePezzo")
    Integer findQuantitaDisponibileByPezzoId(@Param("codicePezzo") Integer codicePezzo);

    // Decrementa la disponibilità del magazzino per un determinato pezzo
    @Modifying
    @Query("UPDATE Magazzino m SET m.quantitaDisponibile = m.quantitaDisponibile - :quantita WHERE m.pezzo.id = :codicePezzo AND m.quantitaDisponibile >= :quantita")
    void decrementaDisponibilita(@Param("codicePezzo") Integer codicePezzo, @Param("quantita") Integer quantita);
}

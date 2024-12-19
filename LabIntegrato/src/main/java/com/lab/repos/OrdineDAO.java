package com.lab.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Ordine;

@Repository
public interface OrdineDAO extends JpaRepository<Ordine, Long> {
	
	List<Ordine> findByClienteId(Integer idCliente);

}

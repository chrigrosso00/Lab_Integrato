package com.lab.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Cliente;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Long> {

	Optional<Cliente> findByNome(String nome);

	Optional<Cliente> findByPartitaIva(String partitaIva);

}

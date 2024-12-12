package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.Cliente;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Long> {

}

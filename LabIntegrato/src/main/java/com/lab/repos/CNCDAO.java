package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab.entities.CNC;

@Repository
public interface CNCDAO extends JpaRepository<CNC, Long> {

}

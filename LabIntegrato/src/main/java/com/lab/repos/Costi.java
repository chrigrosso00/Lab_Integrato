package com.lab.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Costi extends JpaRepository<Costi, Long> {

}

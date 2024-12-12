package com.lab.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lab.entities.User;

public interface UserDAO extends JpaRepository<User, Long> {
	
    Optional<User> findByUsername(String username);
}

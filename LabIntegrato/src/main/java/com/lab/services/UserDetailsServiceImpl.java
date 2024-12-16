package com.lab.services;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lab.entities.Cliente;
import com.lab.entities.User;
import com.lab.repos.ClienteDAO;
import com.lab.repos.UserDAO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userRepository;
    
    private final ClienteDAO clienteDAO;

    public UserDetailsServiceImpl(UserDAO userRepository, ClienteDAO clienteDAO) {
        this.userRepository = userRepository;
        this.clienteDAO = clienteDAO;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Prova a trovare l'utente nella tabella User
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList())
            );
        }

        // Prova a trovare l'utente nella tabella Cliente
        Cliente cliente = clienteDAO.findByNome(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con Username: " + username));

        return new org.springframework.security.core.userdetails.User(
                cliente.getNome(),
                cliente.getPassword(),
                cliente.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList())
        );
    }
}


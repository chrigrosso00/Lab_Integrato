package com.lab.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lab.DTO.UtenteRegistrationDTO;
import com.lab.entities.Cliente;
import com.lab.entities.Role;
import com.lab.entities.RoleName;
import com.lab.entities.User;
import com.lab.repos.ClienteDAO;
import com.lab.repos.RoleDAO;
import com.lab.repos.UserDAO;
import com.lab.services.ClienteService;
import com.lab.services.UserService;
import com.lab.utils.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
    private UserDAO userRepository;
	
	@Autowired
    private ClienteDAO clienteDAO;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@Autowired
    private RoleDAO roleRepository;
	
	@Autowired
    private ClienteService clienteService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/utente/registrazione")
    public ResponseEntity<String> registraUtente(@RequestBody @Valid UtenteRegistrationDTO utenteDto) {
        try {
            if ("cliente".equals(utenteDto.getAccountType())) {
                // Verifica e salva i dati aggiuntivi per il cliente
                clienteService.registraCliente(utenteDto);
            } else {
                // Registra un normale utente
            	userService.registraUtente(utenteDto);
            }
            return ResponseEntity.ok("Registrazione avvenuta con successo!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante la registrazione: " + e.getMessage());
        }
    }
    
    @PostMapping("/addRole")
    public String addRole(@RequestParam String username, @RequestParam String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);
        return "Role assigned successfully!";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        // Ottieni i ruoli dell'utente e convertili in una lista di stringhe
        List<String> roles = existingUser.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(user.getUsername(), roles, user.getId());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
    
    @PostMapping("/login/cliente")
    public ResponseEntity<Map<String, String>> login(@RequestBody Cliente cliente) {
        try {
            // Recupera il cliente dal database in base al nome
            Cliente existingUser = clienteDAO.findByNome(cliente.getNome());

            // Verifica la password con il PasswordEncoder
            if (!passwordEncoder.matches(cliente.getPassword(), existingUser.getPassword())) {
                throw new RuntimeException("Credenziali non valide");
            }

            // Ottieni i ruoli del cliente e convertili in una lista di stringhe
            List<String> roles = existingUser.getRoles().stream()
                    .map(role -> role.getName().name())
                    .collect(Collectors.toList());

            // Genera il token JWT usando i dati reali dal database
            String token = jwtUtil.generateToken(existingUser.getNome(), roles, existingUser.getId());

            // Crea la mappa di risposta
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login effettuato con successo");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenziali non valide"));
        }
    }

}

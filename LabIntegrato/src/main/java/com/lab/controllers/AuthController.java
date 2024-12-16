package com.lab.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lab.DTO.LoginDTO;
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
    public ResponseEntity<Map<String, String>> registraUtente(@RequestBody @Valid UtenteRegistrationDTO utenteDto) {
        try {
            if ("cliente".equals(utenteDto.getAccountType())) {
                // Verifica e salva i dati aggiuntivi per il cliente
                clienteService.registraCliente(utenteDto);
            } else {
                // Registra un normale utente
            	userService.registraUtente(utenteDto);
            }
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Registrazione avvenuta con successo!");
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
        } catch (Exception e) {
              Map<String, String> errorResponse = new HashMap<>();
              errorResponse.put("message", "Errore durante la registrazione: " + e.getMessage());
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginDTO loginDTO) {
        try {
            if ("cliente".equals(loginDTO.getAccountType())) {
                // Usa Optional e orElseThrow() per ottenere il cliente o lanciare un errore
                Cliente existingUser = clienteDAO.findByPartitaIva(loginDTO.getPartitaIVA())
                    .orElseThrow(() -> new UsernameNotFoundException("Cliente non trovato con Partita IVA: " + loginDTO.getPartitaIVA()));

                if (!passwordEncoder.matches(loginDTO.getPassword(), existingUser.getPassword())) {
                    throw new BadCredentialsException("Credenziali non valide");
                }

                List<String> roles = existingUser.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList());

                String token = jwtUtil.generateToken(existingUser.getNome(), roles, existingUser.getId());

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Login effettuato con successo");
                return ResponseEntity.ok(response);
            } else {
                // Logica per l'utente normale
                User existingUser = userRepository.findByUsername(loginDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con Username: " + loginDTO.getUsername()));

                if (!passwordEncoder.matches(loginDTO.getPassword(), existingUser.getPassword())) {
                    throw new BadCredentialsException("Credenziali non valide");
                }

                List<String> roles = existingUser.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList());

                String token = jwtUtil.generateToken(existingUser.getUsername(), roles, existingUser.getId());

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Login effettuato con successo");
                return ResponseEntity.ok(response);
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Utente non trovato: " + e.getMessage()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenziali non valide"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Errore interno al server"));
        }
    }

}

package com.lab.controllers;

import com.lab.entities.Role;
import com.lab.entities.RoleName;
import com.lab.entities.User;
import com.lab.repos.RoleDAO;
import com.lab.repos.UserDAO;
import com.lab.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserDAO userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleDAO roleRepository;

    public AuthController(UserDAO userRepository, PasswordEncoder passwordEncoder, RoleDAO roleRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
		this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assegna il ruolo predefinito ROLE_USER
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(userRole);
        userRepository.save(user);
        return "User registered successfully!";
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

        String token = jwtUtil.generateToken(user.getUsername(), roles);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}

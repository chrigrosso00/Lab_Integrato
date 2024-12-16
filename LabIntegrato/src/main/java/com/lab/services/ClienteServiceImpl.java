
package com.lab.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lab.DTO.UtenteRegistrationDTO;
import com.lab.entities.Cliente;
import com.lab.entities.Role;
import com.lab.entities.RoleName;
import com.lab.entities.User;
import com.lab.repos.ClienteDAO;
import com.lab.repos.RoleDAO;

@Service
public class ClienteServiceImpl implements ClienteService{
	
	@Autowired
	private RoleDAO roleRepository;
	
	@Autowired
	private ClienteDAO clienteDAO;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public void registraCliente(UtenteRegistrationDTO utenteDto) {
		
		System.out.println(utenteDto);
		
		Cliente cliente = new Cliente();
		cliente.setNome(utenteDto.getNome());
		cliente.setPartitaIva(utenteDto.getPartitaIVA());
		
		cliente.setPassword(passwordEncoder.encode(utenteDto.getPassword()));

        // Assegna il ruolo predefinito ROLE_USER
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        // Assegna il ruolo predefinito ROLE_CLIENTE
        Role clientRole = roleRepository.findByName(RoleName.ROLE_CLIENTE)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        cliente.getRoles().add(userRole);
        cliente.getRoles().add(clientRole);
        clienteDAO.save(cliente);
		
	}

}

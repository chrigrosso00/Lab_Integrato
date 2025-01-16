package com.lab.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UtenteRegistrationDTO {

	@NotBlank(message = "Username non può essere vuoto")
    private String username;

	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
	@NotBlank(message = "La password è obbligatoria")
    @Size(min = 8, message = "La password deve essere di almeno 8 caratteri")
    @jakarta.validation.constraints.Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", 
             message = "La password deve contenere almeno un numero, una lettera maiuscola, una minuscola e un carattere speciale")    
	private String password;

    private String accountType;

	@NotBlank(message = "Nome obbligatorio")
    private String nome;

	@NotBlank(message = "Partita IVA è obbligatoria")
	@Size(min = 11, max = 11, message = "Partita IVA deve essere di 11 caratteri")
    private String partitaIVA;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPartitaIVA() {
		return partitaIVA;
	}
	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}
	
}

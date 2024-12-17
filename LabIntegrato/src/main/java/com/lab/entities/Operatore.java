package com.lab.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Operatori")
public class Operatore {
    @Id
    @Column(name = "codice_operatore")
    private String codiceOperatore;

    @Column(name = "nome")
    private String nome;
    
    @Column(name = "cognome")
    private String cognome;

    @Column(name = "id_costo")
    private int idCosto;

    @OneToMany(mappedBy = "operatore")
    private List<Operazione> operazioni;

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public int getIdCosto() {
		return idCosto;
	}

	public void setIdCosto(int idCosto) {
		this.idCosto = idCosto;
	}

	public String getCodiceOperatore() {
		return codiceOperatore;
	}

	public void setCodiceOperatore(String codiceOperatore) {
		this.codiceOperatore = codiceOperatore;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Operazione> getOperazioni() {
		return operazioni;
	}

	public void setOperazioni(List<Operazione> operazioni) {
		this.operazioni = operazioni;
	}

}

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

    @Column(name = "costo_orario")
    private Double costoOrario;

    @OneToMany(mappedBy = "operatore")
    private List<Operazione> operazioni;

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

	public Double getCostoOrario() {
		return costoOrario;
	}

	public void setCostoOrario(Double costoOrario) {
		this.costoOrario = costoOrario;
	}

	public List<Operazione> getOperazioni() {
		return operazioni;
	}

	public void setOperazioni(List<Operazione> operazioni) {
		this.operazioni = operazioni;
	}

}

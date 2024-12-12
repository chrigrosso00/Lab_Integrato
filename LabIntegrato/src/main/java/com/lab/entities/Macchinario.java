package com.lab.entities;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Macchinari")
public class Macchinario {
    @Id
    @Column(name = "codice_macchinario")
    private String codiceMacchinario;

    @Column(name = "tipo")
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "id_costo")
    private Costi costo;

    @OneToMany(mappedBy = "macchinario")
    private List<Operazione> operazioni;

	public String getCodiceMacchinario() {
		return codiceMacchinario;
	}

	public void setCodiceMacchinario(String codiceMacchinario) {
		this.codiceMacchinario = codiceMacchinario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Costi getCosto() {
		return costo;
	}

	public void setCosto(Costi costo) {
		this.costo = costo;
	}

	public List<Operazione> getOperazioni() {
		return operazioni;
	}

	public void setOperazioni(List<Operazione> operazioni) {
		this.operazioni = operazioni;
	}

}

package com.lab.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Acciai")
public class Acciaio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tipo_acciaio")
    private String tipoAcciaio;

    @Column(name = "prezzo_al_kg")
    private BigDecimal prezzoAlKg;

    @OneToMany(mappedBy = "acciaio")
    private List<Magazzino> magazzinoEntries;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoAcciaio() {
		return tipoAcciaio;
	}

	public void setTipoAcciaio(String tipoAcciaio) {
		this.tipoAcciaio = tipoAcciaio;
	}

	public BigDecimal getPrezzoAlKg() {
		return prezzoAlKg;
	}

	public void setPrezzoAlKg(BigDecimal prezzoAlKg) {
		this.prezzoAlKg = prezzoAlKg;
	}

	public List<Magazzino> getMagazzinoEntries() {
		return magazzinoEntries;
	}

	public void setMagazzinoEntries(List<Magazzino> magazzinoEntries) {
		this.magazzinoEntries = magazzinoEntries;
	}

}

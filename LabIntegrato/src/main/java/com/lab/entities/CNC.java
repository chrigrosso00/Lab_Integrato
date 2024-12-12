package com.lab.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "CNC")
public class CNC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cnc")
    private Long id;

    @Column(name = "numero_pezzi_ora")
    private Integer numeroPezziOra;

    @Column(name = "tipo_fermo")
    private String tipoFermo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroPezziOra() {
		return numeroPezziOra;
	}

	public void setNumeroPezziOra(Integer numeroPezziOra) {
		this.numeroPezziOra = numeroPezziOra;
	}

	public String getTipoFermo() {
		return tipoFermo;
	}

	public void setTipoFermo(String tipoFermo) {
		this.tipoFermo = tipoFermo;
	}

}


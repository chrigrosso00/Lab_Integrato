package com.lab.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Costi")
public class Costi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_costo")
    private Long id;

    @Column(name = "tipo_costo")
    private String tipoCosto;

    @Column(name = "entità_collegata")
    private String entitaCollegata;

    @Column(name = "valore")
    private BigDecimal valore;

    @OneToMany(mappedBy = "costo")
    private List<Macchinario> macchinari;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoCosto() {
		return tipoCosto;
	}

	public void setTipoCosto(String tipoCosto) {
		this.tipoCosto = tipoCosto;
	}

	public String getEntitaCollegata() {
		return entitaCollegata;
	}

	public void setEntitaCollegata(String entitaCollegata) {
		this.entitaCollegata = entitaCollegata;
	}

	public BigDecimal getValore() {
		return valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public List<Macchinario> getMacchinari() {
		return macchinari;
	}

	public void setMacchinari(List<Macchinario> macchinari) {
		this.macchinari = macchinari;
	}

}


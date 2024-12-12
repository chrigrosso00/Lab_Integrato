package com.lab.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Stampi")
public class Stampo {
    @Id
    @Column(name = "codice_stampo")
    private String codiceStampo;

    @Column(name = "descrizione")
    private String descrizione;

	public String getCodiceStampo() {
		return codiceStampo;
	}

	public void setCodiceStampo(String codiceStampo) {
		this.codiceStampo = codiceStampo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}


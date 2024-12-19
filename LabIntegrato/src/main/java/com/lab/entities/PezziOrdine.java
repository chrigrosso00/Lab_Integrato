package com.lab.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;


@Entity
@Table(name = "Pezzi_ordine")
public class PezziOrdine {
	
    @EmbeddedId
    private PezziOrdineId id;

    @ManyToOne
    @MapsId("idOrdine")
    @JoinColumn(name = "id_ordine")
    @JsonIgnore
    private Ordine ordine;

    @ManyToOne
    @MapsId("codicePezzo")
    @JoinColumn(name = "id_pezzo")
    private Pezzo pezzo;

    @Column(name = "quantita_totale")
    private int quantitaTotale;

    @Column(name = "quantita_rimanente")
    private int quantita;

	public PezziOrdineId getId() {
		return id;
	}

	public void setId(PezziOrdineId id) {
		this.id = id;
	}

	public Ordine getOrdine() {
		return ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}

	public Pezzo getPezzo() {
		return pezzo;
	}

	public void setPezzo(Pezzo pezzo) {
		this.pezzo = pezzo;
	}

	public int getQuantitaTotale() {
		return quantitaTotale;
	}

	public void setQuantitaTotale(int quantitaTotale) {
		this.quantitaTotale = quantitaTotale;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public int getQuantita() {
		return quantita;
	}

}

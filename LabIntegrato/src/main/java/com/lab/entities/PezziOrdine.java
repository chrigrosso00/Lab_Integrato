package com.lab.entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
class PezziOrdineId implements Serializable {
    @Column(name = "id_ordine")
    private Long idOrdine;

    @Column(name = "id_pezzo")
    private String codicePezzo;

	public Long getIdOrdine() {
		return idOrdine;
	}

	public void setIdOrdine(Long idOrdine) {
		this.idOrdine = idOrdine;
	}

	public String getCodicePezzo() {
		return codicePezzo;
	}

	public void setCodicePezzo(String codicePezzo) {
		this.codicePezzo = codicePezzo;
	}

}

@Entity
@Table(name = "Pezzi_ordine")
public class PezziOrdine {
    @EmbeddedId
    private PezziOrdineId id;

    @ManyToOne
    @MapsId("idOrdine")
    @JoinColumn(name = "id_ordine")
    private Ordine ordine;

    @ManyToOne
    @MapsId("codicePezzo")
    @JoinColumn(name = "id_pezzo")
    private Pezzo pezzo;

    @Column(name = "quantita")
    private Integer quantita;

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

	public Integer getQuantita() {
		return quantita;
	}

	public void setQuantita(Integer quantita) {
		this.quantita = quantita;
	}

}


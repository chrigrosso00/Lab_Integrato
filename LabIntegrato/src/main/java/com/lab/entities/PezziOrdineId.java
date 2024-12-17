package com.lab.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PezziOrdineId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "id_ordine")
    private Long idOrdine;

    @Column(name = "id_pezzo")
    private int codicePezzo;

    // Costruttore vuoto
    public PezziOrdineId() {}

    // Costruttore con parametri
    public PezziOrdineId(Long idOrdine, int codicePezzo) {
        this.idOrdine = idOrdine;
        this.codicePezzo = codicePezzo;
    }

    // Getters e Setters
    public Long getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(Long idOrdine) {
        this.idOrdine = idOrdine;
    }

    public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getCodicePezzo() {
        return codicePezzo;
    }

    public void setCodicePezzo(int codicePezzo) {
        this.codicePezzo = codicePezzo;
    }

    // Override di equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PezziOrdineId that = (PezziOrdineId) o;
        return Objects.equals(idOrdine, that.idOrdine) && Objects.equals(codicePezzo, that.codicePezzo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdine, codicePezzo);
    }
}

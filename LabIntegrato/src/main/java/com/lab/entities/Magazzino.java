package com.lab.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Magazzino")
public class Magazzino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_magazzino")
    private Long id;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "ubicazione")
    private String ubicazione;

    @Column(name = "capacita_totale")
    private Integer capacitaTotale;

    @ManyToOne
    @JoinColumn(name = "codice_pezzo")
    private Pezzo pezzo;

    @ManyToOne
    @JoinColumn(name = "tipo_acciaio")
    private Acciaio acciaio;

    @Column(name = "quantita_disponibile")
    private Integer quantitaDisponibile;

    @Column(name = "data_ultimo_aggiornamento")
    private LocalDateTime dataUltimoAggiornamento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

	public Integer getCapacitaTotale() {
		return capacitaTotale;
	}

	public void setCapacitaTotale(Integer capacitaTotale) {
		this.capacitaTotale = capacitaTotale;
	}

	public Pezzo getPezzo() {
		return pezzo;
	}

	public void setPezzo(Pezzo pezzo) {
		this.pezzo = pezzo;
	}

	public Acciaio getAcciaio() {
		return acciaio;
	}

	public void setAcciaio(Acciaio acciaio) {
		this.acciaio = acciaio;
	}

	public Integer getQuantitaDisponibile() {
		return quantitaDisponibile;
	}

	public void setQuantitaDisponibile(Integer quantitaDisponibile) {
		this.quantitaDisponibile = quantitaDisponibile;
	}

	public LocalDateTime getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	public void setDataUltimoAggiornamento(LocalDateTime dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

}


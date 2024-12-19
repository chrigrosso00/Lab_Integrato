package com.lab.entities;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Pezzi")
public class Pezzo {
	
    @Id
    @Column(name = "codice_pezzo")
    private int codicePezzo;

    @Column(name = "tipo_acciaio")
    private String tipoAcciaio;
    
    @Column(name = "descrizione")
    private String Descrizione;
    
    @Column(name = "prezzo")
    private Double prezzo;
    
    @Column(name = "immagine")
    private String immagineUrl;
    
    @Column(name = "nome")
    private String nome;

    @Column(name = "peso_min")
    private Double pesoMin;

    @Column(name = "peso_max")
    private Double pesoMax;

    @Column(name = "temperatura_min")
    private Double temperaturaMin;

    @Column(name = "temperatura_max")
    private Double temperaturaMax;

    @OneToMany(mappedBy = "pezzo")
    @JsonIgnore
    private List<PezziOrdine> pezziCommesse;

    @OneToMany(mappedBy = "pezzo")
    @JsonIgnore
    private List<Operazione> operazioni;

    @OneToMany(mappedBy = "pezzo")
    @JsonIgnore
    private List<Magazzino> magazzinoEntries;

	public String getDescrizione() {
		return Descrizione;
	}

	public void setDescrizione(String descrizione) {
		Descrizione = descrizione;
	}

	public String getImmagineUrl() {
		return immagineUrl;
	}

	public void setImmagineUrl(String immagineUrl) {
		this.immagineUrl = immagineUrl;
	}

	public Double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCodicePezzo() {
		return codicePezzo;
	}

	public void setCodicePezzo(int codicePezzo) {
		this.codicePezzo = codicePezzo;
	}

	public String getTipoAcciaio() {
		return tipoAcciaio;
	}

	public void setTipoAcciaio(String tipoAcciaio) {
		this.tipoAcciaio = tipoAcciaio;
	}

	public Double getPesoMin() {
		return pesoMin;
	}

	public void setPesoMin(Double pesoMin) {
		this.pesoMin = pesoMin;
	}

	public Double getPesoMax() {
		return pesoMax;
	}

	public void setPesoMax(Double pesoMax) {
		this.pesoMax = pesoMax;
	}

	public Double getTemperaturaMin() {
		return temperaturaMin;
	}

	public void setTemperaturaMin(Double temperaturaMin) {
		this.temperaturaMin = temperaturaMin;
	}

	public Double getTemperaturaMax() {
		return temperaturaMax;
	}

	public void setTemperaturaMax(Double temperaturaMax) {
		this.temperaturaMax = temperaturaMax;
	}

	public List<PezziOrdine> getPezziCommesse() {
		return pezziCommesse;
	}

	public void setPezziCommesse(List<PezziOrdine> pezziCommesse) {
		this.pezziCommesse = pezziCommesse;
	}

	public List<Operazione> getOperazioni() {
		return operazioni;
	}

	public void setOperazioni(List<Operazione> operazioni) {
		this.operazioni = operazioni;
	}

	public List<Magazzino> getMagazzinoEntries() {
		return magazzinoEntries;
	}

	public void setMagazzinoEntries(List<Magazzino> magazzinoEntries) {
		this.magazzinoEntries = magazzinoEntries;
	}

}

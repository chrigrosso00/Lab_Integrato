package com.lab.DTO;

import java.time.LocalDate;
import java.util.List;

public class StoricoOrdineDTO {
	private Long id_ordine;
	private LocalDate data_inizio;
	private LocalDate data_fine;
	private List<PezzoOrdineDTO> pezzi_ordine;
	private String stato;
	private double percentuale_avanzamento;
	
	public StoricoOrdineDTO(Long id_ordine, LocalDate data_inizio, LocalDate data_fine, List<PezzoOrdineDTO> pezzi_ordione,
			String stato, double percentuale_avanzamento) {
		super();
		this.id_ordine = id_ordine;
		this.data_inizio = data_inizio;
		this.data_fine = data_fine;
		this.pezzi_ordine = pezzi_ordione;
		this.stato = stato;
		this.percentuale_avanzamento = percentuale_avanzamento;
	}
	public Long getId_ordine() {
		return id_ordine;
	}
	public void setId_ordine(Long id_ordine) {
		this.id_ordine = id_ordine;
	}
	public LocalDate getData_inizio() {
		return data_inizio;
	}
	public void setData_inizio(LocalDate data_inizio) {
		this.data_inizio = data_inizio;
	}
	public LocalDate getData_fine() {
		return data_fine;
	}
	public void setData_fine(LocalDate data_fine) {
		this.data_fine = data_fine;
	}
	public List<PezzoOrdineDTO> getPezzi_ordione() {
		return pezzi_ordine;
	}
	public void setPezzi_ordione(List<PezzoOrdineDTO> pezzi_ordione) {
		this.pezzi_ordine = pezzi_ordione;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public double getPercentuale_avanzamento() {
		return percentuale_avanzamento;
	}
	public void setPercentuale_avanzamento(double percentuale_avanzamento) {
		this.percentuale_avanzamento = percentuale_avanzamento;
	}
	public List<PezzoOrdineDTO> getPezzi_ordine() {
		return pezzi_ordine;
	}
	public void setPezzi_ordine(List<PezzoOrdineDTO> pezzi_ordine) {
		this.pezzi_ordine = pezzi_ordine;
	}
	
}

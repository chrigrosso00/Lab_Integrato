package com.lab.DTO;

public class StatisticheDTO {
	private int ordini_totali;
	private int ordini_completati;
	private int ordini_in_attesa;
	private int ordini_annullati;

	public StatisticheDTO(int ordini_totali, int ordini_completati, int ordini_in_attesa, int ordini_annullati) {
		super();
		this.ordini_totali = ordini_totali;
		this.ordini_completati = ordini_completati;
		this.ordini_in_attesa = ordini_in_attesa;
		this.ordini_annullati = ordini_annullati;
	}
	
	public int getOrdini_totali() {
		return ordini_totali;
	}
	public void setOrdini_totali(int ordini_totali) {
		this.ordini_totali = ordini_totali;
	}
	public int getOrdini_completati() {
		return ordini_completati;
	}
	public void setOrdini_completati(int ordini_completati) {
		this.ordini_completati = ordini_completati;
	}
	public int getOrdini_in_attesa() {
		return ordini_in_attesa;
	}
	public void setOrdini_in_attesa(int ordini_in_attesa) {
		this.ordini_in_attesa = ordini_in_attesa;
	}
	public int getOrdini_annullati() {
		return ordini_annullati;
	}
	public void setOrdini_annullati(int ordini_annullati) {
		this.ordini_annullati = ordini_annullati;
	}
	
	
}

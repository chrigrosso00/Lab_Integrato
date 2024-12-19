package com.lab.DTO;

public class PezzoOrdineDTO {
	
    private int codicePezzo;
    private String nome;
    private String immagineUrl;
    private int quantitaTotale;
    private int quantita;
    private double prezzo;
    private double prezzoTotalePezzo;
    
    public PezzoOrdineDTO(int codicePezzo, String nome, String immagineUrl, int quantitaTotale, int quantita, double prezzo, double prezzoTotalePezzo) {
        this.codicePezzo = codicePezzo;
        this.nome = nome;
        this.immagineUrl = immagineUrl;
        this.quantitaTotale = quantitaTotale;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.prezzoTotalePezzo = prezzoTotalePezzo;
    }

	public int getCodicePezzo() {
		return codicePezzo;
	}

	public void setCodicePezzo(int codicePezzo) {
		this.codicePezzo = codicePezzo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getImmagineUrl() {
		return immagineUrl;
	}

	public void setImmagineUrl(String immagineUrl) {
		this.immagineUrl = immagineUrl;
	}

	public int getQuantitaTotale() {
		return quantitaTotale;
	}

	public void setQuantitaTotale(int quantitaTotale) {
		this.quantitaTotale = quantitaTotale;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public double getPrezzoTotalePezzo() {
		return prezzoTotalePezzo;
	}

	public void setPrezzoTotalePezzo(double prezzoTotalePezzo) {
		this.prezzoTotalePezzo = prezzoTotalePezzo;
	}
    
}

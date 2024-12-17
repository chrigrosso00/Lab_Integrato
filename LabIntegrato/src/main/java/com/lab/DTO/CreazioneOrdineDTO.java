package com.lab.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreazioneOrdineDTO {
	
	@NotNull(message = "Il codice del pezzo non può essere nullo")
	private int codicePezzo;
	
	@Min(value = 1, message = "La quantità deve essere almeno 1")
    private int quantita;

    // Getters e Setters
    public int getCodicePezzo() {
        return codicePezzo;
    }

    public void setCodicePezzo(int codicePezzo) {
        this.codicePezzo = codicePezzo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

}

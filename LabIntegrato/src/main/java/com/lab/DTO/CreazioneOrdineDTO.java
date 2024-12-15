package com.lab.DTO;

import java.util.List;

import com.lab.entities.Operazione;
import com.lab.entities.PezziOrdine;

import jakarta.validation.constraints.NotEmpty;

public class CreazioneOrdineDTO {
	@NotEmpty(message = "La lista delle operazioni non può essere vuota")
    private List<Operazione> operazioni;

    @NotEmpty(message = "La lista dei pezzi ordine non può essere vuota")
    private List<PezziOrdine> pezziOrdine;
    
	public List<Operazione> getOperazioni() {
		return operazioni;
	}
	public void setOperazioni(List<Operazione> operazioni) {
		this.operazioni = operazioni;
	}
	public List<PezziOrdine> getPezziOrdine() {
		return pezziOrdine;
	}
	public void setPezziOrdine(List<PezziOrdine> pezziOrdine) {
		this.pezziOrdine = pezziOrdine;
	}

}

package com.lab.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Ordine")
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ordine")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @Column(name = "data_inizio")
    private LocalDate dataInizio;

    @Column(name = "data_fine")
    private LocalDate dataFine;

    @Column(name = "stato")
    private String stato;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Operazione> operazioni = new ArrayList<>();

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PezziOrdine> pezziOrdine = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
	}

	public LocalDate getDataFine() {
		return dataFine;
	}

	public void setDataFine(LocalDate dataFine) {
		this.dataFine = dataFine;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

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
	
    public void addOperazione(Operazione operazione) {
        operazioni.add(operazione);
        operazione.setOrdine(this);
    }

    public void addPezzoOrdine(PezziOrdine pezzoOrdine) {
        pezziOrdine.add(pezzoOrdine);
        pezzoOrdine.setOrdine(this);
    }
	
}


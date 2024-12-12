package com.lab.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(mappedBy = "ordine")
    private List<Operazione> operazioni;

    @OneToMany(mappedBy = "ordine")
    private List<PezziOrdine> pezziOrdine;

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
	
}


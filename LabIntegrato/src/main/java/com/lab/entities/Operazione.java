package com.lab.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Operazioni")
public class Operazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operazione")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ordine")
    private Ordine ordine;

    @ManyToOne
    @JoinColumn(name = "codice_pezzo")
    private Pezzo pezzo;

    @ManyToOne
    @JoinColumn(name = "codice_macchinario")
    private Macchinario macchinario;

    @ManyToOne
    @JoinColumn(name = "codice_operatore")
    private Operatore operatore;

    @Column(name = "timestamp_inizio")
    private LocalDateTime timestampInizio;

    @Column(name = "timestamp_fine")
    private LocalDateTime timestampFine;

    @OneToOne(mappedBy = "operazione")
    private AnomaliaOperazione anomaliaOperazione;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ordine getOrdine() {
		return ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}

	public Pezzo getPezzo() {
		return pezzo;
	}

	public void setPezzo(Pezzo pezzo) {
		this.pezzo = pezzo;
	}

	public Macchinario getMacchinario() {
		return macchinario;
	}

	public void setMacchinario(Macchinario macchinario) {
		this.macchinario = macchinario;
	}

	public Operatore getOperatore() {
		return operatore;
	}

	public void setOperatore(Operatore operatore) {
		this.operatore = operatore;
	}

	public LocalDateTime getTimestampInizio() {
		return timestampInizio;
	}

	public void setTimestampInizio(LocalDateTime timestampInizio) {
		this.timestampInizio = timestampInizio;
	}

	public LocalDateTime getTimestampFine() {
		return timestampFine;
	}

	public void setTimestampFine(LocalDateTime timestampFine) {
		this.timestampFine = timestampFine;
	}

	public AnomaliaOperazione getAnomaliaOperazione() {
		return anomaliaOperazione;
	}

	public void setAnomaliaOperazione(AnomaliaOperazione anomaliaOperazione) {
		this.anomaliaOperazione = anomaliaOperazione;
	}

}


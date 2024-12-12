package com.lab.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Anomalia_operazione")
public class AnomaliaOperazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long internalId;

    @ManyToOne
    @JoinColumn(name = "id_anomalia")
    private Anomalia anomalia;

    @OneToOne
    @JoinColumn(name = "id_operazione")
    private Operazione operazione;

    @Column(name = "note")
    private String note;

	public Long getInternalId() {
		return internalId;
	}

	public void setInternalId(Long internalId) {
		this.internalId = internalId;
	}

	public Anomalia getAnomalia() {
		return anomalia;
	}

	public void setAnomalia(Anomalia anomalia) {
		this.anomalia = anomalia;
	}

	public Operazione getOperazione() {
		return operazione;
	}

	public void setOperazione(Operazione operazione) {
		this.operazione = operazione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}

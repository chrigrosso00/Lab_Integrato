package com.lab.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Anomalia")
public class Anomalia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anomalia")
    private Long id;

    @Column(name = "tipo_anomalia")
    private String tipoAnomalia;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "anomalia")
    private List<Forgiatura> forgiature;

    @OneToMany(mappedBy = "anomalia")
    private List<AnomaliaOperazione> anomaliaOperazioni;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipoAnomalia() {
		return tipoAnomalia;
	}

	public void setTipoAnomalia(String tipoAnomalia) {
		this.tipoAnomalia = tipoAnomalia;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<Forgiatura> getForgiature() {
		return forgiature;
	}

	public void setForgiature(List<Forgiatura> forgiature) {
		this.forgiature = forgiature;
	}

	public List<AnomaliaOperazione> getAnomaliaOperazioni() {
		return anomaliaOperazioni;
	}

	public void setAnomaliaOperazioni(List<AnomaliaOperazione> anomaliaOperazioni) {
		this.anomaliaOperazioni = anomaliaOperazioni;
	}

}

package com.lab.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Forgiatura")
public class Forgiatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_forgiatura")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_operazione", referencedColumnName = "id_operazione") 
    private Operazione operazione;
    
    @Column(name = "peso_effettivo")
    private Double pesoEffettivo;

    @Column(name = "temperatura_effettiva")
    private Double temperaturaEffettiva;

    @ManyToOne
    @JoinColumn(name = "id_anomalia")
    private Anomalia anomalia;

	public Operazione getOperazione() {
		return operazione;
	}

	public void setOperazione(Operazione operazione) {
		this.operazione = operazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPesoEffettivo() {
		return pesoEffettivo;
	}

	public void setPesoEffettivo(Double pesoEffettivo) {
		this.pesoEffettivo = pesoEffettivo;
	}

	public Double getTemperaturaEffettiva() {
		return temperaturaEffettiva;
	}

	public void setTemperaturaEffettiva(Double temperaturaEffettiva) {
		this.temperaturaEffettiva = temperaturaEffettiva;
	}

	public Anomalia getAnomalia() {
		return anomalia;
	}

	public void setAnomalia(Anomalia anomalia) {
		this.anomalia = anomalia;
	}

}

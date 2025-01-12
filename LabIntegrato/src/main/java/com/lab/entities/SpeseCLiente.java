package com.lab.entities;

import jakarta.persistence.*;

@Entity
public class SpeseCLiente {

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tot;

    public Long getTot() {
        return tot;
    }
    public void setTot(Long tot) {
        this.tot = tot;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
}

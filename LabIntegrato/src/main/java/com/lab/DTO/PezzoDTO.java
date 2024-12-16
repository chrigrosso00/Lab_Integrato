package com.lab.DTO;

public class PezzoDTO {
    private int id;
    private String nome;
    private String immagineUrl;
    private double prezzo;
    private int disponibilita;

    // Costruttori
    public PezzoDTO() {}

    public PezzoDTO(int id, String nome, String immagineUrl, double prezzo, int disponibilita) {
        this.id = id;
        this.nome = nome;
        this.immagineUrl = immagineUrl;
        this.prezzo = prezzo;
        this.disponibilita = disponibilita;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public double getPrezzo() {
        return prezzo;
    }
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }
    public int getDisponibilita() {
        return disponibilita;
    }
    public void setDisponibilita(int disponibilita) {
        this.disponibilita = disponibilita;
    }
}

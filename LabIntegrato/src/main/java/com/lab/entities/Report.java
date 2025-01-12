package com.lab.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String text;
    Long superVisorId;
    Date timestamp;
    String codMacchinario;
    Long userId;
    String stato;

    public Report(String text, Long superVisorId, Date timestamp, String codMacchinario, Long userId, String stato) {
        this.text = text;
        this.superVisorId = superVisorId;
        this.timestamp = timestamp;
        this.codMacchinario = codMacchinario;
        this.userId = userId;
        this.stato = stato;
    }
    public Report() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getText() {
        return text;
    }

    public void setText(@NotBlank String text) {
        this.text = text;
    }

    public Long getSuperVisorId() {
        return superVisorId;
    }

    public void setSuperVisorId(Long superVisorId) {
        this.superVisorId = superVisorId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCodMacchinario() {
        return codMacchinario;
    }

    public void setCodMacchinario(String codMacchinario) {
        this.codMacchinario = codMacchinario;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}

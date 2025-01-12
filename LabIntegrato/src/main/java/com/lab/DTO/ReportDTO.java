package com.lab.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class ReportDTO {





    @NotBlank
    String testo;
    Long superVisorId;
    Date timestamp;
    String codiceMacchinario;
    Long userID;
    String statoReport;

    public ReportDTO(String codiceMacchinario, Date timestamp, Long superVisorId, String testo,Long userID) {
        this.codiceMacchinario = codiceMacchinario;
        this.timestamp = timestamp;
        this.superVisorId = superVisorId;
        this.testo = testo;
        this.userID = userID;
    }

    public @NotBlank String getTesto() {
        return testo;
    }

    public void setTesto(@NotBlank String testo) {
        this.testo = testo;
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

    public String getCodiceMacchinario() {
        return codiceMacchinario;
    }

    public void setCodiceMacchinario(String codiceMacchinario) {
        this.codiceMacchinario = codiceMacchinario;
    }

    public Long getUserID() {
        return userID;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public String getStatoReport() {
        return statoReport;
    }
    public void setStatoReport(String statoReport) {
        this.statoReport = statoReport;
    }

}

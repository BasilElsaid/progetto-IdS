package it.unicam.filiera.controllers.dto.create;

import java.time.LocalDateTime;

public class CreateEventoRequest {

    private String nome;
    private LocalDateTime dataOra;
    private Double prezzo;
    private String tipo;
    private Integer posti;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getPosti() {
        return posti;
    }

    public void setPosti(Integer posti) {
        this.posti = posti;
    }
}
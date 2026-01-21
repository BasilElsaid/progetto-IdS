package it.unicam.filiera.controllers.dto.response;

import java.time.LocalDateTime;

public class EventoResponse {

    private Long id;
    private String nome;
    private LocalDateTime dataOra;
    private Double prezzo;
    private String tipo;
    private int posti;

    // Costruttore completo
    public EventoResponse(Long id, String nome, LocalDateTime dataOra, Double prezzo, String tipo, int posti) {
        this.id = id;
        this.nome = nome;
        this.dataOra = dataOra;
        this.prezzo = prezzo;
        this.tipo = tipo;
        this.posti = posti;
    }

    // Costruttore vuoto per Jackson
    public EventoResponse() {
    }

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public Double getPrezzo() { return prezzo; }
    public void setPrezzo(Double prezzo) { this.prezzo = prezzo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getPosti() { return posti; }
    public void setPosti(int posti) { this.posti = posti; }
}
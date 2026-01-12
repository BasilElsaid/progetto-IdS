package it.unicam.filiera.models;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class UtenteGenerico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String username;
    protected String email;

    @Enumerated(EnumType.STRING)
    protected Ruolo ruolo;

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Ruolo getRuolo() { return ruolo; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }
}

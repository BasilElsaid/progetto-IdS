package it.unicam.filiera.models;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class UtenteGenerico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true, nullable = false)
    protected String username;

    @Column(unique = true, nullable = false)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Enumerated(EnumType.STRING)
    protected Ruolo ruolo;

    protected boolean attivo = true;

    // ===== GETTER =====
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Ruolo getRuolo() { return ruolo; }
    public boolean isAttivo() { return attivo; }

    // ===== SETTER =====
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }
    public void setAttivo(boolean attivo) { this.attivo = attivo; }
}

package it.unicam.filiera.models;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_utente")
public class UtenteGenerico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * NOTA IMPORTANTE (per il prof):
     * - UTENTE_GENERICO = guest: può consultare informazioni ma NON ha login (username/password null).
     * - Tutti gli altri ruoli possono avere credenziali (gestite via /api/auth/register + /api/auth/login).
     */
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo = Ruolo.UTENTE_GENERICO;

    private String username; // null per guest
    private String email;    // opzionale
    private String password; // null per guest (oppure hashed in futuro)

    public UtenteGenerico() {}

    // GET
    public Long getId() { return id; }
    public Ruolo getRuolo() { return ruolo; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    // SET
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}

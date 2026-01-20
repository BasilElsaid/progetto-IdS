package it.unicam.filiera.domain;

import it.unicam.filiera.enums.StatoRimborso;
import it.unicam.filiera.models.Acquirente;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "richieste_rimborso")
public class RichiestaRimborso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Ordine ordine;

    @ManyToOne(optional = false)
    private Acquirente acquirente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoRimborso stato = StatoRimborso.IN_VALUTAZIONE;

    @Column(nullable = false, length = 2000)
    private String motivazione;

    private String notaGestore;

    @Column(nullable = false)
    private LocalDateTime richiestaIl = LocalDateTime.now();

    private LocalDateTime decisoIl;

    protected RichiestaRimborso() {}

    public RichiestaRimborso(Ordine ordine, Acquirente acquirente, String motivazione) {
        this.ordine = ordine;
        this.acquirente = acquirente;
        this.motivazione = motivazione;
        this.stato = StatoRimborso.IN_VALUTAZIONE;
        this.richiestaIl = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Ordine getOrdine() { return ordine; }
    public Acquirente getAcquirente() { return acquirente; }

    public StatoRimborso getStato() { return stato; }
    public void setStato(StatoRimborso stato) { this.stato = stato; }

    public String getMotivazione() { return motivazione; }
    public String getNotaGestore() { return notaGestore; }
    public void setNotaGestore(String notaGestore) { this.notaGestore = notaGestore; }

    public LocalDateTime getRichiestaIl() { return richiestaIl; }
    public LocalDateTime getDecisoIl() { return decisoIl; }
    public void setDecisoIl(LocalDateTime decisoIl) { this.decisoIl = decisoIl; }
}

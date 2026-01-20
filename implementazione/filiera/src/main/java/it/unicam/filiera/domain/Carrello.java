package it.unicam.filiera.domain;

import it.unicam.filiera.models.Acquirente;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrelli")
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Acquirente acquirente;

    private LocalDateTime creatoIl = LocalDateTime.now();

    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarrelloItem> items = new ArrayList<>();

    protected Carrello() {}

    public Carrello(Acquirente acquirente) {
        this.acquirente = acquirente;
        this.creatoIl = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Acquirente getAcquirente() { return acquirente; }
    public LocalDateTime getCreatoIl() { return creatoIl; }

    public List<CarrelloItem> getItems() { return items; }

    public void addItem(CarrelloItem item) {
        item.setCarrello(this);
        items.add(item);
    }
}

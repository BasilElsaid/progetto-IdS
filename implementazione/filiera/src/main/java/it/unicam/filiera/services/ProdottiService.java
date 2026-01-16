package it.unicam.filiera.services;

import java.util.List;

import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.UtenteGenerico;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.repositories.ProdottoRepository;

@Service
public class ProdottiService {

    private final ProdottoRepository repo;

    public List<Prodotto> list(String nome, String categoria) {
        return repo.findAll();
    }

    public ProdottiService(ProdottoRepository repo) {
        this.repo = repo;
    }

    public Prodotto crea(Prodotto p) {
        Produttore produttore = getProduttoreLoggato();
        p.setProduttore(produttore);
        return repo.save(p);
    }

    public Prodotto get(Long id) {
        Produttore produttore = getProduttoreLoggato();

        return repo.findByIdAndProduttore(id, produttore)
                .orElseThrow(() ->
                        new NotFoundException("Prodotto con id " + id + " non trovato"));
    }

    public List<Prodotto> all() {
        return repo.findByProduttore(getProduttoreLoggato());
    }

    public Prodotto update(Long id, Prodotto p) {
        Produttore produttore = getProduttoreLoggato();
        Prodotto existing = repo.findByIdAndProduttore(id, produttore)
                .orElseThrow(() ->
                        new NotFoundException("Prodotto non trovato"));
        existing.setNome(p.getNome());
        existing.setCategoria(p.getCategoria());
        existing.setPrezzo(p.getPrezzo());

        return repo.save(existing);
    }

    public void delete(Long id) {
        Produttore produttore = getProduttoreLoggato();
        Prodotto existing = repo.findByIdAndProduttore(id, produttore)
                .orElseThrow(() ->
                        new NotFoundException("Prodotto non trovato"));

        repo.delete(existing);
    }

    public Prodotto addProcesso(Long id, String descrizione) {
        return get(id);
    }

    public Prodotto addCertificazione(Long id, String nome) {
        return get(id);
    }

    private Produttore getProduttoreLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof Produttore produttore)) {
            throw new ForbiddenException("Utente non autenticato o non produttore");
        }

        return produttore;
    }

}

package it.unicam.filiera.services;

import java.util.List;

import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.UtenteGenerico;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('PRODUTTORE')")
    public Prodotto crea(Prodotto p) {
        // prendi l'utente loggato dal SecurityContext
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UtenteGenerico produttoreLoggato = (UtenteGenerico) auth.getPrincipal();

        // associa il produttore loggato al prodotto
        p.setProduttore((Produttore) produttoreLoggato);

        return repo.save(p);
    }

    public Prodotto get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PreAuthorize("hasRole('PRODUTTORE')")
    public List<Prodotto> all() {
        Produttore produttore = (Produttore) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repo.findByProduttore(produttore);
    }

    @PreAuthorize("hasRole('PRODUTTORE')")
    public Prodotto update(Long id, Prodotto p) {
        Prodotto existing = get(id);
        Produttore produttore = (Produttore) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!existing.getProduttore().getId().equals(produttore.getId())) {
            throw new SecurityException("Non puoi modificare prodotti di altri produttori");
        }
        existing.setNome(p.getNome());
        existing.setCategoria(p.getCategoria());
        existing.setPrezzo(p.getPrezzo());
        return repo.save(existing);
    }

    @PreAuthorize("hasRole('PRODUTTORE')")
    public void delete(Long id) {
        Prodotto existing = get(id);
        Produttore produttore = (Produttore) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!existing.getProduttore().getId().equals(produttore.getId())) {
            throw new SecurityException("Non puoi eliminare prodotti di altri produttori");
        }
        repo.deleteById(id);
    }

    public Prodotto addProcesso(Long id, String descrizione) {
        return get(id);
    }

    public Prodotto addCertificazione(Long id, String nome) {
        return get(id);
    }

}

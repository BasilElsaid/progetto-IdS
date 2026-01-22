package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.create.CreateProdottoRequest;
import it.unicam.filiera.controllers.dto.response.ProdottoResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.services.ProdottiService;

@RestController
@RequestMapping("/api/prodotti")
@PreAuthorize("hasAnyRole('PRODUTTORE', 'GESTORE_PIATTAFORMA')")
public class ProdottiController {

    private final ProdottiService service;

    public ProdottiController(ProdottiService service) {
        this.service = service;
    }

    @PostMapping
    public ProdottoResponse crea(@RequestBody @Valid CreateProdottoRequest dto) {
        return service.crea(dto);
    }

    @GetMapping("/{id}")
    public ProdottoResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<ProdottoResponse> all() {
        return service.all();
    }

    @PutMapping("/{id}")
    public ProdottoResponse update(
            @PathVariable Long id,
            @RequestBody @Valid CreateProdottoRequest dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

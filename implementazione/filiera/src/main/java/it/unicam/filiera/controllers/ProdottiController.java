package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateProdottoRequest;
import it.unicam.filiera.dto.response.ProdottoResponse;
import it.unicam.filiera.dto.update.UpdateProdottoRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import it.unicam.filiera.services.ProdottiService;

@RestController
@RequestMapping("/api/prodotti")
@Tag(name = "05 - Prodotti", description = "Gestione prodotti")
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

    @PatchMapping("/{id}")
    public ProdottoResponse patch(@PathVariable Long id,
                                  @RequestBody UpdateProdottoRequest dto) {
        return service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

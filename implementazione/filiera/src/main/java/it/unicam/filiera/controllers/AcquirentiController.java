package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.CreateUtenteRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.services.AcquirentiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acquirenti")
@Tag(name = "Acquirenti", description = "Gestione Acquirenti")
public class AcquirentiController {

    private final AcquirentiService service;

    public AcquirentiController(AcquirentiService service) {
        this.service = service;
    }

    @PostMapping
    public Acquirente crea(@RequestBody CreateUtenteRequest request) {
        return service.creaAcquirente(request);
    }

    @GetMapping
    public List<UtenteResponse> lista() {
        return service.listaAcquirenti();
    }

    @GetMapping("/{id}")
    public UtenteResponse get(@PathVariable Long id) {
        return service.getAcquirente(id);
    }
}
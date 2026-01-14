package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.CreateAziendaRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.services.AziendeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aziende")
@Tag(name = "Aziende", description = "Gestione Produttori, Trasformatori e Distributori di tipicità")
public class AziendeController {

    private final AziendeService service;

    public AziendeController(AziendeService service) {
        this.service = service;
    }

    @PostMapping
    public Object crea(@RequestBody CreateAziendaRequest request) {
        // switch interno per decidere il tipo di azienda
        return service.creaAzienda(request);
    }

    @GetMapping
    public List<UtenteResponse> listaTutti() {
        return service.listaAziende();
    }

    @GetMapping("/{id}")
    public UtenteResponse get(@PathVariable Long id) {
        return service.getAzienda(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteAzienda(id);
    }

    @PatchMapping("/{id}")
    public UtenteResponse patch(@PathVariable Long id, @RequestBody CreateAziendaRequest request) {
        return service.patchAzienda(id, request);
    }
}
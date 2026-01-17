package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.controllers.dto.CreateProcessoRequest;
import it.unicam.filiera.domain.ProcessoTrasformazione;
import it.unicam.filiera.services.ProcessiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/processi")
public class ProcessiController {

    private final ProcessiService service;

    public ProcessiController(ProcessiService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProcessoTrasformazione> lista() {
        return service.lista();
    }

    @PostMapping
    public ProcessoTrasformazione crea(@RequestBody CreateProcessoRequest request) {
        return service.crea(request);
    }

    @PostMapping("/{id}/chiudi")
    public ProcessoTrasformazione chiudi(@PathVariable Long id) {
        return service.chiudi(id);
    }
}


package it.unicam.filiera.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/processi")
@PreAuthorize("hasAnyRole('TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
public class ProcessiController {

    @PostMapping("/{id}/chiudi")
    public String chiudi(@PathVariable Long id) {
        return "Processo " + id + " chiuso";
    }
}


package it.unicam.filiera.controllers;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/processi")
public class ProcessiController {

    @PostMapping("/{id}/chiudi")
    public String chiudi(@PathVariable Long id) {
        return "Processo " + id + " chiuso";
    }
}

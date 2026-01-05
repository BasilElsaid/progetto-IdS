
package it.unicam.filiera.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import it.unicam.filiera.models.Prodotto;
import it.unicam.filiera.services.ProdottiService;




@RestController
@RequestMapping("/api/processi")
public class ProcessiController {

    @PostMapping("/{id}/chiudi")
    public String chiudi(@PathVariable Long id) {
        return "Processo " + id + " chiuso";
    }
}

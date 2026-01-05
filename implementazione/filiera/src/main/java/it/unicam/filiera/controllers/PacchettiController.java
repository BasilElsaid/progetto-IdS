package it.unicam.filiera.controllers;

import it.unicam.filiera.prodotto.Pacchetto;
import it.unicam.filiera.repositories.PacchettiRepo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacchetti")
public class PacchettiController {

    private final PacchettiRepo pacchettiRepo;

    public PacchettiController(PacchettiRepo pacchettiRepo) {
        this.pacchettiRepo = pacchettiRepo;
    }

    @GetMapping
    public List<Pacchetto> list() {
        return pacchettiRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pacchetto create(@RequestBody Pacchetto p) {
        return pacchettiRepo.save(p);
    }
}

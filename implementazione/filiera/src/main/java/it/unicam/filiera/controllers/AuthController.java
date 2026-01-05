package it.unicam.filiera.controllers;

import org.springframework.web.bind.annotation.*;

import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.repositories.ProduttoreRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ProduttoreRepository produttoreRepository;

    public AuthController(ProduttoreRepository produttoreRepository) {
        this.produttoreRepository = produttoreRepository;
    }

    @PostMapping("/register")
    public Produttore register(@RequestBody Produttore produttore) {
        return produttoreRepository.save(produttore);
    }

    @PostMapping("/login")
    public String login() {
        return "TOKEN_FAKE_OK";
    }
}

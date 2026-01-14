package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CreateAziendaRequest;
import it.unicam.filiera.controllers.dto.LoginRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UtenteResponse register(@RequestBody @Valid CreateAziendaRequest request) {
        return UtenteResponse.from(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}

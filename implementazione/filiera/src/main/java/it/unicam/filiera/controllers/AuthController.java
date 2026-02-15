package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateAcquirenteRequest;
import it.unicam.filiera.dto.create.CreateAziendaRequest;
import it.unicam.filiera.dto.create.CreateLoginRequest;
import it.unicam.filiera.dto.create.CreatePersonaleRequest;
import it.unicam.filiera.dto.response.LoginResponse;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.services.AuthService;
import it.unicam.filiera.services.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "01 - Auth", description = "Gestione autenticazione Registrazione e Login")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody CreateLoginRequest request) {
        UtenteGenerico user =
                authService.authenticate(request.username(), request.password());

        return new LoginResponse(jwtService.generateToken(user));
    }

    @PostMapping("/register/aziende")
    public List<UtenteResponse> registerAziende(@RequestBody List<CreateAziendaRequest> reqs) {
        return reqs.stream()
                .map(authService::registerAzienda)
                .toList();
    }

    @PostMapping("/register/personale")
    public List<UtenteResponse> registerPersonale(@RequestBody List<CreatePersonaleRequest> reqs) {
        return reqs.stream()
                .map(authService::registerPersonale)
                .toList();
    }

    @PostMapping("/register/acquirenti")
    public List<UtenteResponse> registerAcquirenti(@RequestBody List<CreateAcquirenteRequest> reqs) {
        return reqs.stream()
                .map(authService::registerAcquirente)
                .toList();
    }
}
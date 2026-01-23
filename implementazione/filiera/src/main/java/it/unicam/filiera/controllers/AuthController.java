package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateLoginRequest;
import it.unicam.filiera.dto.response.LoginResponse;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.services.AuthService;
import it.unicam.filiera.services.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "01 - Auth", description = "Gestione autenticazione Login")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService,
                                   JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody CreateLoginRequest request) {
        UtenteGenerico user =
                authService.authenticate(request.getUsername(), request.getPassword());

        return new LoginResponse(jwtService.generateToken(user));
    }
}
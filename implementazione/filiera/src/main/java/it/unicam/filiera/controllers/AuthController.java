package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.LoginRequest;
import it.unicam.filiera.controllers.dto.LoginResponse;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.services.AuthService;
import it.unicam.filiera.services.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService,
                                   JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        UtenteGenerico user =
                authService.authenticate(request.getUsername(), request.getPassword());

        return new LoginResponse(jwtService.generateToken(user));
    }
}
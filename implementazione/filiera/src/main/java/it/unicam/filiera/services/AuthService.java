package it.unicam.filiera.services;

import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.models.*;
import it.unicam.filiera.repositories.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UtenteRepository utenteRepository,
                       PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UtenteGenerico authenticate(String username, String password) {
        UtenteGenerico u = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("Utente non trovato"));

        if (!passwordEncoder.matches(password, u.getPassword())) {
            throw new BadRequestException("Password errata");
        }

        return u;
    }
}
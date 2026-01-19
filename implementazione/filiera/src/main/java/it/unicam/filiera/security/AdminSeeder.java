package it.unicam.filiera.security;

import it.unicam.filiera.models.GestorePiattaforma;
import it.unicam.filiera.repositories.UtenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner seedAdmin(UtenteRepository utenti, PasswordEncoder encoder) {
        return args -> {
            String username = "admin";

            // findByUsername ESISTE nel tuo UtenteRepository
            if (utenti.findByUsername(username).isEmpty()) {
                GestorePiattaforma admin = new GestorePiattaforma();
                admin.setUsername(username);
                admin.setPassword(encoder.encode("admin"));
                admin.setEmail("admin@filiera.local"); // obbligatorio e unique

                // opzionali (Personale)
                admin.setNome("Admin");
                admin.setCognome("Filiera");
                admin.setTelefono("0000000000");

                // save ESISTE perché UtenteRepository estende JpaRepository
                utenti.save(admin);
            }
        };
    }
}

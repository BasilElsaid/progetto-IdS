package it.unicam.filiera.security;

import it.unicam.filiera.models.Animatore;
import it.unicam.filiera.models.Curatore;
import it.unicam.filiera.models.GestorePiattaforma;
import it.unicam.filiera.repositories.UtentiRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PersonaleSeeder {

    @Bean
    CommandLineRunner seedPersoanle(UtentiRepository utenti, PasswordEncoder encoder) {
        return args -> {
            String username = "admin";

            if (utenti.findByUsername(username).isEmpty()) {
                GestorePiattaforma admin = new GestorePiattaforma();
                admin.setUsername(username);
                admin.setPassword(encoder.encode("admin"));
                admin.setEmail("admin@filiera.local"); // obbligatorio e unique
                admin.setNome("Admin");
                admin.setCognome("Filiera");
                admin.setTelefono("0000000000");
                utenti.save(admin);
            }

            if (utenti.findByUsername("curatore").isEmpty()) {
                Curatore curatore = new Curatore();
                curatore.setUsername("curatore");
                curatore.setPassword(encoder.encode("curatore"));
                curatore.setEmail("curatore@filiera.local");
                curatore.setNome("Curatore");
                curatore.setCognome("Filiera");
                curatore.setTelefono("1111111111");
                utenti.save(curatore);
            }

            // Seed Animatore
            if (utenti.findByUsername("animatore").isEmpty()) {
                Animatore animatore = new Animatore();
                animatore.setUsername("animatore");
                animatore.setPassword(encoder.encode("animatore"));
                animatore.setEmail("animatore@filiera.local");
                animatore.setNome("Animatore");
                animatore.setCognome("Filiera");
                animatore.setTelefono("2222222222");
                utenti.save(animatore);
            }
        };
    }
}

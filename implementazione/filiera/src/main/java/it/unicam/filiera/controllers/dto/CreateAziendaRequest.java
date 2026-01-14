package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.models.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateAziendaRequest {

    @NotNull
    private Ruolo ruolo;

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    public Ruolo getRuolo() { return ruolo; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}

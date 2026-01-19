package it.unicam.filiera.controllers.dto.update;

import jakarta.validation.constraints.Email;

public class UpdateAcquirenteRequest {

    @Email
    private String email;

    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
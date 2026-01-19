package it.unicam.filiera.controllers.dto.create;

import it.unicam.filiera.controllers.dto.CoordinateDTO;
import it.unicam.filiera.enums.Ruolo;
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

    private String nomeAzienda;          // nuovo
    private String partitaIva;           // nuovo
    private String sede;                 // NUOVO
    private String areaDistribuzione;    // per distributore
    private String laboratorio;          // per trasformatore
    private CoordinateDTO coordinate;    // già presente

    // ===== GETTERS =====
    public Ruolo getRuolo() { return ruolo; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNomeAzienda() { return nomeAzienda; }
    public String getPartitaIva() { return partitaIva; }
    public String getSede() { return sede; }
    public String getAreaDistribuzione() { return areaDistribuzione; }
    public String getLaboratorio() { return laboratorio; }
    public CoordinateDTO getCoordinate() { return coordinate; }

    // ===== SETTERS =====
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNomeAzienda(String nomeAzienda) { this.nomeAzienda = nomeAzienda; }
    public void setPartitaIva(String partitaIva) { this.partitaIva = partitaIva; }
    public void setSede(String sede) { this.sede = sede; }
    public void setAreaDistribuzione(String areaDistribuzione) { this.areaDistribuzione = areaDistribuzione; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }
    public void setCoordinate(CoordinateDTO coordinate) { this.coordinate = coordinate; }
}
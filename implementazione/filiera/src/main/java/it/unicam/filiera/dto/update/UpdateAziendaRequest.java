package it.unicam.filiera.dto.update;

import it.unicam.filiera.dto.create.CreateCoordinateRequest;
import jakarta.validation.constraints.Email;

public class UpdateAziendaRequest {

    @Email
    private String email;

    private String password;

    private String nomeAzienda;
    private String partitaIva;
    private String sede;
    private String areaDistribuzione;
    private String laboratorio;
    private CreateCoordinateRequest coordinate;

    public UpdateAziendaRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNomeAzienda() { return nomeAzienda; }
    public void setNomeAzienda(String nomeAzienda) { this.nomeAzienda = nomeAzienda; }

    public String getPartitaIva() { return partitaIva; }
    public void setPartitaIva(String partitaIva) { this.partitaIva = partitaIva; }

    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }

    public String getAreaDistribuzione() { return areaDistribuzione; }
    public void setAreaDistribuzione(String areaDistribuzione) { this.areaDistribuzione = areaDistribuzione; }

    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }

    public CreateCoordinateRequest getCoordinate() { return coordinate; }
    public void setCoordinate(CreateCoordinateRequest coordinate) { this.coordinate = coordinate; }
}
package it.unicam.filiera.dto.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateCoordinateRequest {

    @NotNull(message = "Latitudine obbligatoria")
    @Min(value = -90, message = "Latitudine minima -90")
    @Max(value = 90, message = "Latitudine massima 90")
    public Double lat;

    @NotNull(message = "Longitudine obbligatoria")
    @Min(value = -180, message = "Longitudine minima -180")
    @Max(value = 180, message = "Longitudine massima 180")
    public Double lon;
}
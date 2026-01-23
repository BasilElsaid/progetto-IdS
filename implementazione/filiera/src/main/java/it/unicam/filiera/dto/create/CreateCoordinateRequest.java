package it.unicam.filiera.dto.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class CreateCoordinateRequest {
    @Min(-90) @Max(90)
    public double lat;

    @Min(-180) @Max(180)
    public double lon;
}
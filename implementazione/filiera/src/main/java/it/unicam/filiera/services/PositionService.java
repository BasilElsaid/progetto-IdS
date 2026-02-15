package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreateCoordinateRequest;
import it.unicam.filiera.utilities.CoordinateOSM;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    public CoordinateOSM validaEGenera(CreateCoordinateRequest dto) {
        if (dto == null) return null;

        if (dto.lat() < -90 || dto.lat() > 90 || dto.lon() < -180 || dto.lon() > 180) {
            throw new IllegalArgumentException("Coordinate non valide");
        }

        return new CoordinateOSM(dto.lat(), dto.lon());
    }
}
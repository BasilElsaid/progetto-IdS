package it.unicam.filiera.dto.update;

import java.util.List;

public record UpdatePacchettoRequest(
        String nome,
        List<Long> prodottiIds
) {}
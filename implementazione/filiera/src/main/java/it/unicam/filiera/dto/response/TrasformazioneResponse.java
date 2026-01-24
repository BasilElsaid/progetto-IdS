package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.TrasformazioneProdotto;

import java.time.LocalDateTime;

public record TrasformazioneResponse(
        Long id,
        Long processoId,
        Long trasformatoreId,
        Long inputId,
        Long outputId,
        Double quantitaInput,
        Double quantitaOutput,
        String note,
        LocalDateTime creatoIl
) {
    public static TrasformazioneResponse from(TrasformazioneProdotto t) {
        return new TrasformazioneResponse(
                t.getId(),
                t.getProcesso().getId(),
                t.getTrasformatore().getId(),
                t.getInputProdotto().getId(),
                t.getOutputProdotto().getId(),
                t.getQuantitaInput(),
                t.getQuantitaOutput(),
                t.getNote(),
                t.getCreatoIl()
        );
    }
}
package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.domain.TrasformazioneProdotto;

public class TrasformazioneResponse {

    public Long id;
    public Long processoId;
    public Long trasformatoreId;
    public Long prodottoInputId;
    public Long prodottoOutputId;
    public Double quantitaInput;    
    public Double quantitaOutput;    
    public String note;

    public static TrasformazioneResponse from(TrasformazioneProdotto t) {
        TrasformazioneResponse r = new TrasformazioneResponse();
        r.id = t.getId();
        r.processoId = t.getProcesso().getId();
        r.trasformatoreId = t.getTrasformatore().getId();
        r.prodottoInputId = t.getProdottoInput().getId();
        r.prodottoOutputId = t.getProdottoOutput().getId();
        r.quantitaInput = t.getQuantitaInput();   
        r.quantitaOutput = t.getQuantitaOutput(); 
        r.note = t.getNote();
        return r;
    }
}

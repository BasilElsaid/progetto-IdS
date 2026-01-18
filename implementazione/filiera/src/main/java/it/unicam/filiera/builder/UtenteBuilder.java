package it.unicam.filiera.builder;

import it.unicam.filiera.models.UtenteGenerico;

public interface UtenteBuilder {

	UtenteBuilder setUsername(String username);
	UtenteBuilder setPassword(String password);
	UtenteBuilder setEmail(String email);

	UtenteGenerico build();
}
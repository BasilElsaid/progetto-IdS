package it.unicam.filiera.builder;

import it.unicam.filiera.models.Acquirente;

public class AcquirenteBuilder implements UtenteBuilder {

	private final Acquirente acquirente;

	public AcquirenteBuilder() {
		this.acquirente = new Acquirente();
	}

	@Override
	public UtenteBuilder setUsername(String username) {
		acquirente.setUsername(username);
		return this;
	}

	@Override
	public UtenteBuilder setPassword(String password) {
		acquirente.setPassword(password);
		return this;
	}

	@Override
	public UtenteBuilder setEmail(String email) {
		acquirente.setEmail(email);
		return this;
	}

	@Override
	public Acquirente build() {
		return acquirente;
	}
}
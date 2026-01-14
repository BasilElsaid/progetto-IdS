package it.unicam.filiera.builder;

import it.unicam.filiera.models.Trasformatore;

public class TrasformatoreBuilder implements UtenteBuilder {

	private final Trasformatore trasformatore;

	public TrasformatoreBuilder() {
		this.trasformatore = new Trasformatore();
	}

	@Override
	public UtenteBuilder setUsername(String username) {
		trasformatore.setUsername(username);
		return this;
	}

	@Override
	public UtenteBuilder setPassword(String password) {
		trasformatore.setPassword(password);
		return this;
	}

	@Override
	public UtenteBuilder setEmail(String email) {
		trasformatore.setEmail(email);
		return this;
	}

	public TrasformatoreBuilder setLaboratorio(String laboratorio) {
		trasformatore.setLaboratorio(laboratorio);
		return this;
	}

	@Override
	public Trasformatore build() {
		return trasformatore;
	}
}
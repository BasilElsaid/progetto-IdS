package it.unicam.filiera.builder;

import it.unicam.filiera.models.Produttore;

public class ProduttoreBuilder implements UtenteBuilder {

	private final Produttore produttore;

	public ProduttoreBuilder() {
		this.produttore = new Produttore();
	}

	@Override
	public UtenteBuilder setUsername(String username) {
		produttore.setUsername(username);
		return this;
	}

	@Override
	public UtenteBuilder setPassword(String password) {
		produttore.setPassword(password);
		return this;
	}

	@Override
	public UtenteBuilder setEmail(String email) {
		produttore.setEmail(email);
		return this;
	}

	public ProduttoreBuilder setNomeAzienda(String nomeAzienda) {
		produttore.setNomeAzienda(nomeAzienda);
		return this;
	}

	public ProduttoreBuilder setPartitaIva(String partitaIva) {
		produttore.setPartitaIva(partitaIva);
		return this;
	}

	@Override
	public Produttore build() {
		return produttore;
	}
}
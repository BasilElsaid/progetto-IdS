package it.unicam.filiera.builder;

import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.models.Ruolo;
import it.unicam.filiera.utilities.CoordinateOSM;

public class ProduttoreBuilder implements UtenteBuilder, AziendeBuilder {

	private final Produttore produttore;

	public ProduttoreBuilder() {
		this.produttore = new Produttore();
		this.produttore.setRuolo(Ruolo.PRODUTTORE);
	}

	// --- UtenteBuilder ---
	@Override
	public ProduttoreBuilder setUsername(String username) {
		produttore.setUsername(username);
		return this;
	}

	@Override
	public ProduttoreBuilder setPassword(String password) {
		produttore.setPassword(password);
		return this;
	}

	@Override
	public ProduttoreBuilder setEmail(String email) {
		produttore.setEmail(email);
		return this;
	}

	// --- AziendeBuilder ---
	@Override
	public ProduttoreBuilder setNomeAzienda(String nome) {
		produttore.setNomeAzienda(nome);
		return this;
	}

	@Override
	public ProduttoreBuilder setSede(String sede) {
		produttore.setSede(sede);
		return this;
	}

	@Override
	public ProduttoreBuilder setCoordinate(CoordinateOSM coordinate) {
		produttore.setCoordinate(coordinate);
		return this;
	}

	@Override
	public ProduttoreBuilder setPartitaIva(String partitaIva) {
		produttore.setPartitaIva(partitaIva);
		return this;
	}

	@Override
	public Produttore build() {
		return produttore;
	}
}
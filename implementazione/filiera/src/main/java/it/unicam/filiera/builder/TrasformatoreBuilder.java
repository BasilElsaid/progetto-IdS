package it.unicam.filiera.builder;

import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.utilities.CoordinateOSM;

public class TrasformatoreBuilder implements UtenteBuilder, AziendeBuilder {

	private final Trasformatore trasformatore;

	public TrasformatoreBuilder() {
		this.trasformatore = new Trasformatore();
	}

	// UtenteBuilder
	@Override
	public TrasformatoreBuilder setUsername(String username) {
		trasformatore.setUsername(username);
		return this;
	}

	@Override
	public TrasformatoreBuilder setPassword(String password) {
		trasformatore.setPassword(password);
		return this;
	}

	@Override
	public TrasformatoreBuilder setEmail(String email) {
		trasformatore.setEmail(email);
		return this;
	}

	// AziendeBuilder
	@Override
	public TrasformatoreBuilder setNomeAzienda(String nome) {
		trasformatore.setNomeAzienda(nome);
		return this;
	}

	@Override
	public TrasformatoreBuilder setSede(String sede) {
		trasformatore.setSede(sede);
		return this;
	}

	@Override
	public TrasformatoreBuilder setCoordinate(CoordinateOSM coordinate) {
		trasformatore.setCoordinate(coordinate);
		return this;
	}

	@Override
	public TrasformatoreBuilder setPartitaIva(String partitaIva) {
		trasformatore.setPartitaIva(partitaIva);
		return this;
	}

	// Builder specifico Trasformatore
	public TrasformatoreBuilder setLaboratorio(String laboratorio) {
		trasformatore.setLaboratorio(laboratorio);
		return this;
	}

	@Override
	public Trasformatore build() {
		return trasformatore;
	}
}
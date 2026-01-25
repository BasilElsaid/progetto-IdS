package it.unicam.filiera.builder;

import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.utilities.CoordinateOSM;

public class DistributoreTipicitaBuilder implements UtenteBuilder, AziendeBuilder {

	private final DistributoreTipicita distributore;

	public DistributoreTipicitaBuilder() {
		this.distributore = new DistributoreTipicita();
		this.distributore.setRuolo(Ruolo.DISTRIBUTORE_TIPICITA);
	}

	// UtenteGenerico
	@Override
	public DistributoreTipicitaBuilder setUsername(String username) {
		distributore.setUsername(username);
		return this;
	}

	@Override
	public DistributoreTipicitaBuilder setPassword(String password) {
		distributore.setPassword(password);
		return this;
	}

	@Override
	public DistributoreTipicitaBuilder setEmail(String email) {
		distributore.setEmail(email);
		return this;
	}

	// Azienda
	@Override
	public DistributoreTipicitaBuilder setNomeAzienda(String nome) {
		distributore.setNomeAzienda(nome);
		return this;
	}

	@Override
	public DistributoreTipicitaBuilder setSede(String sede) {
		distributore.setSede(sede);
		return this;
	}

	@Override
	public DistributoreTipicitaBuilder setCoordinate(CoordinateOSM coordinate) {
		distributore.setCoordinate(coordinate);
		return this;
	}

	@Override
	public DistributoreTipicitaBuilder setPartitaIva(String partitaIva) {
		distributore.setPartitaIva(partitaIva);
		return this;
	}

	// DistributoreTipicita specifico
	public DistributoreTipicitaBuilder setAreaDistribuzione(String area) {
		distributore.setAreaDistribuzione(area);
		return this;
	}

	// Build finale
	@Override
	public DistributoreTipicita build() {
		return distributore;
	}
}
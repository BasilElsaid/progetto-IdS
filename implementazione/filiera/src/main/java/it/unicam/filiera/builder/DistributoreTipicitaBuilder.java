package it.unicam.filiera.builder;

import it.unicam.filiera.models.DistributoreTipicita;

public class DistributoreTipicitaBuilder implements UtenteBuilder {

	private final DistributoreTipicita distributore;

	public DistributoreTipicitaBuilder() {
		this.distributore = new DistributoreTipicita();
	}

	@Override
	public UtenteBuilder setUsername(String username) {
		distributore.setUsername(username);
		return this;
	}

	@Override
	public UtenteBuilder setPassword(String password) {
		distributore.setPassword(password);
		return this;
	}

	@Override
	public UtenteBuilder setEmail(String email) {
		distributore.setEmail(email);
		return this;
	}

	public DistributoreTipicitaBuilder setAreaDistribuzione(String area) {
		distributore.setAreaDistribuzione(area);
		return this;
	}

	@Override
	public DistributoreTipicita build() {
		return distributore;
	}
}
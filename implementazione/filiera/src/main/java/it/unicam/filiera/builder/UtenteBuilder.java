package it.unicam.filiera.builder;

public interface UtenteBuilder {

	/**
	 * 
	 * @param username
	 */
	void setUsername(String username);

	/**
	 * 
	 * @param password
	 */
	void setPassword(String password);

	/**
	 * 
	 * @param nome
	 */
	void setNome(String nome);

	/**
	 * 
	 * @param email
	 */
	void setEmail(String email);

	/**
	 * 
	 * @param telefono
	 */
	void setTelefono(String telefono);

	abstract void build();

}
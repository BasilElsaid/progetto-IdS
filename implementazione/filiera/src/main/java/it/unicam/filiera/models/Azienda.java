package it.unicam.filiera.models;

import it.unicam.filiera.utilities.*;
import java.util.*;
import it.unicam.filiera.prodotto.*;

public abstract class Azienda {

	protected String nome;
	protected String indirizzo;
	protected String telefono;
	protected String email;
	protected String username;
	protected String password;
	protected String partitaIVA;
	protected CoordinateOSM coordinate;
	protected Collection<Prodotto> prodotti = new ArrayList<>();
	private int id;

}
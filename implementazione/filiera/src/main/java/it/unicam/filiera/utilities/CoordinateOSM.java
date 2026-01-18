package it.unicam.filiera.utilities;

import jakarta.persistence.Embeddable;

@Embeddable
public class CoordinateOSM {

	private double lat;
	private double lon;

	// Costruttore vuoto richiesto da JPA
	public CoordinateOSM() {}

	// Costruttore con parametri
	public CoordinateOSM(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	// Getters e setters
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
}
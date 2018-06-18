package com.cgi.udev.diplome;

import java.time.LocalDate;

public class Controle extends Examen {

	private float note;
	
	public Controle(LocalDate date, String code) throws DateExamenException {
		super(date, code);
	}

	public Controle(LocalDate date, String code, float n) throws DateExamenException {
		super(date, code);
		if (n > 20 || n < 0) {
			throw new IllegalArgumentException("Une note doit être comprise entre 0 et 20 !");
		}
		this.note = n;
	}

	@Override
	public float getNote() {
		return this.note;
	}
	
	public void setNote(float note) {
		this.note = note;
	}
}
package com.cgi.udev.diplome;

import java.time.LocalDate;

public class Projet extends Examen {

	private float noteEcrit;
	private float noteOral;

	public Projet(LocalDate date, String code, float noteE, float noteO) throws NoteIllegaleException, DateExamenException {
		super(date, code);
		if (noteE < 0 || noteO < 0) {
			throw new NoteIllegaleException("La note ne peut pas être négative");
		} else if (noteE > 10 || noteO > 10) {
			throw new NoteIllegaleException("La note ne peut pas être supérieure à 10");
		}
		this.noteEcrit = noteE;
		this.noteOral = noteO;
	}

	@Override
	public float getNote() {
		float n = this.noteEcrit + this.noteOral;
		return n;
	}
	
	public float getNoteEcrit() {
		return noteEcrit;
	}
	
	public void setNoteEcrit(float noteEcrit) {
		this.noteEcrit = noteEcrit;
	}
	
	public float getNoteOral() {
		return noteOral;
	}
	
	public void setNoteOral(float noteOral) {
		this.noteOral = noteOral;
	}
}
package com.cgi.udev.diplome;

import java.time.LocalDate;

public class Qcm extends Examen {

	private int nbQuestions;
	private int nbReponsesCorrectes;

	public Qcm(LocalDate date, String code, int nbQuest) throws NombreQuestionsException, DateExamenException {
		super(date, code);
		if (nbQuest <= 0) {
			throw new NombreQuestionsException("Le nombre de questions est incorrect");
		}
		this.nbQuestions = nbQuest;
	}
	
	public Qcm(LocalDate date, String code, int nbQuest, int nbReponsesCorrectes) throws NombreQuestionsException, DateExamenException {
		super(date, code);
		if (nbQuest <= 0) {
			throw new NombreQuestionsException("Le nombre de questions est incorrect");
		}
		this.nbQuestions = nbQuest;
		this.setNbReponsesCorrectes(nbReponsesCorrectes);
	}

	public void setNbReponsesCorrectes(int nbReponsesCorrectes) {
		if (nbReponsesCorrectes > this.nbQuestions) {
			throw new IllegalArgumentException();
		}
		this.nbReponsesCorrectes = nbReponsesCorrectes;
	}

	@Override
	public float getNote() {
		float n = this.nbReponsesCorrectes * 20 / this.nbQuestions;
		return n;
	}
	
	public int getNbQuestions() {
		return nbQuestions;
	}
	
	public int getNbReponsesCorrectes() {
		return nbReponsesCorrectes;
	}
}
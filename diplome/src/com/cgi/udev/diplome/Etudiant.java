package com.cgi.udev.diplome;

import java.time.LocalDate;

public class Etudiant implements EtudiantDAO {

	private int codeEtudiant;
	private String nomEtudiant;
	private String prenomEtudiant;
	private LocalDate dateNaissanceEtudiant;
	private Diplome diplome;

	public Etudiant() {
	}

	public Etudiant(String nomEtudiant, String prenomEtudiant, LocalDate dateNaissanceEtudiant,
			Diplome diplome) {
		super();
		this.nomEtudiant = nomEtudiant;
		this.prenomEtudiant = prenomEtudiant;
		this.dateNaissanceEtudiant = dateNaissanceEtudiant;
		this.diplome = diplome;
	}

	public int getCodeEtudiant() {
		return codeEtudiant;
	}

	public void setCodeEtudiant(int codeEtudiant) {
		this.codeEtudiant = codeEtudiant;
	}

	public String getNomEtudiant() {
		return nomEtudiant;
	}

	public void setNomEtudiant(String nomEtudiant) {
		this.nomEtudiant = nomEtudiant;
	}

	public String getPrenomEtudiant() {
		return prenomEtudiant;
	}

	public void setPrenomEtudiant(String prenomEtudiant) {
		this.prenomEtudiant = prenomEtudiant;
	}

	public Diplome getDiplome() {
		return diplome;
	}

	public void setDiplome(Diplome diplome) {
		this.diplome = diplome;
	}

	public LocalDate getDateNaissanceEtudiant() {
		return dateNaissanceEtudiant;
	}

	public void setDateNaissanceEtudiant(LocalDate dateNaissanceEtudiant) {
		this.dateNaissanceEtudiant = dateNaissanceEtudiant;
	}
	
	public String toString() {
		String res = "Etudiant N°" + this.codeEtudiant + " Nom : " + this.nomEtudiant + " Prénom : " + this.prenomEtudiant + " Date de naissance : " + this.dateNaissanceEtudiant;
		return res;
	}
}
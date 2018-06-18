package com.cgi.udev.diplome;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
//import java.util.OptionalDouble;
//import java.util.stream.Collectors;
//import java.util.stream.DoubleStream;
//import java.util.stream.IntStream;
//import java.util.stream.Stream;

public class Diplome {

	private int id;
	private List<Examen> exams;

	public Diplome() {

	}

	public Diplome(List<Examen> listeExams) {
		this.exams = listeExams;
	}

	public void sauvegarderDiplome(Diplome d) throws SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		String url = "jdbc:mysql://localhost:3306/udev";
		String login = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			String requestSql = "INSERT INTO diplome(etu_id) VALUES (?)";
			try (PreparedStatement stmt = connection.prepareStatement(requestSql)) {
				stmt.setInt(1, d.getId());
				stmt.executeUpdate();
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float calculMoyenne() {
		float total = 0.0f;
		for (Examen e : this.exams) {
			total += e.getNote();
		}
		float moyenne = total / exams.size();
		return moyenne;
	}

	// Essai pour calculer la moyenne avec un stream... (TODO)
	// private OptionalDouble calculMoyenne() {
	// int[] listeNotes = null;
	// int i = 0;
	// for (Examen e : this.exams) {
	// listeNotes[i] = (int) e.getNote();
	// i++;
	// }
	// OptionalDouble moyenne = Arrays.stream(listeNotes).average();
	// return moyenne;
	// }

	public boolean isValide() {
		return this.calculMoyenne() >= 10;
	}

	public Mention getMention(float note) {
		if (note >= 10 && note < 12) {
			return Mention.PASSABLE;
		} else if (note >= 12 && note < 14) {
			return Mention.BIEN;
		} else if (note >= 14 && note < 16) {
			return Mention.TRES_BIEN;
		} else if (note >= 16 && note <= 20) {
			return Mention.EXCELLENT;
		}
		return null;
	}

	public List<Examen> getExamens() {
		return exams;
	}

	// Ancienne méthode qui utilisait les comparateurs
	public List<Examen> getExamens(Comparator<Examen> c) {
		exams.sort(c);
		return exams;
	}

	public void ajouterExamen(Examen exam) {
		this.exams.add(exam);
	}

	public List<Examen> getExamensNoteInferieureA(float note) {
		List<Examen> examensFiltres = new ArrayList<Examen>();
		for (Examen e : this.exams) {
			if (e.getNote() < note) {
				examensFiltres.add(e);
			}
		}
		return examensFiltres;
	}

	public void supprimerExamenParCode(String code) {
		for (Examen e : this.exams) {
			if (e.getCode().equals(code)) {
				this.exams.remove(e);
			}
		}
	}

	public String afficherDetailDesNotes() {
		String res = "";
		for (Examen e : this.exams) {
			res += String.format("%5.2f", e.getNote()) + "/20 à l'examen " + e.getCode() + " du "
					+ e.getDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRANCE)) + "\n";
		}
		return res;
	}

	public String toString() {
		String res = afficherDetailDesNotes();
		res += "+------------------+---------------+----------------+ \n";
		res += "|  Diplôme validé  | Note globale  |     Mention    | \n";
		res += "+------------------+---------------+----------------+ \n";
		res += isValide() ? "|       Oui        |" : "|        Non       |";
		res += "     " + String.format("%5.2f", this.calculMoyenne()) + "     |    "
				+ String.format("%9s", this.getMention(this.calculMoyenne())) + "   | \n";
		res += "+------------------+---------------+----------------+ \n";
		return res;
	}
}
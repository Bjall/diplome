package com.cgi.udev.diplome;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DiplomeDAO {

	public static void sauvegarderDiplome(Etudiant e) throws SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		String url = "jdbc:mysql://localhost:3306/udev";
		String login = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			String requestSql = "INSERT INTO diplome(etu_id) VALUES ((SELECT etu_id FROM etudiant WHERE etu_nom = ? AND etu_prenom = ? AND etu_ddn = ?))";
			try (PreparedStatement stmt = connection.prepareStatement(requestSql)) {
				stmt.setString(1, e.getNomEtudiant().toUpperCase());
				stmt.setString(2, e.getPrenomEtudiant().toUpperCase());
				stmt.setDate(3, java.sql.Date.valueOf(e.getDateNaissanceEtudiant()));
				stmt.executeUpdate();
			}
		}
	}

	public static Diplome getDiplome(Etudiant etudiant) throws SQLException {
		List<Examen> exams = new ArrayList<>();
		Diplome diplome = new Diplome(exams);
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		String url = "jdbc:mysql://localhost:3306/udev";
		String login = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			String requestSql = "SELECT dip_id FROM diplome WHERE etu_id = ?";
			try (PreparedStatement stmt = connection.prepareStatement(requestSql)) {
				stmt.setInt(1, etudiant.getCodeEtudiant());
				try (ResultSet resultSet = stmt.executeQuery()) {
					while (resultSet.next()) {
						diplome.setId(resultSet.getInt("dip_id"));
					}
				}
			}
		}
		return diplome;
	}
}
package com.cgi.udev.diplome;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface EtudiantDAO {

	public static void sauvegarderEtudiant(Etudiant e) throws SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		String url = "jdbc:mysql://localhost:3306/udev";
		String login = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			String requestSql = "INSERT INTO etudiant(etu_nom, etu_prenom, etu_ddn) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = connection.prepareStatement(requestSql)) {
				stmt.setString(1, e.getNomEtudiant().toUpperCase());
				stmt.setString(2, e.getPrenomEtudiant().toUpperCase());
				stmt.setDate(3, java.sql.Date.valueOf(e.getDateNaissanceEtudiant()));
				stmt.executeUpdate();
			}
		}
	}

	public static Etudiant getEtudiant(int codeEtudiant) throws SQLException {
		Etudiant etudiant = new Etudiant();
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		String url = "jdbc:mysql://localhost:3306/udev";
		String login = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			String requestSql = "SELECT * FROM etudiant WHERE etu_id = ?";
			try (PreparedStatement stmt = connection.prepareStatement(requestSql)) {
				stmt.setInt(1, codeEtudiant);
				try (ResultSet resultSet = stmt.executeQuery()) {
					while (resultSet.next()) {
						etudiant.setCodeEtudiant(resultSet.getInt("etu_id"));
						etudiant.setNomEtudiant(resultSet.getString("etu_nom"));
						etudiant.setPrenomEtudiant(resultSet.getString("etu_prenom"));
						etudiant.setDateNaissanceEtudiant(resultSet.getDate("etu_ddn").toLocalDate());
					}
				}
			}
		}
		return etudiant;
	}
}
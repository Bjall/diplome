package com.cgi.udev.diplome;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface ProjetDAO {

	public static void sauvegarderProjet(Etudiant etudiant, Projet projet) throws SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		String url = "jdbc:mysql://localhost:3306/udev";
		String login = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			connection.setAutoCommit(false);
			boolean transactionOk = false;

			try {

				// On insère les données dans la table examen
				String insert1 = "INSERT INTO examen(exa_code, exa_date, dip_id) VALUES (?, ?, (SELECT dip_id FROM diplome WHERE etu_id = (SELECT etu_id FROM etudiant WHERE etu_nom = ? AND etu_prenom = ?)))";
				try (PreparedStatement stmt = connection.prepareStatement(insert1)) {
					stmt.setString(1, projet.getCode());
					stmt.setDate(2, java.sql.Date.valueOf(projet.getDate()));
					stmt.setString(3, etudiant.getNomEtudiant().toUpperCase());
					stmt.setString(4, etudiant.getPrenomEtudiant().toUpperCase());

					stmt.executeUpdate();
				}

				// On insère les données dans la table projet
				String insert2 = "INSERT INTO projet(pro_noteecrit, pro_noteoral, exa_id) VALUES (?, ?, LAST_INSERT_ID())";
				try (PreparedStatement stmt = connection.prepareStatement(insert2)) {
					stmt.setFloat(1, projet.getNoteEcrit());
					stmt.setFloat(2, projet.getNoteOral());

					stmt.executeUpdate();
				}

				transactionOk = true;
			}

			finally {
				if (transactionOk) {
					connection.commit();
				} else {
					connection.rollback();
				}
			}
		}
	}
	
	public static List<Projet> getListeProjets(Diplome diplome) throws SQLException, DateExamenException, NoteIllegaleException {
		List<Projet> liste = new ArrayList<>();
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		String url = "jdbc:mysql://localhost:3306/udev";
		String login = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			String requestSql = "SELECT projet.exa_id, exa_code, exa_date, 	pro_noteecrit, pro_noteoral\r\n" + 
					"FROM (SELECT exa_id, exa_code, exa_date FROM examen WHERE dip_id = ?) AS examens\r\n" + 
					"JOIN projet\r\n" + 
					"ON examens.exa_id = projet.exa_id";
			try (PreparedStatement stmt = connection.prepareStatement(requestSql)) {
				stmt.setInt(1, diplome.getId());
				try (ResultSet resultSet = stmt.executeQuery()) {
					while (resultSet.next()) {
						LocalDate date = resultSet.getDate("exa_date").toLocalDate();
						String code = resultSet.getString("exa_code");
						Float noteEcrit = resultSet.getFloat("pro_noteecrit");
						Float noteOral = resultSet.getFloat("pro_noteoral");
						Projet projet = new Projet(date, code, noteEcrit, noteOral);
						liste.add(projet);
					}
				}
			}
		}
		return liste;
	}
	
}

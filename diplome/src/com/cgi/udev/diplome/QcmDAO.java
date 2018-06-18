package com.cgi.udev.diplome;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface QcmDAO {

	public static void sauvegarderQcm(Etudiant etudiant, Qcm qcm) throws SQLException {
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
					stmt.setString(1, qcm.getCode());
					stmt.setDate(2, java.sql.Date.valueOf(qcm.getDate()));
					stmt.setString(3, etudiant.getNomEtudiant().toUpperCase());
					stmt.setString(4, etudiant.getPrenomEtudiant().toUpperCase());

					stmt.executeUpdate();
				}

				// On insère les données dans la table projet
				String insert2 = "INSERT INTO qcm(qcm_nbquestions, qcm_nbreponsescorrectes, exa_id) VALUES (?, ?, LAST_INSERT_ID())";
				try (PreparedStatement stmt = connection.prepareStatement(insert2)) {
					stmt.setFloat(1, qcm.getNbQuestions());
					stmt.setFloat(2, qcm.getNbReponsesCorrectes());

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
	
	public static List<Qcm> getListeQcm(Diplome diplome) throws SQLException, DateExamenException, NombreQuestionsException {
		List<Qcm> liste = new ArrayList<>();
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		String url = "jdbc:mysql://localhost:3306/udev";
		String login = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			String requestSql = "SELECT qcm.exa_id, exa_code, exa_date, qcm_nbquestions, qcm_nbreponsescorrectes\r\n" + 
					"FROM (SELECT exa_id, exa_code, exa_date FROM examen WHERE dip_id = ?) AS examens\r\n" + 
					"JOIN qcm\r\n" + 
					"ON examens.exa_id = qcm.exa_id";
			try (PreparedStatement stmt = connection.prepareStatement(requestSql)) {
				stmt.setInt(1, diplome.getId());
				try (ResultSet resultSet = stmt.executeQuery()) {
					while (resultSet.next()) {
						LocalDate date = resultSet.getDate("exa_date").toLocalDate();
						String code = resultSet.getString("exa_code");
						int nbQuests = resultSet.getInt("qcm_nbquestions");
						int nbRepons = resultSet.getInt("qcm_nbreponsescorrectes");
						Qcm qcm = new Qcm(date, code, nbQuests, nbRepons);
						liste.add(qcm);
					}
				}
			}
		}
		return liste;
	}
}
package com.cgi.udev.diplome;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public abstract class Examen {

	private LocalDate date;
	private String code;

	public abstract float getNote();

	public Examen(LocalDate date, String code) throws DateExamenException {
		if (verifierDate(date)) {
			this.date = date;
		}
		this.code = code;
	}
	
	public void sauvegarderExamen(Diplome d, Examen e) throws SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		String url = "jdbc:mysql://localhost:3306/udev";
		String login = "root";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			String requestSql = "INSERT INTO examen(exa_date, exa_note, dip_id) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = connection.prepareStatement(requestSql)) {
				stmt.setDate(1, java.sql.Date.valueOf(e.getDate()));
				stmt.setFloat(1, e.getNote());
				stmt.setInt(1, d.getId());
				stmt.executeUpdate();
			}
		}
	}

	public boolean verifierDate(LocalDate date) throws DateExamenException {
		LocalDate debutAnnee = LocalDate.now();
		LocalDate finAnnee = LocalDate.now();

		if (LocalDate.now().isBefore(LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 31))) {
			debutAnnee = LocalDate.of(LocalDate.now().getYear(), Month.OCTOBER, 1);
			finAnnee = LocalDate.of(LocalDate.now().getYear() + 1, Month.JUNE, 30);
		} else {
			debutAnnee = LocalDate.of(LocalDate.now().getYear() - 1, Month.OCTOBER, 1);
			finAnnee = LocalDate.of(LocalDate.now().getYear(), Month.JUNE, 30);
		}

		if (date.isBefore(debutAnnee) && date.isAfter(finAnnee)) {
			throw new DateExamenException("La date de l'examen ne correspond pas à l'année scolaire en cours !");
		} else if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			throw new DateExamenException("Les examens ne peuvent être saisis les samedis et dimanches !");
		}
		return true;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public String getCode() {
		return code;
	}
}
package com.cgi.udev.diplome;

import java.util.Comparator;

public class CompareDateExamen implements Comparator<Examen> {

	@Override
	public int compare(Examen exam1, Examen exam2) {
		return (exam1.getDate().compareTo(exam2.getDate()));
	}
}
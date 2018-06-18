package com.cgi.udev.diplome;

import java.util.Comparator;

public class CompareCodeExamen implements Comparator<Examen> {

	@Override
	public int compare(Examen exam1, Examen exam2) {
		return (exam1.getCode().compareTo(exam2.getCode()));
		
	}
}
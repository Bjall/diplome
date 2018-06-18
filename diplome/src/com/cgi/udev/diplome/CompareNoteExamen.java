package com.cgi.udev.diplome;

import java.util.Comparator;

public class CompareNoteExamen implements Comparator<Examen> {

	@Override
	public int compare(Examen exam1, Examen exam2) {
		int res = 0;
		if (exam1.getNote() > exam2.getNote()) {
			res = 1;
		}
		else if (exam1.getNote() < exam2.getNote()) {
			res = -1;
		}
		return res;
	}
}
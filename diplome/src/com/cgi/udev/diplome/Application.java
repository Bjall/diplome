/**
 * Système de gestion des diplômes
 *
 * @author Benoit Jallet
 *
 */
package com.cgi.udev.diplome;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Application {

	private static Scanner scanner;

	public static void main(String[] args)
			throws NoteIllegaleException, NombreQuestionsException, DateExamenException, SQLException {

		bienvenue();

		scanner = new Scanner(System.in);
		String choixUtilisateur = "O";

		do {

			String choixMenu = afficherMenu();

			if (choixMenu.equals("1")) {
				List<Examen> listeExamens = new ArrayList<>();

				Diplome diplome = new Diplome(listeExamens);

				Etudiant etudiant = new Etudiant(saisieNomEtudiant(), saisiePrenomEtudiant(),
						saisieDateNaissanceEtudiant(), diplome);

				EtudiantDAO.sauvegarderEtudiant(etudiant);
				DiplomeDAO.sauvegarderDiplome(etudiant);

				lireChoix(diplome, etudiant);

				System.out.println("L'étudiant a bien été enregistré sous le code " + etudiant.getCodeEtudiant());

			} else if (choixMenu.equals("2")) {
				int choixEtudiant = 0;
				Etudiant etudiant = new Etudiant();

				do {
					System.out.println("Saisissez le code de l'étudiant à afficher :");
					choixEtudiant = scanner.nextInt();

					etudiant = EtudiantDAO.getEtudiant(choixEtudiant);
					System.out.println("Etudiant inexistant");
				} while (etudiant.getCodeEtudiant() == 0);

				Diplome diplome = DiplomeDAO.getDiplome(etudiant);

				buildListeExamens(diplome);

				etudiant.setDiplome(diplome);

				trierExamens(diplome);

				afficherDiplome(diplome, etudiant);

			} else {
				System.err.println("Soyez sérieux svp ! Cela ne fait pas partie des choix proposés !");
			}

			System.out.println("\n");
			System.out.println("Voulez-vous insérer ou afficher un étudiant ? [O/N]");
			choixUtilisateur = scanner.next().toUpperCase();

		} while (choixUtilisateur.equals("O"));
		
		System.out.println("Fermeture du programme.");
		
		scanner.close();

	}

	private static void bienvenue() {
		System.out.println("Bienvenue dans le programme de gestion des diplômes !");
		System.out.println("-----------------------------------------------------");
	}

	private static String afficherMenu() {
		System.out.println("Que voulez-vous faire ? (saisir le numéro)");
		System.out.println("[1] Enregistrer un nouvel étudiant et ses résultats");
		System.out.println("[2] Consulter les résultats d'un étudiant (avec son code)");
		String choixMenu = scanner.next();
		return choixMenu;
	}

	public static String saisieNomEtudiant() {
		System.out.println("Saisissez le nom de l'étudiant : ");
		String nomEtudiant = scanner.next();
		return nomEtudiant;
	}

	public static String saisiePrenomEtudiant() {
		System.out.println("Saisissez le prénom de l'étudiant : ");
		String prenomEtudiant = scanner.next();
		return prenomEtudiant;
	}

	public static LocalDate saisieDateNaissanceEtudiant() {
		LocalDate dateNaissanceEtudiant = null;
		do {
			try {
				System.out.println("Saisissez la date de naissance de l'étudiant (JJ/MM/AAAA) :");
				String str = scanner.next();
				if (str.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					formatter = formatter.withLocale(Locale.FRANCE);
					dateNaissanceEtudiant = LocalDate.parse(str, formatter);
				}
			} catch (InputMismatchException e) {
				System.err.println("Date non valide");
			}
		} while (dateNaissanceEtudiant == null);
		return dateNaissanceEtudiant;
	}

	private static void lireChoix(Diplome diplome, Etudiant etudiant)
			throws DateExamenException, NombreQuestionsException, NoteIllegaleException, SQLException {
		String choix = "o";
		do {
			diplome.ajouterExamen(ajouterNouvelExamen(diplome, etudiant));
			System.out.println("Souhaitez-vous ajouter un nouvel examen ? [O/N] ");
			choix = scanner.next().toUpperCase();
		} while (choix.equals("O"));
	}

	public static Examen ajouterNouvelExamen(Diplome diplome, Etudiant etudiant)
			throws DateExamenException, NombreQuestionsException, NoteIllegaleException, SQLException {
		String typeExamen = null;
		do {
			try {
				System.out.println("Quel type d'examen souhaitez-vous saisir pour cet étudiant ?");
				System.out.println("Controle = c / QCM = q / Projet = p");
				typeExamen = scanner.next().toUpperCase();
			} catch (InputMismatchException e) {
				System.err.println("Type d'examen incorrect");
			}
		} while (typeExamen == null);

		if (typeExamen.equals("C")) {
			System.out.println("Saisie d'un nouveau contrôle");
			System.out.println("----------------------------");
			Controle controle = new Controle(saisieDateExamen(), saisieCodeExamen(), saisieNoteExamen());
			ControleDAO.sauvegarderControle(etudiant, controle);
			return controle;
		} else if (typeExamen.equals("Q")) {
			System.out.println("Saisie d'un nouveau QCM");
			System.out.println("-----------------------");
			Qcm qcm = new Qcm(saisieDateExamen(), saisieCodeExamen(), saisieNbQuestions(), saisieNbReponses());
			QcmDAO.sauvegarderQcm(etudiant, qcm);
			return qcm;
		} else if (typeExamen.equals("P")) {
			System.out.println("Saisie d'un nouveau projet");
			System.out.println("--------------------------");
			Projet projet = new Projet(saisieDateExamen(), saisieCodeExamen(), saisieNoteProjetEcrit(),
					saisieNoteProjetOral());
			ProjetDAO.sauvegarderProjet(etudiant, projet);
			return projet;
		} else {
			System.out.println("Veuillez choisir parmi les choix proposés !");
			return null;
		}
	}

	public static LocalDate saisieDateExamen() {
		LocalDate dateExamen = null;
		do {
			try {
				System.out.println("Saisissez la date de l'examen (JJ/MM/AAAA) :");
				String str = scanner.next();
				if (str.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					formatter = formatter.withLocale(Locale.FRANCE);
					dateExamen = LocalDate.parse(str, formatter);
				}
			} catch (InputMismatchException e) {
				System.err.println("Date non valide");
			}
		} while (dateExamen == null);
		return dateExamen;
	}

	public static String saisieCodeExamen() {
		String codeControle = null;
		do {
			try {
				System.out.println("Saisissez le code de l'examen : ");
				codeControle = scanner.next();
			} catch (InputMismatchException e) {
				System.err.println("Code non valide");
			}
		} while (codeControle == null);
		return codeControle;
	}

	public static float saisieNoteExamen() {
		float noteExamen = -1;
		do {
			
			try {
				System.out.println("Saisissez la note du controle (/20)");
				noteExamen = scanner.nextFloat();
			} catch (InputMismatchException e) {
				System.err.println("Note non valide");
			}
		} while (noteExamen == -1);
		return noteExamen;
	}

	public static int saisieNbQuestions() {
		int nbQuestions = 0;
		do {
			try {
				System.out.println("Nombre de questions du QCM : ");
				nbQuestions = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Nombre non valide");
			}
		} while (nbQuestions == 0);
		return nbQuestions;
	}

	public static int saisieNbReponses() {
		int nbReponses = -1;
		do {
			try {
				System.out.println("Nombre de réponses correctes : ");
				nbReponses = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Nombre non valide");
			}
		} while (nbReponses == -1);
		return nbReponses;
	}

	public static float saisieNoteProjetEcrit() {
		float noteProjet = -1;
		do {
			try {
				System.out.println("Saisissez la note de l'écrit (/10) : ");
				noteProjet = scanner.nextFloat();
			} catch (InputMismatchException e) {
				System.err.println("Note non valide");
			} catch (NoSuchElementException e) {
				System.err.println(e.getMessage());
			}
		} while (noteProjet < 0 && noteProjet > 10);
		return noteProjet;
	}

	public static float saisieNoteProjetOral() {
		float noteProjet = -1;
		do {
			try {
				System.out.println("Saisissez la note de l'oral (/10) : ");
				noteProjet = scanner.nextFloat();
			} catch (InputMismatchException e) {
				scanner.next();
				System.err.println("Note non valide");
			} catch (NoSuchElementException e) {
				System.err.println(e.getMessage());
			}
		} while (noteProjet < 0 && noteProjet > 10);
		return noteProjet;
	}

	// La méthode utilisait auparavant les comparators.
	// Elle est utilisée ici avec des lambdas à la place.
	private static void trierExamens(Diplome diplome) {
		String methodeTri = null;
		do {
			try {
				System.out.println("Souhaitez-vous trier les examens par ?");
				System.out.println("Code = C / Date = D / Note = N");
				methodeTri = scanner.next().toUpperCase();
			} catch (InputMismatchException e) {
				System.err.println("Entrée incorrecte");
			}
		} while (methodeTri == null);

		if (methodeTri.equals("C")) {
			// diplome.getExamens(new CompareCodeExamen());
			diplome.getExamens().sort((Examen exam1, Examen exam2) -> exam1.getCode().compareTo(exam2.getCode()));
		} else if (methodeTri.equals("D")) {
			// diplome.getExamens(new CompareDateExamen());
			diplome.getExamens().sort((Examen exam1, Examen exam2) -> exam1.getDate().compareTo(exam2.getDate()));
		} else if (methodeTri.equals("N")) {
			// diplome.getExamens(new CompareNoteExamen());
			diplome.getExamens().sort((Examen exam1, Examen exam2) -> exam1.getNote() > exam2.getNote() ? 1 : -1);
		}
	}

	private static void afficherDiplome(Diplome diplome, Etudiant etudiant) {
		System.out.println("\n");
		System.out.println("Résultats de l'étudiant " + String.format("%6s", etudiant.getCodeEtudiant()) + " "
				+ String.format("%10s", etudiant.getNomEtudiant()) + " "
				+ String.format("%10s", etudiant.getPrenomEtudiant()));
		System.out.println("-----------------------------------------------------");
		System.out.println(diplome);
	}

//	private static void validerAuRattrapage(Diplome diplome, Etudiant etudiant) {
//		if (!diplome.isValide()) {
//			System.out.println(etudiant.getPrenomEtudiant() + etudiant.getNomEtudiant()
//					+ " n'a pas eu son diplôme du premier coup.");
//			System.out.println("Heureusement il y a les rattrapges ! Allez on supprime les mauvaises notes !");
//			validerLeDiplomeQuandMeme(diplome);
//		}
//	}

//	public static void validerLeDiplomeQuandMeme(Diplome diplome) {
//		List<Examen> examensASupprimer = diplome.getExamensNoteInferieureA(10);
//		diplome.getExamens(new CompareNoteExamen()).removeAll(examensASupprimer);
//	}

	private static void buildListeExamens(Diplome diplome)
			throws SQLException, DateExamenException, NoteIllegaleException, NombreQuestionsException {
		List<Controle> listeControles = ControleDAO.getListeControles(diplome);
		for (Controle c : listeControles) {
			diplome.ajouterExamen(c);
		}

		List<Projet> listeProjets = ProjetDAO.getListeProjets(diplome);
		for (Projet p : listeProjets) {
			diplome.ajouterExamen(p);
		}

		List<Qcm> listeQcm = QcmDAO.getListeQcm(diplome);
		for (Qcm q : listeQcm) {
			diplome.ajouterExamen(q);
		}
	}
}
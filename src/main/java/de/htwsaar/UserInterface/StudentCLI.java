package de.htwsaar.UserInterface;
import java.util.Scanner;

public class StudentCLI implements CI {

    private final StudentService studentService;
    private final Scanner scanner;

    public StudentCLI(StudentService studentService, Scanner scanner) {
        this.studentService = studentService;
        this.scanner = scanner;
    }

    @Override
    public void starten() {
        try {
        boolean abbruch = false;

        while(!abbruch) {

            int auswahl = scanner.nextInt();
            try {
                auswahl = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Fehler beim lesen der Auswahl");
            }

            System.out.println(" -- Studenten --");
            System.out.println("1) Student hinzufügen");
            System.out.println("2) Studentstammdaten bearbeiten");
            System.out.println("3) Student löschen");
            System.out.println("4) Alle Studenten anzeigen");
            System.out.println("5) Leistungsübersicht eines Studenten");
            System.out.println("6) Durchschnittsnote eines Studenten in einem Kurs");
            System.out.println("0) Beenden");

            try {
                switch (auswahl) {
                    case 1:
                        addstudent();
                    case 2:
                        //Student bearbeiten
                    case 3:
                        //Student löschen
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Ungültige Zahleneingabe!");
            } catch (IllegalArgumentException e) {
                System.out.println("❌ " + e.getMessage());
            }
         }
        } finally {
            scanner.close();
        }
    }

    public void addstudent() {
        System.out.println("Name des Studenten: ");
        String name = scanner.nextLine();

        System.out.println("Matrikelnummer: ");
        int matrikelnummer = scanner.nextInt();

        System.out.println("Studienfach: ");
        String studienfach = scanner.nextLine();

        //Methode zum hinzuügen in StudentSerivce Klasse


    }
}

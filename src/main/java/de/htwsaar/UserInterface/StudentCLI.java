package de.htwsaar.UserInterface;

import de.htwsaar.student.Student;
import de.htwsaar.student.StudentService;

import java.util.*;

public class StudentCLI implements CI {

    private final StudentService studentService;
    private final Scanner scanner;

    public StudentCLI(StudentService studentService, Scanner scanner) {
        this.studentService = studentService;
        this.scanner = scanner;
    }

    @Override
    public void starten() {
        while(true) {
            System.out.println("-- Studentenservice --");
            System.out.println("1) Neuen Studenten anlegen");
            System.out.println("2) Alle Studenten anzeigen");
            System.out.println("3) Studenten nach Matrikelnummer suchen");
            System.out.println("4) Studenten nach Name suchen");
            System.out.println("5) Studenten löschen nach Matrikelnummer");
            System.out.println("6) Studenten löschen nach Vor-& Nachname");
            System.out.println("7) Studiengang ändern nach Matrikelnummer");
            System.out.println("0) Exit");

            String input = scanner.nextLine();

            switch(input) {
                case "1" -> createStudent();
                case "2" -> showAllStudents();
                case "3" -> findStudentByMatrikelnummer();
                case "4" -> findStudentByName();
                case "5" -> deleteStudentByMatrikelnummer();
                case "6" -> deleteStudentByName();
                case "7" -> changeStudiengang();
                case "0" -> { return; }
                default -> System.out.println("Ungültige Eingabe, bitte Zahl auswählen.");
            }
        }
    }

    private void createStudent() {
        System.out.println("Vorname: ");
        String vorname = scanner.nextLine().trim();
        System.out.println("Nachname: ");
        String nachname = scanner.nextLine().trim();
        System.out.println("Studiengang: ");
        String studiengang = scanner.nextLine().trim();

        try{
            Student student = studentService.createStudent(vorname, nachname, studiengang);
            System.out.println("✅ Student angelegt: " + student);
        } catch(IllegalArgumentException e) {
            System.out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void showAllStudents() {
        List<Student> studenten = studentService.showAllStudents();
        if(studenten.isEmpty()) {
            System.out.println("Keine Studenten vorhanden.");
        } else {
            studenten.forEach(System.out::println);
        }
    }

    private void findStudentByMatrikelnummer() {
        System.out.println("Matrikelnummer: ");
        int matrikelnummer = Integer.parseInt(scanner.nextLine().trim());
        Optional<Student> student = studentService.showStudentByMatrikelnummer(matrikelnummer);
        student.ifPresentOrElse(System.out::println, () -> System.out.println("Kein Student gefunden."));
    }

    private void findStudentByName() {
        System.out.println("Vorname: ");
        String vorname = scanner.nextLine().trim();
        System.out.println("Nachname: ");
        String nachname = scanner.nextLine().trim();

        List<Student> studenten = studentService.showStudentByName(vorname, nachname);
        if(studenten.isEmpty()) {
            System.out.println("Kein Student gefunden.");
        } else {
            studenten.forEach(System.out::println);
        }
    }

    private void deleteStudentByMatrikelnummer() {
        System.out.println("Matrikelnummer: ");
        int matrikelnummer = Integer.parseInt(scanner.nextLine().trim());

        try{
            studentService.deleteStudentByMatrikelnummer(matrikelnummer);
            System.out.println("✅ Student gelöscht");
        } catch(IllegalArgumentException e) {
            System.out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void deleteStudentByName() {
        System.out.println("Vorname: ");
        String vorname = scanner.nextLine().trim();
        System.out.println("Nachname: ");
        String nachname = scanner.nextLine().trim();

        try{
            studentService.deleteStudentByName(vorname, nachname);
            System.out.println("✅ Student(en) gelöscht.");
        } catch(IllegalArgumentException e) {
            System.out.println("❌ Fehler: " + e.getMessage());
        }
    }

    private void changeStudiengang() {
        System.out.println("Matrikelnummer: ");
        int matrikelnummer = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Neuer Studiengang: ");
        String neuerStudiengang = scanner.nextLine().trim();

        try{
            studentService.changeStudiengang(matrikelnummer, neuerStudiengang);
            System.out.println("✅ Studiengang geändert.");
        } catch(IllegalArgumentException e) {
            System.out.println("❌ Fehler: " + e.getMessage());
        }
    }
}

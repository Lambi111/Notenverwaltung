package de.htwsaar.UserInterface;

import de.htwsaar.student.Student;
import de.htwsaar.student.StudentService;

import java.util.*;

public class StudentCLI  extends AbstraktCLI{
    private final StudentService studentService;

    public  StudentCLI(StudentService studentService, Scanner scanner) {
        super(scanner);
        this.studentService = studentService;
    }

    @Override
    public void starten() {
        while(true) {
            header("Studentenverwaltung");
            System.out.println("1) Student anlegen");
            System.out.println("2) Alle Studenten anzeigen");
            System.out.println("3) Suche Student nach Matrikelnummer");
            System.out.println("4) Suche Student nach Vor- & Nachname");
            System.out.println("5) Student nach Matrikelnummer löschen");
            System.out.println("6) Student nach Vor- & Nachname löschen");
            System.out.println("7) Studiengang ändern");
            System.out.println("0) Zurück");

            try{
                switch(read("Auswahl")) {
                    case "1" -> studentAnlegen();
                    case "2" -> alleAnzeigen();
                    case "3" -> sucheStudentNachMatrikelnummer();
                    case "4" -> sucheStudentNachName();
                    case "5" -> loescheStudentNachMatrikelnummer();
                    case "6" -> loescheStudentNachName();
                    case "7" -> aendereStudiengang();
                    case "0" -> { return;}
                    default -> System.out.println("❌ Ungültige Auswahl");
                }
            } catch(IllegalArgumentException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    private void studentAnlegen() {
        Student s = studentService.createStudent(
                read("Vorname"),
                read("Nachname"),
                read("Studiengang")
        );
        System.out.println("✅ Angelegt: " + s);
    }

    private void alleAnzeigen() {
        List<Student> students = studentService.showAllStudents();
        students.forEach(System.out::println);
    }

    private void sucheStudentNachMatrikelnummer() {
        int nr = readInt("Matrikelnummer");
        Optional<Student> student = studentService.showStudentByMatrikelnummer(nr);
        student.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("❌ Kein Student gefunden")
        );
    }

    private void sucheStudentNachName() {
        List<Student> students = studentService.showStudentByName(read("Vorname"), read("Nachname"));
        students.forEach(System.out::println);
    }

    private void loescheStudentNachMatrikelnummer() {
        studentService.deleteStudentByMatrikelnummer(readInt("Matrikelnummer"));
        System.out.println("✅ Student gelöscht");
    }

    private void loescheStudentNachName() {
        studentService.deleteStudentByName(read("Vorname"), read("Nachname"));
        System.out.println("✅ Student(en) gelöscht");
    }

    private void aendereStudiengang() {
        studentService.changeStudiengang(readInt("Matrikelnummer"), read("Neuer Studiengang"));
        System.out.println("✅ Studiengang geändert");
    }
}

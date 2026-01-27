package de.htwsaar.student;

import java.util.*;

public class StudentService {
    private StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student createStudent(String vorname, String nachname, String studiengang) {
        Student student = new Student(vorname, nachname, studiengang);
        repository.saveStudent(student);
        return student;
    }

    public List<Student> showAllStudents() {
        return repository.showAllStudents();
    }

    public Optional<Student> showStudentByMatrikelnummer(int matrikelnummer) {
        return repository.showStudentByMatrikelnummer(matrikelnummer);
    }

    public List<Student> showStudentByName(String vorname, String nachname) {
        return repository.showStudentByName(vorname, nachname);
    }

    public void deleteStudentByMatrikelnummer(int matrikelnummer) {
        validateStudentByMatrikelnummer(matrikelnummer);
        repository.deleteStudentByMatrikelnummer(matrikelnummer);
    }

    public void deleteStudentByName(String vorname, String nachname) {
        validateStudentByName(vorname, nachname);
        repository.deleteStudentByName(vorname, nachname);

    }

    public void changeStudiengang(int matrikelnummer, String neuerStudiengang) {
        Optional<Student> student = repository.showStudentByMatrikelnummer(matrikelnummer);
        validateStudentByMatrikelnummer(matrikelnummer);
        repository.changeStudiengang(matrikelnummer, neuerStudiengang);
    }

    private Student validateStudentByMatrikelnummer(int matrikelnummer) {
        Optional<Student> optStudent =
                repository.showStudentByMatrikelnummer(matrikelnummer);

        if(optStudent.isEmpty()) {
            throw new IllegalArgumentException(
                    "Student mit Matrikelnummer " + matrikelnummer + " existiert nicht");
        }

        return optStudent.get();
    }

    private void validateStudentByName(String vorname, String nachname) {
        List<Student> students = repository.showStudentByName(vorname, nachname);

        if(students.isEmpty()) {
            throw new IllegalArgumentException(
                    "Kein Student mit Namen " + vorname + " " + nachname + " gefunden"
            );
        }
    }

}

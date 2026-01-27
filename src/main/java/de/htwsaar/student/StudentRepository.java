package de.htwsaar.student;

import java.util.*;

public interface StudentRepository {
    void saveStudent(Student student);

    List<Student> showAllStudents();

    Optional<Student> showStudentByMatrikelnummer(int matrikelnummer);

    List<Student> showStudentByName(String vorname, String nachname);

    void deleteStudentByMatrikelnummer(int matrikelnummer);

    void deleteStudentByName(String vorname, String nachname);

    void changeStudiengang(int matrikelnummer, String neuerStudiengang);
}


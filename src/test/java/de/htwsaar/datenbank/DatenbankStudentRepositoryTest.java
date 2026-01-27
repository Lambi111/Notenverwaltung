package de.htwsaar.datenbank;

import de.htwsaar.student.Student;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class DatenbankStudentRepositoryTest {
    private DatenbankStudentRepository repository;

    @BeforeEach
    void setUp()  throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:database/notenverwaltung.db");
        DSLContext dsl = DSL.using(conn, SQLDialect.SQLITE);

        repository = new DatenbankStudentRepository(dsl);

        dsl.deleteFrom(DatenbankStudentRepository.Tabelle.STUDENT).execute();
    }

    @Test
    void testMatrikelnummer() {
        Student s = new Student("Max", "Mustermann", "PI");

        repository.saveStudent(s);
        assertEquals(1000000, s.getMatrikelnummer());
    }

    @Test
    void matrikelnummerSevenDigits() {
        Student s = new Student("Max", "Mustermann", "PI");
        repository.saveStudent(s);

        int matrikelnummer = s.getMatrikelnummer();
        assertTrue(matrikelnummer >= 1000000);
        assertTrue(matrikelnummer <= 9999999);
    }

    @Test
    void reusedMatrikelnummer() {
        Student s1 = new Student("Max", "Mustermann", "PI");
        Student s2 = new Student("Erika", "Mustermann", "WI");

        repository.saveStudent(s1);
        repository.saveStudent(s2);

        int deletedNumber = s1.getMatrikelnummer();
        repository.deleteStudentByMatrikelnummer(deletedNumber);

        Student s3 = new Student("Anna", "Meier", "BWL");
        repository.saveStudent(s3);

        assertEquals(deletedNumber, s3.getMatrikelnummer());
    }

    @Test
    void showAllSavedStudents() {
        repository.saveStudent(new Student("Max", "Mustermann", "PI"));
        repository.saveStudent(new Student("Erika", "Mustermann", "WI"));

        List<Student> studenten = repository.showAllStudents();

        assertEquals(2, studenten.size());
        assertEquals("Max", studenten.get(0).getVorname());
        assertEquals("Erika", studenten.get(1).getVorname());
    }

    @Test
    void showStudentByMatrikelnummer() {
        Student s = new Student("Max", "Mustermann", "PI");
        repository.saveStudent(s);

        Optional<Student> gefunden =
                repository.showStudentByMatrikelnummer(s.getMatrikelnummer());
        assertTrue(gefunden.isPresent());
        assertEquals(s, gefunden.get());
    }

    @Test
    void findeKeinenStudentNachMatrikelnummer() {
        Optional<Student> gefunden = repository.showStudentByMatrikelnummer(1234567);

        assertFalse(gefunden.isPresent());
    }

    @Test
    void findeStudentNachName() {
        Student s1 = new Student("Max", "Mustermann", "PI");
        Student s2 = new Student("Max", "Mustermann", "PI");

        repository.saveStudent(s1);
        repository.saveStudent(s2);

        List<Student> studenten = repository.showStudentByName("Max", "Mustermann");
        assertEquals(2, studenten.size());
        assertNotEquals(studenten.get(0).getMatrikelnummer(), studenten.get(1).getMatrikelnummer());
    }

    @Test
    void loescheNachMatrikelnummer() {
        Student s = new Student("Max", "Mustermann", "PI");
        repository.saveStudent(s);

        repository.deleteStudentByMatrikelnummer(s.getMatrikelnummer());
        assertTrue(repository.showStudentByMatrikelnummer(s.getMatrikelnummer()).isEmpty());
    }

}

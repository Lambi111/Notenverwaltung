package de.htwsaar.student;

import de.htwsaar.datenbank.DatenbankStudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.*;

public class StudentServiceTest {
    private StudentService studentService;
    private DSLContext dsl;

    @BeforeEach
    void setUp() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:database/notenverwaltung.db");
        dsl = DSL.using(conn, SQLDialect.SQLITE);

        dsl.deleteFrom(DSL.table("Student")).execute();

        StudentRepository repository = new DatenbankStudentRepository(dsl);
        studentService = new StudentService(repository);
    }

    @Test
    void createStudent() {
        Student s = studentService.createStudent("Max", "Mustermann", "PI");
        Optional<Student> gefunden = studentService.showStudentByMatrikelnummer(s.getMatrikelnummer());
        assertTrue(gefunden.isPresent());
        assertEquals(s, gefunden.get());
    }

    @Test
    void reuseDeletedMatrikelnummer() {
        Student s = studentService.createStudent("Max", "Mustermann", "PI");
        int oldMatrikelnummer = s.getMatrikelnummer();
        studentService.deleteStudentByMatrikelnummer(oldMatrikelnummer);

        Student s2 = studentService.createStudent("Erika", "Mustermann", "PI");
        assertEquals("Erika", s2.getVorname());
        assertEquals(oldMatrikelnummer, s2.getMatrikelnummer());
    }

    @Test
    void showAllStudents() {
        studentService.createStudent("Max", "Mustermann", "PI");
        studentService.createStudent("Erika", "Mustermann", "WI");
        List<Student> studenten = studentService.showAllStudents();
        assertEquals(2, studenten.size());
        assertEquals("Max", studenten.get(0).getVorname());
        assertEquals("Erika", studenten.get(1).getVorname());
    }

    @Test
    void showStudentByMatrikelnummer() {
        Student s = studentService.createStudent("Max", "Mustermann", "PI");

        Optional<Student> gefunden = studentService.showStudentByMatrikelnummer(s.getMatrikelnummer());

        assertTrue(gefunden.isPresent());
        assertEquals(s,gefunden.get());
    }

    @Test
    void showNOStudentForUnknownMatrikelnummer() {
        Optional<Student> gefunden = studentService.showStudentByMatrikelnummer(1234567);
        assertFalse(gefunden.isPresent());
    }

    @Test
    void showStudentByName() {
        Student s = studentService.createStudent("Max","Mustermann", "PI");
        studentService.createStudent("Erika", "Mustermann", "WI");
        List<Student> students = studentService.showStudentByName("Max","Mustermann");
        assertEquals(1, students.size());
        assertEquals(s, students.get(0));
    }

    @Test
    void deleteStudentByMatrikelnummer() {
        Student s = studentService.createStudent("Max", "Mustermann", "PI");
        studentService.deleteStudentByMatrikelnummer(s.getMatrikelnummer());

        assertTrue(studentService
                .showStudentByMatrikelnummer(s.getMatrikelnummer())
                .isEmpty());
    }

    @Test
    void deleteNoStudentForUnknownMatrikelnummer() {
        assertThrows(IllegalArgumentException.class, () ->
                studentService.deleteStudentByMatrikelnummer(1234567));

    }

    @Test
    void deleteStudentByName() {
        Student s1 = studentService.createStudent("Max", "Mustermann", "PI");
        Student s2 = studentService.createStudent("Erika", "Mustermann", "WI");

        assertFalse(studentService.showStudentByName("Max","Mustermann").isEmpty());
        studentService.deleteStudentByName("Max", "Mustermann");

        assertTrue(studentService.showStudentByName(s1.getVorname(), s1.getNachname()).isEmpty());
        assertFalse(studentService.showStudentByName(s2.getVorname(), s2.getNachname()).isEmpty());
    }

    @Test
    void changeStudiengang() {
        Student s = studentService.createStudent("Max", "Mustermann", "WI");

        studentService.changeStudiengang(s.getMatrikelnummer(), "PI");

        Optional<Student> updated = studentService.showStudentByMatrikelnummer(s.getMatrikelnummer());

        assertTrue(updated.isPresent());
        assertEquals("PI",updated.get().getStudiengang());
    }

}

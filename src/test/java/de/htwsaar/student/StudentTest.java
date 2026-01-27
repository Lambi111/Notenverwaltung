package de.htwsaar.student;

import de.htwsaar.student.Student;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StudentTest {
    @Test
    void vornameDarfNichtNullSein() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Student(null,"Mustermann", "PI");
        });
    }

    @Test
    void nachnameDarfNichtLeerSein() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Student("Max", " ", "PI");
        });
    }

    @Test
    void studiengangDarfNichtLeerSein() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Student("Max", "Mustermann", "");
        });
    }

    @Test
    void matrikelNummerIstZuBeginnNull() {
        Student s = new Student("Max", "Mustermann", "PI");
        assertEquals(0,s.getMatrikelnummer());
    }

    @Test
    void testtoStringAusgabe() {
        Student s = new Student("Max", "Mustermann", "PI");
        String erwartet = "Student [matrikelnummer=" + s.getMatrikelnummer()
                + ", vorname=" + s.getVorname() + ", nachname=" + s.getNachname()
                + ", studiengang=" + s.getStudiengang() + "]";
        assertEquals(erwartet, s.toString());
    }

    @Test
    void NichtGleichBeiGleicherMatrikelnummerMit0() {
        Student s1 = new Student("Max", "Mustermann", "PI");
        Student s2 = new Student("Max", "Mustermann", "PI");

        assertNotEquals(s1, s2);
    }

}

package de.htwsaar.Kurs;

import static org.junit.jupiter.api.Assertions.*;

import de.htwsaar.kurs.Kurs;
import org.junit.jupiter.api.Test;

public class KursTest {

    @Test
    void automatischeKursIdVergabe_VonEinsAufsteigend() {
        Kurs.resetIds();

        Kurs prog3 = new Kurs("Programmierung", "-", 3);

        assertEquals(1, prog3.getKursId());
    }

    @Test
    void titleNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Kurs( null, "Klausur", 1);
        });
    }

    @Test
    void titleEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Kurs("", "Klausur", 1);
        });
    }

    @Test
    void semesterKleiner0() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Kurs( "Mathe 1", "Klausur", -1);
        });
    }

    @Test
    void testToString() {
        Kurs mathe = new Kurs( "Mathe 1", "Klausur", 1);
        String expected = "KursId: " + mathe.getKursId() + ", Kurs: " + mathe.getTitel() + "\n" +
                "Semester: " + mathe.getSemester() + "\n" +
                "Beschreibung: " + mathe.getBeschreibung();
        assertEquals(expected, mathe.toString());
    }

    @Test
    void testEqualsNachKursId() {
        Kurs k1 = new Kurs( "Mathe 1", "Klausur", 1);
        Kurs k2 = new Kurs( "Mathe 1", "Klausur", 1);
        assertFalse(k1.equals(k2));
    }

    @Test
    void testHashCodeNachKursId() {
        Kurs k1 = new Kurs( "Mathe 1", "Klausur", 1);
        Kurs k2 = new Kurs( "Mathe 1", "Klausur", 1);
        assertNotEquals(k1.hashCode(), k2.hashCode());
    }

}

package de.htwsaar.Kurs;

import static org.junit.jupiter.api.Assertions.*;

import de.htwsaar.kurs.Kurs;
import org.junit.jupiter.api.Test;

public class KursTest {

    @Test
    void titleNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Kurs(1, null, "Klausur", 1);
        });
    }

    @Test
    void titleEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Kurs(1, "", "Klausur", 1);
        });
    }

    @Test
    void semesterKleiner0() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Kurs(1, "Mathe 1", "Klausur", -1);
        });
    }

    @Test
    void testToString() {
        Kurs mathe = new Kurs(1, "Mathe 1", "Klausur", 1);
        String expected = "Kurs: " + mathe.getTitel() + " (" + mathe.getKursId() + ")" + "\n" +
                "Semester: " + mathe.getSemester() + "\n" +
                "Beschreibung: " + mathe.getBeschreibung();
        assertEquals(expected, mathe.toString());
    }

    @Test
    void testEqualsNachKursId() {
        Kurs k1 = new Kurs(1, "Mathe 1", "Klausur", 1);
        Kurs k2 = new Kurs(1, "Mathe 1", "Klausur", 1);
        assertTrue(k1.equals(k2));
    }

    @Test
    void testHashCodeNachKursId() {
        Kurs k1 = new Kurs(1, "Mathe 1", "Klausur", 1);
        Kurs k2 = new Kurs(1, "Mathe 1", "Klausur", 1);
        assertEquals(k1.hashCode(), k2.hashCode());
    }

}

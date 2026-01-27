package de.htwsaar.Note;

import static org.junit.jupiter.api.Assertions.*;

import de.htwsaar.note.Note;
import org.junit.jupiter.api.Test;

public class NoteTest {

    @Test
    void noteKleinerGleich0() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Note(0, 1, 12345);
        });
    }

    @Test
    void kursIdKleinerGleich0() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Note(2, 0, 12345);
        });
    }

    @Test
    void matrikelnummerKleinerGleich0() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Note(2, 1, 0);
        });
    }

    @Test
    void testToString() {
        Note note = new Note(2, 10, 12345);
        String expected = "NoteId: " + note.getId() +
                ", Note: " + note.getNote() +
                ", KursId: " + note.getKursId() +
                ", Matrikelnummer: " + note.getMatrikelnummer();

        assertEquals(expected, note.toString());
    }

    @Test
    void testEqualsNachId() {
        Note n1 = new Note(2, 10, 12345);
        Note n2 = new Note(2, 10, 12345);

        // id ist bei beiden 0 ? dürfen NICHT gleich sein
        assertFalse(n1.equals(n2));
    }

    @Test
    void testHashCodeNachId() {
        Note n1 = new Note(2, 10, 12345);
        Note n2 = new Note(2, 10, 12345);

        assertNotEquals(n1.hashCode(), n2.hashCode());
    }
}

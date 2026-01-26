package de.htwsaar.datenbank;

import de.htwsaar.note.Note;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatenbankNoteRepositoryTest {

    private DatenbankNoteRepository repo;

    @BeforeEach
    void setUp() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:database/notenverwaltung.db");
        DSLContext dsl = DSL.using(conn, SQLDialect.SQLITE);
        repo = new DatenbankNoteRepository(dsl);
        repo.loescheAlleNoten();
    }

    @Test
    void testIdWirdNachLoeschenWiederverwendet() {
        Note n1 = new Note(1, 10, 111);
        Note n2 = new Note(2, 20, 222);

        repo.speichere(n1);
        repo.speichere(n2);

        repo.loescheNoteNachId(n1.getId());

        Note n3 = new Note(3, 30, 333);
        repo.speichere(n3);

        assertEquals(n1.getId(), n3.getId());
    }

    @Test
    void findeNoteNachId_gibtNoteZurueck() {
        Note note = new Note(2, 10, 12345);
        repo.speichere(note);

        Optional<Note> gefunden = repo.findeNoteNachId(note.getId());
        assertTrue(gefunden.isPresent());
        assertEquals(note, gefunden.get());
    }

    @Test
    void findeNoteNachId_gibtLeeresOptional() {
        Optional<Note> gefunden = repo.findeNoteNachId(1);
        assertTrue(gefunden.isEmpty());
    }

    @Test
    void speichereNote() {
        Note note = new Note(1, 10, 111);
        repo.speichere(note);

        Optional<Note> gefunden = repo.findeNoteNachId(note.getId());
        assertTrue(gefunden.isPresent());
    }

    @Test
    void enthaeltGespeicherteNote() {
        Note note = new Note(2, 15, 222);
        repo.speichere(note);

        List<Note> noten = repo.zeigeAlleNoten();

        assertEquals(2, noten.get(0).getNote());
        assertEquals(15, noten.get(0).getKursId());
        assertEquals(222, noten.get(0).getMatrikelnummer());
    }

    @Test
    void entferntNoteNachLoescheNoteNachId() {
        Note note = new Note(3, 10, 111);
        repo.speichere(note);

        repo.loescheNoteNachId(note.getId());

        assertTrue(repo.findeNoteNachId(note.getId()).isEmpty());
    }

    @Test
    void entferntNoteNachKursIdUndMatrikelnummer() {
        Note n1 = new Note(1, 10, 111);
        Note n2 = new Note(2, 20, 222);

        repo.speichere(n1);
        repo.speichere(n2);

        repo.loescheNoteNachKursIdUndMatrikelnummer(10, 111);

        List<Note> noten = repo.zeigeAlleNoten();
        assertEquals(1, noten.size());
        assertEquals(20, noten.get(0).getKursId());
    }

    @Test
    void findeAlleNoteNachKursId() {
        repo.speichere(new Note(1, 10, 111));
        repo.speichere(new Note(2, 10, 222));
        repo.speichere(new Note(3, 20, 333));

        List<Note> noten = repo.findeAlleNoteNachKursId(10);
        assertEquals(2, noten.size());
    }

    @Test
    void findeAlleNoteNachMatrikelnummer() {
        repo.speichere(new Note(1, 10, 111));
        repo.speichere(new Note(2, 20, 111));

        List<Note> noten = repo.findeAlleNoteNachMatrikelnummer(111);
        assertEquals(2, noten.size());
    }

    @Test
    void aendereNoteNachId() {
        Note note = new Note(5, 10, 111);
        repo.speichere(note);

        repo.aendereNoteNachId(note.getId(), 1);

        Note aktualisiert = repo.findeNoteNachId(note.getId()).get();
        assertEquals(1, aktualisiert.getNote());
    }

    @Test
    void loescheAlleNoten() {
        repo.speichere(new Note(1, 10, 111));
        repo.speichere(new Note(2, 20, 222));

        repo.loescheAlleNoten();

        assertTrue(repo.zeigeAlleNoten().isEmpty());
    }

    @Test
    void berechneDurchschnittsnoteNachMatrikelnummer() {
        repo.speichere(new Note(1, 10, 111));
        repo.speichere(new Note(2, 20, 111));
        repo.speichere(new Note(3, 30, 111));

        double avg = repo.berechneDurchschnittsnoteNachMatrikelnummer(111);

        assertEquals(2.0, avg);
    }

}

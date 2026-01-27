package de.htwsaar.Note;

import de.htwsaar.note.Note;

import de.htwsaar.datenbank.DatenbankNoteRepository;
import de.htwsaar.note.NoteService;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class NoteServiceTest {

    private NoteService noteService;
    private DatenbankNoteRepository repo;

    @BeforeEach
    void setUp() throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:sqlite:database/notenverwaltung.db"
        );
        DSLContext dsl = DSL.using(conn, SQLDialect.SQLITE);
        repo = new DatenbankNoteRepository(dsl);
        repo.loescheAlleNoten();
        noteService = new NoteService(repo);
    }

    @Test
    void speichereErstellteNoteInDatenbank() {
        noteService.erstelleNote(2, 1, 12345);

        List<Note> noten = repo.zeigeAlleNoten();
        assertEquals(2, noten.get(0).getNote());
    }

    @Test
    void erstelleNoteMitUngueltigerNote() {
        assertThrows(IllegalArgumentException.class, () ->
                noteService.erstelleNote(0, 1, 12345)
        );
    }

    @Test
    void loescheNoteNachId() {
        noteService.erstelleNote(2, 1, 12345);
        Note note = repo.zeigeAlleNoten().get(0);

        noteService.loescheNoteNachId(note.getId());

        assertTrue(repo.findeNoteNachId(note.getId()).isEmpty());
    }

    @Test
    void loescheNoteNachKursIdUndMatrikelnummer() {
        noteService.erstelleNote(1, 10, 111);
        noteService.erstelleNote(2, 20, 222);

        noteService.loescheNoteNachKursIdUndMatrikelnummer(10, 111);

        List<Note> verbleibend = repo.zeigeAlleNoten();
        assertEquals(1, verbleibend.size());
        assertEquals(20, verbleibend.get(0).getKursId());
    }

    @Test
    void loescheNichtVorhandeneNoteNachId() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.loescheNoteNachId(99)
        );

        assertEquals("Note mit Id 99 existiert nicht", ex.getMessage());
    }

    @Test
    void zeigeAlleNoten() {
        noteService.erstelleNote(1, 10, 111);
        noteService.erstelleNote(2, 20, 222);

        List<Note> noten = noteService.zeigeAlleNoten();

        assertEquals(2, noten.size());
    }

    @Test
    void findeNoteNachId() {
        noteService.erstelleNote(3, 10, 123);
        Note note = repo.zeigeAlleNoten().get(0);

        Optional<Note> gefunden = Optional.ofNullable(noteService.findeNoteNachId(note.getId()));

        assertEquals(3, gefunden.get().getNote());
    }

    @Test
    void findeNoteNachIdNichtVorhanden() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.findeNoteNachId(55)
        );

        assertEquals("Note mit Id 55 existiert nicht", ex.getMessage());
    }

    @Test
    void findeAlleNotenNachKursId() {
        noteService.erstelleNote(1, 10, 111);
        noteService.erstelleNote(2, 10, 222);
        noteService.erstelleNote(3, 20, 333);

        List<Note> noten = noteService.findeAlleNoteNachKursId(10);

        assertEquals(2, noten.size());
    }

    @Test
    void findeAlleNotenNachMatrikelnummer() {
        noteService.erstelleNote(1, 10, 111);
        noteService.erstelleNote(2, 20, 111);

        List<Note> noten = noteService.findeAlleNoteNachMatrikelnummer(111);

        assertEquals(2, noten.size());
    }

    @Test
    void aendereNoteNachId() {
        noteService.erstelleNote(4, 10, 111);
        Note note = repo.zeigeAlleNoten().get(0);

        noteService.aendereNoteNachId(note.getId(), 1);

        Note aktualisiert = repo.findeNoteNachId(note.getId()).get();
        assertEquals(1, aktualisiert.getNote());
    }

    @Test
    void aendereNoteNachKursUndMatrikelnummer() {
        noteService.erstelleNote(5, 10, 111);

        noteService.aendereNoteNachKursIDUndMatrikelnummer(10, 111, 2);

        Note aktualisiert = repo.zeigeAlleNoten().get(0);
        assertEquals(2, aktualisiert.getNote());
    }

    @Test
    void berechneDurchschnittsnote() {
        noteService.erstelleNote(1, 10, 111);
        noteService.erstelleNote(2, 20, 111);
        noteService.erstelleNote(3, 30, 111);

        double durchschnitt = noteService.berechneDurchschnittsnote(111);

        assertEquals(2.0, durchschnitt);
    }

    @Test
    void berechneDurchschnittsnoteKeineNoten() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.berechneDurchschnittsnote(999)
        );

        assertEquals("Keine Noten fuer Matrikelnummer 999 gefunden", ex.getMessage());
    }

    @Test
    void berechneDurchschnittsnoteNachKursId() {
        noteService.erstelleNote(1, 10, 111);
        noteService.erstelleNote(2, 10, 222);
        noteService.erstelleNote(3, 10, 333);

        double durchschnitt = noteService.berechneDurchschnittsnoteNachKursId(10);

        assertEquals(2.0, durchschnitt);
    }

    @Test
    void berechneDurchschnittsnoteNachKursIdKeineNoten() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.berechneDurchschnittsnoteNachKursId(99)
        );

        assertEquals("Keine Noten fuer KursId 99 gefunden", ex.getMessage());
    }

}

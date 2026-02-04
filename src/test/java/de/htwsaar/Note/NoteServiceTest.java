package de.htwsaar.Note;

import de.htwsaar.datenbank.DatenbankKursRepository;
import de.htwsaar.datenbank.DatenbankStudentRepository;
import de.htwsaar.kurs.Kurs;
import de.htwsaar.note.Note;

import de.htwsaar.datenbank.DatenbankNoteRepository;
import de.htwsaar.note.NoteService;
import de.htwsaar.student.Student;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class NoteServiceTest {

    private NoteService noteService;
    private DatenbankNoteRepository repo;
    private DatenbankKursRepository kursRepo;
    private DatenbankStudentRepository studentRepo;

    @BeforeEach
    void setUp() throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:sqlite:database/notenverwaltung.db"
        );
        DSLContext dsl = DSL.using(conn, SQLDialect.SQLITE);
        repo = new DatenbankNoteRepository(dsl);
        repo.loescheAlleNoten();
        kursRepo = new DatenbankKursRepository(dsl);
        kursRepo.loescheAlleKurse();
        studentRepo = new DatenbankStudentRepository(dsl);
        studentRepo.deleteAllStudents();
        noteService = new NoteService(repo, kursRepo, studentRepo);
    }

    @Test
    void speichereErstellteNoteInDatenbank() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);

        List<Note> noten = repo.zeigeAlleNoten();
        assertEquals(1, noten.get(0).getNote());
    }

    @Test
    void erstelleNoteMitUngueltigerNote() {
        assertThrows(IllegalArgumentException.class, () ->
                noteService.erstelleNote(0, 1, 12345)
        );
    }

    @Test
    void loescheNoteNachId() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);
        Note note = repo.zeigeAlleNoten().get(0);

        noteService.loescheNoteNachId(note.getId());

        assertTrue(repo.findeNoteNachId(note.getId()).isEmpty());
    }

    @Test
    void loescheNoteNachKursIdUndMatrikelnummer() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);

        noteService.loescheNoteNachKursIdUndMatrikelnummer(1, 1000000);

        List<Note> verbleibend = repo.zeigeAlleNoten();
        assertEquals(1, verbleibend.size());
        assertEquals(2, verbleibend.get(0).getKursId());
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
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);

        List<Note> noten = noteService.zeigeAlleNoten();

        assertEquals(2, noten.size());
    }

    @Test
    void findeNoteNachId() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);
        Note note = repo.zeigeAlleNoten().get(0);

        Optional<Note> gefunden = Optional.ofNullable(noteService.findeNoteNachId(note.getId()));
        assertTrue(gefunden.isPresent());
        assertEquals(1, gefunden.get().getNote());
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
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);

        List<Note> noten = noteService.findeAlleNoteNachKursId(1);

        assertEquals(1, noten.size());
    }

    @Test
    void findeAlleNotenNachMatrikelnummer() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);

        List<Note> noten = noteService.findeAlleNoteNachMatrikelnummer(1000000);

        assertEquals(2, noten.size());
    }

    @Test
    void aendereNoteNachId() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);
        Note note = repo.zeigeAlleNoten().get(0);

        noteService.aendereNoteNachId(note.getId(), 1);
        Optional<Note> opt = repo.findeNoteNachId(note.getId());

        assertTrue(opt.isPresent());
        Note aktualisiert = opt.get();
        assertEquals(1, aktualisiert.getNote());
    }

    @Test
    void aendereNoteNachKursUndMatrikelnummer() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);

        noteService.aendereNoteNachKursIDUndMatrikelnummer(1, 1000000, 3);

        Note aktualisiert = repo.zeigeAlleNoten().get(0);
        assertEquals(3, aktualisiert.getNote());
    }

    @Test
    void berechneDurchschnittsnote() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);

        double durchschnitt = noteService.berechneDurchschnittsnote(1000000);

        assertEquals(1.5, durchschnitt);
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
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        studentRepo.saveStudent(new Student("Erika", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 1, 1000001);

        double durchschnitt = noteService.berechneDurchschnittsnoteNachKursId(1);

        assertEquals(1.5, durchschnitt);
    }

    @Test
    void berechneDurchschnittsnoteNachKursIdKeineNoten() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.berechneDurchschnittsnoteNachKursId(99)
        );

        assertEquals("Keine Noten fuer KursId 99 gefunden", ex.getMessage());
    }

    @Test
    void erstelleLeistungsberichtFuerStudent() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(6, 2, 1000000);

        List<String> bericht = noteService.erstelleLeistungsberichtStudent(1000000);

        assertEquals("Kurs-ID: 1, Note: 1, Status: bestanden.", bericht.get(0));
        assertEquals("Kurs-ID: 2, Note: 6, Status: nicht bestanden.", bericht.get(1));
    }

    @Test
    void erstelleLeistungsberichtStudentKeineNoten() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.erstelleLeistungsberichtStudent(999)
        );

        assertEquals(
                "Keine Noten für Matrikelnummer 999", ex.getMessage()
        );
    }

    @Test
    void erstelleLeistungsberichtKurs() {
        kursRepo.speichere(new Kurs("Mathe 1", "wichtig", 1));
        kursRepo.speichere(new Kurs("Informatik 1", "noch wichtiger", 1));
        studentRepo.saveStudent(new Student("Max", "Mustermann", "PI"));
        noteService.erstelleNote(1, 1, 1000000);
        noteService.erstelleNote(2, 2, 1000000);

        Map<String, Integer> bericht = noteService.erstelleLeistungsberichtKurs(1);

        assertEquals(1, bericht.get("bestanden"));
        assertEquals(0, bericht.get("nicht bestanden"));
    }

    @Test
    void erstelleLeistungsberichtKursKeineNoten() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> noteService.erstelleLeistungsberichtKurs(99)
        );

        assertEquals("Kein Kurs 99 gefunden", ex.getMessage());
    }

}

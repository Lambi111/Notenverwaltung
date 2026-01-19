package de.htwsaar.datenbank;

import de.htwsaar.kurs.Kurs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class DatenbankKursRepositoryTest {
    private DatenbankKursRepository repo;

    @BeforeEach
    void setUp() {
        repo = new DatenbankKursRepository();
    }

    @Test
    void findeKursNachId_gibtKursZurueck() {
        Kurs prog3 = new Kurs(0,"Programmierung 3", "Projektarbeit",3);
        repo.speichere(prog3);

        Optional<Kurs> gefunden = repo.findeKursNachId(prog3.getKursId());
        assertTrue(gefunden.isPresent());
        assertEquals(prog3, gefunden.get());
    }

    @Test
    void findeKursNachId_gibtLeeresOptional() {
        Optional<Kurs> gefunden = repo.findeKursNachId(1);
        assertTrue(gefunden.isEmpty());
    }

    @Test
    void speichereKursIdVergeben() {
        Kurs prog3 = new Kurs(0,"Programmierung 3", "",3);

        Kurs gespeichert = repo.speichere(prog3);

        assertEquals(1,gespeichert.getKursId());
    }
    @Test
    void speichereKurs() {
        Kurs prog3 = new Kurs(0,"Programmierung 3", "",3);
        repo.speichere(prog3);

        Optional<Kurs> gefunden = repo.findeKursNachId(prog3.getKursId());
        assertTrue(gefunden.isPresent());
    }

    @Test
    void enhaeltGespeichertenKurs() {
        Kurs prog3 = new Kurs(0, "Programmierung 3", "-",3);
        repo.speichere(prog3);

        List<Kurs> kurse = repo.zeigeAlleKurse();

        assertEquals(1,kurse.get(0).getKursId());
        assertEquals("Programmierung 3", kurse.get(0).getTitel());
        assertEquals("-",kurse.get(0).getBeschreibung());
        assertEquals(3,kurse.get(0).getSemester());
    }

    @Test
    void entferntKursNachLoescheKursNachId() {
        Kurs prog3 = new Kurs(0,"Programmierung 3", "",3);
        repo.speichere(prog3);
        repo.loescheKursNachId(prog3.getKursId());
        assertTrue(repo.findeKursNachId(prog3.getKursId()).isEmpty());
    }

    @Test
    void entferntKurseNachLoescheKurseNachTitel() {
        Kurs prog1 = new Kurs(0,"Programmierung", "-",1);
        Kurs prog2 = new Kurs(0,"Programmierung", "-",2);
        Kurs mathe = new Kurs(0,"Mathe", "-",1);

        repo.speichere(prog1);
        repo.speichere(prog2);
        repo.speichere(mathe);

        repo.loescheKurseNachTitel("Programmierung");

        List<Kurs> verbleibendeKurse = repo.zeigeAlleKurse();
        assertEquals(1,repo.zeigeAlleKurse().size());
        assertEquals("Mathe", verbleibendeKurse.get(0).getTitel());
    }

    @Test
    void findeAlleKurseNachTitel() {
        Kurs prog1 = new Kurs(0,"Programmierung", "-",1);
        Kurs prog2 = new Kurs(0,"Programmierung", "-",2);
        Kurs tI = new Kurs(0,"Theoretische Informatik", "Klausur", 3);

        repo.speichere(prog1);
        repo.speichere(prog2);
        repo.speichere(tI);

        List<Kurs> kurse = repo.findeAlleKurseNachTitel("Programmierung");
        assertEquals(2,kurse.size());
    }

    @Test
    void findeAlleKurseNachTitel_keineTreffer() {
        List<Kurs> kurse = repo.findeAlleKurseNachTitel("Programmierung");

        assertTrue(kurse.isEmpty());
    }

}

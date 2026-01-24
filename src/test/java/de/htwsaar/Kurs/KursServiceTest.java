package de.htwsaar.Kurs;

import de.htwsaar.datenbank.DatenbankKursRepository;
import de.htwsaar.kurs.Kurs;
import de.htwsaar.kurs.KursService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KursServiceTest {
    private KursService kursService;
    private DatenbankKursRepository repo;

    @BeforeEach
    void setUp() throws SQLException {
      Connection conn = DriverManager.getConnection(
              "jdbc:sqlite:database/notenverwaltung.db"
      );
      DSLContext dsl = DSL.using(conn, SQLDialect.SQLITE);
      repo = new DatenbankKursRepository(dsl);
      repo.loescheAlleKurse();
      kursService = new KursService(repo);
    }

    @Test
    void speichereErstelltenKursInDatenbank() {
        kursService.erstelleKurs("Prog 3", "Projekt",3);

        List<Kurs> kurse = repo.zeigeAlleKurse();
        assertEquals("Prog 3", kurse.get(0).getTitel());
    }

    @Test
    void erstelleKursMitUngueltigemSemester() {
        assertThrows(IllegalArgumentException.class, () ->
                kursService.erstelleKurs("Prog 3", "Projekt", 0)
        );
    }

    @Test
    void loescheKursNachId() {
        kursService.erstelleKurs("Prog 1", "Klausur", 1);
        Kurs kurs = repo.zeigeAlleKurse().get(0);

        kursService.loescheKurs(kurs.getKursId());

        assertTrue(repo.findeKursNachId(kurs.getKursId()).isEmpty());

    }

    @Test
    void loescheMehrereKurseNachTitel() {
        kursService.erstelleKurs("Prog", "Klausur", 1);
        kursService.erstelleKurs("Prog", "Projekt", 3);
        kursService.erstelleKurs("Mathe", "Klausur", 3);

        kursService.loescheKurseNachTitel("Prog");

        List<Kurs> verbleibendeKurse = repo.zeigeAlleKurse();

        assertEquals("Mathe",verbleibendeKurse.get(0).getTitel());

    }

    @Test
    void loescheKeineKurseNachUngueltigenTitel() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> kursService.loescheKurseNachTitel("Physik")
        );

        assertEquals("Kurs mit Physik existiert nicht",ex.getMessage());
    }

    @Test
    void ausgabeAllerGespeichertenKurse() {
        kursService.erstelleKurs("Prog 1", "Klausur", 1);
        kursService.erstelleKurs("Prog 2", "Klausur", 2);

        List<Kurs> kurse = kursService.zeigeAlleKurse();

        assertEquals("Prog 2", kurse.get(1).getTitel());
        assertEquals("Prog 1", kurse.get(0).getTitel());
    }

    @Test
    void findeKursNachId() {
        kursService.erstelleKurs("Prog 3", "-", 3);
        Kurs kurs = repo.zeigeAlleKurse().get(0);

        Optional<Kurs> gefunden = Optional.ofNullable(kursService.findeKursNachId(kurs.getKursId()));

        assertEquals("Prog 3", gefunden.get().getTitel());
    }

    @Test
    void findeKursNachIdMitNichtVorhandenerId() {
        //Optional<Kurs> gefunden = Optional.ofNullable(kursService.findeKursNachId(12));
        //assertTrue(gefunden.isEmpty());
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> kursService.findeKursNachId(12)
        );

        assertEquals("Kurs mit 12 existiert nicht", ex.getMessage());
    }

    @Test
    void findeMehrereKurseNachTitel() {
        kursService.erstelleKurs("Prog", "-", 1);
        kursService.erstelleKurs("Prog", "-", 2);
        kursService.erstelleKurs("Mathe", "Klausur", 1);

        List<Kurs> kurse = kursService.findeKursNachTitel("Prog");

        assertEquals(2, kurse.size());
    }

    @Test
    void findeKursNachTitelMitNichtVorhandenemTitel() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> kursService.findeKursNachTitel("Physik")
        );

        assertEquals("Kurs mit Physik existiert nicht", ex.getMessage());
    }

    @Test
    void aendereBeschreibungNachId() {
        kursService.erstelleKurs("Prog 1", "-", 1);
        Kurs kurs = repo.zeigeAlleKurse().get(0);

        kursService.aendereBeschreibungNachId(kurs.getKursId(), "Klausur");

        Kurs aktualisiert = repo.findeKursNachId(kurs.getKursId()).get();
        assertEquals("Klausur", aktualisiert.getBeschreibung());
    }

    @Test
    void aendereKeineBeschreibungWegenUngueltigerId() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> kursService.aendereBeschreibungNachId(99, "Neu")
        );

        assertEquals("Kurs mit 99 existiert nicht", ex.getMessage());
    }

    @Test
    void aendertMehrereKurseNachTitel() {
        kursService.erstelleKurs("Prog", "-", 1);
        kursService.erstelleKurs("Prog", "alt", 2);

        kursService.aendereBeschreibungNachTitel("Prog", "Klausur");

        List<Kurs> kurse = repo.findeAlleKurseNachTitel("Prog");
        assertTrue(kurse.stream()
                .allMatch(k -> k.getBeschreibung().equals("Klausur")));
    }
}

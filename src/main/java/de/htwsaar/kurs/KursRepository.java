package de.htwsaar.kurs;

import java.util.*;

public interface KursRepository {


    void speichere(Kurs kurs);

    List<Kurs> zeigeAlleKurse();

    void loescheKursNachId(int kursId);

    void loescheKurseNachTitel(String titel);

    Optional<Kurs> findeKursNachId(int kursId);

    List<Kurs> findeAlleKurseNachTitel(String titel);

    void loescheAlleKurse();

    void aendereBeschreibungNachId(int kursId, String neueBeschreibung);

    void aendereBeschreibungNachTitel(String titel, String neueBeschreibung);

}

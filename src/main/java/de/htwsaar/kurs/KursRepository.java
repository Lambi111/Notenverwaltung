package de.htwsaar.kurs;

import java.util.*;

public interface KursRepository {

    Kurs speichere(Kurs kurs);

    List<Kurs> zeigeAlleKurse();

    void loescheKursNachId(int kursId);

    void loescheKursNachTitel(String titel);

    Optional<Kurs> findeKursNachId(int kursId);

    Optional<Kurs> findeKursNachTitel(String titel);


}

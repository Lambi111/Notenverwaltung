package de.htwsaar.kurs;

import java.util.*;

public class KursService {
    private final KursRepository kursRepository;

    public KursService(KursRepository kursRepository) {
        this.kursRepository = kursRepository;
    }

    public void erstelleKurs(String titel, String beschreibung, int semester) {
        Kurs kurs = new Kurs(titel, beschreibung, semester);
        kursRepository.speichere(kurs);
    }

    public void loescheKurs(int id) {
        if(kursRepository.findeKursNachId(id).isEmpty()) {
            throw new IllegalArgumentException("Kurs mit " + id + " existiert nicht");
        }
        kursRepository.loescheKursNachId(id);
    }

    public List<Kurs> zeigeAlleKurse() {
        return kursRepository.zeigeAlleKurse();
    }

    //ab hier neu
    public void loescheKurseNachTitel(String titel) {
        kursRepository.loescheKurseNachTitel(titel);
    }

    public void aendereBeschreibungNachId(int kursId,String beschreibung) {
        kursRepository.aendereBeschreibungNachId(kursId, beschreibung);
    }

    public void aendereBeschreibungNachTitel(String titel, String beschreibung) {
        kursRepository.aendereBeschreibungNachTitel(titel, beschreibung);
    }

    public Optional<Kurs> findeKursNachId(int kursId) {
        return kursRepository.findeKursNachId(kursId);
    }

    public List<Kurs> findeKursNachTitel(String titel) {
        return kursRepository.findeAlleKurseNachTitel(titel);
    }
}

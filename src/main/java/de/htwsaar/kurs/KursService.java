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

    public void loescheKurseNachTitel(String titel) {
        List<Kurs> kurse = kursRepository.findeAlleKurseNachTitel(titel);

        if(kurse.isEmpty()) {
            throw new IllegalArgumentException("Kurs mit " + titel + " existiert nicht");
        }

        kursRepository.loescheKurseNachTitel(titel);
    }

    public void aendereBeschreibungNachId(int kursId,String beschreibung) {
        if(kursRepository.findeKursNachId(kursId).isEmpty()) {
            throw new IllegalArgumentException("Kurs mit " + kursId + " existiert nicht");
        }

        kursRepository.aendereBeschreibungNachId(kursId, beschreibung);
    }

    public void aendereBeschreibungNachTitel(String titel, String beschreibung) {
        List<Kurs> kurse = kursRepository.findeAlleKurseNachTitel(titel);

        if(kurse.isEmpty()) {
            throw new IllegalArgumentException("Kurs mit " + titel + " existiert nicht");
        }

        kursRepository.aendereBeschreibungNachTitel(titel, beschreibung);
    }

    public Kurs findeKursNachId(int kursId) {
        Optional<Kurs> kursOpt = kursRepository.findeKursNachId(kursId);

        if(kursOpt.isEmpty()) {
            throw new IllegalArgumentException("Kurs mit " + kursId + " existiert nicht");
        }

        return kursOpt.get();
    }

    public List<Kurs> findeKursNachTitel(String titel) {
        List<Kurs> kurse = kursRepository.findeAlleKurseNachTitel(titel);

        if(kurse.isEmpty()) {
            throw new IllegalArgumentException("Kurs mit " + titel + " existiert nicht");
        }

        return kurse;
    }

}

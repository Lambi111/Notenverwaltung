package de.htwsaar.datenbank;

import de.htwsaar.kurs.Kurs;
import de.htwsaar.kurs.KursRepository;
import java.util.*;
import java.util.stream.Collectors;

public class DatenbankKursRepository implements KursRepository {

    private final Map<Integer,Kurs> speicher = new HashMap<>();
    private int nextKursId = 1;

    @Override
    public Kurs speichere(Kurs kurs) {
        if(kurs.getKursId() == 0) {
            kurs.setKursId(nextKursId++);
        }
        speicher.put(kurs.getKursId(), kurs);
        return kurs;
    }

    @Override
    public List<Kurs> zeigeAlleKurse() {
        return speicher.values().stream().collect(Collectors.toList());
    }

    @Override
    public void loescheKursNachId(int kursId) {
        speicher.remove(kursId);
    }

    @Override
    public void loescheKurseNachTitel(String titel) {
        speicher.values().removeIf(k -> k.getTitel().equals(titel));
    }

    @Override
    public Optional<Kurs> findeKursNachId(int kursId) {
        return Optional.ofNullable(speicher.get(kursId));
    }

    @Override
    public List<Kurs> findeAlleKurseNachTitel(String titel) {
        return speicher.values().stream()
                .filter(kurs -> kurs.getTitel().equals(titel))
                .collect(Collectors.toList());
    }

}

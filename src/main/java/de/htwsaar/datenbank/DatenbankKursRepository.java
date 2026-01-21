package de.htwsaar.datenbank;

import de.htwsaar.kurs.Kurs;
import de.htwsaar.kurs.KursRepository;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record4;
import org.jooq.impl.DSL;

import java.util.*;
import java.util.stream.Collectors;

import static de.htwsaar.datenbank.Tabelle.*;

public class DatenbankKursRepository implements KursRepository {

    //private final Map<Integer,Kurs> speicher = new HashMap<>();
    //private final Set<Integer> verwendeteIds = new HashSet<>();
    //private int nextKursId = 1;

    private final DSLContext dsl;
    private final PriorityQueue<Integer> freieIds = new PriorityQueue<>();

    public DatenbankKursRepository(DSLContext dsl) {
        this.dsl = dsl;
    }


    /*@Override
    public Kurs speichere(Kurs kurs) {
        if(kurs.getKursId() == 0) {
            kurs.setKursId(nextKursId++);
        }
        speicher.put(kurs.getKursId(), kurs);
        return kurs;
    }*/
    @Override
    public void speichere(Kurs kurs) {
            //kurs.setKursId(Kurs.generiereNeueId());
            //kurs.setKursId(nextKursId++);
        if(kurs.getKursId() == 0) {
            int neueId;
            if(!freieIds.isEmpty()) {
                neueId = freieIds.poll();
            } else {
                Integer maxId = dsl.select(DSL.max(Tabelle.KURSID))
                        .from(Tabelle.KURS)
                        .fetchOne(0, Integer.class);
                neueId = (maxId == null) ? 1 : maxId + 1;
            }
            kurs.setKursId(neueId);
        }

        dsl.insertInto(Tabelle.KURS)
                .set(Tabelle.KURSID, kurs.getKursId())
                .set(Tabelle.TITEL, kurs.getTitel())
                .set(Tabelle.BESCHREIBUNG, kurs.getBeschreibung())
                .set(Tabelle.SEMESTER, kurs.getSemester())
                .execute();
        //speicher.put(kurs.getKursId(), kurs);
    }

    /*@Override
    public List<Kurs> zeigeAlleKurse() {
        return speicher.values().stream().collect(Collectors.toList());
    }*/
    @Override
    public List<Kurs> zeigeAlleKurse() {
        List<Record4<Integer,String, String, Integer>> records = dsl.select(KURSID,TITEL, BESCHREIBUNG, SEMESTER)
                .from(KURS)
                .fetch();

        return records.stream()
                .map(this::recordZuKurs)
                .collect(Collectors.toList());
    }

    /*@Override
    public void loescheKursNachId(int kursId) {
        speicher.remove(kursId);
    }*/

    public void loescheKursNachId(int kursId) {
        dsl.deleteFrom(KURS)
                .where(KURSID.eq(kursId))
                .execute();
        //Kurs.entferneId(kursId);
        freieIds.add(kursId);
    }

    @Override
    public void loescheKurseNachTitel(String titel) {
        //speicher.values().removeIf(k -> k.getTitel().equals(titel));
        dsl.deleteFrom(KURS)
                .where(TITEL.eq(titel))
                .execute();
    }

    @Override
    public Optional<Kurs> findeKursNachId(int kursId) {
        //return Optional.ofNullable(speicher.get(kursId));
        Record4<Integer, String, String, Integer> record = dsl.select(KURSID, TITEL, BESCHREIBUNG, SEMESTER)
                .from(KURS)
                .where(KURSID.eq(kursId))
                .fetchOne();

        if(record == null) return Optional.empty();
        return Optional.of(recordZuKurs(record));
    }

    @Override
    public List<Kurs> findeAlleKurseNachTitel(String titel) {
        /*return speicher.values().stream()
                .filter(kurs -> kurs.getTitel().equals(titel))
                .collect(Collectors.toList());*/
        List<Record4<Integer, String, String, Integer>> records = dsl.select(KURSID, TITEL, BESCHREIBUNG, SEMESTER)
                .from(KURS)
                .where(TITEL.eq(titel))
                .fetch();

        return records.stream()
                .map(this::recordZuKurs)
                .collect(Collectors.toList());
    }

    // Hilfsmethode: Record -> Kurs
    private Kurs recordZuKurs(Record4<Integer, String, String, Integer> record) {
        Kurs kurs = new Kurs(record.get(TITEL),record.get(BESCHREIBUNG),record.get(SEMESTER));
        kurs.setKursId(record.get(KURSID));
        /*kurs.setKursId(record.get(KURSID));
        kurs.setTitel(record.get(TITEL));
        kurs.setBeschreibung(record.get(BESCHREIBUNG));
        kurs.setSemester(record.get(SEMESTER));*/
        return kurs;
    }

    public void loescheAlleKurse() {
        dsl.deleteFrom(Tabelle.KURS).execute();
    }


}

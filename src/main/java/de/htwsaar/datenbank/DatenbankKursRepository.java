package de.htwsaar.datenbank;

import de.htwsaar.kurs.Kurs;
import de.htwsaar.kurs.KursRepository;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.util.*;
import java.util.stream.Collectors;


public class DatenbankKursRepository implements KursRepository {

    public static class Tabelle {
        public static final Table<Record> KURS = DSL.table("Kurs");
        public static final Field<Integer> KURSID = DSL.field("kursID", SQLDataType.INTEGER);
        public static final Field<String> TITEL = DSL.field("titel", SQLDataType.VARCHAR);
        public static final Field<String> BESCHREIBUNG = DSL.field("beschreibung", SQLDataType.VARCHAR);
        public static final Field<Integer> SEMESTER = DSL.field("semester", SQLDataType.INTEGER);
    }

    private final DSLContext dsl;
    private final PriorityQueue<Integer> freieIds = new PriorityQueue<>();

    public DatenbankKursRepository(DSLContext dsl) {
        this.dsl = dsl;
    }


    @Override
    public void speichere(Kurs kurs) {
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

    }

    @Override
    public List<Kurs> zeigeAlleKurse() {
        List<Record4<Integer,String, String, Integer>> records = dsl.select(Tabelle.KURSID,Tabelle.TITEL, Tabelle.BESCHREIBUNG, Tabelle.SEMESTER)
                .from(Tabelle.KURS)
                .fetch();

        return records.stream()
                .map(this::recordZuKurs)
                .collect(Collectors.toList());
    }

    public void loescheKursNachId(int kursId) {
        dsl.deleteFrom(Tabelle.KURS)
                .where(Tabelle.KURSID.eq(kursId))
                .execute();
        freieIds.add(kursId);
    }

    @Override
    public void loescheKurseNachTitel(String titel) {
        dsl.deleteFrom(Tabelle.KURS)
                .where(Tabelle.TITEL.eq(titel))
                .execute();
    }

    @Override
    public Optional<Kurs> findeKursNachId(int kursId) {
        Record4<Integer, String, String, Integer> record = dsl.select(Tabelle.KURSID, Tabelle.TITEL, Tabelle.BESCHREIBUNG, Tabelle.SEMESTER)
                .from(Tabelle.KURS)
                .where(Tabelle.KURSID.eq(kursId))
                .fetchOne();

        if(record == null) return Optional.empty();
        return Optional.of(recordZuKurs(record));
    }

    @Override
    public List<Kurs> findeAlleKurseNachTitel(String titel) {
        List<Record4<Integer, String, String, Integer>> records = dsl.select(Tabelle.KURSID, Tabelle.TITEL, Tabelle.BESCHREIBUNG, Tabelle.SEMESTER)
                .from(Tabelle.KURS)
                .where(Tabelle.TITEL.eq(titel))
                .fetch();

        return records.stream()
                .map(this::recordZuKurs)
                .collect(Collectors.toList());
    }

    // Hilfsmethode: Record -> Kurs
    private Kurs recordZuKurs(Record4<Integer, String, String, Integer> record) {
        Kurs kurs = new Kurs(record.get(Tabelle.TITEL),record.get(Tabelle.BESCHREIBUNG),record.get(Tabelle.SEMESTER));
        kurs.setKursId(record.get(Tabelle.KURSID));
        return kurs;
    }

    public void loescheAlleKurse() {
        dsl.deleteFrom(Tabelle.KURS).execute();
    }

    //ab hier neu
    public void aendereBeschreibungNachId(int kursId, String neueBeschreibung) {
        dsl.update(Tabelle.KURS)
                .set(Tabelle.BESCHREIBUNG, neueBeschreibung)
                .where(Tabelle.KURSID.eq(kursId))
                .execute();
    }

    public void aendereBeschreibungNachTitel(String titel, String neueBeschreibung) {
        dsl.update(Tabelle.KURS)
                .set(Tabelle.BESCHREIBUNG, neueBeschreibung)
                .where(Tabelle.TITEL.eq(titel))
                .execute();
    }
}

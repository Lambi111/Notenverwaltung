package de.htwsaar.datenbank;

import de.htwsaar.note.Note;
import de.htwsaar.note.NoteRepository;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;


import java.util.*;
import java.util.stream.Collectors;

public class DatenbankNoteRepository implements NoteRepository {

    public static class Tabelle {
        public static final Table<Record> NOTE = DSL.table("Note");
        public static final Field<Integer> ID = DSL.field("id", SQLDataType.INTEGER);
        public static final Field<Integer> NOTE_WERT = DSL.field("note", SQLDataType.INTEGER);
        public static final Field<Integer> KURSID = DSL.field("kursId", SQLDataType.INTEGER);
        public static final Field<Integer> MATRIKELNUMMER = DSL.field("matrikelnummer", SQLDataType.INTEGER);
    }

    private final DSLContext dsl;
    private final PriorityQueue<Integer> freieIds = new PriorityQueue<>();

    public DatenbankNoteRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void speichere(Note note) {
        if (note.getId() == 0) {
            int neueId;
            if (!freieIds.isEmpty()) {
                neueId = freieIds.poll();
            } else {
                Integer maxId = dsl.select(DSL.max(Tabelle.ID))
                        .from(Tabelle.NOTE)
                        .fetchOne(0, Integer.class);
                neueId = (maxId == null) ? 1 : maxId + 1;
            }
            note.setId(neueId);
        }

        dsl.insertInto(Tabelle.NOTE)
                .set(Tabelle.ID, note.getId())
                .set(Tabelle.NOTE_WERT, note.getNote())
                .set(Tabelle.KURSID, note.getKursId())
                .set(Tabelle.MATRIKELNUMMER, note.getMatrikelnummer())
                .execute();
    }

    @Override
    public List<Note> zeigeAlleNoten() {
        List<Record4<Integer, Integer, Integer, Integer>> records =
                dsl.select(Tabelle.ID, Tabelle.NOTE_WERT, Tabelle.KURSID, Tabelle.MATRIKELNUMMER)
                        .from(Tabelle.NOTE)
                        .fetch();

        return records.stream()
                .map(this::recordZuNote)
                .collect(Collectors.toList());
    }

    @Override
    public void loescheNoteNachId(int id) {
        dsl.deleteFrom(Tabelle.NOTE)
                .where(Tabelle.ID.eq(id))
                .execute();
        freieIds.add(id);
    }

    @Override
    public void loescheNoteNachKursIdUndMatrikelnummer(int kursId, int matrikelnummer) {
        dsl.deleteFrom(Tabelle.NOTE)
                .where(Tabelle.KURSID.eq(kursId)
                        .and(Tabelle.MATRIKELNUMMER.eq(matrikelnummer)))
                .execute();
    }

    @Override
    public Optional<Note> findeNoteNachId(int id) {
        Record4<Integer, Integer, Integer, Integer> record =
                dsl.select(Tabelle.ID, Tabelle.NOTE_WERT, Tabelle.KURSID, Tabelle.MATRIKELNUMMER)
                        .from(Tabelle.NOTE)
                        .where(Tabelle.ID.eq(id))
                        .fetchOne();

        if (record == null) return Optional.empty();
        return Optional.of(recordZuNote(record));
    }

    @Override
    public List<Note> findeAlleNoteNachKursId(int kursId) {
        return dsl.select(Tabelle.ID, Tabelle.NOTE_WERT, Tabelle.KURSID, Tabelle.MATRIKELNUMMER)
                .from(Tabelle.NOTE)
                .where(Tabelle.KURSID.eq(kursId))
                .fetch()
                .stream()
                .map(this::recordZuNote)
                .collect(Collectors.toList());
    }

    @Override
    public List<Note> findeAlleNoteNachMatrikelnummer(int matrikelnummer) {
        return dsl.select(Tabelle.ID, Tabelle.NOTE_WERT, Tabelle.KURSID, Tabelle.MATRIKELNUMMER)
                .from(Tabelle.NOTE)
                .where(Tabelle.MATRIKELNUMMER.eq(matrikelnummer))
                .fetch()
                .stream()
                .map(this::recordZuNote)
                .collect(Collectors.toList());
    }

    @Override
    public void loescheAlleNoten() {
        dsl.deleteFrom(Tabelle.NOTE).execute();
    }

    @Override
    public void aendereNoteNachId(int id, int neueNote) {
        dsl.update(Tabelle.NOTE)
                .set(Tabelle.NOTE_WERT, neueNote)
                .where(Tabelle.ID.eq(id))
                .execute();
    }

    @Override
    public void aendereNoteNachKursIDUndMatrikelnummer(int kursId, int matrikelnummer, int neueNote) {
        dsl.update(Tabelle.NOTE)
                .set(Tabelle.NOTE_WERT, neueNote)
                .where(Tabelle.KURSID.eq(kursId)
                        .and(Tabelle.MATRIKELNUMMER.eq(matrikelnummer)))
                .execute();
    }

    // Hilfsmethode
    private Note recordZuNote(Record4<Integer, Integer, Integer, Integer> record) {
        Note note = new Note(
                record.get(Tabelle.NOTE_WERT),
                record.get(Tabelle.KURSID),
                record.get(Tabelle.MATRIKELNUMMER)
        );
        note.setId(record.get(Tabelle.ID));
        return note;
    }
}

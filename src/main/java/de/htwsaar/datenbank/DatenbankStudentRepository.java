package de.htwsaar.datenbank;

import de.htwsaar.student.Student;
import de.htwsaar.student.StudentRepository;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.util.*;
import java.util.stream.Collectors;

public class DatenbankStudentRepository implements StudentRepository {

    public static class Tabelle {
        public static final Table<Record> STUDENT = DSL.table("Student");
        public static final Field<Integer> MATRIKELNUMMER = DSL.field("Matrikelnummer", SQLDataType.INTEGER);
        public static final Field<String> VORNAME = DSL.field("Vorname", SQLDataType.VARCHAR);
        public static final Field<String> NACHNAME = DSL.field("Nachname", SQLDataType.VARCHAR);
        public static final Field<String> STUDIENGANG = DSL.field("Studiengang", SQLDataType.VARCHAR);
    }

    private final DSLContext dsl;
    private final PriorityQueue<Integer> freieMatrikelnummer = new PriorityQueue<>();

    private static final int START_MATRIKELNUMMER = 1000000;

    public DatenbankStudentRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void saveStudent(Student student) {
        if (student.getMatrikelnummer() == 0) {
            Integer max = dsl.select(DSL.max(Tabelle.MATRIKELNUMMER))
                    .from(Tabelle.STUDENT)
                    .fetchOne(0, Integer.class);

            int neueNummer = freieMatrikelnummer.isEmpty()
                    ? (max == null ? START_MATRIKELNUMMER : max + 1)
                    : freieMatrikelnummer.poll();

            student.setMatrikelnummer(neueNummer);
        }

        dsl.insertInto(Tabelle.STUDENT)
                .set(Tabelle.MATRIKELNUMMER, student.getMatrikelnummer())
                .set(Tabelle.VORNAME, student.getVorname())
                .set(Tabelle.NACHNAME, student.getNachname())
                .set(Tabelle.STUDIENGANG, student.getStudiengang())
                .execute();
    }

    @Override
    public List<Student> showAllStudents() {
        return dsl.selectFrom(Tabelle.STUDENT)
                .fetch()
                .stream()
                .map(this::recordZuStudent)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Student> showStudentByMatrikelnummer(int matrikelnummer) {
        var record = dsl.selectFrom(Tabelle.STUDENT)
                .where(Tabelle.MATRIKELNUMMER.eq(matrikelnummer))
                .fetchOne();

        if(record == null) return Optional.empty();
        return Optional.of(recordZuStudent(record));
    }

    @Override
    public List<Student> showStudentByName(String vorname, String nachname) {
        return dsl.selectFrom(Tabelle.STUDENT)
                .where(Tabelle.VORNAME.eq(vorname).and(Tabelle.NACHNAME.eq(nachname)))
                .fetch()
                .stream()
                .map(this::recordZuStudent)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStudentByMatrikelnummer(int matrikelnummer) {
        dsl.deleteFrom(Tabelle.STUDENT)
                .where(Tabelle.MATRIKELNUMMER.eq(matrikelnummer))
                .execute();

        freieMatrikelnummer.add(matrikelnummer);

    }

    @Override
    public void deleteStudentByName(String vorname, String nachname) {
        List<Integer> numbers = dsl.select(Tabelle.MATRIKELNUMMER)
                .from(Tabelle.STUDENT)
                .where(Tabelle.VORNAME.eq(vorname).and(Tabelle.NACHNAME.eq(nachname))
                )
                .fetch(Tabelle.MATRIKELNUMMER);

        dsl.deleteFrom(Tabelle.STUDENT)
                .where(Tabelle.VORNAME.eq(vorname).and(Tabelle.NACHNAME.eq(nachname)))
                .execute();

        freieMatrikelnummer.addAll(numbers);
    }

    @Override
    public void changeStudiengang(int matrikelnummer, String neuerStudiengang) {
        dsl.update(Tabelle.STUDENT)
                .set(Tabelle.STUDIENGANG, neuerStudiengang)
                .where(Tabelle.MATRIKELNUMMER.eq(matrikelnummer))
                .execute();
    }

    private Student recordZuStudent(Record record) {
        Student student = new Student(
                record.get(Tabelle.VORNAME),
                record.get(Tabelle.NACHNAME),
                record.get(Tabelle.STUDIENGANG)
        );
        student.setMatrikelnummer(record.get(Tabelle.MATRIKELNUMMER));
        return student;
    }

}

package de.htwsaar.datenbank;

import de.htwsaar.student.Student;
import de.htwsaar.student.StudentRepository;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static de.htwsaar.datenbank.DatenbankStudentRepository.Tabelle.MATRIKELNUMMER;
import static de.htwsaar.datenbank.DatenbankStudentRepository.Tabelle.STUDENT;

public class DatenbankStudentRepository implements StudentRepository {

    public static class Tabelle {
        public static final Table<Record> STUDENT = DSL.table(DSL.name("Student"));

        public static final Field<Integer> MATRIKELNUMMER =
                STUDENT.field(DSL.name("Matrikelnummer"), Integer.class);

        public static final Field<String> VORNAME =
                STUDENT.field(DSL.name("Vorname"), String.class);

        public static final Field<String> NACHNAME =
                STUDENT.field(DSL.name("Nachname"), String.class);

        public static final Field<String> STUDIENGANG =
                STUDENT.field(DSL.name("Studiengang"), String.class);
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
                    .from(STUDENT)
                    .fetchOne(0, Integer.class);

            int neueNummer = freieMatrikelnummer.isEmpty()
                    ? (max == null ? START_MATRIKELNUMMER : max + 1)
                    : freieMatrikelnummer.poll();

            student.setMatrikelnummer(neueNummer);
        }

        dsl.insertInto(STUDENT)
                .set(Tabelle.MATRIKELNUMMER, student.getMatrikelnummer())
                .set(Tabelle.VORNAME, student.getVorname())
                .set(Tabelle.NACHNAME, student.getNachname())
                .set(Tabelle.STUDIENGANG, student.getStudiengang())
                .execute();
    }


    @Override
    public boolean existsByMatrikelnummer(int matrikelnummer) {
        Field<Integer> matrikelnummerField = DSL.field(DSL.name("matrikelnummer"), Integer.class);

        return dsl.fetchExists(
                dsl.selectFrom(DSL.table(DSL.name("Student")))
                        .where(matrikelnummerField.eq(matrikelnummer))
        );
    }

    @Override
    public List<Student> showAllStudents() {
        return dsl.selectFrom(STUDENT)
                .fetch()
                .stream()
                .map(this::recordZuStudent)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Student> showStudentByMatrikelnummer(int matrikelnummer) {
        var record = dsl.selectFrom(STUDENT)
                .where(Tabelle.MATRIKELNUMMER.eq(matrikelnummer))
                .fetchOne();

        if(record == null) return Optional.empty();
        return Optional.of(recordZuStudent(record));
    }

    @Override
    public List<Student> showStudentByName(String vorname, String nachname) {
        return dsl.selectFrom(STUDENT)
                .where(Tabelle.VORNAME.eq(vorname).and(Tabelle.NACHNAME.eq(nachname)))
                .fetch()
                .stream()
                .map(this::recordZuStudent)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStudentByMatrikelnummer(int matrikelnummer) {
        dsl.deleteFrom(STUDENT)
                .where(Tabelle.MATRIKELNUMMER.eq(matrikelnummer))
                .execute();

        freieMatrikelnummer.add(matrikelnummer);

    }

    @Override
    public void deleteStudentByName(String vorname, String nachname) {
        List<Integer> numbers = dsl.select(Tabelle.MATRIKELNUMMER)
                .from(STUDENT)
                .where(Tabelle.VORNAME.eq(vorname).and(Tabelle.NACHNAME.eq(nachname))
                )
                .fetch(Tabelle.MATRIKELNUMMER);

        dsl.deleteFrom(STUDENT)
                .where(Tabelle.VORNAME.eq(vorname).and(Tabelle.NACHNAME.eq(nachname)))
                .execute();

        freieMatrikelnummer.addAll(numbers);
    }

    @Override
    public void changeStudiengang(int matrikelnummer, String neuerStudiengang) {
        dsl.update(STUDENT)
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

    @Override
    public void deleteAllStudents() {
        dsl.deleteFrom(Tabelle.STUDENT).execute();
    }

}

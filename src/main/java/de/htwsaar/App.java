package de.htwsaar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import de.htwsaar.UserInterface.MainCLI;
import de.htwsaar.UserInterface.NoteCLI;
import de.htwsaar.UserInterface.StudentCLI;
import de.htwsaar.datenbank.DatenbankNoteRepository;
import de.htwsaar.datenbank.DatenbankStudentRepository;
import de.htwsaar.kurs.*;
        import de.htwsaar.datenbank.DatenbankKursRepository;
import de.htwsaar.note.NoteRepository;
import de.htwsaar.note.NoteService;
import de.htwsaar.student.StudentRepository;
import de.htwsaar.student.StudentService;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import de.htwsaar.UserInterface.KursCLI;


public class App {
    public static void main(String[] args) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:sqlite:database/notenverwaltung.db", "sa", "");
        DSLContext dsl = DSL.using(conn, SQLDialect.SQLITE);

        Scanner scanner = new Scanner(System.in);

        KursRepository kursRepo = new DatenbankKursRepository(dsl);
        KursService kursService = new KursService(kursRepo);
        KursCLI kursCli = new KursCLI(kursService, scanner);

        NoteRepository noteRepo = new DatenbankNoteRepository(dsl);
        NoteService noteService = new NoteService(noteRepo);
        NoteCLI notenCli = new NoteCLI(noteService, scanner);

        StudentRepository studentRepo = new DatenbankStudentRepository(dsl);
        StudentService studentService = new StudentService(studentRepo);
        StudentCLI studentCli = new StudentCLI(studentService, scanner);


        MainCLI mainCLI = new MainCLI(kursCli, studentCli, notenCli, scanner);
        mainCLI.starten();

    }
}
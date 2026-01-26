package de.htwsaar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.htwsaar.kurs.*;
        import de.htwsaar.datenbank.DatenbankKursRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;


public class App {
    public static void main(String[] args) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:sqlite:database/notenverwaltung.db", "sa", "");
        DSLContext dsl = DSL.using(conn, SQLDialect.SQLITE);

        KursRepository kursRepo = new DatenbankKursRepository(dsl);

        KursService kursService = new KursService(kursRepo);

        KursCLI kursCli = new KursCLI(kursService);

        kursCli.start();

    }
}
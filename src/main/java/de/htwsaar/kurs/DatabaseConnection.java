package de.htwsaar.kurs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL =
            "jdbc:sqlite:database/notenverwaltung.db";

    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");

            return DriverManager.getConnection(DB_URL);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC Treiber nicht gefunden", e);
        } catch (SQLException e) {
            throw new RuntimeException("Fehler bei DB-Verbindung", e);
        }
    }
}



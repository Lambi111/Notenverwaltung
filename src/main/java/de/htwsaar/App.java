package de.htwsaar;

import de.htwsaar.kurs.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hello world!
 */
//public class App {
//    public static void main(String[] args) {
//        System.out.println("Hello World!");
//    }
//}

public class App {
    public static void main(String[] args) throws SQLException {
        try (Connection conn = DatabaseConnection.connect()) {
            System.out.println("Verbindung erfolgreich!");
        }
    }
}


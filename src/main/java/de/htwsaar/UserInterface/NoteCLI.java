package de.htwsaar.UserInterface;

import java.util.Scanner;

public class NoteCLI implements CI {

    private final NotenService notenService;
    private final Scanner scanner;

    public NoteCLI(NotenService noteService, Scanner scanner) {
        this.notenService = noteService;
        this.scanner = scanner;
    }

    @Override
    public void starten() {
        try {
            boolean abbruch = false;

            while (!abbruch) {

                int auswahl = scanner.nextInt();
                try {
                    auswahl = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Fehler beim lesen der Auswahl");
                }

                System.out.println(" -- Noten --");
                System.out.println("1) Note bearbeiten");
                System.out.println("2) Note löschen");
                System.out.println("3) Note zuweisen");
                System.out.println("0) beenden");

                try {
                    switch (auswahl) {
                        case 1:
                            notenService
                        case 2:
                            //Student bearbeiten
                        case 3:
                            //Student löschen
                    }
                } catch (Exception e) {
                    System.out.println("❌ Ungültige Zahleneingabe!");
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ " + e.getMessage());
                }
            }
        } finally {
            scanner.close();
        }

    }

}

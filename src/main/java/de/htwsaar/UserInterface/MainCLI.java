package de.htwsaar.UserInterface;


import java.util.*;

public class MainCLI implements CI {
    private final Scanner scanner;

    private final KursCLI kursUI;
    private final StudentCLI studentUI;
    private final NoteCLI notenUI;


    public MainCLI(KursCLI kursUI, StudentCLI studentUI, NoteCLI notenUI, Scanner scanner) {

        this.kursUI = kursUI;
        this.studentUI = studentUI;
        this.notenUI = notenUI;
        this.scanner = scanner;
    }

    @Override
    public void starten() {
        try {

            while(true) {
                System.out.println(" -- Hauptmenü -- ");
                System.out.println("1) Kursverwaltung");
                System.out.println("2) Studentverwaltung");
                System.out.println("3) Notenverwaltung");
                System.out.println("0) Beenden");
                System.out.println("> ");

                String eingabe = scanner.nextLine().trim();

                int auswahl;
                try{
                    auswahl = Integer.parseInt(eingabe);
                } catch(NumberFormatException e) {
                    System.out.println("❌ Bitte eine Zahl eingeben!");
                    continue;
                }

                try{
                    switch (auswahl) {
                        case 1 -> kursUI.starten();

                        case 2 -> studentUI.starten();

                        case 3 -> notenUI.starten();

                        case 0 -> {
                            System.out.println("Programm beendet!");
                            return;
                        }

                        default -> System.out.println("❌ Ungültige Auswahl!" + auswahl);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Ungültige Zahleneingabe!");
                } catch(IllegalArgumentException e ) {
                    System.out.println("❌ " + e.getMessage());
                }
            }
        } finally {
            scanner.close();
        }

    }
}
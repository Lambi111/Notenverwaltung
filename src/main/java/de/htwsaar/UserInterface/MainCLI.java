package de.htwsaar.kurs;


import de.htwsaar.UserInterface.KursCLInew;
import de.htwsaar.UserInterface.NoteCLI;
import de.htwsaar.UserInterface.StudentCLI;
import de.htwsaar.UserInterface.CI



import java.util.*;

public class MainCLI implements CI {
    private final Scanner scanner;

    private final KursCLInew kursUI;
    private final StudentCLI studentUI;
    private final NoteCLI notenUI;


    public MainCLI(KursCLInew kursUI, StudentCLI studentUI, NoteCLI notenUI, Scanner scanner) {

        this.kursUI = kursUI;
        this.studentUI = studentUI;
        this.notenUI = notenUI;
        this.scanner = scanner;
    }

    @Override
    public void starten() {
        try {
            boolean running = true;

            while(running) {

                int auswahl = scanner.nextInt();
                try{
                    auswahl = Integer.parseInt(scanner.nextLine());
                } catch(NumberFormatException e) {
                    System.out.println("❌ Bitte eine Zahl eingeben!");
                    continue;
                }

                System.out.println("\n -- Hauptmenü -- ");
                System.out.println("1) Kursverwaltung");
                System.out.println("2) Studentverwaltung");
                System.out.println("3) Notenverwaltung");
                System.out.println("0) Beenden");
                System.out.println("> ");

                try{
                    switch (auswahl) {
                        case 1:
                            kursUI.starten();
                            break;
                        case 2:
                            studentUI.starten();
                            break;
                        case 3:
                            notenUI.starten();
                            break;
                        case 0:
                        {
                            System.out.println("Programm beendet!");
                            running = false;
                        }
                        default:
                            System.out.println("❌ Ungültige Auswahl!" + auswahl);
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
package de.htwsaar.kurs;

import java.util.*;

public class KursCLI {
    private final KursService kursService;

    public KursCLI(KursService kursService) {
        this.kursService = kursService;
    }

    private void zeigeMenue() {
        System.out.println("\nBitte wählen:");
        System.out.println("1) Kurs anlegen");
        System.out.println("2) Alle Kurse anzeigen");
        System.out.println("3) Kurs nach ID lösschen");
        System.out.println("4) Kurse nach Titel löschen");
        System.out.println("5) Kursbeschreibung ändern");
        System.out.println("6) Kurs suchen");
        System.out.println("0) Beenden");
        System.out.println("> ");
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        try{
            boolean running = true;

            while(running) {
                zeigeMenue();

                int auswahl;
                try{
                    auswahl = Integer.parseInt(scanner.nextLine());
                } catch(NumberFormatException e) {
                    System.out.println("❌ Bitte eine Zahl eingeben!");
                    continue;
                }

                try{
                    switch (auswahl) {
                        case 1 -> kursAnlegen(scanner);
                        case 2 -> alleKurseAnzeigen();
                        case 3 -> kursNachIdLoeschen(scanner);
                        case 4 -> kursNachTitelLoeschen(scanner);
                        case 5 -> beschreibungAendern(scanner);
                        case 6 -> kursSuchen(scanner);
                        case 0 -> {
                            System.out.println("Programm beendet!");
                            running = false;
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

    private void kursAnlegen(Scanner scanner) {
        System.out.println("Titel: ");
        String titel = scanner.nextLine();

        System.out.println("Beschreibung: ");
        String beschreibung = scanner.nextLine();

        System.out.println("Semester: ");
        int semester = Integer.parseInt(scanner.nextLine());

        kursService.erstelleKurs(titel, beschreibung, semester);
        System.out.println("✅ Kurs gespeichert");
    }

    private void alleKurseAnzeigen() {
        var kurse = kursService.zeigeAlleKurse();
        if(kurse.isEmpty()) {
            System.out.println("ℹ️ Keine Kurse vorhanden");
        } else {
            kurse.forEach(System.out::println);
        }
    }

    private void kursNachIdLoeschen(Scanner scanner) {
        System.out.println("Nach ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        kursService.loescheKurs(id);
        System.out.println("✅ Kurs gelöscht");
    }

    private void kursNachTitelLoeschen(Scanner scanner) {
        System.out.println("Titel: ");
        String titel = scanner.nextLine();
        kursService.loescheKurseNachTitel(titel);
        System.out.println("✅ Kurse gelöscht");
    }

    private void beschreibungAendern(Scanner scanner) {
        System.out.println("1) Nach ID:");
        System.out.println("2) Nach Titel:");
        System.out.println("> ");
        int wahl = Integer.parseInt(scanner.nextLine());

        System.out.println("Neue Beschreibung: ");
        String neu = scanner.nextLine();

        if(wahl == 1) {
            System.out.println("Kurs-ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            kursService.aendereBeschreibungNachId(id, neu);
        } else if(wahl == 2) {
            System.out.println("Titel: ");
            String titel = scanner.nextLine();
            kursService.aendereBeschreibungNachTitel(titel, neu);
        } else {
            System.out.println("❌ Ungültige Auswahl!");
        }
    }

    private void kursSuchen(Scanner scanner) {
        System.out.println("1) Nach ID:");
        System.out.println("2) Nach Titel:");
        System.out.println("> ");
        int wahl = Integer.parseInt(scanner.nextLine());

        if(wahl == 1) {
            System.out.println("Kurs-ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            kursService.findeKursNachId(id)
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("❌ Kurs nicht gefunden!")
                    );
        } else if(wahl == 2) {
            System.out.println("Titel: ");
            String titel = scanner.nextLine();
            var kurse = kursService.findeKursNachTitel(titel);

            if(kurse.isEmpty()) {
                System.out.println("❌ Keine Kurse gefunden");
            } else {
                kurse.forEach(System.out::println);
            }
        } else {
            System.out.println("❌ Ungültige Auswahl!");
        }
    }
}

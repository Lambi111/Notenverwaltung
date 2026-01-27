package de.htwsaar.UserInterface;

import de.htwsaar.kurs.Kurs;
import de.htwsaar.kurs.KursService;

import java.util.Optional;
import java.util.Scanner;

public class KursCLInew implements CI {

    private final KursService kursService;
    private final Scanner scanner;

    public KursCLInew(KursService kursService, Scanner scanner) {
        this.kursService = kursService;
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

                System.out.println("\n -- KursService -");
                System.out.println("1) Kurs anlegen");
                System.out.println("2) Alle Kurse anzeigen");
                System.out.println("3) Kurs nach ID lösschen");
                System.out.println("4) Kurse nach Titel löschen");
                System.out.println("5) Kursbeschreibung ändern");
                System.out.println("6) Kurs suchen");
                System.out.println("0) Beenden");
                System.out.println("> ");

                try {
                    switch (auswahl) {
                        case 1:
                            kursAnlegen();
                            break;
                        case 2:
                            alleKurseAnzeigen();
                            break;
                        case 3:
                            kursNachIdLoeschen();
                            break;
                        case 4:
                            kursNachTitelLoeschen();
                            break;
                        case 5:
                            beschreibungAendern();
                            break;
                        case 6:
                            kursSuchen();
                            break;
                        case 7:
                            leistungsUebersicht();
                            break;
                        case 8:
                            durchscnitssnoteKurs();
                            break;
                        default:
                            System.out.println("❌ Ungültige Auswahl!" + auswahl);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Ungültige Zahleneingabe!");
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ " + e.getMessage());
                }
            }
        } finally {
            scanner.close();
        }

    }

    private void kursAnlegen() {
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

    private void kursNachIdLoeschen() {
        System.out.println("Nach ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        kursService.loescheKurs(id);
        System.out.println("✅ Kurs gelöscht");
    }

    private void kursNachTitelLoeschen() {
        System.out.println("Titel: ");
        String titel = scanner.nextLine();
        kursService.loescheKurseNachTitel(titel);
        System.out.println("✅ Kurse gelöscht");
    }

    private void beschreibungAendern() {
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

    private void kursSuchen() {
        System.out.println("1) Nach ID:");
        System.out.println("2) Nach Titel:");
        System.out.println("> ");
        int wahl = Integer.parseInt(scanner.nextLine());

        if(wahl == 1) {
            System.out.println("Kurs-ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            /*kursService.findeKursNachId(id)
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("❌ Kurs nicht gefunden!")
                    );*/
            Optional<Kurs> kursOpt = Optional.ofNullable(kursService.findeKursNachId(id));

            if(kursOpt.isPresent()) {
                System.out.println(kursOpt.get());
            } else {
                System.out.println("❌ Kurs nicht gefunden");
            }
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

    private void leistungsUebersicht() {
        System.out.println("1) Nach ID:");
        System.out.println("2) Nach Titel:");
        System.out.println("> ");
        int wahl = Integer.parseInt(scanner.nextLine());
        if (wahl == 1) {
            System.out.println("Kurs-ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            //Methodenaufruf Leistung
        }
        if (wahl == 2) {
            System.out.println("Titel: ");
            String titel = scanner.nextLine();
            //Methodenaufruf Leistung
        } else {
            System.out.println("Ungültige Auswahl!");
        }
    }

    private void durchscnitssnoteKurs() {
        System.out.println("1) Nach ID:");
        System.out.println("2) Nach Titel:");
        System.out.println("> ");
        int wahl = Integer.parseInt(scanner.nextLine());
        if (wahl == 1) {
            System.out.println("Kurs-ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            //Methodenaufruf durchschnitt
        }
        if (wahl == 2) {
            System.out.println("Titel: ");
            String titel = scanner.nextLine();
            //Methodenaufruf durchschnitt
        } else {
            System.out.println("Ungültige Auswahl!");
        }
    }
}


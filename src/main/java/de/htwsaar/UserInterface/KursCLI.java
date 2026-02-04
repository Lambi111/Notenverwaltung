package de.htwsaar.UserInterface;

import de.htwsaar.kurs.Kurs;
import de.htwsaar.kurs.KursService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class KursCLI extends AbstraktCLI {
    private final KursService kursService;

    public KursCLI(KursService kursService, Scanner scanner) {
        super(scanner);
        this.kursService = kursService;
    }

    @Override
    public void starten() {
        while(true) {
            header("Kursverwaltung");
            System.out.println("1) Kurs anlegen");
            System.out.println("2) Alle Kurse anzeigen");
            System.out.println("3) Kurs nach ID loeschen");
            System.out.println("4) Kurse nach Titel loeschen");
            System.out.println("5) Beschreibung aendern");
            System.out.println("6) Kurs suchen");
            System.out.println("0) Zurueck");

            try{
                switch(read("Auswahl")) {
                    case "1" -> kursAnlegen();
                    case "2" -> alleKurseAnzeigen();
                    case "3" -> kursNachIDLoeschen();
                    case "4" -> kursNachTitelLoeschen();
                    case "5" -> beschreibungAendern();
                    case "6" -> kursSuchen();
                    case "0" -> { return; }
                    default -> System.out.println("❌ Ungültige Auswahl");
                }
            } catch(IllegalArgumentException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    private void kursAnlegen() {
        String titel = read("Titel");
        String beschreibung = read("Beschreibung");
        int semester = readInt("Semester");

        kursService.erstelleKurs(titel, beschreibung, semester);
        System.out.println("✅ Kurs gespeichert");
    }

    private void alleKurseAnzeigen() {
        List<Kurs> kurse = kursService.zeigeAlleKurse();
        if(kurse.isEmpty()) {
            System.out.println("ℹ️ Keine Kurse vorhanden");
        } else {
            kurse.forEach(System.out::println);
        }
    }

    private void kursNachIDLoeschen() {
        int id = readInt("Kurs-ID");
        kursService.loescheKurs(id);
        System.out.println("✅ Kurs gelöscht");
    }

    private void kursNachTitelLoeschen() {
        String titel = read("Titel");
        kursService.loescheKurseNachTitel(titel);
        System.out.println("✅ Kurse gelöscht");
    }

    private void beschreibungAendern() {
        System.out.println("1) Nach ID");
        System.out.println("2) Nach Titel");

        String wahl = read("Auswahl");
        String neu = read("Neue Beschreibung");

        if(wahl.equals("1")) {
            kursService.aendereBeschreibungNachId(readInt("Kurs-ID"), neu);
        } else if(wahl.equals("2")) {
            kursService.aendereBeschreibungNachTitel(read("Titel"), neu);
        } else {
            System.out.println("❌ Ungültige Auswahl");
        }
    }

    private void kursSuchen() {
        System.out.println("1) Nach ID");
        System.out.println("2) Nach Titel");

        String wahl = read("Auswahl");

        if(wahl.equals("1")) {
            int id = readInt("Kurs-ID");
            Optional<Kurs> kurs = Optional.ofNullable(kursService.findeKursNachId(id));
            kurs.ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("❌ Kurs nicht gefunden")
            );
        } else if(wahl.equals("2")) {
            List<Kurs> kurse = kursService.findeKursNachTitel(read("Titel"));
            kurse.forEach(System.out::println);
        } else {
            System.out.println("❌ Ungültige Auswahl");
        }
    }
}
package de.htwsaar.UserInterface;


import de.htwsaar.note.NoteService;

import java.util.*;

public class NoteCLI implements CI {

    private final NoteService noteService;
    private final Scanner scanner;

    public NoteCLI(NoteService noteService, Scanner scanner) {
        this.noteService = noteService;
        this.scanner = scanner;
    }

    private void zeigeMenue() {
        System.out.println("-- Studentservice --");
        System.out.println("1) Note anlegen");
        System.out.println("2) Alle Noten anzeigen");
        System.out.println("3) Note nach ID löschen");
        System.out.println("4) Note nach KursId & Matrikelnummer löschen");
        System.out.println("5) Note ändern");
        System.out.println("6) Note suchen");
        System.out.println("7) Durchschnittsnote eines Studenten berechnen");
        System.out.println("8) Durchschnittsnote eines Kurses berechnen");
        System.out.println("9) Leistungsübersicht eines Studenten");
        System.out.println("0) Beenden");
        System.out.println("> ");
    }

    @Override
    public void starten() {
        try {
            boolean running = true;

            while (running) {
                zeigeMenue();

                String input = scanner.nextLine();

                try {
                    switch (input) {
                        case "1" -> noteAnlegen(scanner);
                        case "2" -> alleNotenAnzeigen();
                        case "3" -> noteNachIdLoeschen(scanner);
                        case "4" -> noteNachKursUndMatrikelLoeschen(scanner);
                        case "5" -> noteAendern(scanner);
                        case "6" -> noteSuchen(scanner);
                        case "7" -> durchschnittBerechnen(scanner);
                        case "8" -> durchschnittKursBerechnen(scanner);
                        case "0" -> {
                            System.out.println("Programm beendet!");
                            running = false;
                        }
                        default -> System.out.println("❌ Ungültige Auswahl! " + input);
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

    private void noteAnlegen(Scanner scanner) {
        System.out.println("Note: ");
        int note = Integer.parseInt(scanner.nextLine());

        System.out.println("KursId: ");
        int kursId = Integer.parseInt(scanner.nextLine());

        System.out.println("Matrikelnummer: ");
        int matrikelnummer = Integer.parseInt(scanner.nextLine());

        noteService.erstelleNote(note, kursId, matrikelnummer);
        System.out.println("✅ Note gespeichert");
    }

    private void alleNotenAnzeigen() {
        var noten = noteService.zeigeAlleNoten();
        if (noten.isEmpty()) {
            System.out.println("ℹ️ Keine Noten vorhanden");
        } else {
            noten.forEach(System.out::println);
        }
    }

    private void noteNachIdLoeschen(Scanner scanner) {
        System.out.println("Note-ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        noteService.loescheNoteNachId(id);
        System.out.println("✅ Note gelöscht");
    }

    private void noteNachKursUndMatrikelLoeschen(Scanner scanner) {
        System.out.println("KursId: ");
        int kursId = Integer.parseInt(scanner.nextLine());

        System.out.println("Matrikelnummer: ");
        int matrikelnummer = Integer.parseInt(scanner.nextLine());

        noteService.loescheNoteNachKursIdUndMatrikelnummer(kursId, matrikelnummer);
        System.out.println("✅ Note gelöscht");
    }

    private void noteAendern(Scanner scanner) {
        System.out.println("1) Nach ID");
        System.out.println("2) Nach KursId & Matrikelnummer");
        System.out.println("> ");
        int wahl = Integer.parseInt(scanner.nextLine());

        System.out.println("Neue Note: ");
        int neueNote = Integer.parseInt(scanner.nextLine());

        if (wahl == 1) {
            System.out.println("Note-ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            noteService.aendereNoteNachId(id, neueNote);
            System.out.println("✅ Note aktualisiert");
        } else if (wahl == 2) {
            System.out.println("KursId: ");
            int kursId = Integer.parseInt(scanner.nextLine());

            System.out.println("Matrikelnummer: ");
            int matrikelnummer = Integer.parseInt(scanner.nextLine());

            noteService.aendereNoteNachKursIDUndMatrikelnummer(kursId, matrikelnummer, neueNote);
            System.out.println("✅ Note aktualisiert");
        } else {
            System.out.println("❌ Ungültige Auswahl!");
        }
    }

    private void noteSuchen(Scanner scanner) {
        System.out.println("1) Nach ID");
        System.out.println("2) Nach KursId");
        System.out.println("3) Nach Matrikelnummer");
        System.out.println("> ");
        int wahl = Integer.parseInt(scanner.nextLine());

        if (wahl == 1) {
            System.out.println("Note-ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println(noteService.findeNoteNachId(id));
        } else if (wahl == 2) {
            System.out.println("KursId: ");
            int kursId = Integer.parseInt(scanner.nextLine());
            noteService.findeAlleNoteNachKursId(kursId)
                    .forEach(System.out::println);
        } else if (wahl == 3) {
            System.out.println("Matrikelnummer: ");
            int matrikelnummer = Integer.parseInt(scanner.nextLine());
            noteService.findeAlleNoteNachMatrikelnummer(matrikelnummer)
                    .forEach(System.out::println);
        } else {
            System.out.println("❌ Ungültige Auswahl!");
        }
    }

    private void durchschnittBerechnen(Scanner scanner) {
        System.out.println("Matrikelnummer: ");
        int matrikelnummer = Integer.parseInt(scanner.nextLine());

        double durchschnitt = noteService.berechneDurchschnittsnote(matrikelnummer);
        System.out.println("📊 Durchschnittsnote: " + durchschnitt);
    }

    private void durchschnittKursBerechnen(Scanner scanner) {
        System.out.println("KursId: ");
        int kursId = Integer.parseInt(scanner.nextLine());

        double durchschnitt = noteService.berechneDurchschnittsnoteNachKursId(kursId);
        System.out.println("📊 Kurs-Durchschnittsnote: " + durchschnitt);
    }

}

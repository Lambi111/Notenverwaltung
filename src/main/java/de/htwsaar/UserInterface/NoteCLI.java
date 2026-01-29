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

    @Override
    public void starten() {

            while (true) {
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
                String input = scanner.nextLine();

                try {
                    switch (input) {
                        case "1" -> noteAnlegen();
                        case "2" -> alleNotenAnzeigen();
                        case "3" -> noteNachIdLoeschen();
                        case "4" -> noteNachKursUndMatrikelLoeschen();
                        case "5" -> noteAendern();
                        case "6" -> noteSuchen();
                        case "7" -> durchschnittBerechnen();
                        case "8" -> durchschnittKursBerechnen();
                        case "0" -> {
                            System.out.println("Programm beendet!");
                            return;
                        }
                        default -> System.out.println("❌ Ungültige ent! " + input);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Ungültige Zahleneingabe!");
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ " + e.getMessage());
                }
            }
    }

    private void noteAnlegen() {
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

    private void noteNachIdLoeschen() {
        System.out.println("Note-ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        noteService.loescheNoteNachId(id);
        System.out.println("✅ Note gelöscht");
    }

    private void noteNachKursUndMatrikelLoeschen() {
        System.out.println("KursId: ");
        int kursId = Integer.parseInt(scanner.nextLine());

        System.out.println("Matrikelnummer: ");
        int matrikelnummer = Integer.parseInt(scanner.nextLine());

        noteService.loescheNoteNachKursIdUndMatrikelnummer(kursId, matrikelnummer);
        System.out.println("✅ Note gelöscht");
    }

    private void noteAendern() {
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

    private void noteSuchen() {
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

    private void durchschnittBerechnen() {
        System.out.println("Matrikelnummer: ");
        int matrikelnummer = Integer.parseInt(scanner.nextLine());

        double durchschnitt = noteService.berechneDurchschnittsnote(matrikelnummer);
        System.out.println("📊 Durchschnittsnote: " + durchschnitt);
    }

    private void durchschnittKursBerechnen() {
        System.out.println("KursId: ");
        int kursId = Integer.parseInt(scanner.nextLine());

        double durchschnitt = noteService.berechneDurchschnittsnoteNachKursId(kursId);
        System.out.println("📊 Kurs-Durchschnittsnote: " + durchschnitt);
    }

}

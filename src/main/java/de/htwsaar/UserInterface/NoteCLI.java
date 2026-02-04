package de.htwsaar.UserInterface;


import de.htwsaar.note.NoteService;

import java.util.*;

public class NoteCLI extends AbstraktCLI{
    private final NoteService noteService;

    public NoteCLI(NoteService noteService, Scanner scanner) {
        super(scanner);
        this.noteService = noteService;
    }

    @Override
    public void starten() {
        while(true){
            header("Notenverwaltung");
            System.out.println("1) Note anlegen");
            System.out.println("2) Alle Noten anzeigen");
            System.out.println("3) Note nach ID löschen");
            System.out.println("4) Alle Noten löschen");
            System.out.println("5) Note nach KursId & Matrikelnummer löschen");
            System.out.println("6) Note ändern");
            System.out.println("7) Note suchen");
            System.out.println("8) Durchschnittsnote eines Studenten berechnen");
            System.out.println("9) Durchschnittsnote eines Kurses berechnen");
            System.out.println("10) Leistungsübersicht eines Kurses");
            System.out.println("11) Leistungsübersicht eines Studenten");
            System.out.println("0) Beenden");

            try{
                switch(read("Auswahl")) {
                    case "1" -> noteAnlegen();
                    case "2" -> alleNotenAnzeigen();
                    case "3" -> noteNachIDLoeschen();
                    case "4" -> alleNotenLoeschen();
                    case "5" -> noteNachKursIdUndMatrikelnummerLoeschen();
                    case "6" -> noteAendern();
                    case "7" -> noteSuchen();
                    case "8" -> durchschnittsnoteStudentBerechnen();
                    case "9" -> durchschnittsnoteKursBerechnen();
                    case "10" -> leistungsuebersichtKurs();
                    case "11" -> leistungsuebersichtStudent();
                    case "0" -> {return;}
                    default -> System.out.println("❌ Ungültige Auswahl");
                }
            } catch(IllegalArgumentException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }

    }

    private void noteAnlegen() {
        int note = readInt("Note");
        int kursId = readInt("KursId");
        int matrikelnummer = readInt("Matrikelnummer");

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

    private void noteNachIDLoeschen() {
        int id = readInt("Note-ID");
        noteService.loescheNoteNachId(id);
        System.out.println("✅ Note gelöscht");
    }

    private void alleNotenLoeschen() {
        noteService.loescheAlleNoten();
        System.out.println("Alle Noten wurden erfolgreich gelöscht");
    }

    private void noteNachKursIdUndMatrikelnummerLoeschen() {
        int kursId = readInt("Kurs-ID");
        int matrikelnummer = readInt("Matrikelnummer");

        noteService.loescheNoteNachKursIdUndMatrikelnummer(kursId, matrikelnummer);
        System.out.println("✅ Note gelöscht");
    }

    private void noteAendern() {
        System.out.println("1) Nach ID");
        System.out.println("2) Nach KursId & Matrikelnummer");

        int wahl = readInt("Auswahl");
        int neueNote = readInt("Note");

        if (wahl == 1) {
            int id = readInt("Note-ID");
            noteService.aendereNoteNachId(id, neueNote);
            System.out.println("✅ Note aktualisiert");
        } else if (wahl == 2) {
            int kursId = readInt("Kurs-ID");
            int matrikelnummer = readInt("Matrikelnummer");
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
        int wahl = readInt("Auswahl");

        if (wahl == 1) {
            int id = readInt("Note-ID");
            System.out.println(noteService.findeNoteNachId(id));
        } else if (wahl == 2) {
            int kursId = readInt("KursId");
            noteService.findeAlleNoteNachKursId(kursId)
                    .forEach(System.out::println);
        } else if (wahl == 3) {
            int matrikelnummer = readInt("Matrikelnummer");
            noteService.findeAlleNoteNachMatrikelnummer(matrikelnummer)
                    .forEach(System.out::println);
        } else {
            System.out.println("❌ Ungültige Auswahl!");
        }
    }

    private void durchschnittsnoteStudentBerechnen() {
        int matrikelnummer = readInt("Matrikelnummer");
        double durchschnitt = noteService.berechneDurchschnittsnote(matrikelnummer);
        System.out.println("📊 Durchschnittsnote: " + durchschnitt);
    }

    private void durchschnittsnoteKursBerechnen() {
        int kursId = readInt("Kurs-ID");
        double durchschnitt = noteService.berechneDurchschnittsnoteNachKursId(kursId);
        System.out.println("📊 Kurs-Durchschnittsnote: " + durchschnitt);
    }

    private void leistungsuebersichtKurs() {
        int kursId = readInt("Kurs-ID");
        var bericht = noteService.erstelleLeistungsberichtKurs(kursId);
        System.out.println("Kurserfolg: " + bericht);
    }

    private void leistungsuebersichtStudent() {
        int matrikelnummer = readInt("Matrikelnummer");

        var bericht = noteService.erstelleLeistungsberichtStudent(matrikelnummer);

        System.out.println("Leistungsbericht: ");
        bericht.forEach(System.out::println);

    }

}

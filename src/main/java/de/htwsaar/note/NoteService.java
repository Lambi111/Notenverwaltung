package de.htwsaar.note;

import java.util.*;

public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void erstelleNote(int note, int kursId, int matrikelnummer) {
        Note neueNote = new Note(note, kursId, matrikelnummer);
        noteRepository.speichere(neueNote);
    }

    public List<Note> zeigeAlleNoten() {
        return noteRepository.zeigeAlleNoten();
    }

    public void loescheNoteNachId(int id) {
        if(noteRepository.findeNoteNachId(id).isEmpty()) {
            throw new IllegalArgumentException("Note mit Id " + id + " existiert nicht");
        }
        noteRepository.loescheNoteNachId(id);
    }

    public void loescheNoteNachKursIdUndMatrikelnummer(int kursId, int matrikelnummer) {
        List<Note> notenKurs = noteRepository.findeAlleNoteNachKursId(kursId);
        List<Note> notenStudent = noteRepository.findeAlleNoteNachMatrikelnummer(matrikelnummer);

        boolean existiert = notenKurs.stream()
                .anyMatch(n -> n.getMatrikelnummer() == matrikelnummer);

        if(!existiert) {
            throw new IllegalArgumentException(
                    "Note für Kurs " + kursId + " und Matrikelnummer " + matrikelnummer + " existiert nicht");
        }

        noteRepository.loescheNoteNachKursIdUndMatrikelnummer(kursId, matrikelnummer);
    }

    public Note findeNoteNachId(int id) {
        Optional<Note> noteOpt = noteRepository.findeNoteNachId(id);

        if(noteOpt.isEmpty()) {
            throw new IllegalArgumentException("Note mit Id " + id + " existiert nicht");
        }

        return noteOpt.get();
    }

    public List<Note> findeAlleNoteNachKursId(int kursId) {
        List<Note> noten = noteRepository.findeAlleNoteNachKursId(kursId);

        if(noten.isEmpty()) {
            throw new IllegalArgumentException("Keine Noten für KursId " + kursId + " gefunden");
        }

        return noten;
    }

    public List<Note> findeAlleNoteNachMatrikelnummer(int matrikelnummer) {
        List<Note> noten = noteRepository.findeAlleNoteNachMatrikelnummer(matrikelnummer);

        if(noten.isEmpty()) {
            throw new IllegalArgumentException("Keine Noten für Matrikelnummer " + matrikelnummer + " gefunden");
        }

        return noten;
    }

    public void loescheAlleNoten() {
        noteRepository.loescheAlleNoten();
    }

    public void aendereNoteNachId(int id, int neueNote) {
        if(noteRepository.findeNoteNachId(id).isEmpty()) {
            throw new IllegalArgumentException("Note mit Id " + id + " existiert nicht");
        }

        noteRepository.aendereNoteNachId(id, neueNote);
    }

    public void aendereNoteNachKursIDUndMatrikelnummer(int kursId, int matrikelnummer, int neueNote) {
        List<Note> noten = noteRepository.findeAlleNoteNachKursId(kursId);

        boolean existiert = noten.stream()
                .anyMatch(n -> n.getMatrikelnummer() == matrikelnummer);

        if(!existiert) {
            throw new IllegalArgumentException(
                    "Note für Kurs " + kursId + " und Matrikelnummer " + matrikelnummer + " existiert nicht");
        }

        noteRepository.aendereNoteNachKursIDUndMatrikelnummer(kursId, matrikelnummer, neueNote);
    }

    public double berechneDurchschnittsnote(int matrikelnummer) {
        List<Note> noten = noteRepository.findeAlleNoteNachMatrikelnummer(matrikelnummer);

        if (noten.isEmpty()) {
            throw new IllegalArgumentException("Keine Noten fuer Matrikelnummer " + matrikelnummer + " gefunden");
        }

        return noteRepository.berechneDurchschnittsnoteNachMatrikelnummer(matrikelnummer);
    }

}

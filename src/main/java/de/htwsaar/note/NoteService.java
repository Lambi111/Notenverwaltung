package de.htwsaar.note;

import de.htwsaar.kurs.KursRepository;
import de.htwsaar.student.StudentRepository;

import java.util.*;

public class NoteService {

    private final NoteRepository noteRepository;
    private final KursRepository kursRepository;
    private final StudentRepository studentRepository;

    public NoteService(NoteRepository noteRepository, KursRepository kursRepository,
                       StudentRepository studentRepository) {
        this.noteRepository = noteRepository;
        this.kursRepository = kursRepository;
        this.studentRepository = studentRepository;
    }

    public void erstelleNote(int note, int kursId, int matrikelnummer) {

        if (kursId <= 0 && matrikelnummer <= 0) {
            throw new IllegalArgumentException("KursId und Matrikelnummer sind ungültig.");
        }

        if (kursId <= 0) {
            throw new IllegalArgumentException("KursId ist ungültig.");
        }

        if (matrikelnummer <= 0) {
            throw new IllegalArgumentException("Matrikelnummer ist ungültig.");
        }

        boolean kursExistiert = kursRepository.existsById(kursId);
        boolean studentExistiert = studentRepository.existsByMatrikelnummer(matrikelnummer);

        if (!kursExistiert && !studentExistiert) {
            throw new IllegalArgumentException(
                    "Kurs mit ID " + kursId + " und Student mit Matrikelnummer "
                            + matrikelnummer + " existieren nicht."
            );
        }

        if (!kursExistiert) {
            throw new IllegalArgumentException(
                    "Kurs mit der ID " + kursId + " existiert nicht."
            );
        }

        if (!studentExistiert) {
            throw new IllegalArgumentException(
                    "Student mit der Matrikelnummer " + matrikelnummer + " existiert nicht."
            );
        }

        // --- Optional: Notenvalidierung ---
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("Note muss zwischen 1 und 5 liegen.");
        }

        // --- Speichern ---
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

    public double berechneDurchschnittsnoteNachKursId(int kursId) {
        List<Note> noten = noteRepository.findeAlleNoteNachKursId(kursId);

        if (noten.isEmpty()) {
            throw new IllegalArgumentException("Keine Noten fuer KursId " + kursId + " gefunden");
        }

        return noteRepository.berechneDurchschnittsnoteNachKursId(kursId);
    }

    public List<String> erstelleLeistungsberichtStudent(int matrikelnummer) {
        List<Note> noten = noteRepository.findeAlleNoteNachMatrikelnummer(matrikelnummer);

        if (noten.isEmpty()) {
            throw new IllegalArgumentException(
                    "Keine Noten für Matrikelnummer " + matrikelnummer
            );
        }

        List<String> bericht = new ArrayList<>();

        for (Note note : noten) {
            String status;

            if(note.getNote() <= 4) {
                status = "bestanden.";
            } else {
                status = "nicht bestanden.";
            }

            String eintrag =
                    "Kurs-ID: " + note.getKursId() +
                    ", Note: " + note.getNote() +
                    ", Status: " + status;

            bericht.add(eintrag);
        }
        return bericht;
    }

    public Map<String, Integer> erstelleLeistungsberichtKurs(int kursId) {
        List<Note> noten = noteRepository.findeAlleNoteNachKursId(kursId);

        if (noten.isEmpty()) {
            throw new IllegalArgumentException(
                    "Kein Kurs " + kursId + " gefunden"
            );
        }

        int bestanden = 0;
        int nichtBestanden = 0;

        for (Note note : noten) {
            if(note.getNote() <= 4) {
                bestanden++;
            } else {
                nichtBestanden++;
            }
        }

        Map<String, Integer> bericht = new HashMap<>();

        bericht.put("bestanden", bestanden);
        bericht.put("nicht bestanden", nichtBestanden);

        return bericht;

    }
}

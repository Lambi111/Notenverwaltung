package de.htwsaar.note;

import java.util.*;

public interface NoteRepository {

    void speichere(Note note);

    List<Note> zeigeAlleNoten();

    void loescheNoteNachKursIdUndMatrikelnummer(int kursId, int matrikelnummer);

    void loescheNoteNachId(int id);

    Optional<Note> findeNoteNachId(int id);

    List<Note> findeAlleNoteNachKursId(int kursId);

    List<Note> findeAlleNoteNachMatrikelnummer(int matrikelnummer);

    void loescheAlleNoten();

    void aendereNoteNachId(int id, int neueNote);

    void aendereNoteNachKursIDUndMatrikelnummer(int kursId, int matrikelnummer, int neueNote);

    double berechneDurchschnittsnoteNachMatrikelnummer(int matrikelnummer);

    double berechneDurchschnittsnoteNachKursId(int kursId);

}

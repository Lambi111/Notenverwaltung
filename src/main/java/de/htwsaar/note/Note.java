package de.htwsaar.note;

import java.util.Objects;

public class Note {
    private int id;
    private int note;
    private int kursId;
    private int matrikelnummer;

    public Note(int note, int kursId, int matrikelnummer) {

        if(note <= 0) {
            throw new IllegalArgumentException("Note muss größer als 0 sein!");
        }

        if(kursId <= 0) {
            throw new IllegalArgumentException("KursId muss größer als 0 sein!");
        }

        if(matrikelnummer <= 0) {
            throw new IllegalArgumentException("Matrikelnummer muss größer als 0 sein!");
        }

        this.id = 0;
        this.note = note;
        this.kursId = kursId;
        this.matrikelnummer = matrikelnummer;
    }

    public int getId() {
        return id;
    }

    public int getNote() {
        return note;
    }

    public int getKursId() {
        return kursId;
    }

    public int getMatrikelnummer() {
        return matrikelnummer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNote(int note) {
        if(note <= 0) {
            throw new IllegalArgumentException("Note muss größer als 0 sein!");
        }
        this.note = note;
    }

    public void setKursId(int kursId) {
        this.kursId = kursId;
    }

    public void setMatrikelnummer(int matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    @Override
    public String toString() {
        return "NoteId: " + id +
                ", Note: " + note +
                ", KursId: " + kursId +
                ", Matrikelnummer: " + matrikelnummer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note noteObj = (Note) o;
        return id == noteObj.id && id != 0;
    }

    @Override
    public int hashCode() {
        return id == 0 ? System.identityHashCode(this) : Objects.hash(id);
    }
}

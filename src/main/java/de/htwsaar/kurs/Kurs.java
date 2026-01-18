package de.htwsaar.kurs;

import java.util.*;

public class Kurs {
    private final int kursId;
    private String titel;
    private String beschreibung;
    private final int semester;

    public Kurs(int kursId, String titel, String beschreibung, int semester) {
        if(kursId <= 0) {
            throw new IllegalArgumentException("KursId muss > 0 sein");
        }

        if(semester <= 0) {
            throw new IllegalArgumentException("Semester muss > 0 sein");
        }

        if(titel == null || titel.trim().isEmpty()) {
            throw new IllegalArgumentException("Titel darf weder null noch leer sein");
        }

        this.kursId = kursId;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.semester = semester;
    }

    public int getKursId() {
        return kursId;
    }

    public String getTitel() {
        return titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public int getSemester() {
        return semester;
    }


    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public String toString() {
        return  "Kurs: " + titel + " (" + kursId + ")" + "\n" +
                "Semester: " + semester + "\n" +
                "Beschreibung: " + beschreibung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Kurs)) return false;
        Kurs kurs = (Kurs)o;
        return kursId == kurs.kursId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kursId);
    }

}

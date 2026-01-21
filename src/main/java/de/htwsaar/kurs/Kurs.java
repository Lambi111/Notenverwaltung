package de.htwsaar.kurs;

import java.util.*;

public class Kurs {
    private static final TreeSet<Integer> vergebeneIds = new TreeSet<>();
    private int kursId;
    private String titel;
    private String beschreibung;
    private int semester;

    public Kurs(String titel, String beschreibung, int semester) {
        if(semester <= 0) {
            throw new IllegalArgumentException("Semester kann nicht 0 oder negativ sein!");
        }

        if(titel == null || titel.trim().isEmpty()) {
            throw new IllegalArgumentException("Titel darf weder null noch leer sein");
        }
        //this.kursId = 0;
        this.kursId = generiereNeueId();
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.semester = semester;
    }

    public static int generiereNeueId() {
        int id = 1;
        for(int vergebeneId : vergebeneIds) {
            if(id < vergebeneId) break;
            id++;
        }
        vergebeneIds.add(id);
        return id;
    }

    public static void entferneId(int id) {
        vergebeneIds.remove(id);
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

    public void setKursId(int kursId) {
        this.kursId = kursId;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return  "KursId: " + kursId + ", Kurs: " + titel + "\n" +
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

    //Hilfsklasse für Test(automatischeKursIdVergabe)
    public static void resetIds() {
        vergebeneIds.clear();
    }

}

package de.htwsaar.student;

import java.util.*;

public class Student {
    private int matrikelnummer;
    private String vorname;
    private String nachname;
    private String studiengang;

    public Student(String vorname, String nachname, String studiengang) {
        if(vorname == null || vorname.isBlank())
            throw new IllegalArgumentException("Vorname darf nicht leer sein");
        if(nachname == null || nachname.isBlank())
            throw new IllegalArgumentException("Nachname darf nicht leer sein");
        if(studiengang == null || studiengang.isBlank())
            throw new IllegalArgumentException("Studiengang darf nicht leer sein");

        this.matrikelnummer = 0;
        this.vorname = vorname;
        this.nachname = nachname;
        this.studiengang = studiengang;
    }

    public int getMatrikelnummer() {
        return matrikelnummer;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getStudiengang() {
        return studiengang;
    }

    public void setMatrikelnummer(int matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setStudiengang(String studiengang) {
        this.studiengang = studiengang;
    }


    @Override
    public String toString() {
        return "Student [matrikelnummer=" + matrikelnummer
                + ", vorname=" + vorname + ", nachname=" + nachname
                + ", studiengang=" + studiengang + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(!(obj instanceof Student other)) return false;
        return matrikelnummer == other.matrikelnummer && matrikelnummer != 0;
    }

    @Override
    public int hashCode() {
        return matrikelnummer == 0 ? System.identityHashCode(this) : Objects.hash(matrikelnummer);
    }
}

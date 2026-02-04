# Notenverwaltung
Abschlussprojekt Programmierung 3

Projektbeschreibung:

Dieses Projekt ist ein textbasiertes Notenverwaltungssystem, das im Rahmen der Veranstaltung Programmierung 3 (PIB-PR3) entwickelt wurde.

Die Anwendung ermöglicht es Anwender:innen, Studierende, Kurse und Noten zu verwalten. Alle Daten werden persistent in einer lokalen SQLite-Datenbank gespeichert, sodass sie über mehrere Programmsitzungen hinweg verfügbar bleiben.

Das Projekt folgt einer klaren Drei-Schichten-Architektur und orientiert sich an grundlegenden Prinzipien des Software Engineerings (zB.: SOLID).



Funktionen:

- Anlegen, Anzeigen und Verwalten von Studierenden  
- Anlegen, Anzeigen und Verwalten von Kursen  
- Verknüpfung von Studierenden mit Kursen und Noten
- Berechnung von Durchschnittsnoten
- Erstellen einer Leistungsübersicht für Kurs/ für Student  
- Persistente Speicherung der Daten in einer SQLite-Datenbank  
- Textbasierte Benutzeroberfläche (Menüführung im Terminal)



Architektur

Die Anwendung ist nach dem Prinzip der Drei-Schichten-Architektur aufgebaut:

1. Präsentationsschicht  
   - Textbasierte Benutzeroberfläche (CLI/TUI)
   - Verantwortlich für Benutzereingaben und -ausgaben

2. Logikschicht (Service Layer)  
   - Enthält die Geschäftslogik
   - Verarbeitung und Validierung der Benutzereingaben

3. Persistenzschicht
   - Speicherung und Laden der Daten
   - SQLite-Datenbank
   - Datenbankzugriff abstrahiert über jOOQ

Diese Trennung sorgt für Wartbarkeit, Erweiterbarkeit und klare Verantwortlichkeiten.



Anwendung: 

Projekt bauen und Tests ausführen: mvn clean test
Projekt ausführen: mvn exec:java

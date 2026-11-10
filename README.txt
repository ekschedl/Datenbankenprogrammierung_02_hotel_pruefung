Hotelverwaltung – Java, Hibernate und SQLite

Konsolenbasiertes Prüfungsprojekt aus dem Fach Datenbankprogrammierung II. Die Anwendung verwaltet Gäste, Zimmer, Buchungen und Zusatzleistungen und demonstriert den Einsatz von Hibernate/JPA und HQL.

Funktionen

Gäste und Zimmer anlegen, anzeigen, bearbeiten und löschen (CRUD)

Buchungen mit Transaktionen speichern und bei Fehlern zurückrollen

Zusatzleistungen Gästen zuordnen

HQL-Abfragen mit Filtern, benannten Parametern, Gruppierungen, Sortierung und Pagination

Entitäten und Beziehungen mit JPA-Annotationen modellieren

Die Gastadresse über ein Hibernate-XML-Mapping konfigurieren

Datenmodell

Das Modell umfasst Gast, GastAdresse, Zimmer, Buchung und Zusatzleistung.

Ein Gast kann mehrere Buchungen haben.

Jede Buchung verweist auf einen Gast und ein Zimmer.

Ein Gast hat eine Adresse.

Gäste und Zusatzleistungen sind über eine Many-to-Many-Beziehung verbunden.



Technologien

Java 23 · Maven · Hibernate ORM 6.6 · Jakarta Persistence (JPA) 3.1 · SQLite

Projektstruktur

src/main/java/at/campus02/dbp/model/ – Entitätsklassen

src/main/java/at/campus02/dbp/helpers/ – DAOs, Services und HQL-Abfragen

src/main/java/at/campus02/dbp/HibernateUtil.java – Erstellung der Hibernate-SessionFactory

src/main/resources/ – Hibernate-Konfiguration und XML-Mapping

src/main/dokumentation.txt – Erläuterungen zu Annotationen, Beziehungen und XML-Mapping

Starten

Java 23 installieren.

Das Repository klonen und den Projektordner in IntelliJ IDEA öffnen:

git clone https://github.com/ekschedl/Datenbankenprogrammierung_02_hotel_pruefung.git

Maven-Abhängigkeiten laden lassen.

src/main/java/at/campus02/dbp/Main.java öffnen und über das grüne Dreieck neben main starten.

Die Anwendung gibt die Demo-Ergebnisse in der Konsole aus. Hibernate verwendet die SQLite-Datei hotelprojekt.db und aktualisiert das Datenbankschema mit hbm2ddl.auto=update.

Umfang und Hinweise

Das Projekt ist ein Lern- und Prüfungsprojekt ohne grafische Benutzeroberfläche. Die Buchungslogik prüft den Zimmerstatus und das Guthaben, berechnet aber keine zeitlichen Überschneidungen zwischen Buchungen.

Die Demo fügt bei jedem Start erneut Beispieldaten ein. Bei wiederholten Starts können dadurch Duplikate entstehen. Außerdem verwendet der Buchungsablauf feste IDs; sein Ergebnis hängt daher vom Datenbankstand ab.
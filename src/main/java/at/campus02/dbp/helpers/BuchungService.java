package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil; 
import at.campus02.dbp.model.Buchung;
import at.campus02.dbp.model.Gast;
import at.campus02.dbp.model.Zimmer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;

public class BuchungService {

    // Öffentliche Service-Methode: Ein Gast bucht ein Zimmer für einen Zeitraum
    public void gastBuchtZimmer(Long gastId, Long zimmerId,
                                LocalDate von, LocalDate bis) {

        Session session = null;  // Session-Referenz, bewusst außerhalb von try → damit sie im finally geschlossen werden kann
        Transaction tx = null;  // Transaktions-Referenz → damit im Fehlerfall ein Rollback möglich ist

        try {
              // -------- TRY: normaler Ablauf --------
            session = HibernateUtil.getSessionFactory().openSession(); // Öffnet eine neue Hibernate-Session (DB-Verbindung)
            tx = session.beginTransaction(); //Start einer Transaktion → alle folgenden DB-Operationen gehören logisch zusammen

            Gast gast = session.get(Gast.class, gastId);  // Lädt den Gast aus der DB (managed Entity)
            Zimmer zimmer = session.get(Zimmer.class, zimmerId);  // Lädt das Zimmer aus der DB (managed Entity)

            if (gast == null || zimmer == null) {// Validierung: Gast oder Zimmer existiert nicht
                throw new RuntimeException("Gast oder Zimmer nicht gefunden"); // Exception erzwingt Abbruch → Rollback
            }

            if (!zimmer.getIstFrei()) { // Ein Zimmer darf nur gebucht werden, wenn es frei ist
                throw new RuntimeException("Zimmer ist bereits belegt");
            }

            if (gast.getGuthaben() < zimmer.getPreis()) { // Gast braucht genug Guthaben
                throw new RuntimeException("Nicht genug Guthaben");
            }

            Buchung buchung = new Buchung();   // Neue Buchung (transient, neu, nur Java, noch nicht in DB)
            buchung.setGast(gast); // Verknüpft Buchung mit Gast (n:1)
            buchung.setZimmer(zimmer); // Verknüpft Buchung mit Zimmer (n:1)
            buchung.setVonDatum(von);  // Setzt Buchungszeitraum
            buchung.setBisDatum(bis);  // Setzt Buchungszeitraum

            session.persist(buchung);// Speichert die Buchung in der DB,also : transient → persistent

            gast.setGuthaben(gast.getGuthaben() - zimmer.getPreis());// Guthaben des Gastes reduzieren
            zimmer.setIstFrei(false);// Zimmer als belegt markieren

            //Nicht falsch, nur redundant?:
            // merge Technisch nicht zwingend, da die Entitäten bereits persistent sind.
            //Hibernate würde die Änderungen auch ohne merge beim Commit speichern.
            // session.merge(gast); //
            //session.merge(zimmer);//

            tx.commit(); //  Commit bei Erfolg → ALLE Änderungen werden dauerhaft gespeichert

            System.out.println("\u001B[92mBuchung erfolgreich\u001B[0m");
        } catch (Exception e) {
            // -------- CATCH: Fehlerfall --------
            if (tx != null && tx.isActive()) { // Läuft die Transaktion gerade noch?
                tx.rollback(); // Session noch offen, Rollback macht ALLE Änderungen dieser Transaktion rückgängig,  verhindert inkonsistente Daten
            }
            System.out.println("\u001B[91mBuchung fehlgeschlagen: " + e.getMessage() + "\u001B[0m");            // Fehlermeldung für Demo / Debug

        } finally {
             // -------- FINALLY: immer ausführen --------
            if (session != null) {
                session.close(); // erst ganz am Ende schließen,  Session wird IMMER geschlossen,verhindert Memory Leaks & offene DB-Verbindungen
            }
        }
    }
}

/*
Warum is hier der RICHTIGE Ort für Rollback?

In EINER Methode gastBuchtZimmer(...) passieren mehrere Datenbankänderungen:
    Buchung anlegen
    Gast-Guthaben reduzieren
    Zimmer auf „belegt“ setzen

 Mehrere DB-Änderungen gehören logisch zusammen
!!!!!! Alle oder keine dürfen passieren   - sonst Gefahr: Inkonsistente Daten!!!!!!!
Rollback sorgt für „Alles oder nichts“   -Datenbank bleibt konsistent


persist() → neues Objekt speichern
get() → Objekt ist automatisch persistent
merge() → nur nötig bei detached Objekten
commit() → speichert alle Änderungen an persistenten Objekten
 */
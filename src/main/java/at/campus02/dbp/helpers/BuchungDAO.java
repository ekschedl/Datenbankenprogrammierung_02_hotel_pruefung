package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Buchung;
import at.campus02.dbp.model.Gast;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class BuchungDAO {

    // CREATE: speichert eine Buchung in der Datenbank
    public Buchung save(Buchung buchung) {
        // try-with-resources:
        // Session wird automatisch geschlossen (auch bei Fehlern)
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start einer Transaktion
            Transaction tx = session.beginTransaction();


            // transient -> persistent
            // Buchung wird vom Persistence Context verwaltet
            // INSERT wird vorgemerkt (noch nicht in DB ausgeführt)
            session.persist(buchung);

            // Commit der Transaktion
            // INSERT wird jetzt in der Datenbank ausgeführt
            // ID der Buchung wird gesetzt
            tx.commit();

            // Rückgabe der gespeicherten Buchung
            // Objekt ist jetzt persistent (hat eine ID)
            return buchung;
        }
    }
}


/*
wieso ohne Rollback:
	•	Wird commit() nicht erreicht →
Transaktion wird beim session.close() verworfen
	•	Keine inkonsistenten Daten, weil:
	•	nichts committed wurde
	•	nur 1 DB-Operation

Rollback ist hier Komfort, kein Muss


 */
package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Zusatzleistung;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ZusatzleistungDAO {

    // CREATE: speichert eine Zusatzleistung
    public Zusatzleistung save(Zusatzleistung z) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            session.persist(z);
            tx.commit();

            System.out.println(
                    "\u001B[32mGESPEICHERT: Zusatzleistung "
                            + z.getName() + " (€" + z.getPreis() + ")\u001B[0m"
            );
            return z;
        }
    }
}
/*

Wieso hier ohne Rollback:
	•	Nur eine DB-Operation → nichts kann „halb gespeichert“ werden
	•	Ohne commit() wird nichts gespeichert → DB bleibt unverändert
	•	Exception unterbricht den Code → commit() wird nicht ausgeführt
	•	Session-Schließen verwirft die Transaktion automatisch
	•	Rollback ist hier nur Komfort, keine Pflicht

Rollback braucht man vor allem in Services, wenn mehrere Schritte zusammengehören (z. B. Buchung + Guthaben + Zimmerstatus).

*/

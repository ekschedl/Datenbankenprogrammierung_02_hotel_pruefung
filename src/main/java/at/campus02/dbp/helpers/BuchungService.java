package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Buchung;
import at.campus02.dbp.model.Gast;
import at.campus02.dbp.model.Zimmer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;

public class BuchungService {

    public void gastBuchtZimmer(Long gastId, Long zimmerId,
                                LocalDate von, LocalDate bis) {

        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Gast gast = session.get(Gast.class, gastId);
            Zimmer zimmer = session.get(Zimmer.class, zimmerId);

            if (gast == null || zimmer == null) {
                throw new RuntimeException("Gast oder Zimmer nicht gefunden");
            }

            if (!zimmer.getIstFrei()) {
                throw new RuntimeException("Zimmer ist bereits belegt");
            }

            if (gast.getGuthaben() < zimmer.getPreis()) {
                throw new RuntimeException("Nicht genug Guthaben");
            }

            Buchung buchung = new Buchung();
            buchung.setGast(gast);
            buchung.setZimmer(zimmer);
            buchung.setVonDatum(von);
            buchung.setBisDatum(bis);

            session.persist(buchung);

            gast.setGuthaben(gast.getGuthaben() - zimmer.getPreis());
            zimmer.setIstFrei(false);

            session.merge(gast);
            session.merge(zimmer);

            tx.commit(); //  alles OK

            System.out.println("\u001B[92mBuchung erfolgreich\u001B[0m");
        } catch (Exception e) {

            if (tx != null && tx.isActive()) {
                tx.rollback(); // Session noch offen
            }

            System.out.println("\u001B[91mBuchung fehlgeschlagen: " + e.getMessage() + "\u001B[0m");
        } finally {
            if (session != null) {
                session.close(); // erst ganz am Ende schließen
            }
        }
    }
}
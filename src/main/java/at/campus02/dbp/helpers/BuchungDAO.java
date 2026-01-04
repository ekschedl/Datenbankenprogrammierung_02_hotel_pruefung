package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Buchung;
import at.campus02.dbp.model.Gast;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BuchungDAO {


    // CREATE (Buchung speichern )


    public Buchung save(Buchung buchung) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(buchung);
            tx.commit();
            return buchung;
        }
    }
}

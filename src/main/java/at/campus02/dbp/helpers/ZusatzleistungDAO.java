package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Zusatzleistung;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ZusatzleistungDAO {
    public Zusatzleistung save(Zusatzleistung z) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(z);
            tx.commit();
            return z;
        }
    }
}

package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Gast;
import at.campus02.dbp.model.Zusatzleistung;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ZusatzleistungService {



        public void fuegeZusatzleistungZuGast(Long gastId, Long zusatzId) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();

                Gast gast = session.get(Gast.class, gastId);
                Zusatzleistung z = session.get(Zusatzleistung.class, zusatzId);

                gast.getZusatzleistungen().add(z);

                tx.commit();
            }
        }

    public void zeigeZusatzleistungen(Long gastId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Gast gast = session.get(Gast.class, gastId);

            System.out.println("Zusatzleistungen von Gast " + gastId + ":");
            gast.getZusatzleistungen().forEach(System.out::println);
        }
    }
}
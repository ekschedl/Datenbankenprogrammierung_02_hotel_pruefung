package at.campus02.dbp.helpers;


import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Gast;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
//eigene Klasse nur für HQL
public class GastQuery {
    public List<Gast> zeigeStammgaeste(boolean istStammgast) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Gast> query = session.createQuery(
                    "SELECT g FROM Gast g WHERE g.istStammgast = :stammgast",
                    Gast.class
            );

            query.setParameter("stammgast", istStammgast);
            return query.getResultList();
        }
    }

    // HQL-Abfrage mit GROUP BY und Aggregatfunktion
    public List<Object[]> countStammgaeste() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Object[]> query = session.createQuery(
                    "SELECT g.istStammgast, COUNT(g) FROM Gast g GROUP BY g.istStammgast",
                    Object[].class
            );

            return query.getResultList();
        }
    }

    public List<Gast> alleGaesteSortiertNachName() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT g FROM Gast g ORDER BY g.familienname ASC",
                    Gast.class
            ).getResultList();
        }
    }

    public List<Gast> zeigeGaestePaginiert(int start, int anzahl) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<Gast> query = session.createQuery(
                    "SELECT g FROM Gast g ORDER BY g.id",
                    Gast.class
            );

            query.setFirstResult(start);
            query.setMaxResults(anzahl);

            return query.getResultList();
        }
    }
}

package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Zimmer;
import at.campus02.dbp.model.ZimmerKategorie;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ZimmerQuery {

    // ---- WHERE + Named Parameter -> Zimmer nach Kategorie: ----
    public List<Zimmer> findByKategorie(ZimmerKategorie kat) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Zimmer z WHERE z.kategorie = :kat";
            Query<Zimmer> q = session.createQuery(hql, Zimmer.class);
            q.setParameter("kat", kat);
            return q.getResultList();
        }
    }

    // ---- GROUP BY + Aggregatfunktion-> Anzahl Zimmer pro Kategorie: ----
    public List<Object[]> countZimmerProKategorie() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
            SELECT z.kategorie, COUNT(z)
            FROM Zimmer z
            GROUP BY z.kategorie
        """;
            return session.createQuery(hql, Object[].class).getResultList();
        }
    }

    // ---- ORDER BY + Alias  -> Zimmer nach Preis sortiert:
    public List<Zimmer> findAllOrderedByPreis(boolean desc) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String order = desc ? "DESC" : "ASC";
            String hql = "FROM Zimmer z ORDER BY z.preis " + order;
            return session.createQuery(hql, Zimmer.class).getResultList();
        }
    }

    // ---- Pagination - >Erste 5 Zimmer laden:
    public List<Zimmer> findZimmerPage(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM Zimmer";

            Query<Zimmer> q = session.createQuery(hql, Zimmer.class);

            q.setFirstResult((page - 1) * size);
            q.setMaxResults(size);

            return q.getResultList();
        }
    }


// ---- JOIN zwischen Gast und Zimmer
   public List<Zimmer> findZimmerByGast(Long gastId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = """
            SELECT b.zimmer
            FROM Buchung b
            WHERE b.gast.id = :id
        """;
            Query<Zimmer> q = session.createQuery(hql, Zimmer.class);
            q.setParameter("id", gastId);
            return q.getResultList();
        }
    }
}




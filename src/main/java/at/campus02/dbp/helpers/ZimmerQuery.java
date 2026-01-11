package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Zimmer;
import at.campus02.dbp.model.ZimmerKategorie;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ZimmerQuery {

    // ---- WHERE + Named Parameter -> Zimmer nach bestimmter Kategorie: ----
    public List<Zimmer> findByKategorie(ZimmerKategorie kat) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL arbeitet mit Entitäten, nicht mit Tabellen
            String hql = "FROM Zimmer z WHERE z.kategorie = :kat";

            // Query vorbereiten
            Query<Zimmer> q = session.createQuery(hql, Zimmer.class);

            // Named Parameter setzen
            q.setParameter("kat", kat);

            // Ergebnisliste zurückgeben
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
            // Rückgabe: Object[0] = Kategorie, Object[1] = Anzahl
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

    // ---- Pagination - >:
    public List<Zimmer> findZimmerPage(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM Zimmer";

            Query<Zimmer> q = session.createQuery(hql, Zimmer.class);
            // page beginnt logisch bei 1 → DB beginnt bei 0
            q.setFirstResult((page - 1) * size); // Startposition
            q.setMaxResults(size);     // Anzahl Datensätze

            return q.getResultList();
        }
    }


    // JOIN
    // Gibt alle Zimmer zurück, die ein bestimmter Gast gebucht hat
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




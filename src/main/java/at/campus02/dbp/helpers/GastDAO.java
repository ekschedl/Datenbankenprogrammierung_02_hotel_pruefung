package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Gast;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class GastDAO {

    // CREATE (Gast speichern inkl. Adresse)
    public Gast save(Gast gast) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(gast);  // speichert Gast + Adresse automatisch
            tx.commit();
            return gast;
        }
    }

    // READ by ID
    public Gast findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Gast.class, id);
        }
    }

    // READ ALL
    public List<Gast> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Gast";
            Query<Gast> query = session.createQuery(hql, Gast.class);
            return query.getResultList();
        }
    }


    // UPDATE
    public Gast update(Gast gast) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Gast merged = (Gast) session.merge(gast);
            tx.commit();
            return merged;
        }
    }

    // DELETE
    public void delete(Gast gast) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Gast managed = (Gast) session.merge(gast);
            session.remove(managed);
            tx.commit();
        }
    }

    // DELETE by ID
    public void deleteById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Gast gast = session.get(Gast.class, id);
            if (gast != null) session.remove(gast);
            tx.commit();
        }
    }

    public List<Gast> findAllPaged(int start, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Gast", Gast.class)
                    .setFirstResult(start)
                    .setMaxResults(size)
                    .list();
        }

    }



}
package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Zimmer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ZimmerDAO {

    // ---- neuer Zimmer anlegen/speichern ---------------------
    public Zimmer save(Zimmer zimmer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(zimmer); // transient -> persistent
            tx.commit(); // ID wird gesetzt
            return zimmer;// jetzt persistent (mit ID)
        }// detached nach try-with-resources
    }

    // ------ Zimmer per ID lesen--------------------------------------------
    public Zimmer findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Zimmer.class, id);
        }
    }

    // ------Daten bei Zimmer ändern--------------------------------------------
    public Zimmer update(Zimmer zimmer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            // zimmer ist in typischen Fällen detached -> merge gibt eine
            // gemanagte (persistent) Instanz zurück
            Zimmer merged = (Zimmer) session.merge(zimmer);
            tx.commit();
            return merged;                 // gemanagte Version zurückgeben
        } // merged ist nach Schließen der Session wieder detached
    }

    // ------ Zimmer Objekt löschen--------------------------------------------
    public void delete(Zimmer zimmer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            // falls zimmer detached ist, zuerst mergen oder neu laden
            Zimmer managed = (Zimmer) session.merge(zimmer);
            session.remove(managed);
            tx.commit();
        }
    }

    // ------Zimmer per Primärschlüssel löschen--------------------------------------------
    public void deleteById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Zimmer zimmer = session.get(Zimmer.class, id);
            if (zimmer != null) {
                session.remove(zimmer);
            }

            tx.commit();
        }
    }

    // ------alles Zimmer anzeigen--------------------------------------------
    public List<Zimmer> getAlleZimmer(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "FROM Zimmer ";
            Query query = session.createQuery(hql);
            List results = query.list();
            return  results;
        }

    }

}
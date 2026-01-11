package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Zimmer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
/*
Ein Zimmer = eine Buchung
Mehrere Zimmer → mehrere Buchungen

Ein Gast kann mehrere Zimmer buchen.
Dies wird durch mehrere Buchungen modelliert, wobei jede Buchung genau ein Zimmer repräsentiert.
Dadurch bleibt das Modell einfach und gut nachvollziehbar.
*/


public class ZimmerDAO {

    // ---- neuer Zimmer anlegen/speichern ---------------------
    public Zimmer save(Zimmer zimmer) {
        // try-with-resources:
        // Session wird automatisch geschlossen (auch bei Fehlern)
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Start einer Transaktion
            Transaction tx = session.beginTransaction();

            // transient -> persistent
            // Zimmer wird vom Persistence Context verwaltet
            // INSERT wird vorgemerkt (noch nicht in DB)
            session.persist(zimmer);

            // Commit der Transaktion
            // INSERT wird in der Datenbank ausgeführt
            // ID wird gesetzt
            tx.commit();

            System.out.println(
                    "\u001B[32mGESPEICHERT: Zimmer "
                            + zimmer.getKategorie() + " /"+ zimmer.getPreis() + ")\u001B[0m"
            );
            // Objekt ist gespeichert und hat jetzt eine ID
            return zimmer;

        }
        // Session geschlossen
        // Objekt ist jetzt detached
    }



    // ------ Zimmer per ID lesen--------------------------------------------
    public Zimmer findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Zimmer.class, id);
        }
    }

    // ------Daten bei Zimmer ändern--------------------------------------------
    public Zimmer update(Zimmer zimmer) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            // zimmer ist in typischen Fällen detached -> merge gibt eine
            // gemanagte (persistent) Instanz zurück
            Zimmer merged = (Zimmer) session.merge(zimmer);
            tx.commit();
            return merged;                 // gemanagte Version zurückgeben
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // ------ Zimmer Objekt löschen--------------------------------------------
    public void delete(Zimmer zimmer) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            // falls zimmer detached ist, zuerst mergen oder neu laden
            Zimmer managed = (Zimmer) session.merge(zimmer);
            session.remove(managed);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // ------Zimmer per Primärschlüssel löschen--------------------------------------------
    public void deleteById(Long id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Zimmer zimmer = session.get(Zimmer.class, id);
            if (zimmer != null) {
                session.remove(zimmer);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // ------alles Zimmer anzeigen--------------------------------------------
    public List<Zimmer> findAll(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Zimmer";
            Query<Zimmer> query = session.createQuery(hql, Zimmer.class);
            return query.getResultList();
        }

    }

    }


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
            System.out.println(
                    "\u001B[32mGESPEICHERT: Gast "
                            + gast.getName() + " /"+ gast.getFamilienname() + ")\u001B[0m"
            );
            return gast;
        }
    }

    // READ by ID
    //lädt genau einen Datensatz anhand des Primärschlüssels
    // und gibt ein detached Entity-Objekt zurück.
    public Gast findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Gast.class, id);
        }
    }

    // READ ALL
    //lädt alle Datensätze einer Entität und
    // gibt sie als Liste von detached Objekten zurück.
    public List<Gast> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Gast";
            Query<Gast> query = session.createQuery(hql, Gast.class);
            return query.getResultList();
        }
    }


    // UPDATE: aktualisiert einen bestehenden Gast in der Datenbank
    public Gast update(Gast gast) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // merge():
            // - gast ist meist detached (z. B. aus einer früheren Session)
            // - Hibernate erzeugt eine managed (persistente) Kopie
            // - Änderungen werden im Persistence Context verwaltet
            Gast merged = (Gast) session.merge(gast);

            // Commit der Transaktion
            // UPDATE wird in der Datenbank ausgeführt
            tx.commit();

            // Rückgabe der gemanagten (persistenten) Entität
            return merged;
        }
    }

    // DELETE: löscht einen bestehenden Gast
    public void delete(Gast gast) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // remove() löscht nur managed Entitäten.
            // gast ist evtl. detached → wird wieder managed gemacht
            //Detached Objekte müssen vorher gemerged oder neu geladen werden.
            Gast managed = (Gast) session.merge(gast);

            // Löscht den Gast aus der Datenbank
            session.remove(managed);

            // Änderungen dauerhaft speichern
            tx.commit();
        }
    }

    // DELETE by ID: löscht einen Gast über die ID
    public void deleteById(Long id) {
        // Session automatisch schließen
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Start der Transaktion
            Transaction tx = session.beginTransaction();
            // Gast aus der DB laden (managed)
            Gast gast = session.get(Gast.class, id);
            // Nur löschen, wenn der Gast existiert
            if (gast != null) {
                session.remove(gast);
            }
            // Änderungen übernehmen
            tx.commit();
        }
    }
// READ mit Pagination: lädt Gäste seitenweise.
//start - Startdatensatz, size  - die Anzahl der geladenen Datensätze
    public List<Gast> findAllPaged(int start, int size) {
        // Keine Transaktion nötig (nur Lesen)
        // Session öffnen (wird automatisch geschlossen)
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // HQL-Abfrage (arbeitet auf Entitäten, nicht auf Tabellen)
            String hql = "FROM Gast";

            // Query erzeugen
            Query<Gast> query = session.createQuery(hql, Gast.class);

            // Startposition (Offset)
            query.setFirstResult(start);

            // Anzahl der Datensätze
            query.setMaxResults(size);

            // Ergebnisliste zurückgeben
            return query.getResultList();
        }

    }



}

/**^
 
/*-------------------------------- 
 try (Session session = ...) {
    Transaction tx = session.beginTransaction();
    ...
    tx.commit();

    Wenn vor commit() eine Exception fliegt:
        commit() wird nicht erreicht
        Session wird geschlossen
        Transaktion wird von Hibernate verworfen
}

/*-------------------------------- 
Warum man Rollback NICHT in jedem DAO benötigt

Beispiel GastDAO.save():

    Transaction tx = session.beginTransaction();
    session.persist(gast);
    tx.commit();

    nur eine DB-Operation
    keine Business-Regeln
    kein zusammengesetzter Ablauf
    -daher wäre Rollback Overengineering
    Rollback gehört in Services, nicht in einfache DAOs.
 */
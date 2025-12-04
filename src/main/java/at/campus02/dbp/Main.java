package at.campus02.dbp;

import at.campus02.dbp.helpers.GastDAO;
import at.campus02.dbp.model.Gast;
import at.campus02.dbp.model.GastAdresse;
import org.hibernate.SessionFactory;

public class Main {

    private static SessionFactory sessionFactory;

    public static void main(String[] args) {

        // --- Adressen anlegen ---
        // NICHT die id mit – die wird automatisch in der DB erzeugt (identity).
        /*
        GastAdresse adresse1 = new GastAdresse("Österreich", "Graz", "Hauptstraße", "12A", "Top 3");
        GastAdresse adresse2 = new GastAdresse("Österreich", "Wien", "Mariahilfer Straße", "88", "Top 12");
        GastAdresse adresse3 = new GastAdresse("Deutschland", "Berlin", "Alexanderplatz", "5", "4. Stock");
        GastAdresse adresse4 = new GastAdresse("Schweiz", "Zürich", "Bahnhofstrasse", "2", "Top 1");
        GastAdresse adresse5 = new GastAdresse("Österreich", "Linz", "Landstraße", "99", "7");
        */
        // Du speicherst NICHT direkt adr selbst!
        //   Das macht Hibernate automatisch, wenn Gast persistiert wird.
        //   (Dank OneToOne + JoinColumn)


        // --- Gäste anlegen ---
        // nur Java-Objekte erstellt, aber noch NICHTS wurde in die Datenbank gespeichert
        /*
        Gast gast1 = new Gast("Katja", "Schedlberger", true, 2000.00);
        Gast gast2 = new Gast("Irina", "Schedlberger", false, 20000.00);
        Gast gast3 = new Gast("Max", "Schedlberger", false, 400.00);
        Gast gast4 = new Gast("Valerie", "Müller", true, 0);
        Gast gast5 = new Gast("Markus", "Huber", true, 20000.00);
        */

        // --- Beziehungen setzen: Gast mit Adresse verknüpfen---
        /*
        gast1.setAdresse(adresse1);
        gast2.setAdresse(adresse2);
        gast3.setAdresse(adresse3);
        gast4.setAdresse(adresse4);
        gast5.setAdresse(adresse5);
        */

        // --- Speichern folgt  über GastDAO
        GastDAO gastDAO = new GastDAO();
        /*
        gastDAO.save(gast1);
        gastDAO.save(gast2);
        gastDAO.save(gast3);
        gastDAO.save(gast4);
        gastDAO.save(gast5);
        */
        System.out.println("Alle Gäste wurden gespeichert!");
    }
}
package at.campus02.dbp;

import at.campus02.dbp.helpers.*;

import java.time.LocalDate;

import at.campus02.dbp.model.*;
import org.hibernate.SessionFactory;

import java.util.List;

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

        ZimmerDAO zimmerDAO = new ZimmerDAO();

       /* Zimmer zimmer1 = new Zimmer(101, 90.00, ZimmerKategorie.STANDARD);
        Zimmer zimmer2 = new Zimmer(102, 130.00, ZimmerKategorie.DELUXE);
        Zimmer zimmer3 = new Zimmer(201, 200.00, ZimmerKategorie.SUITE);
        Zimmer zimmer4 = new Zimmer(202, 150.00, ZimmerKategorie.DELUXE);
        Zimmer zimmer5 = new Zimmer(203, 160.00, ZimmerKategorie.DELUXE);
        Zimmer zimmer6 = new Zimmer(301, 300.00, ZimmerKategorie.SUITE);

        zimmerDAO.save(zimmer1);
        zimmerDAO.save(zimmer2);
        zimmerDAO.save(zimmer3);
        zimmerDAO.save(zimmer4);
        zimmerDAO.save(zimmer5);
        zimmerDAO.save(zimmer6);*/

        System.out.println("Alle Zimmer wurden gespeichert!");

        var alleZimmer = zimmerDAO.getAlleZimmer();
        System.out.println("----------------------------------------");
        System.out.println("\u001B[93mZimmerübersicht:\u001B[0m");
        for (Zimmer z : alleZimmer) {
            System.out.printf(
                    "Zimmer %-3d | %-8s | € %6.2f | %s%n",
                    z.getNummer(),
                    z.getKategorie(),
                    z.getPreis(),
                    z.getIstFrei() ? "frei" : "belegt"
            );
        }
        System.out.println("----------------------------------------");


        var alleGaeste = gastDAO.findAll();
        System.out.println("----------------------------------------");
        System.out.println("\u001B[96mGästeübersicht:\u001B[0m");
        for (Gast g : alleGaeste) {
            System.out.printf(
                    "Gast %-3d | %-10s %-15s | %-18s | € %8.2f%n",
                    g.getId(),
                    g.getName(),
                    g.getFamilienname(),
                    g.isIstStammgast() ? "Stammgast" : "kein Stammgast",
                    g.getGuthaben()
            );
        }
        System.out.println("----------------------------------------");

        ZimmerQuery zimmerQuery = new ZimmerQuery();
        GastQuery gastQuery = new GastQuery();

        //WHERE + Named Parameter
        System.out.println("\u001B[96mZimmer Kategorie DELUXE:\u001B[0m");
        zimmerQuery.findByKategorie(ZimmerKategorie.DELUXE)
                .forEach(z ->
                        System.out.printf(
                                "Zimmer %d | %s | € %.2f%n",
                                z.getNummer(),
                                z.getKategorie(),
                                z.getPreis()
                        )
                );

        System.out.println("----------------------------------------");
        // GROUP BY + Aggregatfunktion
        for (Object[] row : zimmerQuery.countZimmerProKategorie()) {
            System.out.printf(
                    "%-8s : %d%n",
                    row[0],   // Kategorie
                    row[1]    // Anzahl
            );
        }

        System.out.println("----------------------------------------");
        //ORDER BY + Alias
        System.out.println("\nZimmer nach Preis (absteigend):");
        zimmerQuery.findAllOrderedByPreis(true)
                .forEach(z ->
                        System.out.printf(
                                "Zimmer %d | € %.2f | %s%n",
                                z.getNummer(),
                                z.getPreis(),
                                z.getKategorie()
                        )
                );

        System.out.println("----------------------------------------");
        //Pagination demonstrieren
        System.out.println("\nZimmer – Pagination:");
        List<Zimmer> pagList = zimmerQuery.findZimmerPage(1, 3);
        for (Zimmer z : pagList) {
            System.out.println(z);
        }

        System.out.println("----------------------------------------");
        // Beispiel: Gast 1 bucht Zimmer 1
        Gast gast1 = gastDAO.findById(1L);
        Zimmer zimmer1 = zimmerDAO.findById(1L);
        Zimmer zimmer2 = zimmerDAO.findById(2L);

        BuchungDAO buchungDAO = new BuchungDAO();

        Buchung buchung1 = new Buchung();
       // buchung1.setGast(gast1);
       // buchung1.setZimmer(zimmer1);
       // buchung1.setVonDatum(LocalDate.of(2026, 1, 10));
       // buchung1.setBisDatum(LocalDate.of(2026, 1, 15));

        //buchungDAO.save(buchung1); //  jetzt wird es benutzt

        // Beispiel: Gast 1 bucht Zimmer 2
        Buchung buchung2 = new Buchung();
//        buchung2.setGast(gast1);
//        buchung2.setZimmer(zimmer2);
//        buchung2.setVonDatum(LocalDate.of(2024, 4, 3));
//        buchung2.setBisDatum(LocalDate.of(2024, 4, 12));
//
//        buchungDAO.save(buchung2); //

        System.out.println("----------------------------------------");
        //JOIN
        System.out.println("\nZimmer von Gast mit ID = 1:");
        zimmerQuery.findZimmerByGast(1L)
                .forEach(z ->
                        System.out.printf(
                                "Zimmer %d | %s | € %.2f%n",
                                z.getNummer(),
                                z.getKategorie(),
                                z.getPreis()
                        )
                );

        BuchungService service = new BuchungService();

        service.gastBuchtZimmer(
                8L,                // Gast-ID
                6L,                // Zimmer-ID
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );

        System.out.println("----------------------------------------");
        //JOIN
        System.out.println("\nZimmer von Gast mit ID = 2:");
        zimmerQuery.findZimmerByGast(2L)
                .forEach(z ->
                        System.out.printf(
                                "Zimmer %d | %s | € %.2f%n",
                                z.getNummer(),
                                z.getKategorie(),
                                z.getPreis()
                        )
                );

        ZusatzleistungDAO zusatzleistungDAO= new ZusatzleistungDAO();

        //  Zusatzleistungen anlegen (einmal)
        Zusatzleistung fruehstueck = new Zusatzleistung();
        fruehstueck.setName("Frühstück");
        fruehstueck.setPreis(15);

        Zusatzleistung parkplatz = new Zusatzleistung();
        parkplatz.setName("Parkplatz");
        parkplatz.setPreis(10);

        //jetzt persistieren, einmal ausführen, danach sind sie fix in der DB.
        zusatzleistungDAO.save(fruehstueck);
        zusatzleistungDAO.save(parkplatz);

        //Zusatzleistung einem Gast zuweisen (Many-to-Many)
        ZusatzleistungService zusatzleistungService = new ZusatzleistungService();
        zusatzleistungService.fuegeZusatzleistungZuGast(
                1L,                 // Gast-ID
                fruehstueck.getId() // Zusatzleistung-ID
        );
        zusatzleistungService.fuegeZusatzleistungZuGast(
                1L,
                parkplatz.getId()
        );
        zusatzleistungService.zeigeZusatzleistungen(1L);

    }





}
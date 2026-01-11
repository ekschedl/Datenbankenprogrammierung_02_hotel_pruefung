package at.campus02.dbp;

import at.campus02.dbp.helpers.*;

import java.time.LocalDate;

import at.campus02.dbp.model.*;
import org.hibernate.SessionFactory;

import java.util.List;

public class Main {


    public static void main(String[] args) {

        //Da der Gast den Fremdschlüssel auf die Adresse hält, muss Hibernate zuerst die Adresse persistieren.
        // Durch Cascade wird das automatisch korrekt erledigt

        // --- Adressen anlegen ---
        // id NICHT mit – die wird automatisch in der DB erzeugt (identity).

        GastAdresse adresse1 = new GastAdresse("Österreich", "Graz", "Hauptstraße", "12A", "Top 3");
        GastAdresse adresse2 = new GastAdresse("Österreich", "Wien", "Mariahilfer Straße", "88", "Top 12");
        GastAdresse adresse3 = new GastAdresse("Deutschland", "Berlin", "Alexanderplatz", "5", "4. Stock");
        GastAdresse adresse4 = new GastAdresse("Schweiz", "Zürich", "Bahnhofstrasse", "2", "Top 1");
        GastAdresse adresse5 = new GastAdresse("Österreich", "Linz", "Landstraße", "99", "7");

        // adresse speichere ich NICHT direkt selbst!
        //   Das macht Hibernate automatisch, wenn Gast persistiert wird.
        //   (Dank OneToOne + JoinColumn)


        // --- Gäste anlegen ---
        // nur Java-Objekte erstellt, aber noch NICHTS wurde in die Datenbank gespeichert

        Gast gast1 = new Gast("Katja", "Schedlberger", true, 2000.00);
        Gast gast2 = new Gast("Irina", "Schedlberger", false, 20000.00);
        Gast gast3 = new Gast("Max", "Schedlberger", false, 400.00);
        Gast gast4 = new Gast("Valerie", "Müller", true, 0);
        Gast gast5 = new Gast("Markus", "Huber", true, 20000.00);


        // --- Beziehungen setzen: Gast mit Adresse verknüpfen---

        gast1.setAdresse(adresse1);
        gast2.setAdresse(adresse2);
        gast3.setAdresse(adresse3);
        gast4.setAdresse(adresse4);
        gast5.setAdresse(adresse5);


        // --- Speichern folgt  über GastDAO
        GastDAO gastDAO = new GastDAO();

        gastDAO.save(gast1);
        gastDAO.save(gast2);
        gastDAO.save(gast3);
        gastDAO.save(gast4);
        gastDAO.save(gast5);

        System.out.println("--------------------------------------------");

        ZimmerDAO zimmerDAO = new ZimmerDAO();

        Zimmer zimmer1 = new Zimmer(101, 90.00, ZimmerKategorie.STANDARD);
        Zimmer zimmer2 = new Zimmer(102, 130.00, ZimmerKategorie.DELUXE);
        Zimmer zimmer3 = new Zimmer(203, 200.00, ZimmerKategorie.SUITE);
        Zimmer zimmer4 = new Zimmer(202, 150.00, ZimmerKategorie.DELUXE);
        Zimmer zimmer5 = new Zimmer(402, 160.00, ZimmerKategorie.DELUXE);
        Zimmer zimmer6 = new Zimmer(303, 300.00, ZimmerKategorie.SUITE);

        zimmerDAO.save(zimmer1);
        zimmerDAO.save(zimmer2);
        zimmerDAO.save(zimmer3);
        zimmerDAO.save(zimmer4);
        zimmerDAO.save(zimmer5);
        zimmerDAO.save(zimmer6);

        System.out.println("--------------------------------------------");

        var alleZimmer = zimmerDAO.findAll();
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
        System.out.println("\u001B[93mGästeübersicht:\u001B[0m");
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

        System.out.println("\n\u001B[96mAnzahl Gäste nach Stammgast-Status:\u001B[0m");

        // Iteriert über das Ergebnis der GROUP-BY-Abfrage (jede Zeile = Object[])
        for (Object[] row : gastQuery.countStammgaeste()) {
            // row[0] enthält den Gruppierungswert (true = Stammgast, false = kein Stammgast)
            boolean istStammgast = (boolean) row[0];

            // row[1] enthält das Ergebnis der Aggregatfunktion COUNT(*)
            long anzahl = (long) row[1];

            System.out.printf(
                    "%-15s : %d%n",
                    istStammgast ? "Stammgäste" : "Keine Stammgäste",
                    anzahl
            );
        }
        System.out.println("----------------------------------------");

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
        System.out.println("\u001B[96mAnzahl Zimmer nach Kategorie:\u001B[0m");

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
        System.out.println("\u001B[96mSortieren Zimmer nach Preis (absteigend):\u001B[0m");
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
        //Pagination
        System.out.println("\u001B[96mZimmer – Pagination:\u001B[0m");
        List<Zimmer> pagList = zimmerQuery.findZimmerPage(1, 3);
        for (Zimmer z : pagList) {
            System.out.println(z);
        }

        System.out.println("----------------------------------------");
        // Beispiel: Gast 1 bucht Zimmer 1
        Gast gastFirst = gastDAO.findById(1L);
        Zimmer zimmerFirst = zimmerDAO.findById(1L);
        Zimmer zimmerSecond = zimmerDAO.findById(2L);

        BuchungDAO buchungDAO = new BuchungDAO();

        Buchung buchung1 = new Buchung();
        buchung1.setGast(gastFirst);
        buchung1.setZimmer(zimmerFirst);
        buchung1.setVonDatum(LocalDate.of(2026, 1, 10));
        buchung1.setBisDatum(LocalDate.of(2026, 1, 15));

        buchungDAO.save(buchung1); //  nur technische Speicherung (DAO), keine Business-Logik, keine Prüfungen
        // Beispiel: Gast 1 bucht Zimmer 2
        Buchung buchung2 = new Buchung();
        buchung2.setGast(gastFirst);
        buchung2.setZimmer(zimmerSecond);
        buchung2.setVonDatum(LocalDate.of(2024, 4, 3));
        buchung2.setBisDatum(LocalDate.of(2024, 4, 12));

        buchungDAO.save(buchung2); //  nur technische Speicherung (DAO), keine Business-Logik, keine Prüfungen

        //JOIN
        System.out.println("\u001B[96mZimmer von Gast mit ID = 1:\u001B[0m");
        zimmerQuery.findZimmerByGast(1L) //JOIN-Abfragen erzeugen zusätzliche SELECTs-  bz output
                .forEach(z ->
                        System.out.printf(
                                "Zimmer %d | %s | € %.2f%n",
                                z.getNummer(),
                                z.getKategorie(),
                                z.getPreis()
                        )
                );


        //mit Business-Logik:Service enthält Geschäftsregeln (gibts Gast? Zimmer frei? genug Guthaben??)
        BuchungService service = new BuchungService();
        service.gastBuchtZimmer(
                2L,                // Gast-ID
                4L,                // Zimmer-ID
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );
     /* Nicht mit fixen IDs, sondern mit Objekten:Business-Logik sollte mit Fachattributen arbeiten - robust, sauber
        Gast gastO = gastDAO.findByName("Katja");
        Zimmer zimmerO = zimmerDAO.findByNummer(102);
        service.gastBuchtZimmer(
            gastO.getId(),
            zimmerO.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(3)
        );

      */
        System.out.println("----------------------------------------");
        //JOIN
        System.out.println("\u001B[96mZimmer von Gast mit ID = 2:\u001B[0m");
        zimmerQuery.findZimmerByGast(2L)
                .forEach(z ->
                        System.out.printf(
                                "Zimmer %d | %s | € %.2f%n",
                                z.getNummer(),
                                z.getKategorie(),
                                z.getPreis()
                        )
                );
        System.out.println("----------------------------------------");

        ZusatzleistungDAO zusatzleistungDAO= new ZusatzleistungDAO();

        //  Zusatzleistungen anlegen (einmal)
        Zusatzleistung fruehstueck = new Zusatzleistung();
        fruehstueck.setName("Frühstück");
        fruehstueck.setPreis(15);

        Zusatzleistung parkplatz = new Zusatzleistung();
        parkplatz.setName("Parkplatz");
        parkplatz.setPreis(10);

        Zusatzleistung fruehstueck2 = new Zusatzleistung();
        fruehstueck2.setName("Frühstück2");
        fruehstueck2.setPreis(150);

        //jetzt persistieren, einmal ausführen, danach sind sie fix in der DB.
        zusatzleistungDAO.save(fruehstueck);
        zusatzleistungDAO.save(parkplatz);
        zusatzleistungDAO.save(fruehstueck2);

        //Zusatzleistung einem Gast zuweisen (Many-to-Many)
        ZusatzleistungService zusatzleistungService = new ZusatzleistungService();
        zusatzleistungService.fuegeZusatzleistungZuGast(
                2L,                 // Gast-ID
                fruehstueck.getId() // Zusatzleistung-ID
        );
        zusatzleistungService.fuegeZusatzleistungZuGast(
                1L,
                parkplatz.getId()
        );
        zusatzleistungService.fuegeZusatzleistungZuGast(
                1L,
                fruehstueck2.getId()
        );
        System.out.println("----------------------------------------");
        zusatzleistungService.zeigeZusatzleistungen(1L);


        System.out.println("----------------------------------------");
        zusatzleistungService.zeigeGaesteZuZusatzleistung(2L);



        //demoLazy();
        //demoEagerMitQuery();
       // demoZimmerUpdateUndDelete();
        //demoZimmerAlleMethoden();
    }


    // === LAZY DEMO ===
    public static void demoLazy() {
        Gast gast;
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            gast = session.get(Gast.class, 1L);
            System.out.println("Gast geladen: " + gast.getName());
        } // Session zu

        //  Fehler erwartet (LazyInitializationException) -Session zu → LAZY kann nicht nachladen
        gast.getZusatzleistungen().forEach(System.out::println);
    }

    // === EAGER-Demo per JOIN FETCH (JPQL)
    // überschreibt LAZY-Mapping nur für diese Query ===
    // JOIN FETCH lädt Beziehungen gezielt eager, ohne das Mapping global zu ändern
    public static void demoEagerMitQuery() {

        System.out.println("\n\u001B[94m=== DEMO: JOIN FETCH (EAGER per Query) ===\u001B[0m");

        try (var session = HibernateUtil.getSessionFactory().openSession()) {

            System.out.println("Session geöffnet.");
            System.out.println("JPQL mit JOIN FETCH wird ausgeführt...");
            System.out.println("→ Gast + Zusatzleistungen werden in EINEM SELECT geladen");

            Gast gast = session.createQuery(
                            "select g from Gast g left join fetch g.zusatzleistungen where g.id = :id",
                            Gast.class
                    )
                    .setParameter("id", 1L)
                    .getSingleResult();      // Ergebnis aus der Datenbank holen

            System.out.println("Gast geladen: " + gast.getName());
            System.out.println("Session ist noch offen.");

            // Zugriff auf Zusatzleistungen funktioniert, obwohl Mapping LAZY ist,
            // weil JOIN FETCH die Daten bereits geladen hat
            System.out.println("Zusatzleistungen :");
            gast.getZusatzleistungen().forEach(z ->
                    System.out.println("  - " + z.getName() + " (€ " + z.getPreis() + ")")
            );

            System.out.println("Keine LazyInitializationException.");
        }

        System.out.println("Session geschlossen.");

        System.out.println("\u001B[94m=== DEMO ENDE ===\u001B[0m");

    }

    public static void demoZimmerUpdateUndDelete() {

        ZimmerDAO zimmerDAO = new ZimmerDAO();

        System.out.println("\n\u001B[95m=== DEMO: Zimmer UPDATE & DELETE ===\u001B[0m");

        // 1Zimmer laden
        Zimmer zimmer = zimmerDAO.findById(2L);
        if (zimmer == null) {
            System.out.println("Zimmer nicht gefunden.");
            return;
        }

        System.out.println("Vor UPDATE:");
        System.out.println(zimmer);

        // UPDATE: Preis und Kategorie ändern
        zimmer.setPreis(25);
        zimmer.setKategorie(ZimmerKategorie.DELUXE);

        zimmerDAO.update(zimmer);

        System.out.println("Nach UPDATE:");
        System.out.println(zimmerDAO.findById(zimmer.getId()));

        // DELETE: Zimmer löschen
        System.out.println("\nZimmer wird gelöscht...");
        zimmerDAO.deleteById(zimmer.getId());

        // Kontrolle
        Zimmer deleted = zimmerDAO.findById(zimmer.getId());
        System.out.println(
                deleted == null
                        ? "Zimmer erfolgreich gelöscht."
                        : "FEHLER: Zimmer existiert noch!"
        );

        System.out.println("\u001B[95m=== DEMO ENDE ===\u001B[0m");
    }
    public static void demoZimmerAlleMethoden() {

        ZimmerDAO zimmerDAO = new ZimmerDAO();

        System.out.println("\n\u001B[95m=== DEMO: ZimmerDAO – ALLE METHODEN ===\u001B[0m");

        // SAVE – neues Zimmer anlegen
        Zimmer demoZimmer = new Zimmer(999, 111.00, ZimmerKategorie.STANDARD);
        zimmerDAO.save(demoZimmer);

        Long id = demoZimmer.getId();
        System.out.println("Gespeicherte Zimmer-ID: " + id);

        // FIND BY ID
        Zimmer gefunden = zimmerDAO.findById(id);
        System.out.println("\nFIND BY ID:");
        System.out.println(gefunden);

        // FIND ALL
        System.out.println("\nFIND ALL:");
        zimmerDAO.findAll().forEach(System.out::println);

        // UPDATE
        System.out.println("\nUPDATE Preis +50:");
        gefunden.setPreis(gefunden.getPreis() + 50);
        gefunden.setKategorie(ZimmerKategorie.DELUXE);

        zimmerDAO.update(gefunden);

        Zimmer nachUpdate = zimmerDAO.findById(id);
        System.out.println("Nach UPDATE:");
        System.out.println(nachUpdate);

        // DELETE (per Objekt)
        System.out.println("\nDELETE (per Objekt):");
        zimmerDAO.delete(nachUpdate);

        Zimmer checkDelete = zimmerDAO.findById(id);
        System.out.println(
                checkDelete == null
                        ? "Zimmer erfolgreich gelöscht (delete)."
                        : "FEHLER: Zimmer existiert noch!"
        );

        // 6SAVE erneut für deleteById
        System.out.println("\nNeues Zimmer für deleteById:");
        Zimmer demoZimmer2 = new Zimmer(998, 99.00, ZimmerKategorie.STANDARD);
        zimmerDAO.save(demoZimmer2);

        Long id2 = demoZimmer2.getId();

        // DELETE BY ID
        System.out.println("DELETE BY ID:");
        zimmerDAO.deleteById(id2);

        Zimmer checkDeleteById = zimmerDAO.findById(id2);
        System.out.println(
                checkDeleteById == null
                        ? "Zimmer erfolgreich gelöscht (deleteById)."
                        : "FEHLER: Zimmer existiert noch!"
        );

        System.out.println("\u001B[95m=== DEMO ENDE ===\u001B[0m");
    }
}

/*

JOIN FETCH ≠ EAGER
	•	EAGER = Mapping-Entscheidung (immer laden)
	•	JOIN FETCH = Query-Entscheidung (nur hier laden)
JOIN FETCH ist sauberer & performanter

vielen SELECTs entstehen weil:
 Hibernate cached NICHT automatisch über Sessions
 jede Methode = neue Session
 Hibernate darf nichts wiederverwenden

Hibernate lädt immer genau das, was man anfordert – nicht mehr, nicht weniger.
Viele SELECTs entstehen durch:
	•	mehrere session.get()
	•	neue Sessions
	•	JOIN-Abfragen
	•	LAZY-Nachladen
 */
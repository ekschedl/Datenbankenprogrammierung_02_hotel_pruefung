package at.campus02.dbp.helpers;

import at.campus02.dbp.HibernateUtil;
import at.campus02.dbp.model.Gast;
import at.campus02.dbp.model.Zusatzleistung;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ZusatzleistungService {

    // try-with-resources garantiert IMMER:
    //	•	am Ende des Blocks → session.close() wird automatisch aufgerufen
    //	•	egal ob:
    //	•	return
    //	•	Exception
    //	•	normaler Ablauf
        public void fuegeZusatzleistungZuGast(Long gastId, Long zusatzId) {//(CREATE → mit Transaktion)
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();

                Gast gast = session.get(Gast.class, gastId);
                Zusatzleistung z = session.get(Zusatzleistung.class, zusatzId);

                if (gast == null || z == null) {
                    System.out.println(
                            "\u001B[31mNICHT GESPEICHERT: Gast oder Zusatzleistung nicht gefunden\u001B[0m"
                    );
                    //Mit rollback → sehr sauber, ohne rollback die Transaktion bleibt offen,
                    // bis die Session geschlossen wird, hibernate/DB räumt das meist auf, ist aber unsauber
                    tx.rollback(); // expliziter Rollback
                    return;
                }

                if (gast.getZusatzleistungen().contains(z)) { //contains() funktioniert nur korrekt, wenn equals() definiert ist,Object.equals() → vergleicht nur Speicheradresse (==)
                    System.out.println(
                            "\u001B[31mNICHT GESPEICHERT: Zusatzleistung bereits vorhanden: "
                                    + z.getName() + " (€" + z.getPreis() + ")\u001B[0m"
                    );
                    tx.rollback(); //Mit rollback → sehr sauber
                    return;
                }
                gast.getZusatzleistungen().add(z);
                tx.commit();
            }
        }

        public void zeigeZusatzleistungen(Long gastId) { //(READ → ohne Transaktion)
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Gast gast = session.get(Gast.class, gastId);
                System.out.println("\u001B[96mZusatzleistungen von Gast " + gastId + ":\u001B[0m");
                gast.getZusatzleistungen().forEach(System.out::println);
            }
        }

        public void zeigeGaesteZuZusatzleistung(Long zusatzleistungId) { //(READ → ohne Transaktion)
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Zusatzleistung z = session.get(Zusatzleistung.class, zusatzleistungId);
                System.out.println("\u001B[96mGäste mit Zusatzleistung " + zusatzleistungId + ":\u001B[0m");
                z.getGaeste().forEach(g ->
                        System.out.println(g.getName())
                );
            }
        }
}


/*

READ = try-with-resources, keine Transaktion
WRITE = try-with-resources + Transaktion


Warum Rollback, obwohl Session geschlossen wird?
Weil Session-Schließen die Transaktion nur implizit beendet.
Rollback macht das Verhalten explizit, sauber und kontrolliert.


*/
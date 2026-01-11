package at.campus02.dbp;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    // Zentrale SessionFactory

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            // Liest die Hibernate-Konfiguration aus hibernate.cfg.xml
            // Erstellt daraus die SessionFactory
            sessionFactory = new Configuration()
                    .configure()          // lädt hibernate.cfg.xml
                    .buildSessionFactory(); // baut die SessionFactory
                }
        return sessionFactory;
    }
}

/*
HibernateUtil stellt zentral eine SessionFactory zur Verfügung.
Die SessionFactory wird einmalig erstellt und
*dient als Fabrik zur Erzeugung von Hibernate-Sessions.


static:
Weil sie nur einmal pro Anwendung erstellt werden soll und
von überall wiederverwendet wird.

*/

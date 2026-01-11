package at.campus02.dbp.model;
import jakarta.persistence.*;

import java.time.LocalDate;

/* WARUM brauche ich Buchung:
* Ein Gast kann viele Zimmer buchen
* Ein Zimmer kann von vielen Gästen gebucht werden
*
* also: Buchung ist das Verbindungsobjekt,Ohne Buchung müsste ich n:m direkt machen
* Buchung macht das Modell einfach und realistisch.
* */

@Entity                 // Buchung wird in DB gespeichert
@Table(name = "buchung") // Tabellenname

public class Buchung {

    @Id //Jede Buchung braucht eine eindeutige ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)   //Eine Buchung ohne Start- oder Enddatum ergibt fachlich keinen Sinn.
    private LocalDate vonDatum;

    @Column(nullable = false)   //Eine Buchung ohne Start- oder Enddatum ergibt fachlich keinen Sinn.
    private LocalDate bisDatum;

    //Buchung → Gast (n:1)
    @ManyToOne(fetch = FetchType.LAZY)//lädt verbundene Entitäten erst bei Bedarf
    @JoinColumn(name = "gast_id", nullable = false) //Eine Buchung ohne Gast ergibt fachlich keinen Sinn.
    private Gast gast;

    //Buchung → Zimmer (n:1)
    @ManyToOne(fetch = FetchType.LAZY)//lädt verbundene Entitäten erst bei Bedarf
    @JoinColumn(name = "zimmer_id", nullable = false) //Eine Buchung ohne Zimmer ergibt fachlich keinen Sinn.
    private Zimmer zimmer;

    // leerer Konstruktor Pflicht für Hibernate
    public Buchung() {
    }

    // -------- Getter & Setter --------

    public Long getId() {
        return id;
    }

    public LocalDate getVonDatum() {
        return vonDatum;
    }

    public void setVonDatum(LocalDate vonDatum) {
        this.vonDatum = vonDatum;
    }

    public LocalDate getBisDatum() {
        return bisDatum;
    }

    public void setBisDatum(LocalDate bisDatum) {
        this.bisDatum = bisDatum;
    }

    public Gast getGast() {
        return gast;
    }

    public void setGast(Gast gast) {
        this.gast = gast;
    }

    public Zimmer getZimmer() {
        return zimmer;
    }

    public void setZimmer(Zimmer zimmer) {
        this.zimmer = zimmer;
    }
}

/**
FetchType.LAZY
    verbundene Entitäten werden nicht sofort geladen
    Daten werden erst bei Bedarf nachgeladen
    bessere Performance
    wird im Projekt verwendet, um unnötige Datenbankzugriffe zu vermeiden

FetchType.EAGER
    verbundene Entitäten werden sofort mitgeladen
    unabhängig davon, ob sie benötigt werden
    kann zu Performance-Problemen führen
    wird nur eingesetzt, wenn Daten immer benötigt werden

 */
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

    @Column(nullable = false)
    private LocalDate vonDatum;

    @Column(nullable = false)
    private LocalDate bisDatum;

    //Buchung → Gast (n:1)
    @ManyToOne
    @JoinColumn(name = "gast_id")
    private Gast gast;

    //Buchung → Zimmer (n:1)
    @ManyToOne
    @JoinColumn(name = "zimmer_id")
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


package at.campus02.dbp.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

//JPA -Java Persistence API.
// Markiert die Klasse als JPA-Entity (wird als Tabelle gespeichert)
@Entity
@Table(name = "gast") //  Tabellenname in der Datenbank
public class Gast {

    @Id // Primärschlüssel der Entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID wird von der Datenbank automatisch erzeugt
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String familienname;

    @Column(nullable = false)
    private boolean istStammgast;

    @Column(nullable = false)
    private double guthaben;

    // -------------------------------
    // Beziehung zwischen Gast und GastAdresse
    // One-to-One, unidirektional
    // -------------------------------
    // Die Adresse ist in der XML gemappt, also NUR hier die JoinColumn setzen.
    /*
     * @OneToOne
     * → Ein Gast hat genau eine Adresse
     *
     * fetch = LAZY
     * → Adresse wird erst geladen, wenn getAdresse() aufgerufen wird
     * → Wenn fetch NICHT gesetzt wird, ist der JPA-Standard EAGER
     *
     * cascade = ALL
     * → Speichern, Ändern und Löschen von Gast
     *    wirkt automatisch auch auf die Adresse
     * → Ohne Cascade müsste die Adresse separat gespeichert werden
     */    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    // Fremdschlüssel-Spalte in der Tabelle "gast"
    // Verweist auf die Adresse, unidirektional
    @JoinColumn(name = "adresse_id")
    private GastAdresse adresse;


    // -------------------------------
    // Beziehung Gast → Buchung
    // One-to-Many (ein Gast – viele Buchungen)
    // -------------------------------
    @OneToMany(
            mappedBy = "gast",          // verweist auf das Feld "gast" in Buchung
            fetch = FetchType.LAZY ,     // Buchungen werden nur bei Bedarf geladen
            cascade = CascadeType.ALL

    )
    private List<Buchung> buchungen = new ArrayList<>();

    // Leerer Konstruktor (von JPA benötigt)
    public Gast() {
    }

    // Konstruktor zum Erstellen eines Gastes
    public Gast(String name, String familienname, boolean istStammgast, double guthaben) {
        this.name = name;
        this.familienname = familienname;
        this.istStammgast = istStammgast;
        this.guthaben = guthaben;
    }
    // Getter für die ID
    public Long getId() {
        return id;
    }

    // ------ Getters und Setters -------
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFamilienname() {
        return familienname;
    }
    public void setFamilienname(String familienname) {
        this.familienname = familienname;
    }

    public boolean isIstStammgast() {
        return istStammgast;
    }
    public void setIstStammgast(boolean istStammgast) {
        this.istStammgast = istStammgast;
    }

    public double getGuthaben() {
        return guthaben;
    }
    public void setGuthaben(double guthaben) {
        this.guthaben = guthaben;
    }

    public GastAdresse getAdresse() {
        return adresse;
    }
    public void setAdresse(GastAdresse adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return "Gast{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", familienname='" + familienname + '\'' +
                ", istStammgast=" + istStammgast +
                ", guthaben=" + guthaben +
                '}';
    }
}
/*
Fetch-Strategien – JPA-Standard (wichtig!!):

@OneToOne     → EAGER
@ManyToOne   → EAGER
@OneToMany   → LAZY
@ManyToMany  → LAZY

→ Wenn fetch nicht angegeben ist, gilt immer der JPA-Default.

	•	Du arbeitest in Java mit Klassen & Objekten
	•	Die Datenbank arbeitet mit Tabellen & Spalten
	•	JPA verbindet beides

➡️ Objekt ↔ Tabelle
➡️ Attribut ↔ Spalte

⸻

🔧 Was macht JPA konkret?

Mit JPA kannst du:
	•	Klassen als Datenbanktabellen speichern (@Entity)
	•	Beziehungen definieren (@OneToOne, @ManyToOne, …)
	•	Daten speichern, lesen, ändern, löschen (CRUD)
	•	Abfragen schreiben (JPQL statt SQL)


JPA ist ein Standard, der festlegt,
wie Java-Objekte in einer Datenbank gespeichert werden.

👉 JPA ist nur eine Schnittstelle (API)
👉 Hibernate ist eine Umsetzung von JPA
also JPA = Regeln / Standard
Hibernate = Werkzeug, das diese Regeln umsetzt
*/
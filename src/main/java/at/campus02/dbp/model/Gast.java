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
     @OneToOne
      → Ein Gast hat genau eine Adresse

     fetch = LAZY
      → Adresse wird erst geladen, wenn getAdresse() aufgerufen wird
      → Wenn fetch NICHT gesetzt wird, ist der JPA-Standard EAGER

     cascade = ALL
     → Speichern, Ändern und Löschen von Gast
         wirkt automatisch auch auf die Adresse
     → Ohne Cascade müsste die Adresse separat gespeichert werden
     */   
     @OneToOne(
            fetch = FetchType.LAZY,  //sonst wäre JPA-Standard @OneToOne  EAGER
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
            mappedBy = "gast",          // verweist auf das Feld "gast" in Buchung, kennzeichnet die inverse Seite einer bidirektionalen Beziehung -Buchung besitzt die Beziehung
            fetch = FetchType.LAZY     // Buchungen werden nur bei Bedarf geladen, explizit gesetzt (entspricht JPA-Standard)
    )
    private List<Buchung> buchungen = new ArrayList<>();


    /*Damit ist Gast die Eigentümerseite (owning side) der Many-to-Many-Beziehung, Zusatzleistung ist die inverse Seite (inverse side)  bidirektional: Gast → Zusatzleistung*/
    @ManyToMany // LAZY  JPA-Standard (EAGER mache nur per JOIN FETCH )
    @JoinTable(
            name = "gast_zusatzleistung", //Name der Join-Tabelle in der Datenbank
            joinColumns = @JoinColumn(name = "gast_id"), //Fremdschlüssel-Spalte zur eigenen Entität (Gast)
            inverseJoinColumns = @JoinColumn(name = "zusatzleistung_id") //Fremdschlüssel-Spalte zur inversen Entität (Zusatzleistung)
    )
    private List<Zusatzleistung> zusatzleistungen = new ArrayList<>();

    public List<Zusatzleistung> getZusatzleistungen() {
        return zusatzleistungen;
    }

    public void addZusatzleistung(Zusatzleistung zl) {
        zusatzleistungen.add(zl);
    }
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
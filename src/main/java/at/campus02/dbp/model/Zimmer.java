package at.campus02.dbp.model;

import jakarta.persistence.*;

@Entity //Markiert die Klasse als JPA-Entity-Hibernate weiß :Diese Klasse soll in der Datenbank gespeichert werden.
@Table(name = "zimmer") //Legt den Tabellennamen fest, Sauberer, kontrollierter DB-Name
public class Zimmer {

    @Id // Markiert feld als Primärschlüssel //ein technisches Feld -Dient nur zur eindeutigen Identifikation -nicht Zimmernummer!
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID wird automatisch erzeugt
    @Column(name = "id")
    private Long id;   // muss Long sein!Hibernate + JPA erwarten für Primärschlüssel Wrapper-Typen, NICHT primitive Typen.id hat folgende Zustände:

    @Column(name = "nummer", nullable = false)
    private int nummer;

    @Column(name = "preis", nullable = false)
    private double preis;

    @Enumerated(EnumType.STRING)
    @Column(name = "kategorie", nullable = false, length = 30)
    private ZimmerKategorie kategorie;

    @Column(name = "ist_frei", nullable = false)
    private boolean istFrei = true;   // Default: jedes Zimmer ist frei

    // leerer Konstruktor - Pflicht für Hibernate
    public Zimmer() {
    }

    // Komfort-Konstruktor ohne Frei-Parameter
    public Zimmer(int nummer, double preis, ZimmerKategorie kategorie) {
        this.nummer = nummer;
        this.preis = preis;
        this.kategorie = kategorie;
    }

    //Hibernate liest & schreibt über Getter/Setter
    public Long getId() {
        return id;
    }

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public ZimmerKategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(ZimmerKategorie kategorie) {
        this.kategorie = kategorie;
    }

    public boolean getIstFrei() {
        return istFrei;
    }

    public void setIstFrei(boolean frei) {
        this.istFrei = frei;
    }

    // für Debugging, Logging und Ausgabe in Main
    @Override
    public String toString() {
        return "Zimmer{" +
                "id=" + id +
                ", nummer=" + nummer +
                ", preis=" + preis +
                ", kategorie=" + kategorie +
                ", istFrei=" + istFrei +
                '}';
    }
}
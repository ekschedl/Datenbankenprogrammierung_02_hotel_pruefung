package at.campus02.dbp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "zimmer")
public class Zimmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    public Zimmer() {
    }

    // Komfort-Konstruktor ohne Frei-Parameter
    public Zimmer(int nummer, double preis, ZimmerKategorie kategorie) {
        this.nummer = nummer;
        this.preis = preis;
        this.kategorie = kategorie;
    }

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
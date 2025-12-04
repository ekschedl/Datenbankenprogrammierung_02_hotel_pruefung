package at.campus02.dbp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gast")
public class Gast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String familienname;

    @Column(nullable = false)
    private boolean istStammgast;

    @Column(nullable = false)
    private double guthaben;

    // --- Beziehung zu Adresse (unidirektional) ---
    // Die Adresse ist in der XML gemappt, also NUR hier die JoinColumn setzen.
    @OneToOne(cascade = CascadeType.ALL) //Ohne Cascade sagt Hibernate:„Wenn du einen Gast speicherst, musst du Adresse vorher selbst speichern!“
    @JoinColumn(name = "adresse_id")
    private GastAdresse adresse;

    public Gast() {
    }

    public Gast(String name, String familienname, boolean istStammgast, double guthaben) {
        this.name = name;
        this.familienname = familienname;
        this.istStammgast = istStammgast;
        this.guthaben = guthaben;
    }

    public Long getId() {
        return id;
    }

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
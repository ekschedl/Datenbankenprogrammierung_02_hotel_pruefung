package at.campus02.dbp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zusatzleistung")
public class Zusatzleistung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double preis;

    // inverse Seite
    @ManyToMany(mappedBy = "zusatzleistungen")
    private List<Gast> gaeste = new ArrayList<>();

    public Zusatzleistung() {}

    public Zusatzleistung(String name, double preis) {
        this.name = name;
        this.preis = preis;
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public double getPreis() { return preis; }

    public void setPreis(double preis) {
        this.preis = preis;
    }
    @Override
    public String toString() {
        return name + " (€" + preis + ")";
    }
}
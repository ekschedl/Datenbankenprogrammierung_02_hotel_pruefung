package at.campus02.dbp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @ManyToMany(mappedBy = "zusatzleistungen") //mappedBy bedeutet: keine eigene JoinTable,  keine DB-Steuerung, nur Spiegel der Beziehung, nur zum Navigieren, Hibernate sagt damit: „Diese Liste wird aus der Join-Tabelle gelesen, aber nicht von hier aus geändert.
    private List<Gast> gaeste = new ArrayList<>(); //bidirektional: Zusatzleistung → Gast


    public List<Gast> getGaeste() { //Hibernate kann die Liste intern füllen, aber ohne Getter kanns ich sie nicht lesen.
        return gaeste;
    }

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

    //Zwei Zusatzleistungen sind gleich, wenn Name und Preis gleich sind.
    //Der Vergleich erfolgt bewusst über fachliche Attribute und nicht über die ID,
    // da diese erst nach dem persist()  gesetzt wird.Vor dem Speichern wäre id == null und equals() wäre unzuverlässig
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zusatzleistung)) return false;
        Zusatzleistung z = (Zusatzleistung) o;
        return name.equals(z.name) && preis == z.preis;
    }

    //Wenn equals() überschrieben wird, MUSS auch hashCode() überschrieben werden
    //sonst :inkonsistent arbeiten Hibernate-interne Collections
    @Override
    public int hashCode() {
        return Objects.hash(name, preis);
    }
}
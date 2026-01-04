package at.campus02.dbp.model;
/*
 * Die Klasse GastAdresse ist bewusst OHNE JPA-Annotationen umgesetzt.
 * Das Mapping erfolgt vollständig über ein Hibernate-XML-Mapping (.hbm.xml).
 *
 * Das XML-Mapping definiert:
 * - die Tabelle (gast_adresse)
 * - die Spalten (land, stadt, strasse, ...)
 * - den Primärschlüssel (id)
 * - Constraints (z. B. NOT NULL)

	Zusammenfassung:
	* XML = Konfiguration/Annotationen oder XML beschreiben das Mapping /
	Das Hibernate-XML-Mapping beschreibt, wie diese Klasse in der Datenbank gespeichert wird.
	* Java-Klasse = Datenmodell, immer Pflicht.


 * Vorteil von XML statt Annotationen:
 * - saubere Trennung von Code und Mapping
 * - Mapping kann geändert werden, ohne Java-Code anzupassen
 */
public class GastAdresse {

    private Long id; // Technische ID (Primärschlüssel, im XML definiert)

    // Fachliche Attribute der Adresse
    private String land;
    private String stadt;
    private String strasse;
    private String hausnummer; // Hausnummer als String, da auch Buchstaben möglich sind
    private String wohnung;

    // Leerer Konstruktor (von Hibernate benötigt)
    public GastAdresse() {
    };

    // Konstruktor zum Erstellen einer Adresse
    public GastAdresse( String land, String stadt, String strasse, String hausnummer, String wohnung) {
        this.land = land;
        this.stadt = stadt;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.wohnung = wohnung;
    }

    // Getter und Setter
    // Hibernate greift über Getter/Setter auf die Felder zu
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getWohnung() {
        return wohnung;
    }

    public void setWohnung(String wohnung) {
        this.wohnung = wohnung;
    }

    // String-Darstellung der Adresse
    @Override
    public String toString() {
        return "GastAdresse{" +
                "id=" + id +
                ", land='" + land + '\'' +
                ", stadt='" + stadt + '\'' +
                ", strasse='" + strasse + '\'' +
                ", hausnummer='" + hausnummer + '\'' +
                ", wohnung='" + wohnung + '\'' +
                '}';
    }
}




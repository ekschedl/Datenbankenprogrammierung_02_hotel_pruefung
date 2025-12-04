package at.campus02.dbp.model;

public class GastAdresse {

    private Long id;
    private String land;
    private String stadt;
    private String strasse;
    private String hausnummer; //da mit Buchstaben sien können sein
    private String wohnung;

    public GastAdresse() {

    };

    public GastAdresse( String land, String stadt, String strasse, String hausnummer, String wohnung) {

        this.land = land;
        this.stadt = stadt;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.wohnung = wohnung;
    }

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




# Checkliste: Prüfungsanforderungen

## ✅ Aufgabe 1 – Modellierung & Mapping (30 Punkte)

### ✅ Mindestens drei verbundene Entitäten (10 Punkte)
- ✅ **Gast** - @Entity, @Id, @GeneratedValue, mehrere Attribute (name, familienname, istStammgast, guthaben)
- ✅ **Zimmer** - @Entity, @Id, @GeneratedValue, mehrere Attribute (nummer, preis, kategorie, istFrei)
- ✅ **Buchung** - @Entity, @Id, @GeneratedValue, mehrere Attribute (vonDatum, bisDatum)
- ✅ **GastAdresse** - XML-Mapping, @Id (im XML), mehrere Attribute (land, stadt, strasse, hausnummer, wohnung)
- ✅ **Zusatzleistung** - @Entity, @Id, @GeneratedValue, mehrere Attribute (name, preis)

### ✅ Konfiguration über Annotationen und XML-Mapping-Files (10 Punkte)
- ✅ **Annotationen**: Gast, Zimmer, Buchung, Zusatzleistung sind primär über Annotationen konfiguriert
- ✅ **XML-Mapping**: GastAdresse ist vollständig über `GastAdresse.hbm.xml` konfiguriert
- ✅ **Kombination**: Gast hat @OneToOne mit GastAdresse (Annotation), während GastAdresse selbst XML-gemappt ist
- ✅ Klar erkennbar: Unterschied zwischen Annotationen und XML ist dokumentiert

### ✅ Dokumentation der verwendeten Annotationen / Properties (10 Punkte)
- ✅ `dokumentation.txt` existiert (ca. 1-2 Seiten)
- ✅ Erklärt: @Entity, @Table, @Id, @GeneratedValue, @Column
- ✅ Erklärt: @OneToMany, @ManyToOne, @OneToOne, @ManyToMany
- ✅ Erklärt: XML-Tags (<hibernate-mapping>, <class>, <id>, <property>)
- ✅ Pro Annotation 1-2 Sätze mit Verwendung im Projekt

---

## ✅ Aufgabe 2 – CRUD-Operationen inkl. Transaktionen (20 Punkte)

### ✅ CRUD für mindestens eine Hauptentität (10 Punkte)
**GastDAO:**
- ✅ `save()` - Create
- ✅ `findById()` - Read
- ✅ `findAll()` - Read
- ✅ `update()` - Update
- ✅ `delete()` / `deleteById()` - Delete

**ZimmerDAO:**
- ✅ `save()` - Create
- ✅ `findById()` - Read
- ✅ `findAll()` - Read All
- ✅ `update()` - Update
- ✅ `delete()` / `deleteById()` - Delete

### ✅ Verwendung von Transaktionen (10 Punkte)
**Architektur:**
- ✅ **Services (BuchungService)**: 
  - Transaktionen mit explizitem rollback im catch-Block
  - **Begründung**: Services kapseln mehrere DB-Operationen und Business-Regeln
  - Rollback ist hier essentiell für Datenkonsistenz
---

## ✅ Aufgabe 3 – Abfragen mit HQL (20 Punkte)

### ✅ WHERE + Named Parameter (3 Punkte)
- ✅ `GastQuery.zeigeStammgaeste()` - `WHERE g.istStammgast = :stammgast`
- ✅ `ZimmerQuery.findByKategorie()` - `WHERE z.kategorie = :kat`

### ✅ GROUP BY + Aggregatfunktion (4 Punkte)
- ✅ `GastQuery.countStammgaeste()` - `SELECT g.istStammgast, COUNT(g) FROM Gast g GROUP BY g.istStammgast`
- ✅ `ZimmerQuery.countZimmerProKategorie()` - `SELECT z.kategorie, COUNT(z) FROM Zimmer z GROUP BY z.kategorie`

### ✅ Alias & Sortierung (ORDER BY) (3 Punkte)
- ✅ `GastQuery.alleGaesteSortiertNachName()` - `ORDER BY g.familienname ASC` mit Alias `g`
- ✅ `ZimmerQuery.findAllOrderedByPreis()` - `ORDER BY z.preis` mit Alias `z`

### ✅ Pagination (5 Punkte)
- ✅ `GastQuery.zeigeGaestePaginiert()` - `setFirstResult(start)` und `setMaxResults(anzahl)`
- ✅ `ZimmerQuery.findZimmerPage()` - `setFirstResult((page - 1) * size)` und `setMaxResults(size)`
- ✅ `GastDAO.findAllPaged()` - `setFirstResult(start)` und `setMaxResults(size)`

### ✅ JOINS (5 Punkte)
- ✅ `ZimmerQuery.findZimmerByGast()` - JOIN über Buchung: `SELECT b.zimmer FROM Buchung b WHERE b.gast.id = :id`

---

## ✅ Aufgabe 4 – Associations / Relationships (30 Punkte)

### ✅ One-to-Many / Many-to-One (10 Punkte)
- ✅ **Gast → Buchung**: @OneToMany mit `mappedBy = "gast"`, FetchType.LAZY, 
- ✅ **Buchung → Gast**: @ManyToOne mit @JoinColumn, FetchType.LAZY
- ✅ **Buchung → Zimmer**: @ManyToOne mit @JoinColumn, FetchType.LAZY
- ✅ Korrekte Verwendung von mappedBy (Gast ist inverse Seite)
- ✅ Eigentümerseite klar: Buchung (hat @JoinColumn)
- ✅ **Unterschied zwischen Eager-Loading und Lazy-Loading wird dokumentiert**
  - Kommentare in `Buchung.java` erklären FetchType.LAZY und FetchType.EAGER
  - Kommentare in `Gast.java` erklären FetchType.LAZY und JPA-Standard (EAGER bei @OneToOne/@ManyToOne)
  - Dokumentation in `dokumentation.txt` erklärt beide Strategien
  - **Hinweis**: JPA-Standard ist EAGER bei @ManyToOne/@OneToOne, LAZY bei @OneToMany/@ManyToMany
  - Projekt verwendet bewusst LAZY für bessere Performance

### ✅ One-to-One (5 Punkte)
- ✅ **Gast → GastAdresse**: @OneToOne mit @JoinColumn, FetchType.LAZY, CascadeType.ALL
- ✅ Unidirektional: Gast kennt Adresse, Adresse kennt Gast nicht
- ✅ Eigentümerseite klar: Gast (hat @JoinColumn)

### ✅ Many-to-Many (5 Punkte)
- ✅ **Gast → Zusatzleistung**: @ManyToMany mit @JoinTable
- ✅ **Zusatzleistung → Gast**: @ManyToMany mit `mappedBy = "zusatzleistungen"`
- ✅ Join-Tabelle: `gast_zusatzleistung` mit `gast_id` und `zusatzleistung_id`
- ✅ Bidirektional

### ✅ Unidirektionale und bidirektionale Beziehungen (10 Punkte)
**Bidirektional:**
- ✅ **Gast ↔ Buchung**: Gast hat `List<Buchung>`, Buchung hat `Gast`
- ✅ **Gast ↔ Zusatzleistung**: Gast hat `List<Zusatzleistung>`, Zusatzleistung hat `List<Gast>`

**Unidirektional:**
- ✅ **Gast → GastAdresse**: Gast kennt Adresse, Adresse kennt Gast nicht

**Demo-Code:**
- ✅ Main zeigt: Beziehungen werden korrekt gespeichert (z.B. `gast.setAdresse(adresse)`)
- ✅ Main zeigt: Beziehungen werden korrekt geladen (z.B. `gast.getZusatzleistungen()`)
- ✅ Main zeigt: Buchungen werden korrekt erstellt und verknüpft

---

##  Implementierungsdetails

### Entitäten:
- **Gast**: Hauptentität mit CRUD-Operationen
- **Zimmer**: Hauptentität mit CRUD-Operationen
- **Buchung**: Verbindungsentität zwischen Gast und Zimmer
- **GastAdresse**: XML-gemappte Entität
- **Zusatzleistung**: Many-to-Many mit Gast

### DAOs:
- **GastDAO**: Vollständiges CRUD, try-with-resources
- **ZimmerDAO**: Vollständiges CRUD, explizites rollback
- **BuchungDAO**: save() mit explizitem rollback
- **ZusatzleistungDAO**: save() mit explizitem rollback

### Services:
- **BuchungService**: Business-Logik mit explizitem rollback
- **ZusatzleistungService**: Many-to-Many-Verwaltung

### Queries:
- **GastQuery**: WHERE, GROUP BY, ORDER BY, Pagination
- **ZimmerQuery**: WHERE, GROUP BY, ORDER BY, Pagination, JOIN

GastDAO verwendet try-with-resources (einfacher). BuchungDAO verwendet manuelles Session-Management (mehr Code). Beide funktionieren. 
Aktuell:
GastDAO: try-with-resources (einfacher, automatisch)
BuchungDAO: manuelles Management (mehr Code, explizites rollback)
Beide Ansätze sind gültig. Für Konsistenz: BuchungDAO auf try-with-resources umstellen (wie GastDAO)?
Vorteil: Konsistent mit GastDAO, weniger Code, automatisches Session-Management. Bei Exception wird die Transaktion automatisch verworfen (try-with-resources).
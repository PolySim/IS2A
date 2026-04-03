# Annales

## Question 2

```java
@Entity
public class Etape {

    @Id
    private String id_etape;

    @ManyToOne
    private Fromage fromage;
    @ManyToOne
    private Cave cave;

    @Id
    private Date dateDebut;

    private Date dateFin;

    public Etape() {}
}
```

## Question 3

```java
interface IGestionAffinage {
  EntityTransaction getTransaction();
  Cave getCaveById(int cave_id) throws CaveInconnuException;
  void createFromage(String type, Date date, int cave_id)
    throws CaveInconnuException;
  Fromage getFromageById(int fromage_id) throws FromageInconnuException;
  Etape getCurrentEtape(int fromage_id)
    throws FromageInconnuException, FromageAffineException;
  void moveFromage(int fromage_id, Date new_date, int cave_id)
    throws FromageInconnuException, FromageAffineException, CaveInconnuException;
}
```

## Question 4

```java
Cave getCaveById(int cave_id) throws CaveInconnuException {
  Cave cave = em.find(Cave.class, cave_id);
  if (cave == null) throw CaveInconnuException;
  return cave;
};

Fromage getFromageById(int fromage_id) throws FromageInconnuException {
  Fromage fromage = em.find(Fromage.class, fromage_id);
  if (fromage == null) throw FromageInconnuException;
  return fromage;
};

void moveFromage(int fromage_id, Date new_date, int cave_id)
  throws FromageInconnuException, FromageAffineException, CaveInconnuException {
    Fromage fromage = getFromageById(fromage_id);
    Etape currentEtape = getCurrentEtape(fromage_id);
    Cave cave = getCave(cave_id);
    currentEtape.setDateFin(new_date);
    Etape new_etape = new Etape();
    new_etape.setFromage(fromage);
    new_etape.setCave(cave);
    new_etape.setDateDebut(new_date);
    em.persist(new_etape);
};
```

### Question 5

```java
try {
  service.getTransaction().begin();
  moveFromage(fromageId, dateTransfert, nouvelleCaveId);
  service.getTransaction().commit();
} catch ( Exception e ) {
  System.err.println("Error");
} finally {
  if (service.getTransaction().isActive()) service.getTransaction().rollback();
}
```

### Question 6

```JPQL
SELECT o FROM Observation o
JOIN o.fromage f
WHERE f.type = 'chèvre'
```

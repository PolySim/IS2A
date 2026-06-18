# Réponses - Entraînement BDN

## I.1 MongoDB

### 1. Liste complète des restaurants en n'affichant que le score des grades et le type de cuisine

```javascript
db.restaurants.find(
  {},
  { _id: 0, cuisine: 1, "grades.score": 1 }
)
```

### 2. Liste des quartiers où il y a au moins un restaurant ayant au moins 4 scores et dont tous les scores sont inférieurs à 5

```javascript
db.restaurants.distinct(
  "borough",
  {
    $expr: { $gte: [{ $size: "$grades" }, 4] },
    grades: { $not: { $elemMatch: { score: { $gte: 5 } } } }
  }
)
```

### 3. Pour le Bronx, nombre de restaurants par nombre de grades

```javascript
db.restaurants.aggregate([
  { $match: { borough: "Bronx" } },
  {
    $project: {
      nbGrades: { $size: "$grades" }
    }
  },
  {
    $group: {
      _id: "$nbGrades",
      nbRestaurants: { $sum: 1 }
    }
  },
  { $sort: { _id: 1 } }
])
```

### 4. Moyenne par quartier des scores moyens de chaque restaurant, triée par ordre croissant

```javascript
db.restaurants.aggregate([
  {
    $project: {
      borough: 1,
      moyenneRestaurant: { $avg: "$grades.score" }
    }
  },
  {
    $group: {
      _id: "$borough",
      moyenneQuartier: { $avg: "$moyenneRestaurant" }
    }
  },
  { $sort: { moyenneQuartier: 1 } }
])
```

### 5. Nombre total de grades de grade B par type de cuisine

```javascript
db.restaurants.aggregate([
  { $unwind: "$grades" },
  { $match: { "grades.grade": "B" } },
  {
    $group: {
      _id: "$cuisine",
      totalGradesB: { $sum: 1 }
    }
  },
  { $sort: { totalGradesB: -1 } }
])
```

### 6. Nombre moyen de grades de grade A par quartier pour les restaurants de type Pizza

```javascript
db.restaurants.aggregate([
  { $match: { cuisine: "Pizza" } },
  {
    $project: {
      borough: 1,
      nbGradesA: {
        $size: {
          $filter: {
            input: "$grades",
            as: "g",
            cond: { $eq: ["$$g.grade", "A"] }
          }
        }
      }
    }
  },
  {
    $group: {
      _id: "$borough",
      moyenneGradesA: { $avg: "$nbGradesA" }
    }
  },
  { $sort: { _id: 1 } }
])
```

## I.2 OrientDB

### 1. "Tu te souviens ce mec qui venait du Mozambique et qui était ami avec Gary, comment il s'appelait ?"

```sql
SELECT name
FROM Profiles
WHERE location.country.name = 'Mozambique'
  AND out('HasFriend').name CONTAINS 'Gary'
```

### 2. "Comment est ce que s'appelait cette femme qui a visité ce chateau Porta del Forno et qui est allée dans ce bar Bar Ventina ?"

```sql
SELECT name
FROM Profiles
WHERE out('Visited').name CONTAINS 'Porta del Forno'
  AND out('Visited').name CONTAINS 'Bar Ventina'
```

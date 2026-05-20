// Q1
db.getCollection("restaurants").find();
// Q2
db.getCollection("restaurants").count({});

// Q3
db.getCollection("restaurants")
  .find({})
  .projection({ name: 1, cuisine: 1, _id: 0 });
db.getCollection("restaurants").find({}, { name: 1, cuisine: 1, _id: 0 });

// Q4
db.getCollection("restaurants").find({}).sort({ borough: 1, name: 1 });

// Q5
db.getCollection("restaurants").find({ borough: "Brooklyn" });

// Q6
db.getCollection("restaurants").find({
  borough: "Brooklyn",
  cuisine: /.*Italian.*/,
});

// Q7
db.getCollection("restaurants").find({
  borough: "Brooklyn",
  cuisine: /.*Italian.*/,
  "address.street": "5 Avenue",
});

// Q8
db.getCollection("restaurants").find({
  borough: "Brooklyn",
  cuisine: /.*Italian.*/,
  "address.street": "5 Avenue",
  $or: [{ name: /.*Pizza.*/ }, { name: /.*pizza.*/ }],
});

// Q9
db.getCollection("restaurants").find({
  borough: "Bronx",
  grades: { $elemMatch: { score: { $lt: 10 } } },
});

// Q10
db.getCollection("restaurants").find({
  borough: "Bronx",
  grades: { $not: { $elemMatch: { score: { $gte: 10 } } } },
});

// Q11
db.getCollection("restaurants").find(
  {
    borough: "Bronx",
    grades: { $not: { $elemMatch: { score: { $gte: 10 } } } },
  },
  { name: 1, grades: 1, _id: 0 },
);

// Q12
db.getCollection("restaurants")
  .find({
    grades: {
      $elemMatch: {
        grade: "C",
        score: {
          $lt: 30,
        },
      },
    },
  })
  .count();

// Q13
db.getCollection("restaurants").distinct("borough", {});

// Q14
db.getCollection("restaurants").distinct("borough", {}).length;

// Q15
db.getCollection("restaurants").distinct("grades.grade", {});

// Q16
db.getCollection("restaurants").distinct("cuisine", {
  $expr: { $gte: [{ $size: "$grades" }, 2] },
  grades: { $not: { $elemMatch: { score: { $lt: 20 } } } },
}).length;

// Q17
db.getCollection("restaurants").createIndex({ "address.coord": "2d" });
db.getCollection("restaurants").find({
  "address.coord": {
    $geoWithin: {
      $centerSphere: [[-73.93414657, 40.82302903], 1 / 6378.1],
    },
  },
});

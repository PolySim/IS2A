// Q1
    db.getCollection("restaurants").find();
    db.getCollection("restaurants").aggregate();
    // Q2
    db.getCollection("restaurants").count({});
    db.getCollection("restaurants").aggregate([
        {
            $group: {
                _id: null,
                total: { $sum: 1 },
                },
            },
        ]);

    // Q3
    db.getCollection("restaurants")
        .find({})
        .projection({ name: 1, cuisine: 1, _id: 0 });
    db.getCollection("restaurants").find({}, { name: 1, cuisine: 1, _id: 0 });
    db.getCollection("restaurants").aggregate([
        {
            $project: { name: 1, cuisine: 1, _id: 0 },
            },
        ]);

    // Q4
    db.getCollection("restaurants").find({}).sort({ borough: 1, name: 1 });
    db.getCollection("restaurants").aggregate([
        {
            $sort: { borough: 1, name: 1 },
            },
        ]);

    // Q5
    db.getCollection("restaurants").find({ borough: "Brooklyn" });
    db.getCollection("restaurants").aggregate([
        {
            $match: { borough: "Brooklyn" },
            },
        ]);

    // Q6
    db.getCollection("restaurants").find({
        borough: "Brooklyn",
        cuisine: /.*Italian.*/,
        });
    db.getCollection("restaurants").aggregate([
        {
            $match: { borough: "Brooklyn", cuisine: /.*Italian.*/ },
            },
        ]);

    // Q7
    db.getCollection("restaurants").find({
        borough: "Brooklyn",
        cuisine: /.*Italian.*/,
        "address.street": "5 Avenue",
        });
    db.getCollection("restaurants").aggregate([
        {
            $match: {
                borough: "Brooklyn",
                cuisine: /.*Italian.*/,
                "address.street": "5 Avenue",
                },
            },
        ]);

    // Q8
    db.getCollection("restaurants").find({
        borough: "Brooklyn",
        cuisine: /.*Italian.*/,
        "address.street": "5 Avenue",
        $or: [{ name: /.*Pizza.*/ }, { name: /.*pizza.*/ }],
        });
    db.getCollection("restaurants").aggregate([
        {
            $match: {
                borough: "Brooklyn",
                cuisine: /.*Italian.*/,
                "address.street": "5 Avenue",
                $or: [{ name: /.*Pizza.*/ }, { name: /.*pizza.*/ }],
                },
            },
        ]);

    // Q9
    db.getCollection("restaurants").find({
        borough: "Bronx",
        grades: { $elemMatch: { score: { $lt: 10 } } },
        });
    db.getCollection("restaurants").aggregate([
        {
            $match: {
                borough: "Bronx",
                grades: { $elemMatch: { score: { $lt: 10 } } },
                },
            },
        ]);

    // Q10
    db.getCollection("restaurants").find({
        borough: "Bronx",
        grades: { $not: { $elemMatch: { score: { $gte: 10 } } } },
        });
    db.getCollection("restaurants").aggregate([
        {
            $match: {
                borough: "Bronx",
                grades: { $not: { $elemMatch: { score: { $gte: 10 } } } },
                },
            },
        ]);

    // Q11
    db.getCollection("restaurants").find(
        {
            borough: "Bronx",
            grades: { $not: { $elemMatch: { score: { $gte: 10 } } } },
            },
        { name: 1, grades: 1, _id: 0 },
        );
    db.getCollection("restaurants").aggregate([
        {
            $match: {
                borough: "Bronx",
                grades: { $not: { $elemMatch: { score: { $gte: 10 } } } },
                },
            },
        {
            $project: { name: 1, grades: 1, _id: 0 },
            },
        ]);

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
    db.getCollection("restaurants").aggregate([
        {
            $match: {
                grades: {
                    $elemMatch: {
                        grade: "C",
                        score: {
                            $lt: 30,
                            },
                        },
                    },}
            }]);

    // Q13
    db.getCollection("restaurants").distinct("borough", {});
    db.getCollection("restaurants").aggregate([
        {
            $group: {
                _id: "$borough"
                }
            },
        {
            $project: {
                _id: 0,
                borough: "$_id"
                }
            }
        ]);

    // Q14
    db.getCollection("restaurants").distinct("borough", {}).length;
    db.getCollection("restaurants").aggregate([
        {
            $group: {
                _id: "$borough"
                }
            },
        {
            $group: {
                _id: null,
                total: {$sum: 1}
                }
            }, {
            $project: {total: 1, _id: 0}
            }
        ]);

    // Q15
    db.getCollection("restaurants").distinct("grades.grade", {});
    db.getCollection("restaurants").aggregate([
        {
            $unwind: "$grades"
            },
        {
            $group: {
                _id: "$grades.grade"
                }
            },
        {
            $project: {
                _id: 0,
                grade: "$_id"
                }
            }
        ]);

    // Q16
    db.getCollection("restaurants").distinct("cuisine", {
        $expr: { $gte: [{ $size: "$grades" }, 2] },
        grades: { $not: { $elemMatch: { score: { $lt: 20 } } } },
        });
    db.getCollection("restaurants").aggregate([
        {
            $match: {
                $expr: { $gte: [{ $size: "$grades" }, 2] },
                grades: { $not: { $elemMatch: { score: { $lt: 20 } } } },
                }
            },
        {
            $group: {
                _id: "$cuisine"
                }
            }])

    // Q17
    db.getCollection("restaurants").createIndex({ "address.coord": "2d" });
    db.getCollection("restaurants").find({
        "address.coord": {
            $geoWithin: {
                $centerSphere: [[-73.93414657, 40.82302903], 1 / 6378.1],
                },
            },
        });
    db.getCollection("restaurants").aggregate([
        {
            $match: {
                "address.coord": {
                    $geoWithin: {
                        $centerSphere: [
                            [-73.93414657, 40.82302903],
                            1 / 6378.1
                            ]
                        }
                    }
                }
            }
        ]);

    // Q18
    db.getCollection("restaurants").aggregate([
        {
            $match: {
                restaurant_id: "30075445"
                }
            },
        {
            $unwind: "$grades"
            }, {
            $project: {
                grades: 1, _id: 0
                }
            }
        ]);

    // Q19
    db.getCollection("restaurants").aggregate([
        {
            $group: {
                _id: "$borough",
                total: {$sum :1}
                }
            }])

    // Q20
    db.getCollection("restaurants").aggregate([
        {
            $unwind: "$grades"
            },
        {
            $group: {
                _id: "$borough",
                mean: {$avg : "$grades.score"}
                }
            },
        {
            $sort: {"mean": -1}
            }])

    // Q21
    db.getCollection("restaurants").aggregate([
        {
            $unwind: "$grades"
            },
        {
            $group: {
                _id: {
                    borough: "$borough",
                    cuisine: "$cuisine"
                    },
                total: {$sum :1}
                }
            }, {
            $sort: {total: -1}
            }, {
            $limit: 10
            }])

    // Q22
    db.getCollection("restaurants").aggregate([
        {
            $unwind: "$grades"
            },
        {
            $group: {
                _id: {
                    grade: "$grades.grade",
                    cuisine: "$cuisine"
                    },
                total: { $sum: 1 }
                }
            },
        {
            $group: {
                _id: "$_id.grade",
                cuisines: {
                    $push: {
                        cuisine: "$_id.cuisine",
                        total: "$total"
                        }
                    }
                }
            },
        {
            $project: {
                _id: 0,
                grade: "$_id",
                cuisines: 1
                }
            }
        ]);

    // Q23
    db.getCollection("restaurants").aggregate([
        {
            $unwind: "$grades"
            },
        {
            $group: {
                _id: {
                    restaurant: "$restaurant_id",
                    grade: "$grades.grade",
                    cuisine: "$cuisine"
                    }
                }
            },
        {
            $group: {
                _id: {
                    grade: "$_id.grade",
                    cuisine: "$_id.cuisine"
                    },
                total: { $sum: 1 }
                }
            },
        {
            $group: {
                _id: "$_id.grade",
                cuisines: {
                    $push: {
                        cuisine: "$_id.cuisine",
                        total: "$total"
                        }
                    }
                }
            },
        {
            $project: {
                _id: 0,
                grade: "$_id",
                cuisines: 1
                }
            }
        ]);
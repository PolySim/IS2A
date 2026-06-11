from pprint import pprint

import matplotlib.pyplot as plt
from pymongo import MongoClient  # pyright: ignore[reportMissingImports]

client = MongoClient(host="localhost", port=27017)
db = client["tp"]

# Q2.3

cursor = db.restaurants.aggregate(
    [
        {"$match": {"restaurant_id": "30075445"}},
    ]
)

l_resultat = list(cursor)
pprint(f"Le nom du restaurant : {l_resultat[0]['name']}")

# Q2.4

for grade in l_resultat[0]["grades"]:
    pprint(
        f"Le grade {grade['grade']} a été mis à jour le {grade['date'].strftime('%d/%m/%Y')}"
    )

print("\n\n")

# Q2.5

cursor = db.restaurants.aggregate(
    [
        {"$sort": {"cuisine": 1}},
        {"$limit": 5},
    ]
)

l_resultat = list(cursor)

for restaurant in l_resultat:
    print(
        f'Le restaurant "{restaurant["name"]}" fait de la cuisine "{restaurant["cuisine"]}"'
    )

print("\n\n")

# Q3.1

cursor = db.restaurants.aggregate(
    [
        {"$match": {"restaurant_id": "30075445"}},
        {"$project": {"grades": 1, "_id": 0}},
        {"$unwind": "$grades"},
        {"$project": {"score": "$grades.score", "date": "$grades.date", "_id": 0}},
        {"$sort": {"date": 1}},
    ]
)

l_resultat = list(cursor)

pprint(l_resultat)

# Q3.2

dates = [doc["date"] for doc in l_resultat]
scores = [doc["score"] for doc in l_resultat]
pprint(dates)
pprint(scores)

# Q3.3

# plt.plot(dates, scores)
# plt.show()

# Q3.4

print("\n\n")

cursor = db.restaurants.aggregate(
    [
        {"$match": {"cuisine": "French"}},
        {"$addFields": {"nb_grades": {"$size": "$grades"}}},
        {"$group": {"_id": "$nb_grades", "restaurants": {"$push": "$$ROOT"}}},
        {"$sort": {"_id": -1}},
        {"$limit": 1},
        {"$unwind": "$restaurants"},
        {"$replaceWith": "$restaurants"},
        {"$project": {"name": 1, "nb_grades": 1, "_id": 0}},
    ]
)


l_resultat = list(cursor)

pprint(l_resultat)

# Q3.7

cursor = db.restaurants.aggregate(
    [
        {"$match": {"cuisine": "French"}},
        {"$addFields": {"nb_grades": {"$size": "$grades"}}},
        {"$group": {"_id": "$nb_grades", "restaurants": {"$push": "$$ROOT"}}},
        {"$sort": {"_id": -1}},
        {"$limit": 1},
        {"$unwind": "$restaurants"},
        {"$replaceWith": "$restaurants"},
        {"$project": {"grades": 1, "name": 1, "_id": 0}},
        {"$unwind": "$grades"},
        {
            "$project": {
                "score": "$grades.score",
                "date": "$grades.date",
                "name": 1,
                "_id": 0,
            }
        },
        {"$sort": {"date": 1}},
    ]
)

l_resultat = list(cursor)

pprint(l_resultat)

# names = list(set([doc["name"] for doc in l_resultat]))
# for name in names:
#     plt.plot(
#         [doc["date"] for doc in l_resultat if doc["name"] == name],
#         [doc["score"] for doc in l_resultat if doc["name"] == name],
#         label=name,
#         marker="o",
#     )
# plt.legend()
# plt.show()

# Q4.1

print("\n\n")

cursor = db.restaurants.aggregate(
    [
        {"$match": {"cuisine": "Sandwiches"}},
        {"$group": {"_id": "$borough", "count": {"$sum": 1}}},
    ]
)

l_resultat = list(cursor)

# plt.pie([doc["count"] for doc in l_resultat], labels=[doc["_id"] for doc in l_resultat])
# plt.show()

pprint(l_resultat)

# Q4.3

print("\n\n")

cursor = db.restaurants.aggregate(
    [
        {
            "$group": {
                "_id": "$cuisine",
                "total": {"$sum": 1},
                "brooklyn_count": {
                    "$sum": {
                        "$cond": {
                            "if": {"$eq": ["$borough", "Brooklyn"]},
                            "then": 1,
                            "else": 0,
                        }
                    }
                },
            }
        },
        {
            "$project": {
                "_id": 0,
                "cuisine": "$_id",
                "total": 1,
                "brooklyn_count": 1,
                "percentage": {
                    "$multiply": [{"$divide": ["$brooklyn_count", "$total"]}, 100]
                },
            }
        },
        {"$sort": {"percentage": -1}},
    ]
)


l_resultat = list(cursor)

pprint(l_resultat)

fig, ax = plt.subplots(figsize=(10, 12))

cuisines = [doc["cuisine"] for doc in l_resultat]
percentages = [doc["percentage"] for doc in l_resultat]
ax.barh(cuisines, percentages, color="skyblue")
ax.set_xlabel("Pourcentage à Brooklyn (%)")
ax.set_title("Pourcentage de chaque cuisine à Brooklyn")

plt.tight_layout()
plt.show()

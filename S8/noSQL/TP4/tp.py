from pprint import pprint

import matplotlib.pyplot as plt
from pymongo import MongoClient

client = MongoClient(host="localhost", port=27017)
db = client["tp"]


cursor = db.restaurants.aggregate(
    [
        {"$match": {"name": {"$not": {"$eq": ""}}}},
        {"$sort": {"name": 1, "borough": 1}},
        {"$limit": 150},
        {
            "$project": {
                "name": 1,
                "borough": 1,
                "x": {"$arrayElemAt": ["$address.coord", 0]},
                "y": {"$arrayElemAt": ["$address.coord", 1]},
            }
        },
    ]
)

l_resultat = list(cursor)
pprint(l_resultat[len(l_resultat) - 1])

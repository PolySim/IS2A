import json

import geopandas as gpd
import pandas as pd
import matplotlib.pyplot as plt
import contextily
import seaborn

import sys


json_path = sys.argv[1]


with open(json_path, "r", encoding="utf-8") as f:
    restaurants = [json.loads(ligne) for ligne in f if ligne.strip()]

for d in restaurants:
    del d["restaurant_id"]
    if len(d["address"]["coord"]) >= 2:
        d["x"] = d["address"]["coord"][0]
        d["y"] = d["address"]["coord"][1]
        if len(d["grades"]) < 3:
            d["nbgrades"] = "Moins de 3"
        elif len(d["grades"]) < 7:
            d["nbgrades"] = "Entre 3 et 6"
        else:
            d["nbgrades"] = "Plus de 6"



data = pd.DataFrame(restaurants)

gdata = gpd.GeoDataFrame(data, geometry=gpd.points_from_xy(data.x, data.y), crs="EPSG:4326")

nyc = gpd.read_file("nyc-neighborhoods.geo.json")

gdataj = gdata.sjoin(nyc,how="inner",predicate="within")

g = seaborn.jointplot(x = "x", y = "y",
              kind = "scatter", data = gdataj, hue="nbgrades")
contextily.add_basemap(
    g.ax_joint,
    crs="EPSG:4326",
    source=contextily.providers.CartoDB.PositronNoLabels,
)

g.plot_joint(seaborn.kdeplot, color="r", zorder=0, levels=6 ,fill=True , alpha=0.5)
plt.show()

#

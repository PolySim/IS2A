<?php
function format_films_to_table($films)
{
    $films_formatted = array();
    foreach ($films as $film) {
        $film_formatted = array();
        $film_formatted['Titre'] = $film['titre'];
        $film_formatted['Réalisateur'] = $film['realisateur'];
        $film_formatted['Année de production'] = $film['anneeproduction'];
        $film_formatted['Categorie'] = $film['libelle'];
        $films_formatted[] = $film_formatted;
    }
    return $films_formatted;
}

?>
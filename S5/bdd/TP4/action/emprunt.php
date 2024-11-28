<?php
require('./dvd.php');
require('../utils/db.php');

$db = connect_to_database();
if (!$db) {
    die("Connection à la database impossible");
}
$film = $_GET['film'];
$abonne = $_GET['abonne'];

if (!dvdIsAvailable($db, $film)) {
    echo "Ce film n'est pas disponible";
    die();
}

$nb_films = nb_film_emprunter($db, $abonne);
if ($nb_films > 3) {
    echo "Vous avez déjà emprunté $nb_films films";
    die();
}

close_db($db);
?>
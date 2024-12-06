<?php
require('./dvd.php');
require('./abonne.php');
require('../utils/db.php');

$db = connect_to_database();
if (!$db) {
    die("Connection à la database impossible");
}
$film = $_GET['film'];
$abonne = $_GET['nom'];

$dvds = dvdAvailable($db, $film);

if (!$dvds || count($dvds) === 0) {
    echo "Ce film n'est pas disponible";
    close_db($db);

//    header('Location: /?add=unavailable');
    die();
}

$nb_films = nb_film_emprunter($db, $abonne);
if ($nb_films > 3) {
    echo "Vous avez déjà emprunté $nb_films films";
    close_db($db);

//    header('Location: /?add=toomuch');
    die();
}

updateEmpruntDvd($db, $dvds[0]['ndvd'], $abonne);

close_db($db);

//header('Location: /?add=success');
die();
?>
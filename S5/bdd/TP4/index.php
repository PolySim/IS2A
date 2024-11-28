<DOCTYPE html>
    <html>
    <head>
        <title>TP4</title>
        <link rel="stylesheet" type="text/css" href="index.css">
    </head>
    <body class="m-4">
    <header class="flex gap-4 justify-center">
        <h1>Mediatheque</h1>
    </header>

    <?php
    require('./utils/db.php');
    require('./action/films.php');
    require('./action/abonne.php');
    require('./component/table.php');
    require('./formateur/films.php');
    require('./formateur/abonne.php');
    $db = connect_to_database();
    $films = getFilms($db);
    $abonnes = getAbonnes($db);
    if (!$db) {
        die("Connection à la database impossible");
    }
    ?>

    <script>
        console.log('Hello world');
    </script>

    <div class="flex justify-center gap-8 flex-wrap center w-fit">
        <div class="flex flex-col gap-4 center w-fit">
            <h3 class="w-fit">Liste des films</h3>
            <?php
            table(format_films_to_table($films), ['Titre', 'Réalisateur', 'Année de production', 'Categorie']);
            ?>
        </div>

        <div class="flex flex-col gap-4 center w-fit">
            <h3 class="w-fit">Liste des abonnes</h3>
            <?php
            table(format_abonne_to_table($abonnes), ['Nom', 'Prenom']);
            ?>
        </div>
    </div>

    </body>
    </html>

<?php
close_db($db);
?>
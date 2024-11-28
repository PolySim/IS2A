<DOCTYPE html>
    <html>
    <head>
        <title>TP4</title>
        <link rel="stylesheet" type="text/css" href="index.css">
    </head>
    <body class="m-4 center max-w-6xl">
    <header class="flex gap-4 justify-center">
        <h1>Mediatheque</h1>
    </header>

    <?php
    require('./utils/db.php');
    require('./action/films.php');
    require('./action/abonne.php');
    require('./component/table.php');
    require('./component/inputText.php');
    require('./component/button.php');
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

    <div class="flex justify-between gap-8 flex-wrap">
        <div class="flex flex-col gap-4 w-fit">
            <h3 class="w-fit">Liste des films</h3>
            <?php
            table(format_films_to_table($films), ['Titre', 'Réalisateur', 'Année de production', 'Categorie']);
            ?>
        </div>

        <div class="flex flex-col gap-4 w-fit">
            <h3 class="w-fit">Liste des abonnes</h3>
            <?php
            table(format_abonne_to_table($abonnes), ['Nom', 'Prenom']);
            ?>
        </div>
    </div>

    <div class="flex flex-col gap-8 mt-4">
        <h3>Emprunter un film</h3>
        <div class="flex gap-4 w-full max-w-2xl">
            <script>
                const filmIsInList = (film, films) => {
                    return films.some(f => f.titre.toLowerCase() === film.toLowerCase());
                }

                const abonneIsInList = (abonne, abonnes) => {
                    return abonnes.some(a => a.nomab.toLowerCase() === abonne.toLowerCase());
                }

                const handleSubmit = (e) => {
                    const form = e.target;
                    const formData = new FormData(form);
                    const data = Object.fromEntries(formData.entries());
                    if (!filmIsInList(data.film, <?php echo json_encode($films); ?>)) {
                        e.preventDefault();
                        alert('Film non trouvé');
                        return;
                    }
                    if (!abonneIsInList(data.nom, <?php echo json_encode($abonnes); ?>)) {
                        e.preventDefault();
                        alert('Abonne non trouvé');
                        return;
                    }
                }
            </script>
            <form action="" class="grid grid-cols-3 gap-4 items-end w-full" onsubmit="handleSubmit(event)">
                <?php
                InputText('Nom', 'nom', '', 'Nom', "p-2 rounded-md", true);
                InputText('Film', 'film', '', 'Film', "p-2 rounded-md", true);
                Button('submit', 'Emprunter', 'bg-blue-500 text-white rounded-md p-2');
                ?>
            </form>
        </div>
    </div>

    </body>
    </html>

<?php
close_db($db);
?>
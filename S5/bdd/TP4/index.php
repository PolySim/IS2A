<DOCTYPE html>
    <html>
    <head>
        <title>TP4</title>
        <link rel="stylesheet" type="text/css" href="index.css">
        <link rel="stylesheet" type="text/css" href="output.css">
    </head>
    <body class="mx-auto px-8 max-w-6xl">
    <header class="flex gap-4 justify-center py-8">
        <h1 class="font-bold text-4xl">Mediatheque</h1>
    </header>

    <?php
    require('./utils/db.php');
    require('./action/films.php');
    require('./action/abonne.php');
    require('./action/dvd.php');
    require('./component/table.php');
    require('./component/inputText.php');
    require('./component/button.php');
    require('./component/select.php');
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
            <h3 class="text-xl font-semibold w-fit">Liste des films</h3>
            <?php
            table(format_films_to_table($films), ['Titre', 'Réalisateur', 'Année de production', 'Categorie']);
            ?>
        </div>

        <div class="flex flex-col gap-4 w-fit">
            <h3 class="text-xl font-semibold w-fit">Liste des abonnes</h3>
            <?php
            table(format_abonne_to_table($abonnes), ['Nom', 'Prenom']);
            ?>
        </div>
    </div>

    <div class="flex flex-col gap-8 mt-16">
        <h3 class="font-semibold text-xl">Emprunter un film (text)</h3>
        <div class="flex gap-4 w-full max-w-2xl">
            <script>
                const filmIsInList = (film, films) => {
                    return films.some(f => f.titre.toLowerCase() === film.toLowerCase());
                }

                const abonneIsInList = (abonne, abonnes) => {
                    return abonnes.some(a => a.nomab.toLowerCase() === abonne.toLowerCase());
                }

                const handleSubmitText = (e) => {
                    const form = e.target;
                    const formData = new FormData(form);
                    const data = Object.fromEntries(formData.entries());
                    console.log(data);
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
            <form action="action/emprunt.php" method="get" class="grid grid-cols-3 gap-4 items-end w-full"
                  onsubmit="handleSubmitText(event)">
                <?php
                InputText('Nom', 'nom', '', 'Nom', "", true);
                InputText('Film', 'film', '', 'Film', "", true);
                Button('submit', 'Emprunter', 'bg-blue-500 hover:bg-blue-700 transition text-white rounded-md p-2');
                ?>
            </form>
        </div>
    </div>

    <div class="flex flex-col gap-8 mt-8">
        <h3 class="font-semibold text-xl">Emprunter un film (select)</h3>
        <div class="flex gap-4 w-full max-w-2xl">
            <form action="action/emprunt.php" method="get" class="grid grid-cols-3 gap-4 items-end w-full"
                  onsubmit="handleSubmitText(event)">
                <?php
                function film_to_option($film)
                {
                    return [
                        'value' => $film['titre'],
                        'label' => $film['titre']
                    ];

                }

                function abonne_to_option($abonne)
                {
                    return [
                        'value' => $abonne['nomab'],
                        'label' => $abonne['nomab'] . ' ' . $abonne['prenomab']
                    ];
                }

                Select(array_map(function ($abonne) {
                    return abonne_to_option($abonne);
                }, $abonnes), 'nom', 'Nom', '', true);
                Select(array_map(function ($film) {
                    return film_to_option($film);
                }, $films), 'film', 'Film', '', true);
                Button('submit', 'Emprunter', 'bg-blue-500 hover:bg-blue-700 transition text-white rounded-md p-2');
                ?>
            </form>
        </div>
    </div>

    <footer class="mt-16"></footer>
    </body>
    </html>

<?php
close_db($db);
?>
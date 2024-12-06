<?php
function getAbonnes($db)
{
    $result = pg_query($db, "select * from abonne;");
    if (!$result) {
        return [];
    }
    return pg_fetch_all($result);
}

function nb_film_emprunter($db, $abonne)
{
    $result = pg_query($db, "select count(emprunt.*) from abonne join emprunt using (nab) where nomab = '$abonne';");
    if (!$result) {
        return 0;
    }
    return pg_fetch_all($result)[0]['count'];
}

?>
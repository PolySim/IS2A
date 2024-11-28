<?php
function getFilms($db)
{
    $result = pg_query($db, "select * from film join categorie using (ncat);");
    if (!$result) {
        return [];
    }
    return pg_fetch_all($result);
}

?>
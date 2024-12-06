<?php
function dvdAvailable($db, $titre)
{
    $result = pg_query($db, "select * from dvd left join emprunt using (ndvd) where nfilm = (select nfilm from film where titre = '$titre') and datefin is null;");
    if (!$result) {
        return false;
    }
    return pg_fetch_all($result);
}

function updateEmpruntDvd($db, $dvd, $abonne)
{
    $result = pg_query($db, "select nab from abonne where nomab = '$abonne';");
    if (!$result) {
        return false;
    }
    $nab = pg_fetch_all($result)[0]['nab'];
    $result = pg_query($db, "insert into emprunt values ('$nab', '$dvd', current_date, null);");
    if (!$result) {
        return false;
    }
    return true;
}

?>
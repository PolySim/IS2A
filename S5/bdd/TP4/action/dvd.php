<?php
function dvdIsAvailable($db, $titre)
{
    $result = pg_query($db, "select * from dvd left join emprunt using (ndvd) where nfilm = (select nfilm from film where titre = $titre) and datefin is null;");
    if (!$result) {
        return false;
    }
    return pg_num_rows($result) !== 0;
}

?>
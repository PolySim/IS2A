<?php
function getAbonnes($db)
{
    $result = pg_query($db, "select * from abonne;");
    if (!$result) {
        return [];
    }
    return pg_fetch_all($result);
}

?>
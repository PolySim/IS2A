<?php
function format_abonne_to_table($abonnes)
{
    $abonnes_formatted = array();
    foreach ($abonnes as $abonne) {
        $abonne_formatted = array();
        $abonne_formatted['Nom'] = $abonne['nomab'];
        $abonne_formatted['Prenom'] = $abonne['prenomab'];
        $abonnes_formatted[] = $abonne_formatted;
    }
    return $abonnes_formatted;
}

?>
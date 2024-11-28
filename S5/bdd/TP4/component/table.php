<?php
function table($data, $headers)
{
    echo "<table class='w-fit'>";
    echo "<thead>";
    echo "<tr>";
    foreach ($headers as $header) {
        echo "<td>$header</td>";
    }
    echo "</tr>";
    echo "</thead>";
    echo "<tbody>";
    $index = 0;
    foreach ($data as $row) {
        if ($index % 2 == 0) {
            echo "<tr class='bg-gray-100'>";
        } else {
            echo "<tr>";
        }
        foreach ($row as $cell) {
            echo "<td>$cell</td>";
        }
        echo "</tr>";
        $index++;
    }
    echo "</tbody>";
    echo "</table>";
}

?>
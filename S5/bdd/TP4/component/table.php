<?php
function table($data, $headers)
{
    ?>
    <table class='w-fit'>
        <thead>
        <tr>
            <?php
            foreach ($headers as $header) {
                ?>
                <td><?= $header ?></td>
                <?php
            }
            ?>
        </tr>
        </thead>
        <tbody>
        <?php
        $index = 0;
        foreach ($data as $row) {
            if ($index % 2 == 0) {
                ?>
                <tr class='bg-gray-100'>
            <?php } else { ?>
                <tr>
            <?php }
            foreach ($row as $cell) {
                ?>
                <td><?= $cell ?></td>
                <?php
            }
            ?>
            </tr>
            <?php
            $index++;
        }
        ?>
        </tbody>

    </table>
    <?php
}

?>
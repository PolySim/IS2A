<?php
function Button($type, $text, $class)
{
    ?>
    <button
            type='<?= $type ?>'
            class='<?= $class ?>'>
        <?= $text ?>
    </button>
    <?php
}

?>
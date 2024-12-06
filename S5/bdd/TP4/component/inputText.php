<?php
function InputText($label, $name, $value, $placeholder, $class = "", $isRequired = false)
{
    ?>
    <div class='flex flex-col gap-4'>
        <label for='<?= $name ?>'><?= $label ?></label>
        <input
                class='p-2 rounded-md border-gray-500 border <?= $class ?>'
                type='text'
                name='<?= $name ?>'
                value='<?= $value ?>'
                placeholder='<?= $placeholder ?>'
                required='<?= $isRequired ?>'
        />
    </div>
    <?php
}

?>
<?php
function Select($options, $name, $label, $class = "", $isRequired = false)
{
    ?>
    <div class='flex flex-col gap-4'>
        <label for="<?= $name ?>"><?= $label ?></label>
        <select
                name="<?= $name ?>"
                class="p-2 rounded-md border-gray-500 border <?= $class ?>"
                required="<?= $isRequired ?>"
        >
            <?php
            foreach ($options as $option) {
                ?>
                <option value="<?= $option["value"] ?>"><?= $option["label"] ?></option>
                <?php
            }
            ?>
        </select>
    </div>
    <?php
}

?>
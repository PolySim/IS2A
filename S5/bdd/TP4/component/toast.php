<?php
function Toast($variant, $message)
{
    ?>
    <script>
        setTimeout(() => {
            document.querySelector('[data-name="toast"]').classList.remove('opacity-100');
            document.querySelector('[data-name="toast"]').style.opacity = '0';
        }, 3000);
    </script>
    <div data-name="toast"
         class="flex justify-center items-center transition bg-<?= $variant ?>-500 text-white p-4 rounded-md fixed w-96 max-w-[90vw] bottom-4 right-4">
        <?php echo $message; ?>
    </div>
    <?php
}

?>
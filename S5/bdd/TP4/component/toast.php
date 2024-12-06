<?php
function Toast($variant, $message)
{
    ?>
    <script>
        setTimeout(() => {
            document.querySelector('[data-name="toast"]').classList.remove('opacity-100');
            document.querySelector('[data-name="toast"]').style.opacity = '0';
        }, 3000);
        const timeoutDelete = setTimeout(() => {
            document.querySelector('[data-name="toast"]').remove();
        }, 3500);
        const onDelete = () => {
            clearTimeout(timeoutDelete);
            document.querySelector('[data-name="toast"]').remove();
        }
    </script>
    <div data-name="toast"
         class="flex justify-center items-center transition bg-<?= $variant ?>-500 text-white p-4 rounded-md fixed w-96 max-w-[90vw] bottom-4 right-4">
        <div class="absolute top-2 right-2 cursor-pointer" onclick="onDelete()">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                 class="lucide lucide-x w-6 h-6">
                <path d="M18 6 6 18"/>
                <path d="m6 6 12 12"/>
            </svg>
        </div>
        <?php echo $message; ?>
    </div>
    <?php
}

?>
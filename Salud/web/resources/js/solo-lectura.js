/*
 * Si el usuario es de sólo lectura, desactiva botones
 *   AUTHOR: Mario Márquez
 */

function deshabilitarUsuarioLectura() {
    if ($('#bandera-usuario-solo-lectura').html() === "Solo lectura") {
        $('a.solo-lectura').removeClass('fancybox');
        $('a.solo-lectura').removeClass('fancybox.iframe');
        $('a.solo-lectura').attr('title', 'Función no disponible para el usuario actual');
        $('a.solo-lectura').prop('onclick', null).off('click');
        $('a.solo-lectura').attr('href', '');
        $("a.solo-lectura").prop('disabled', true);
        $('a.solo-lectura').on('click', function (event) {
            event.preventDefault();
        });
        $("input.solo-lectura").prop('disabled', true);
        $("button.solo-lectura").prop('disabled', true);
        $("button.solo-lectura").parents("form").attr('action', '');
        $('button.solo-lectura').attr('title', 'Función no disponible para el usuario actual');
    }
}


/*
 *   AUTHOR: Gustavo Adolfo Alamillo Medina
 */

// Inicialización
(function () {
    $.ajaxSetup({cache: true});
    $.getScript("/vendors/nifty-modal-js/js/min/jquery.modalEffects.min.js", function (data, textStatus, jqxhr) {
        $.getScript("/vendors/nifty-modal-js/js/min/modernizr.custom.min.js", function (data, textStatus, jqxhr) {
            var css_link = $("<link>", {
                rel: "stylesheet",
                type: "text/css",
                href: "/vendors/nifty-modal-js/css/nifty-component.min.css"
            });
            css_link.appendTo('head');
            css_link = $("<link>", {
                rel: "stylesheet",
                type: "text/css",
                href: "/resources/css/modals.min.css"
            });
            css_link.appendTo('head');

            $.ajaxSetup({cache: false});
        });
    });
})();

//Variables del plugin
var modalIdCount = 0;
var modals = [];
var exito = false;

// DATOS DEL MODAL
var titulo;
var cuerpo;
var tipo;
var funcionAceptar;
var funcionCancelar;
var funcionCerrar;
var funcionAfterCerrar;
var tache;
var sePuedeCerrar;
var cerrarEnOverlay;
var botonContinuar;

function ModalGenerator() {}

function cleanParams() {
    titulo = undefined;
    cuerpo = undefined;
    tipo = undefined;
    funcionAceptar = undefined;
    funcionCancelar = undefined;
    funcionCerrar = undefined;
    funcionAfterCerrar = undefined;
    tache = undefined;
    sePuedeCerrar = undefined;
    cerrarEnOverlay = undefined;
    botonContinuar = undefined;
}
/**
 * @param jsonConfiguracion Parámetros para configurar el modal. 
 * <ol>
 *  <li><b>titulo:</b> Título que tendrá el modal.</li>
 *  <li><b>cuerpo:</b> Texto  que tendrá el modal en el cuerpo.</li>
 *  <li><b>tipo:</b> Estilo que adoptará el modal {INFO,WARNING,ALERT,PRIMARY,SUCCESS,DEFAULT}.</li>
 *  <li><b>funcionAceptar:</b> Función que se ejecutará si el usuario <i>confirma</i> la acción en el modal.</li>
 *  <li><b>funcionCancelar:</b> Función que se ejecutará si el usuario <i>cancela</i> la acción en el modal.</li>
 *  <li><b>funcionAfterCerrar:</b> Función que se ejecutará cuando el usuario <i>cierre</i> el modal y se haya tenido exito en la operación.</li>
 *  <li><b>sePuedeCerrar:</b> Indica si el modal tendrá la opción de cerrarse.</li>
 *  <li><b>tache:</b> Indica si el modal tendrá tache.</li>
 *  <li><b>cerrarEnOverlay:</b> Indica si el modal se puede cerrar haciendo click en el overlay.</li>  
 *  <li><b>botonContinuar:</b> Indica si el modal tiene boton de continuar, si es undefined lo tiene.</li>  
 * </ol>
 */
ModalGenerator.notificacion = function (jsonConfiguracion) {
    cleanParams();
    initParams(jsonConfiguracion);
    addElemtHtml();
    setTimeout(function () {
        $("#modal-btn-" + modalIdCount).removeAttr("style");
        $("#modal-btn-" + modalIdCount).css("display", "none");
        $("#modal-btn-" + modalIdCount).click();
        modalIdCount++;
    }, 10);
};



function initParams(json) {
    sePuedeCerrar = true;
    if (json !== undefined) {

        if (json.tipo !== undefined) {
            tipo = json.tipo;
        }

        if (json.titulo !== undefined) {
            titulo = json.titulo;
        }

        if (json.cuerpo !== undefined) {
            cuerpo = json.cuerpo;
        }

        if (json.funcionAceptar !== undefined) {
            funcionAceptar = json.funcionAceptar;
        }

        if (json.funcionCancelar !== undefined) {
            funcionCancelar = json.funcionCancelar;
        }

        if (json.sePuedeCerrar !== undefined) {
            sePuedeCerrar = json.sePuedeCerrar;
        }

        if (json.funcionCerrar !== undefined) {
            funcionCerrar = json.funcionCerrar;
        }
        
        if (json.funcionAfterCerrar !== undefined) {
            funcionAfterCerrar = json.funcionAfterCerrar;
        }
        
        if (json.tache !== undefined) {
            tache = json.tache;
        }
        
        if (json.cerrarEnOverlay !== undefined){
            cerrarEnOverlay = json.cerrarEnOverlay;
        }
        
        if (json.botonContinuar !== undefined){
            botonContinuar = json.botonContinuar;
        }
    }

    if (titulo === undefined) {
        titulo = "Titulo de la notificación";
    }

    if (cuerpo === undefined) {
        cuerpo = "Cuerpo de la notificación";
    }

    var closeButton = "";
    if (sePuedeCerrar && tache === undefined) {
        closeButton = "<button class='md-close close'>&times;</button>";
    } else {
        closeButton = "<button class='md-close close' style='display:none;'>&times;</button>";
    }

    titulo = closeButton + titulo;
}

function  addElemtHtml() {
    $("body").append($("<div class='md-overlay'></div>"));


    var parent = $("<div id='modal-id-" + modalIdCount + "' class='md-modal md-effect-7'></div>");
    var content = $("<div class='md-content'></div>");
    var header = $("<div class='modal-header'></div>");
    var body = $("<div class='modal-body'></div>");
    var footer = $("<div class='modal-footer'></div>");
    var button = $("<button id='modal-btn-" + modalIdCount + "' class='md-trigger' data-modal='modal-id-" + modalIdCount + "' data-backdrop='" + cerrarEnOverlay + "'/>");

    switch (tipo) {
        case "INFO":
            header.addClass("info-head");
            break;
        case "WARNING":
            header.addClass("warning-head");
            break;
        case "ALERT":
            header.addClass("alert-head");
            break;
        case "PRIMARY":
            header.addClass("primary-head");
            break;
        case "SUCCESS":
            header.addClass("success-head");
            break;
        default:
            header.addClass("default-head");
            break;
    }

    parent.append(content);
    content.append(header);
    content.append(body);
    $("body").prepend(parent);

    if (sePuedeCerrar) {
        if (funcionAceptar === undefined) {
            footer.append("<button type='button' class='btn-modal' onClick='cerrarModal()'>Continuar</button>");
        } else {
            if (funcionCancelar === undefined) {
                footer.append("<button type='button' class='btn-modal' onClick='cerrarModal()'>Cancelar</button>");
            } else {
                footer.append("<button type='button' class='btn-modal' onClick='cancelarAction()'>Cancelar</button>");
            }

            footer.append("<button type='button' class='btn-modal' onClick='confirmarAction()' >Aceptar</button>");
        }
    }
    
    if(botonContinuar){
        footer.append("<button type='button' class='btn-modal' onClick='cerrarModal()'>Continuar</button>");   
    }

    content.append(footer);
    header.append(titulo);
    body.append(cuerpo);
    $("body").append(button);
    $(".md-trigger").modalEffects({
        afterClose: function (button, modal) {
            $(".md-overlay").remove();
            modals.pop().remove();
            return exito;
        }
    });

    modals.push(parent);
}

function cancelarAction() {
    cerrarModal();
    funcionCancelar();
}

function confirmarAction() {
    $(".btn-modal").attr("disabled", "disabled");
    exito = true;
    funcionAceptar();
    cerrar();
}

function cerrarModal() {
    cerrar();
    exito = false;
}

ModalGenerator.cerrarModales = function () {
    cerrar();
};

function cerrar() {
    if (funcionCerrar !== undefined) {
        funcionCerrar();
    }
    $(".md-close").click();
    if (funcionAfterCerrar !== undefined && exito === true) {
                funcionAfterCerrar();
    }
}
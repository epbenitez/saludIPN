/*
 *   AUTHOR: Gustavo Adolfo Alamillo Medina
 */

var tablas = {};
var rowIdGlobal;

// Inicialización
(function () {
    var cargarAlIniciar = document.currentScript.getAttribute('data-init');
    var divId = document.currentScript.getAttribute('data-div');
    var source = document.currentScript.getAttribute('data-ajax');
    var functionName = document.currentScript.getAttribute('data-function');
    var varNameButton = document.currentScript.getAttribute('data-button');
    var longitud = document.currentScript.getAttribute('data-length');
    var escrollX = document.currentScript.getAttribute('data-scrollX');

    $.ajaxSetup({cache: true});//Indica si el navegador debe almacenar en cache las paginas solicitadas
    $.getScript("/vendors/datatables/datatables.min.js", function (data, textStatus, jqxhr) {//By default, $.getScript() sets the cache setting to false.
        $.ajaxSetup({cache: true});
        $.getScript("/vendors/datatables/select_pagination.min.js", function (data, textStatus, jqxhr) {
            var css_link = $("<link>", {
                rel: "stylesheet",
                type: "text/css",
                href: "/vendors/datatables/datatables.min.css"
            });
            css_link.appendTo('head');
            css_link = $("<link>", {
                rel: "stylesheet",
                type: "text/css",
                href: "/resources/css/customTables.min.css"
            });
            css_link.appendTo('head');

            css_link = $("<link>", {
                rel: "stylesheet",
                type: "text/css",
                href: "/resources/css/font_login.min.css"
            });
            css_link.appendTo('head');
            if (cargarAlIniciar) {
                generarTabla(divId, source, functionName, false, varNameButton, longitud, escrollX);
                //console.log('Se genero la tabla en la inicializacion');
            }

        });
    });
    //console.log('function: ' + functionName);
    //console.log('Este primerisimo');
})();
/*
 * GLOBALES
 */

function generarTabla(rowId, source, funcionDraw, esPaginadaDB, jsonBotones, longitud, escrollX) {

    if (!$("#" + arguments[0]).length) {
        throw new Error("No existe el row con ID: " + arguments[0]);
    }

    if (typeof source === 'undefined') {
        throw new Error("No se ha especificado alguna direccion ajax");
    }

    if (typeof esPaginadaDB === 'undefined') {
        esPaginadaDB = true;
    }

    rowIdGlobal = rowId;
//    FIX 24/04/17 - La biblioteca generador-tablas tiene dos métodos para trabajar 
//    con datos: cuando los datos provienen de una respuesta ajax y cuando son datos 
//    contenidos en un arreglo; la función espera una URL ajax de dónde obtener 
//    los datos, o toma el contenido de un arreglo para colocarlo en la tabla. 
//    El problema es que se interpretaban todos los datos como si fueran cadenas
//    (URLs ajax) y no se utilizaban los datos de un arreglo, si no que 
//    tomaba el nombre de la variable que contenía el arreglo y la entendía como 
//    una URL trunca, inservible. Se cambió la manera en que comprueba si es un 
//    arreglo o una URL. Ahora toma la cadena "origen de datos", busca un 
//    elemento que se llame como la cadena, y revisa si es un arreglo. Así, si 
//    es una URL, no encontrará una variable que se llame como la URL, y la 
//    tomará como URL. Y si encuentra un elemento, lo interpretará como arreglo.
    if ($.isArray(window[source]) || !(rowId in tablas)) {
        if ($.isArray(window[source])) {
            source = window[source];
        }
        
        var jsonTabla = generarJSONTabla(rowId, source, esPaginadaDB, funcionDraw, jsonBotones, longitud, escrollX);
        crearTabla(rowId, source, jsonTabla);
    } else {
        if ($.isArray(source)) {
            var idTabla = $("#" + rowId + " table").eq(1).attr("id");
            var objetoTabla = $("#" + idTabla).DataTable();
            objetoTabla.clear();
            objetoTabla.rows.add(source);
            objetoTabla.draw();
        } else {
            crearTabla(rowId, source, undefined, jsonBotones);
        }
    }

    return tablas[rowIdGlobal];
}

function crearTabla(rowId, source, jsonTabla, jsonBotones) {

    if (jsonTabla === undefined) {
        //console.log("RowID crear(jsonTabla existente para tablas generadas con ajax y no con un array de json's): " + rowId);
        tablas[rowId].buttons().remove();
        //jquery each
        $.each(jsonBotones, function (i, e) {
            tablas[rowId].button().add(i, e);
        });
        tablas[rowId].ajax.url(source).load();
        // Reemplaza botones por imágenes
        ponerImagenEnBtns();
    } else {
        var tablaId = $("#" + rowId + " table").attr("id");
        appendLoadingDiv(rowId);
        appendFooter(rowId);
        appendHeader(rowId);
        ponerClasesEnEncabezados(tablaId);
        //console.log("RowID crear(Tabla nueva): " + rowId);
        tablas[rowId] = $("#" + tablaId).on('init.dt', function () {
            //Reacomodar número a mostar en el div correspondiente
            $("#lengthMenuDiv-" + rowId).append($("#" + tablaId + "_length"));
            $("#" + tablaId + "_length").addClass("input-group")
                    .prepend("<span class='input-group-addon' style='width:0px; padding-left:0px; padding-right:0px; border:none;'></span>")
                    .prepend("<span class='input-group-addon' id='priceLabel'>Resultados:</span>");

            //Reacomodar botones en el div correspondiente
            $("#buttonsDiv-" + rowId).append($("#grupoBotones-" + rowId));
            $("#grupoBotones-" + rowId).css("padding-right", "0px");
            $(".dt-buttons").addClass("pull-right");

            //Acomodar paginación
            $("select[name='" + tablaId + "_length']").addClass("form-control").unwrap();
            $("#divInfoPaginacion-" + rowId).append($("#" + tablaId + "_info"));
            $("#" + tablaId + "_info").removeClass("dataTables_info");
            $("#divSelectPaginacion-" + rowId).append($("#" + tablaId + "_paginate"));
            $(".table-footer").show();

            // Reemplaza botones por imágenes
            ponerImagenEnBtns();

            //Acomodar cuadro de búsqueda
            $("#" + tablaId + "_filter").appendTo("#" + rowId + "_searchBox");
            $("#" + tablaId + "_filter label input").attr("placeholder", "Buscar elementos ...")
                    .css("margin-left", "0px");

        }).on('draw.dt', function () {
            tablas[rowIdGlobal].columns.adjust();
            unwrapButtons();
            deshabilitarUsuarioLectura();

        }).on('preXhr.dt', function (e, settings, data) {
            $("#loading-row-" + rowId).show();
            $("#" + rowId).hide();
            $("#paginateFooter-" + rowId + ",#dataTableHeader-" + rowId).hide();
        }).on('xhr.dt', function (e, settings, json, xhr) {
            $("#" + rowId).show();
            $("#paginateFooter-" + rowId + ",#dataTableHeader-" + rowId).show();
            $("#loading-row-" + rowId).hide();
        }).DataTable(jsonTabla);
        $.ajaxSetup({cache: false});
        //console.log('JsonBotones en crear:' + jsonBotones);
        //console.log('Este despues (Crear)');
    }
}

function ponerImagenEnBtns() {
    //Si botones pdf, poner imagen
    if ($(".buttons-pdf").length > 0) {
        var butttonPdf = $(".buttons-pdf");
        butttonPdf.children("span").remove();
        butttonPdf.removeClass("btn btn-default");
        butttonPdf.append("<img src='/resources/img/modules/pdf.svg' width='35' style='padding-bottom:13px;'/>");
    }

    //Si boton excel, poner imagen
    if ($(".buttons-excel").length > 0) {
        var butttonExl = $(".buttons-excel");
        butttonExl.children("span").remove();
        butttonExl.removeClass("btn btn-default");
        butttonExl.append("<img src='/resources/img/modules/excel.svg' width='35' style='padding-bottom:13px;'/>");
    }

    //Si boton csv, poner imagen
    if ($(".buttons-csv").length > 0) {
        var butttonCsv = $(".buttons-csv");
        cambiarCssDeBtn(butttonCsv);
        //Se evita que la imagen del enlace se agregue dos veces o la cantidad de botones tipo csv en el documento
        //Se debe agregar esto en los demas tipos de botones si llega a presentarse la duplicacion de imagenes.
        //Trabajo futuro, hacer que la imagen se agregue solo al enlace en cuestion y no a todos en la clase .buttons-csv
        if (butttonCsv.children("img").length < 1) {
            butttonCsv.append("<img src='/resources/img/modules/excel.svg' width='35' style='padding-bottom:13px;'/>");
        }
    }

    //Si boton requisitos, poner imagen
    if ($(".boton-requisitos").length > 0) {
        var butttonReq = $(".boton-requisitos");
        cambiarCssDeBtn(butttonReq);
        //Se evita que la imagen del enlace se agregue dos veces o la cantidad de botones tipo requisitos en el documento
        //Se debe agregar esto en los demas tipos de botones si llega a presentarse la duplicacion de imagenes.
        if (butttonReq.children("img").length < 1) {
            butttonReq.append("<img src='/resources/img/asignacion_becas/requisitos.png' height='50px' style='padding-bottom:13px;'/>");
        }
    }
    //Si boton presupuesto, poner imagen
    if ($(".boton-presupuesto").length > 0) {
        var butttonPre = $(".boton-presupuesto");
        cambiarCssDeBtn(butttonPre);
        //Se evita que la imagen del enlace se agregue dos veces o la cantidad de botones tipo presupuesto en el documento
        //Se debe agregar esto en los demas tipos de botones si llega a presentarse la duplicacion de imagenes.
        if (butttonPre.children("img").length < 1) {
            butttonPre.append("<img src='/resources/img/asignacion_becas/presupuesto.png' width='35px' style='padding-bottom:13px;'/>");
        }
    }
}

function cambiarCssDeBtn(btn) {
    btn.children("span").remove();
    btn.removeClass("btn btn-default btn-primary");
    btn.css("margin-right", "5px");
    btn.css("margin-left", "15px");
    btn.css("margin-bottom", "15px");
    btn.css("padding-left", "15px");
    btn.css("border-left", "2px solid #DEDFE0");
}

function unwrapButtons() {
    $.each($("#grupoBotones-" + rowIdGlobal + " div a span a"), function (index, element) {
        $(element).unwrap().unwrap();
    });
}

function generarJSONTabla(rowId, source, esPaginada, funcionDraw, jsonBotones, longitud, escrollX) {
    var tablaId = $("#" + rowId + " table").attr("id");
    jsonTabla = {};
    if ($.isArray(source)) {
        jsonTabla.data = source;
    } else {
        if (esPaginada) {
            jsonTabla.processing = true;
            jsonTabla.serverSide = true;
            jsonTabla.sAjaxSource = source;
        } else {
            jsonTabla.ajax = source;
        }
    }
    jsonTabla.pagingType = "input";
    jsonTabla.dom = 'ltpir';
    //console.log("jsonBotones (JSONTabla):" + jsonBotones);

    if (jsonBotones !== null && jsonBotones !== undefined) {
        if (typeof jsonBotones === "string") {
            //console.log("window[jsonbotones] (JSONTabla):" + window[jsonBotones]);
            jsonBotones = window[jsonBotones];
        }
        jsonTabla.dom = '<"#grupoBotones-' + rowId + '.col-lg-12"B>ltpir';
        jsonTabla.buttons = jsonBotones;
    }

    if (!esPaginada) {
        jsonTabla.dom += "f";
        jsonTabla.lengthMenu = [[10, 50, 100, 500, 1000, -1], [10, 50, 100, 500, "1,000", "Todos"]];
    } else {
        jsonTabla.lengthMenu = [[10, 50, 100, 500, 1000, 5000, 10000], [10, 50, 100, 500, "1,000", "5,000", "10,000"]];
    }

    jsonTabla.searching = true;
    jsonTabla.language = {url: "/vendors/datatables/Spanish_1.10.12.json"};
    if (typeof funcionDraw !== "function") {
        //console.log("La funcionDraw no es una funcion y es:" + funcionDraw);
        funcionDraw = window[funcionDraw];
    }
    jsonTabla.fnDrawCallback = funcionDraw;
    jsonTabla.columns = calcularOrders(tablaId);
    jsonTabla.columnDefs = calcularColumnsDefs(tablaId);
    jsonTabla.aaSorting = [];
    jsonTabla.order = calcularOrdenamientos(tablaId);
    if (escrollX) {
        jsonTabla.scrollX = true;
    }
    // FIX 06/04/17 - Dentro de generador-tablas, en la línea 68, existía una 
    // comprobación que asignaba 10 a "longitud". Esto era cuando no había sido 
    // especificado un valor de longitud desde el documento que invocase la 
    // creación de una tabla; sin embargo no es necesario, pues "pageLength" 
    // toma el primer valor del arreglo "lengthMenu". 
    if (longitud !== undefined && longitud !== null) {
        jsonTabla.pageLength = Number(longitud);
    }
    //console.log(jsonTabla);
    return jsonTabla;
}

function ponerClasesEnEncabezados(tablaId) {
    var colors = ["#50AFB3", "#435493", "#2C84BC", "#6E6DA9", "#3186C1"];
    var i = 0;

    $.each($("#" + tablaId + " thead tr th"), function (index, value) {
        if (i === 5)
            i = 0;
        $(this).css("border-bottom", "2px solid " + colors[i++]);
    });
}

function calcularOrdenamientos(tablaId) {
    var ordenamientoDefault = [];
    var lastDefaultIndex = undefined;
    var lasOrderDir = "asc";
    $.each($("#" + tablaId + " thead tr th"), function (index, values) {
        if ($(this).data("defaultOrder")) {
            lastDefaultIndex = index;
            lasOrderDir = $(this).data("defaultDir");
        }
    });
    if (lastDefaultIndex !== undefined) {
        ordenamientoDefault.push([lastDefaultIndex, lasOrderDir]);
    }
    return ordenamientoDefault;
}

function calcularOrders(tablaId) {
    var orderable = [];
    $.each($("#" + tablaId + " thead tr th"), function () {
        var obj = {};
        if ($(this).data("orderable")) {
            obj["orderable"] = true;
        } else {
            obj["orderable"] = false;
        }
        orderable.push(obj);
    });
    return orderable;
}

function calcularColumnsDefs(tablaId) {
    var columnDefs = [];
    var centerIndexes = [];
    var hiddenColumns = [];
    $.each($("#" + tablaId + " thead tr th"), function (index, value) {

        var center = $(this).data("center");
        if (center) {
            centerIndexes.push(index);
        }

        var hidden = $(this).data("hidden");
        if (hidden) {
            hiddenColumns.push(index);
        }
    });
    var objCenter = {};
    objCenter["className"] = "header-center";
    objCenter["targets"] = centerIndexes;
    columnDefs.push(objCenter);

    var objHidden = {};
    objHidden["visible"] = false;
    objHidden["targets"] = hiddenColumns;
    columnDefs.push(objHidden);

    return columnDefs;
}

function appendFooter(rowId) {
    $("#" + rowId).after("<div class='row' id='paginateFooter-" + rowId + "'><div class='col-xs-12'><div class='clearfix table-footer'><div id='divInfoPaginacion-" + rowId + "' class='col-xs-12 col-sm-7'></div><div id='divSelectPaginacion-" + rowId + "' class='col-xs-12 col-sm-5'></div></div></div></div");
}

function appendHeader(rowId) {
    var header = $("<div class='row' id='dataTableHeader-" + rowId + "'>" +
            "<div class='col-xs-12'>" +
            "<div id='lengthMenuDiv-" + rowId + "' class='col-xs-7 col-sm-4 col-md-3 col-lg-3' style='padding:0px;'></div>" +
            "<div id=\'" + rowId + "_searchBox' class='col-sm-3 col-md-3 col-lg-2'  style='padding:0px;'></div>" +
            "<div id='buttonsDiv-" + rowId + "' class='col-lg-7'></div>" +
            "</div>" +
            "</div>");
    $("#" + rowId).before(header);
}

function appendLoadingDiv(rowId) {
    $("#" + rowId).before("<div class='row' id='loading-row-" + rowId + "' style='display:none;'><div class='col-lg-12'><div class='main-box loading-gif'><center><img src='/resources/img/generadorTablas/carga-movil.gif'/></center></div></div></div>");
}

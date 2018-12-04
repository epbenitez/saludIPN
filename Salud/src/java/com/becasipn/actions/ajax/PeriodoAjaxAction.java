package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.Periodo;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class PeriodoAjaxAction extends JSONAjaxAction {

    public String listado() {
        List<Periodo> list = getDaos().getPeriodoDao().findAll();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        for (Periodo periodo : list) {
            getJsonResult().add("[\"" + periodo.getClave()
                    + "\", \"" + periodo.getDescripcion()
                    + "\", \"" + (periodo.getFechaInicial() == null ? "" : sdf.format(periodo.getFechaInicial()))
                    + "\", \"" + (periodo.getFechaFinal() == null ? "" : sdf.format(periodo.getFechaFinal()))
                    + "\", \"" + (periodo.getPeriodoAnterior() == null ? "" : periodo.getPeriodoAnterior().getClave())
                    + "\", \"" + (periodo.getPeriodoAnterior() == null ? "" : periodo.getCicloEscolar().getNombre())
                    + "\", \"" + (periodo.getEstatus() == Boolean.TRUE ? "<span class=\\\"label label-success\\\">Activo</span>" : "<span class=\\\"label label-primary\\\">Inactivo</span>")
                    + "\", \"<a title='Editar periodo' class='fancybox fancybox.iframe solo-lectura'  href='/catalogos/edicionPeriodo.action?periodo.id=" + periodo.getId() + "'>"
                    + "<span class=\\\"fa-stack\\\"><i class=\\\"fa fa-square fa-stack-2x\\\"></i> "
                    + "<i class=\\\"fa fa-pencil-square-o fa-stack-1x fa-inverse\\\"></i> "
                    + "</span>"
                    + "</a>"
                    + "\", \"<a title='Eliminar periodo' class=\\\"table-link danger solo-lectura\\\" href=\\\"#\\\" onclick='eliminar(" + periodo.getId() + ")'> "
                    + "<span class=\\\"fa-stack\\\"><i class=\\\"fa fa-square fa-stack-2x\\\"></i> "
                    + "<i class=\\\"fa fa-trash-o fa-stack-1x fa-inverse\\\"></i> "
                    + "</span>"
                    + "</a>\"]");
        }
        return SUCCESS_JSON;
    }
}

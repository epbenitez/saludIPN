package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.SolicitudBecaIndividual;
import com.becasipn.persistence.model.SolicitudBecaNivel;
import com.becasipn.persistence.model.SolicitudBecaUA;
import java.util.List;

/**
 *
 * @author Augusto H
 */
public class AccesosAjaxAction extends JSONAjaxAction {

    public String listadoUa() {
        List<SolicitudBecaUA> list = getDaos().getSolicitudBecaUADao().findActives();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        for (SolicitudBecaUA solicitud : list) {
            getJsonResult().add("[\"" + solicitud.getPeriodo().getDescripcion()
                    + "\", \"" + solicitud.getUnidadAcademica().getNombreCorto()
                    + "\", \"" + sdf.format(solicitud.getFechaInicio())
                    + "\", \"" + sdf.format(solicitud.getFechaFin())
                    + "\", \"<a title='Editar solicitud' class='fancybox fancybox.iframe solo-lectura' href='/admin/editarAccesoUAcademica.action?nuevoAcceso.id=" + solicitud.getId() + "'> <span class='fa-stack'> <i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"<a title='Eliminar solicitud' class='solo-lectura table-link danger' href='#' onClick='eliminarUa(" + solicitud.getId() + ");'> <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoNvl() {
        List<SolicitudBecaNivel> list = getDaos().getSolicitudBecaNivelDao().findActives();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        for (SolicitudBecaNivel solicitud : list) {
            getJsonResult().add("[\"" + solicitud.getPeriodo().getDescripcion()
                    + "\", \"" + solicitud.getNivel().getNombre()
                    + "\", \"" + sdf.format(solicitud.getFechaInicio())
                    + "\", \"" + sdf.format(solicitud.getFechaFin())
                    + "\", \"<a title='Editar solicitud' class='fancybox fancybox.iframe solo-lectura' href='/admin/editarAccesoNivel.action?nuevoAcceso.id=" + solicitud.getId() + "'> <span class='fa-stack'> <i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"<a title='Eliminar solicitud' class='solo-lectura table-link danger' href='#' onClick='eliminarNvl(" + solicitud.getId() + ");'> <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoAl() {
        List<SolicitudBecaIndividual> list = getDaos().getSolicitudBecaIndividualDao().getActives();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        for (SolicitudBecaIndividual solicitud : list) {
            getJsonResult().add("[\"" + solicitud.getPeriodo().getDescripcion()
                    + "\", \"" + solicitud.getAlumno().getBoleta()
                    + "\", \"" + solicitud.getAlumno().getFullName()
                    + "\", \"" + sdf.format(solicitud.getFechaInicio())
                    + "\", \"" + sdf.format(solicitud.getFechaFin())
                    + "\", \"<a title='Editar solicitud' class='fancybox fancybox.iframe solo-lectura' href='/admin/editarAccesoAlumno.action?nuevoAcceso.id=" + solicitud.getId() + "'> <span class='fa-stack'> <i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"<a title='Eliminar solicitud' class='solo-lectura table-link danger' href='#' onClick='eliminarAl(" + solicitud.getId() + ");'> <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\"]");
        }
        return SUCCESS_JSON;
    }

}

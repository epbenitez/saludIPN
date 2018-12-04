package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.TipoNotificacion;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Sistemas Augusto I. Hernández Ruiz
 */
public class TipoNotificacionAjaxAction extends JSONAjaxAction {

    public TipoNotificacionAjaxAction() {
    }

    public String listado() {
        List<TipoNotificacion> list = getDaos().getTipoNotificacionDao().findAll();
        Collections.sort(list);
        for (TipoNotificacion tipoNotificacion : list) {
            getJsonResult().add("[\"" + tipoNotificacion.getNombre()
                    + "\", \"<span class='fa-stack'> <i class='fa fa-circle fa-stack-2x' style='color:"+tipoNotificacion.getColor()+"'></i></span></a>"
                    + "\", \"<span class='fa-stack'> <i class='fa fa-square fa-stack-2x' style='color:"+tipoNotificacion.getColor()+"'></i> <i class='"+tipoNotificacion.getIcono()+" fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"<a title='Editar Tipo de Notificación' class='fancybox fancybox.iframe solo-lectura' href='/admin/edicionTipoNotificacion.action?tipoNotificacion.id=" + tipoNotificacion.getId() + "'> <span class='fa-stack'> <i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"<a title='Eliminar Notificación' class='table-link danger solo-lectura' href='#' onClick='eliminar(" + tipoNotificacion.getId() + ");' > <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i></span></a>"
                    + " \"]");
        }
        return SUCCESS_JSON;
    }

}

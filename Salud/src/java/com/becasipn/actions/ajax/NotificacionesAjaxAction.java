package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.Notificaciones;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Sistemas Augusto I. Hernández Ruiz
 */
public class NotificacionesAjaxAction extends JSONAjaxAction {

    public NotificacionesAjaxAction() {
    }

    public String listado() {
        List<Notificaciones> list = getDaos().getNotificacionesDao().findall();
        Collections.sort(list);
        for (Notificaciones notificaciones : list) {
            getJsonResult().add("[\"<p class='text-center'>" + notificaciones.getTitulo() + "</p>"
                    + "\", \"<p class='text-center'>" + trimmer(notificaciones.getNotificacion()).replaceAll("\"", "'").replaceAll("</p>", "</p><br>") + "</p>"
                    + "\", \"<p class='text-center'>" + Date_Format(notificaciones.getFechaInicio()) + "</p>"
                    + "\", \"<p class='text-center'>" + Date_Format(notificaciones.getFechaFinal()) + "</p>"
                    + "\", \"<p class='text-center'>" + notificaciones.getTipoNotificacion().getNombre() + "</p>"
                    + "\", \"<p class='text-center'>" + rolList(notificaciones) + "</p>"
                    //+ "\", \"" + notificaciones.getUsuario().getUsuario() + "</p>"
                    + "\", \"<a id=" + notificaciones.getId() + " title='Visualizar Notificación' class='table-link warning text-center' href='#' onClick='visualizar(" + notificaciones.getId() + ");' data='" + obtenerDatos(notificaciones) + "' ><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-eye fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"<a title='Editar Notificación' class='fancybox fancybox.iframe text-center solo-lectura' href='/admin/edicionNotificaciones.action?notificacion.id=" + notificaciones.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-pencil-square-o fa-stack fa-inverse'></i></span></a>"
                    + "\", \"<a title='Eliminar Notificación' class='table-link danger text-center solo-lectura' href='#' onClick='eliminar(" + notificaciones.getId() + ");'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i></span></a>"
                    + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String Date_Format(Date fecha) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String fecha_formato = df.format(fecha);
        return fecha_formato;
    }

    public String rolList(Notificaciones notificacion) {
        String list = "";
        List<String> lista = getDaos().getNotificacionRolDao().findallbynotificacion(notificacion);
        for (String element : lista) {
            list = list + ", " + element;
        }
        list = list.replaceFirst(",", "");
        return list;
    }

    public String trimmer(String list) {
        int maxLength = 140;
        if (list.length() > maxLength) {
            list = list.substring(0, maxLength) + "...";
            return list;
        } else {
            return list;
        }
    }

    public String obtenerDatos(Notificaciones notificacion) {
        String result = notificacion.getTipoNotificacion().getColor() + "^" + notificacion.getTitulo() + "^" + notificacion.getTipoNotificacion().getIcono() + "^" + notificacion.getNotificacion().replaceAll("\"", "").replaceAll("</p>", "</p><br>");
        return result;
    }
}

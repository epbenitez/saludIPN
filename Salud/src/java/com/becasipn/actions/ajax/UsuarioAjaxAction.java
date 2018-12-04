package com.becasipn.actions.ajax;

import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Victor Lozano
 */
public class UsuarioAjaxAction extends JSONAjaxAction {

    /**
     * Devuelve el listado de usuarios administrativos registrados en el sistema
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String listado() {
        List<PersonalAdministrativo> lista = getDaos().getPersonalAdministrativoDao().findAdministrativos();
        for (PersonalAdministrativo personal : lista) {
            getJsonResult().add("[\"<input type='checkbox' id ='"+personal.getId()+"' value='"+personal.getCorreoElectronico()+"' class='personal'>"
                    + "\", \"" + personal.getUsuario().getUsuario()
                    + "\", \"" + personal.getNombre() + " " + personal.getApellidoPaterno() + " " + personal.getApellidoMaterno()
                    + "\", \"" + getRol(personal.getUsuario().getPrivilegios())
                    + "\", \"" + personal.getCargo().getDescripcion()
                    + "\", \"" + personal.getUnidadAcademica().getNombreCorto()
                    + "\", \"<a class='fancybox fancybox.iframe solo-lectura'  title='Editar usuario' href='/admin/edicionUsuario.action?personalAdministrativo.id=" + personal.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"<a class='table-link danger solo-lectura' href='#' title='Eliminar usuario' onclick='eliminar(" + personal.getId() + ")'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i></span></a>\"]");
        }
        return SUCCESS_JSON;
    }

    public String getRol(Set<UsuarioPrivilegio> privilegios) {
        String list = "";
        for (UsuarioPrivilegio up : privilegios) {
            list = list + ", " + up.getPrivilegio().getClave();
        }
        list = list.replaceFirst(",", "");
        return list;
    }
}
 
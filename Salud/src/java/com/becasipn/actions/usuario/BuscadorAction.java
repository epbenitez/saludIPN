/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2016
 *
 */
package com.becasipn.actions.usuario;

import com.becasipn.actions.BaseAction;
import com.becasipn.persistence.model.Menu;
import com.becasipn.persistence.model.RelacionMenuRoles;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public class BuscadorAction extends BaseAction {

    public String keywords;
    public List<Menu> menu = new ArrayList<Menu>();

    public String busca() {
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        List<RelacionMenuRoles> menuroles = getDaos().getRelacionMenuRolesDao().buscadorPorRoles(u.getPrivilegios(), keywords);
        if (menuroles != null) {
            for (RelacionMenuRoles mr : menuroles) {
                menu.add(mr.getMenu());
            }
        }
        return INPUT;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

}

/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.MenuDao;
import com.becasipn.persistence.model.Menu;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

/**
 *
 * @author Patricia Benitez
 */
public class MenuJpaDao extends JpaDaoBase<Menu, BigDecimal> implements MenuDao {

    public MenuJpaDao() {
        super(Menu.class);
    }

    //SQLINJECTION OK
    @Override
    public String getNombreModulo(String action, StringBuilder menuActivo) {
        StringBuilder sb = new StringBuilder();
        
        if (action != null) {
            if (action.split("\\?").length >= 2) {
                action = action.split("\\?")[0];
            }
        }
        
        List<String> columnas = new ArrayList<>();
        columnas.add("am.descripcion");
        columnas.add("m.nombre");        
        
        sb.append(" select ");
        agregaColumnas(sb, columnas);
        sb.append(" FROM Menu m ");
        sb.append(" INNER JOIN RelacionMenuRoles mr ON m = mr.menu ");
        sb.append(" INNER JOIN mr.agrupador am ");
        sb.append(" WHERE m.url LIKE :action ");
        
        Query q = entityManager.createQuery(sb.toString());
        q.setParameter("action", "%" + action + "%");
        
        List<LinkedHashMap<String,Object>> result = creaModelo(q.getResultList(), columnas);

        if (result == null || result.isEmpty()) {
            return "";
        }

        Map menu = result.get(0);        
        menuActivo.append(String.valueOf(menu.get("am.descripcion")).replaceAll(" ", ""));
        return "<li>\n<a href=\"/index.action\">Inicio</a> </li>\n<li>\n\n<span>" + menu.get("am.descripcion") + "<span></li>\n <li class=\"active\">" + menu.get("m.nombre") + "</li>\n ";
    }
}

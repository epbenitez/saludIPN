package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.RelacionMenuRolesDao;
import com.becasipn.persistence.model.RelacionMenuRoles;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Query;

public class RelacionMenuRolesJpaDao extends JpaDaoBase<RelacionMenuRoles, BigDecimal> implements RelacionMenuRolesDao {

    /**
     * Crea una instancia de una <code>AlumnoJpaDao</code>.
     */
    public RelacionMenuRolesJpaDao() {
        super(RelacionMenuRoles.class);
    }

    /**
     * Obtiene las opciones del menú disponibles de acuerdo con un Rol de
     * usuario y las ordena
     *
     * @param roles Lista de roles del usuario
     * @return Cadena con formato HTML de las opciones del menú ordenada por
     * agrupadores
     */
    @Override
    public String findURLByRols(Set<UsuarioPrivilegio> roles) {
        Set<RelacionMenuRoles> menuList = new LinkedHashSet<RelacionMenuRoles>();
        StringBuilder menuString = new StringBuilder();
        menuString.append("<ul class=\"nav nav-pills nav-stacked\">");
        menuString.append("<li class=\"nav-header nav-header-first hidden-sm hidden-xs\">");
        menuString.append("Menú de navegación");
        menuString.append("</li>");
        Map secciones = new LinkedHashMap();
        Map iconos = new LinkedHashMap();
        Map menuOpc = new LinkedHashMap();
        List<RelacionMenuRoles> result = new LinkedList<RelacionMenuRoles>();

        for (UsuarioPrivilegio usuarioPrivilegio : roles) {
            String jpql = "SELECT menuRoles FROM RelacionMenuRoles menuRoles WHERE menuRoles.rol = ?1 AND menuRoles.activo = TRUE ORDER BY menuRoles.agrupador.orden ASC, menuRoles.orden ASC";
            Rol rol = usuarioPrivilegio.getPrivilegio();
            Query query = getEntityManager().createQuery(jpql);
            query.setParameter(1, rol);
            result = (List<RelacionMenuRoles>) query.getResultList();
            menuList.addAll(result);
        }

        if (menuList.size() > 0) {
            List idMenu = new LinkedList();
            for (RelacionMenuRoles menu : menuList) {
                if (menu.getAgrupador() != null && !idMenu.contains(menu.getMenu().getId())) {
                    String idSubMenu = String.valueOf(menu.getMenu().getNombre()).replaceAll(" ", "");
                    if (!secciones.containsKey(menu.getAgrupador().getId())) {
                        secciones.put(menu.getAgrupador().getId(), menu.getAgrupador().getDescripcion());
                    }
                    if (!iconos.containsKey(menu.getAgrupador().getId())) {
                        iconos.put(menu.getAgrupador().getId(), menu.getAgrupador().getIcono());
                    }

                    StringBuilder value = new StringBuilder();
                    if (menuOpc.containsKey(menu.getAgrupador().getId())) {
                        value.append(String.valueOf(menuOpc.remove(menu.getAgrupador().getId())));
                    }

                    value.append("<li id=\"").append(idSubMenu).append("\">");

                    value.append("<a href=\"");
                    value.append(menu.getMenu().getUrl());
                    value.append("\"title=\"");
                    value.append(menu.getMenu().getNombre());
                    value.append("\">").append(menu.getMenu().getNombre()).append("</a></li>");
                    menuOpc.put(menu.getAgrupador().getId(), value.toString());
                    idMenu.add(menu.getMenu().getId());
                }
            }
        }

        if (menuOpc.size() > 0) {
            Set keys = secciones.keySet();
            for (Object id : keys) {
                BigDecimal llave = (BigDecimal) id;
                if (menuOpc.containsKey(llave)) {
                    String idMenu = String.valueOf(secciones.get(llave)).replaceAll(" ", "");
                    menuString.append("<li>");
                    menuString.append("<a id=\"").append(idMenu).append("\" href=\"#\" class=\"dropdown-toggle\">");
                    menuString.append("<i class=\"fa ").append(String.valueOf(iconos.get(llave))).append(" \"></i>");
                    menuString.append("<span>").append(String.valueOf(secciones.get(llave))).append("</span>");
                    menuString.append("<i class=\"fa fa fa-angle-right drop-icon\"></i>");
                    menuString.append("</a>");
                    menuString.append("<ul class=\"submenu\">");
                    menuString.append(String.valueOf(menuOpc.get(llave)));
                    menuString.append("</ul>");
                    menuString.append("</li>");
                }
            }
        }
        menuString.append("</ul>");
        return menuString.toString();
    }

    @Override
    public String findURLESE() {
        String resultado = "";
        Map hdr = new LinkedHashMap();
        Map menuOpc = new LinkedHashMap();
        List<RelacionMenuRoles> result = new LinkedList<RelacionMenuRoles>();

        String jpql = "SELECT menuRoles FROM RelacionMenuRoles menuRoles WHERE menuRoles.rol.id = 2 AND menuRoles.activo = TRUE and menuRoles.menu.id in (1, 3, 19, 20, 29, 41, 66) ORDER BY menuRoles.agrupador.orden asc, menuRoles.orden desc";

        Query query = getEntityManager().createQuery(jpql);
        result = (List<RelacionMenuRoles>) query.getResultList();

        if (result.size() > 0) {
            List idMenu = new LinkedList();
            for (RelacionMenuRoles menu : result) {
                String idSubMenu = String.valueOf(menu.getMenu().getNombre()).replaceAll(" ", "");
                if (menu.getAgrupador() != null && !idMenu.contains(menu.getMenu().getId())) {
                    if (!hdr.containsKey(menu.getAgrupador().getId())) {
                        hdr.put(menu.getAgrupador().getId(), menu.getAgrupador().getDescripcion());
                    }
                    if (menuOpc.containsKey(menu.getAgrupador().getId())) {
                        String value = (String) menuOpc.remove(menu.getAgrupador().getId());
                        value += "\n<li id=\"" + idSubMenu + "\"><a href=\"" + menu.getMenu().getUrl() + "\" title=\"" + menu.getMenu().getNombre() + "\"><i class=\"" + menu.getMenu().getIcono() + "\"></i><span>" + menu.getMenu().getNombre() + "</span></a>" + "</li>";
                        menuOpc.put(menu.getAgrupador().getId(), value);
                    } else {
                        menuOpc.put(menu.getAgrupador().getId(), ("\n<li id=\"" + idSubMenu + "\"><a href=\"" + menu.getMenu().getUrl() + "\" title=\"" + menu.getMenu().getNombre() + "\"><i class=\"" + menu.getMenu().getIcono() + "\"></i><span>" + menu.getMenu().getNombre() + "</span></a>" + "</li>"));
                    }
                    idMenu.add(menu.getMenu().getId());
                }
            }
        }

        if (menuOpc.size() > 0) {
            Set keys = hdr.keySet();
            for (Object id : keys) {
                BigDecimal llave = (BigDecimal) id;
                if (menuOpc.containsKey(llave)) {
                    resultado += "<ul class=\"nav nav-pills nav-stacked\"><li class=\"nav-header  hidden-sm hidden-xs\">" + (String) hdr.get(llave) + (String) menuOpc.get(llave) + "</li></ul>";
                }
            }
        }

        return resultado;
    }

    @Override
    public List<RelacionMenuRoles> buscadorPorRoles(Set<UsuarioPrivilegio> roles, String busqueda) {
        StringBuilder rolesDeUsuario = new StringBuilder();
        for (UsuarioPrivilegio r : roles) {
            rolesDeUsuario.append(r.getPrivilegio().getId()).append(",");
        }
        List<RelacionMenuRoles> lista = new ArrayList<RelacionMenuRoles>();
        String jpql = "SELECT menuRoles FROM RelacionMenuRoles menuRoles "
                + " WHERE menuRoles.rol.id in( " + rolesDeUsuario.toString().substring(0, rolesDeUsuario.length() - 1) + ") "
                + " AND menuRoles.activo = TRUE "
                + " and UPPER(menuRoles.menu.palabrasClave) like UPPER('%" + busqueda + "%') "
                + " ORDER BY menuRoles.agrupador.orden asc, menuRoles.orden desc";

        Query query = getEntityManager().createQuery(jpql);
        lista = (List<RelacionMenuRoles>) query.getResultList();

        return lista.isEmpty() ? null : lista;
    }
}

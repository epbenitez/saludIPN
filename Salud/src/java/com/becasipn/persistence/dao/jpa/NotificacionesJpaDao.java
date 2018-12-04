package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.NotificacionesDao;
import com.becasipn.persistence.model.Notificaciones;
import com.becasipn.persistence.model.Rol;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Augusto H.
 */
public class NotificacionesJpaDao extends JpaDaoBase<Notificaciones, BigDecimal> implements NotificacionesDao {

    public NotificacionesJpaDao() {
        super(Notificaciones.class);
    }

    @Override
    public List<Notificaciones> findall() {
        List<Notificaciones> lista;
        String jpql = "select * from ENT_NOTIFICACIONES n order by n.FECHAINICIO ASC";
        lista = executeNativeQuery(jpql, Notificaciones.class);
        return lista;
    }

    @Override
    public List<Notificaciones> findNotificacionesByRol(Rol rol) {
        List<Notificaciones> lista;
        String jpql = "SELECT n FROM Notificaciones n"
                + " INNER JOIN NotificacionesRol nr ON"
                + " nr.notificacion.id = n.id"
                + " WHERE nr.rol.id = ?1 AND"
                + " (?2 BETWEEN n.fechaInicio and n.fechaFinal+1)"
                + " order by n.fechaFinal asc";
        lista = executeQuery(jpql, rol.getId(), new Date());
        return lista;
    }
}

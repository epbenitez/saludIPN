package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.NotificacionesRolDao;
import com.becasipn.persistence.model.Notificaciones;
import com.becasipn.persistence.model.NotificacionesRol;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sistemas DSE
 */
public class NotificacionesRolJpaDao extends JpaDaoBase<NotificacionesRol, BigDecimal> implements NotificacionesRolDao {

    public NotificacionesRolJpaDao() {
        super(NotificacionesRol.class);
    }

    @Override
    public List<NotificacionesRol> findall() {
        return entityManager.createQuery("select n from " + clase.getSimpleName() + " n").getResultList();
    }

    @Override
    public List<String> findallbynotificacion(Notificaciones notificacion) {
        return entityManager.createNativeQuery("select r.clave from CAT_ROL r\n"
                + "inner join RMM_NOTIFICACIONES_ROL nt on nt.ROL_ID = r.ID\n"
                + "inner join ENT_NOTIFICACIONES n on\n"
                + "n.ID = nt.NOTIFICACION_ID\n"
                + "where n.ID = " + notificacion.getId() + "").getResultList();
    }

    @Override
    public List<BigDecimal> rolesByNotificacionId(Notificaciones notificacion) {
        return entityManager.createNativeQuery("select r.id from cat_rol r\n"
                + "inner join RMM_NOTIFICACIONES_ROL cr on r.id = cr.ROL_ID\n"
                + "inner join ENT_NOTIFICACIONES ctn on cr.NOTIFICACION_ID = ctn.ID\n"
                + "where ctn.ID = " + notificacion.getId() + "").getResultList();
    }

    @Override
    public Boolean eliminaRegistrosAsociados(Notificaciones notificacion) {
        int count = entityManager.createNativeQuery("delete from RMM_NOTIFICACIONES_ROL"
                + " where NOTIFICACION_ID = " + notificacion.getId() + "").executeUpdate();

        return Boolean.TRUE;

    }

}

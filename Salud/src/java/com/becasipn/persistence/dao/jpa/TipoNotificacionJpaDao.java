package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.model.TipoNotificacion;
import com.becasipn.persistence.dao.TipoNotificacionDao;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sistemas DSE
 */
public class TipoNotificacionJpaDao extends JpaDaoBase<TipoNotificacion, BigDecimal> implements TipoNotificacionDao {

    public TipoNotificacionJpaDao() {
        super(TipoNotificacion.class);
    }

    @Override
    public List<TipoNotificacion> findall() {
        return entityManager.createQuery("SELECT n FROM " + clase.getSimpleName() + " n").getResultList();
    }

    @Override
    public Boolean consistencia(TipoNotificacion tipoNotificacion) {
        BigDecimal id = tipoNotificacion.getId();
        String sql = " select count(en.TIPONOTIFICACION_ID) "
                + " from ENT_NOTIFICACIONES en inner join CAT_TIPO_NOTIFICACION"
                + " tn on tn.ID = en.TIPONOTIFICACION_ID\n"
                + " where en.TIPONOTIFICACION_ID = ?1";
        return getCountNativeQuery(sql, id).intValue() > 0;
    }
}

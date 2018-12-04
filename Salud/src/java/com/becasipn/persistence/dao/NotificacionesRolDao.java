package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Notificaciones;
import com.becasipn.persistence.model.NotificacionesRol;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sistemas DSE
 */
public interface NotificacionesRolDao extends DaoBase<NotificacionesRol, BigDecimal> {

    public List<NotificacionesRol> findall();

    public List<String> findallbynotificacion(Notificaciones notificacion);
    
    public List<BigDecimal> rolesByNotificacionId(Notificaciones notificacion);
    
    public Boolean eliminaRegistrosAsociados(Notificaciones notificacion);

}

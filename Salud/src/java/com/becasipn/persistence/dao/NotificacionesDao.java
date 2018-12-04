package com.becasipn.persistence.dao;
import com.becasipn.persistence.model.Notificaciones;
import com.becasipn.persistence.model.Rol;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Augusto H.
 */
public interface NotificacionesDao extends DaoBase<Notificaciones, BigDecimal>{
    
    public List<Notificaciones> findall();
    
    public List<Notificaciones> findNotificacionesByRol(Rol rol);
}

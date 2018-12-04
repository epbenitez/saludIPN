package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.TipoNotificacion;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sistemas DSE
 */
public interface TipoNotificacionDao extends DaoBase<TipoNotificacion, BigDecimal>{
    
    public List<TipoNotificacion> findall();
    
    public Boolean consistencia (TipoNotificacion tipoNotificacion);
}

package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.MotivoRechazoSolicitud;
import java.math.BigDecimal;
import java.util.List;

public interface MotivoRechazoSolicitudDao extends DaoBase<MotivoRechazoSolicitud, BigDecimal> {
    
    public List<MotivoRechazoSolicitud> getMotivosVisibles();

}

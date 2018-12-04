package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.ClasificacionSolicitudDao;
import com.becasipn.persistence.model.ClasificacionSolicitud;
import java.math.BigDecimal;

public class ClasificacionSolicitudJpaDao extends JpaDaoBase<ClasificacionSolicitud, BigDecimal> implements ClasificacionSolicitudDao {

    public ClasificacionSolicitudJpaDao() {
	super(ClasificacionSolicitud.class);
    }

}

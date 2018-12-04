package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.SeguimientoBecarios;
import java.math.BigDecimal;

/**
 *
 * @author Sistemas DSE
 */
public interface SeguimientoBecariosDao extends DaoBase<SeguimientoBecarios, BigDecimal> {

    public Boolean contestoSeguimientoBecarios(BigDecimal alumnoId, BigDecimal periodoId);
    
    public SeguimientoBecarios getSeguimientoAlumno(BigDecimal alumnoId, BigDecimal periodoId);

}

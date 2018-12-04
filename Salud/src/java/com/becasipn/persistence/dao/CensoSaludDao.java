package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.CensoSalud;
import java.math.BigDecimal;

/**
 *
 * Augusto I. Hernández Ruiz
 *
 * "Cualquier tecnología suficientemente avanzada es indistinguible de la magia"
 */
public interface CensoSaludDao extends DaoBase<CensoSalud, BigDecimal> {

    public CensoSalud getSaludAlumno(BigDecimal alumnoId, BigDecimal periodoId);

    public Boolean contestoEncuestaSalud(BigDecimal alumnoId, BigDecimal periodoId);
}

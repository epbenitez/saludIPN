package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.SolicitudReconsideracion;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Augusto I. Hernández Ruiz
 *
 * "El problema real no es si las máquinas piensan, sino si lo hacen los
 * hombres" B. F. Skinner.
 */
public interface SolicitudReconsideracionDao extends DaoBase<SolicitudReconsideracion, BigDecimal> {

    public Boolean tieneSolicitudPeriodoActual(BigDecimal periodoId, BigDecimal alumnoId);

    public List<SolicitudReconsideracion> solicitudesAlumnoPeriodoActual(BigDecimal periodoId, BigDecimal alumnoId);

}

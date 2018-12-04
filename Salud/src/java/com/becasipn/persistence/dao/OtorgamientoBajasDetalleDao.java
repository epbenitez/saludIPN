package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.OtorgamientoBajasDetalle;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public interface OtorgamientoBajasDetalleDao extends DaoBase<OtorgamientoBajasDetalle, BigDecimal> {

	public OtorgamientoBajasDetalle getByOtorgamientoId(BigDecimal id);

	public List<OtorgamientoBajasDetalle> getByOtorgamientoIdL(BigDecimal id);

	public Boolean bajaPeriodoActual(BigDecimal id);

	public Boolean mostrarBaja(BigDecimal otorgamientoId);
}

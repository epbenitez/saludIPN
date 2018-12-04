package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.OtorgamientoBajasDetalleDao;
import com.becasipn.persistence.model.OtorgamientoBajasDetalle;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public class OtorgamientoBajasDetalleJpaDao extends JpaDaoBase<OtorgamientoBajasDetalle, BigDecimal> implements OtorgamientoBajasDetalleDao {

	public OtorgamientoBajasDetalleJpaDao() {
		super(OtorgamientoBajasDetalle.class);
	}

	@Override
	public OtorgamientoBajasDetalle getByOtorgamientoId(BigDecimal id) {
		String jpql = "SELECT o FROM OtorgamientoBajasDetalle o WHERE o.otorgamiento.id = ?1";
		List<OtorgamientoBajasDetalle> lista = executeQuery(jpql, id);
		return lista == null || lista.isEmpty() ? null : lista.get(0);
	}

	@Override
	public List<OtorgamientoBajasDetalle> getByOtorgamientoIdL(BigDecimal id) {
		String jpql = "SELECT o FROM OtorgamientoBajasDetalle o WHERE o.otorgamiento.id = ?1 order by o.id asc";
		List<OtorgamientoBajasDetalle> lista = executeQuery(jpql, id);
		return lista == null || lista.isEmpty() ? null : lista;
	}

	@Override
	public Boolean bajaPeriodoActual(BigDecimal alumno_id) {
		String consulta = "SELECT obd FROM Otorgamiento o, OtorgamientoBajasDetalle obd"
				+ " where o.id = obd.otorgamiento.id and o.alta = 0"
				+ " AND obd.periodo.id = ?1"
				+ " and o.alumno.id = ?2";
		List<OtorgamientoBajasDetalle> lista = executeQuery(consulta, getDaos().getPeriodoDao().getPeriodoActivo().getId(), alumno_id);
		return (lista != null && !lista.isEmpty());
	}

	/**
	 * Verifica si se le debe mostrar o no al alumno su baja para el periodo actual.
	 *
	 * @param otorgamientoId
	 * @return true si el alumno ya puede ver su baja.
	 */
	@Override
	public Boolean mostrarBaja(BigDecimal otorgamientoId) {
		String jpql = "select o from OtorgamientoBajasDetalle o where o.proceso.fechaFinal < current_timestamp "
				+ " and o.proceso.procesoEstatus.id = 4 and o.otorgamiento.id = ?1";
		List<OtorgamientoBajasDetalle> lista = executeQuery(jpql, otorgamientoId);
		return lista != null && !lista.isEmpty();
	}
}

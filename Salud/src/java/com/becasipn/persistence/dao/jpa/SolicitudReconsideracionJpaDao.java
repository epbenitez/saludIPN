package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.SolicitudReconsideracionDao;
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
public class SolicitudReconsideracionJpaDao extends JpaDaoBase<SolicitudReconsideracion, BigDecimal> implements SolicitudReconsideracionDao {

    public SolicitudReconsideracionJpaDao() {
        super(SolicitudReconsideracion.class);
    }

    @Override
    public Boolean tieneSolicitudPeriodoActual(BigDecimal periodoId, BigDecimal alumnoId) {
        String jpql = "select sb from SolicitudBeca sb where sb.alumno.id = ?1 and sb.periodo.id = ?2";
        List<SolicitudReconsideracion> lista = executeQuery(jpql, alumnoId, periodoId);
        return lista != null && !lista.isEmpty();
    }

    @Override
    public List<SolicitudReconsideracion> solicitudesAlumnoPeriodoActual(BigDecimal periodoId, BigDecimal alumnoId) {
        String jpql = "select sr from SolicitudReconsideracion sr where sr.alumno.id = ?1 and sr.periodo.id = ?2";
        List<SolicitudReconsideracion> lista = executeQuery(jpql, alumnoId, periodoId);
        return lista;
    }
}

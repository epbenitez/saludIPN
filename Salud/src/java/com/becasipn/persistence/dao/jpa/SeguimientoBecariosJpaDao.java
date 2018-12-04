package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.SeguimientoBecariosDao;
import com.becasipn.persistence.model.SeguimientoBecarios;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sistemas DSE
 */
public class SeguimientoBecariosJpaDao extends JpaDaoBase<SeguimientoBecarios, BigDecimal> implements SeguimientoBecariosDao {

    public SeguimientoBecariosJpaDao() {
        super(SeguimientoBecarios.class);
    }

    @Override
    public Boolean contestoSeguimientoBecarios(BigDecimal alumnoId, BigDecimal periodoId) {
        String sql = "select * from ent_seguimiento_becarios where periodo_id = ?2 and alumno_id = ?1";
        List<Object[]> lista = executeNativeQuery(sql, alumnoId, periodoId);
        return (lista != null && !lista.isEmpty());
    }

    @Override
    public SeguimientoBecarios getSeguimientoAlumno(BigDecimal alumnoId, BigDecimal periodoId) {
        String jpql = "select sb from SeguimientoBecarios sb where sb.alumno.id = ?1 and sb.periodo.id = ?2";
        List<SeguimientoBecarios> lista = executeQuery(jpql, alumnoId, periodoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }
}

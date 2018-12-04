package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CensoSaludDao;
import com.becasipn.persistence.model.CensoSalud;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sistemas DSE
 */
public class CensoSaludJpaDao extends JpaDaoBase<CensoSalud, BigDecimal> implements CensoSaludDao {

    public CensoSaludJpaDao() {
        super(CensoSalud.class);
    }

    @Override
    public CensoSalud getSaludAlumno(BigDecimal alumnoId, BigDecimal periodoId) {
        String jpql = "select c from CensoSalud c where c.alumno.id = ?1 and c.periodo.id = ?2 and c.cuestionario.id = 3";
        List<CensoSalud> lista = executeQuery(jpql, alumnoId, periodoId);
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public Boolean contestoEncuestaSalud(BigDecimal alumnoId, BigDecimal periodoId) {
        String sql = "select * from ent_censo_salud where cuestionario_id = 3 and periodo_id = ?1 and alumno_id = ?2";
        List<Object[]> lista = executeNativeQuery(sql, periodoId, alumnoId);
        return lista != null && !lista.isEmpty();
    }
}

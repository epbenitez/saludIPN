package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.SolicitudBecaUADao;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBecaUA;
import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin CAE
 */
public class SolicitudBecaUAJpaDao extends JpaDaoBase<SolicitudBecaUA, BigDecimal> implements SolicitudBecaUADao {

    public SolicitudBecaUAJpaDao() {
        super(SolicitudBecaUA.class);
    }

    @Override
    public SolicitudBecaUA buscarUA(UnidadAcademica ua, Periodo p) {
        String jpql = "select c from SolicitudBecaUA c where c.unidadAcademica.id = ?1 and c.periodo.id = ?2 "
                + " and (?3 BETWEEN c.fechaInicio and c.fechaFin+1)";
        List<SolicitudBecaUA> lista = executeQuery(jpql, ua.getId(), p.getId(), new Date());
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<SolicitudBecaUA> findActives() {
        String jpql = "select c from SolicitudBecaUA c where c.estatus = 1";
        return executeQuery(jpql);
    }
}

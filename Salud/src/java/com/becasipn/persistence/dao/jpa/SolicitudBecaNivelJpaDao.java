package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.SolicitudBecaNivelDao;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBecaNivel;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author César Hernández Tavera
 */
public class SolicitudBecaNivelJpaDao extends JpaDaoBase<SolicitudBecaNivel, BigDecimal> implements SolicitudBecaNivelDao {

    public SolicitudBecaNivelJpaDao() {
        super(SolicitudBecaNivel.class);
    }

    @Override
    public SolicitudBecaNivel buscarNivel(Nivel n, Periodo p) {
        String jpql = "select c from SolicitudBecaNivel c where c.nivel.id = ?1 and c.periodo.id = ?2 "
                 + " and (?3 BETWEEN c.fechaInicio and c.fechaFin+1)";
        List<SolicitudBecaNivel> lista = executeQuery(jpql, n.getId(), p.getId(), new Date());
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @Override
    public List<SolicitudBecaNivel> findActives() {
        String jpql = "select c from SolicitudBecaNivel c where c.estatus = 1";
        return executeQuery(jpql);
    }
}

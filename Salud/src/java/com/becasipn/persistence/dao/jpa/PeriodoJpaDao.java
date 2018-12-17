package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.PeriodoDao;
import com.becasipn.persistence.model.Periodo;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class PeriodoJpaDao extends JpaDaoBase<Periodo, BigDecimal> implements PeriodoDao {

    private Periodo periodoActivo;

    public PeriodoJpaDao() {
        super(Periodo.class);
    }

    @Override
    public Periodo getPeriodoActivo() {
        String consulta = "SELECT p FROM Periodo p WHERE p.estatus = 1";
        List<Periodo> lista = executeQuery(consulta);
        return periodoActivo = lista == null || lista.isEmpty() ? null : lista.get(0);
    }
}

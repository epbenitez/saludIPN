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
    private String claveperiodo;

    public PeriodoJpaDao() {
        super(Periodo.class);
    }

    @Override
    public List<Periodo> existenPeriodosActivos(BigDecimal periodoId) {
        String jpql = "SELECT p FROM Periodo p WHERE p.estatus = 1 and p.id != ?1";
        List<Periodo> lista = executeQuery(jpql, periodoId);
        return lista;
    }

    @Override
    public Periodo getPeriodoActivo() {
        String consulta = "SELECT p FROM Periodo p WHERE p.estatus = 1";
        List<Periodo> lista = executeQuery(consulta);
        return periodoActivo = lista == null || lista.isEmpty() ? null : lista.get(0);
    }
    
    public String getClavePeriodo(int periodoId){
        String jpql = "SELECT p FROM Periodo p WHERE p.id = ?1";
        return claveperiodo = executeQuery(jpql, periodoId).get(0).getClave();
    }

    @Override
    public List<Periodo> findAll() {
        String jpql = "select a from Periodo a ORDER BY a.clave DESC";
        return executeQuery(jpql);
    }

    @Override
    public List<Periodo> findAllFrom(BigDecimal periodoId) {
        String jpql = "select p from Periodo p WHERE p.id >= ?1 ORDER BY p.clave DESC";
        return executeQuery(jpql, periodoId);
    }
    
    @Override
    public List<Periodo> findAll(Integer limit) {
        String jpql = "select a from Periodo a  ORDER BY a.clave DESC";
        return executeQuery(limit, jpql);
    }
}

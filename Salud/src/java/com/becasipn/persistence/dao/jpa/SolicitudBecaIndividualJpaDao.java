package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.SolicitudBecaIndividualDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBecaIndividual;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author César Hernández Tavera
 */
public class SolicitudBecaIndividualJpaDao extends JpaDaoBase<SolicitudBecaIndividual, BigDecimal> implements SolicitudBecaIndividualDao {

    public SolicitudBecaIndividualJpaDao() {
        super(SolicitudBecaIndividual.class);
    }    
    
    @Override
    public SolicitudBecaIndividual buscaAlumno(Alumno a, Periodo p) {
        String jpql = "select c from SolicitudBecaIndividual c where c.alumno.id = ?1 and c.periodo.id = ?2 "
        + " and (?3 BETWEEN c.fechaInicio and c.fechaFin+1)";
        List<SolicitudBecaIndividual> lista = executeQuery(jpql, a.getId(), p.getId(), new Date());
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }
    
    @Override
    public List<SolicitudBecaIndividual> findAll() {
        String jpql = "select c from SolicitudBecaIndividual c";
        return executeQuery(jpql);
    }

    @Override
    public List<SolicitudBecaIndividual> getActives() {
        String jpql = "select c from SolicitudBecaIndividual c where c.estatus = 1";
        return executeQuery(jpql);
    }
}

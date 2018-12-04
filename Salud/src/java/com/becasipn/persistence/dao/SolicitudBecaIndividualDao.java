package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBecaIndividual;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author César Hernández Tavera
 */
public interface SolicitudBecaIndividualDao extends DaoBase<SolicitudBecaIndividual, BigDecimal> {
    
    public SolicitudBecaIndividual buscaAlumno(Alumno a, Periodo p);
    
    @Override
    public List<SolicitudBecaIndividual> findAll();
    
    public List<SolicitudBecaIndividual> getActives();
}

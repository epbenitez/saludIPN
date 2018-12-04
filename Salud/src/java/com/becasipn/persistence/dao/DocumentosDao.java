package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Alumno;
import java.math.BigDecimal;

import com.becasipn.persistence.model.Documentos;

/**
 *
 * @author Victor Lozano
 */
public interface DocumentosDao extends DaoBase<Documentos, BigDecimal> {
    
    public Documentos documentosAlumnoPeriodoActual(Alumno a);
    
    public Documentos documentosAlumnoPeriodo(Alumno a, BigDecimal periodoId);
    
}

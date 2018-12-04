package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Periodo;
import java.util.List;

/**
 *
 * @Tania G. Sánchez
 */
public interface PeriodoDao extends DaoBase<Periodo, BigDecimal> {

    public List<Periodo> existenPeriodosActivos(BigDecimal periodoId);
	
    public List<Periodo> findAllFrom(BigDecimal periodoId);

    public Periodo getPeriodoActivo();
    
    public List<Periodo> findAll(Integer limit);
    
    public String getClavePeriodo(int periodoId);
}

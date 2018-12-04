package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBecaUA;
import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.List;


/**
 *
 * @author Admin CAE
 */
public interface SolicitudBecaUADao extends DaoBase<SolicitudBecaUA, BigDecimal> {
    
    public SolicitudBecaUA buscarUA(UnidadAcademica ua, Periodo p);
    public List<SolicitudBecaUA> findActives();
}

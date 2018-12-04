package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBecaNivel;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Admin CAE
 */
public interface SolicitudBecaNivelDao extends DaoBase<SolicitudBecaNivel, BigDecimal> {
    
    public SolicitudBecaNivel buscarNivel(Nivel n, Periodo p);
    public List<SolicitudBecaNivel> findActives();
}

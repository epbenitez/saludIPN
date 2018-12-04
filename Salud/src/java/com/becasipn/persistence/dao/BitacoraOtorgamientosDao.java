package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.BitacoraOtorgamientos;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public interface BitacoraOtorgamientosDao extends DaoBase<BitacoraOtorgamientos, BigDecimal> {
    
    public List<BitacoraOtorgamientos> getBitacora(BigDecimal otorgamiento_id);
    
    public int eliminaBitacora(BigDecimal otorgamiento_id);
}

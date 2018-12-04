package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.MontoMaximoIngresos;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public interface MontoMaximoIngresosDao extends DaoBase<MontoMaximoIngresos, BigDecimal>{
    public boolean existeRegistro (BigDecimal periodoId, boolean deciles);
}
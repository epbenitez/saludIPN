package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.VWConteoDepositos;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface VWConteoDepositosDao extends DaoBase<VWConteoDepositos, BigDecimal>{
    public VWConteoDepositos depositosOtorgamiento (BigDecimal otorgamientoId);
}
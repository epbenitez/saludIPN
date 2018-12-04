
package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.BitacoraOtorgamientoExterno;
import com.becasipn.persistence.model.OtorgamientoExterno;
import java.math.BigDecimal;
import java.util.List;

public interface BitacoraOtorgamientoExternoDao extends DaoBase<BitacoraOtorgamientoExterno,BigDecimal> {

    public List<BitacoraOtorgamientoExterno> bitacoraOtorgamientoExterno(OtorgamientoExterno oe);
}

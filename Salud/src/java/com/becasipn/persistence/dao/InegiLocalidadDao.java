package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.InegiLocalidad;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania Gabriela Sánchez
 */
public interface InegiLocalidadDao extends DaoBase<InegiLocalidad, BigDecimal>{
    public List<InegiLocalidad> localidadPorEntidadMunicipio (BigDecimal estadoId, BigDecimal municipioId);
}
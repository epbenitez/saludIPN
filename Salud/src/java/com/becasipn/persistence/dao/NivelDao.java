package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Nivel;
import java.util.List;

/**
 *
 * @author User-03
 */
public interface NivelDao extends DaoBase<Nivel, BigDecimal> {

    public List<Nivel> nivelesActivos();

    public Nivel findByNombre(String nombre);
}

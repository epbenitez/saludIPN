package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.LocalidadColonia;
import com.becasipn.persistence.model.MunicipioDelegacion;
import com.becasipn.persistence.model.RelacionGeografica;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public interface RelacionGeograficaDao extends DaoBase<RelacionGeografica, BigDecimal> {
    public List<MunicipioDelegacion> getMunicipiosByEstado(BigDecimal estadoId);
    public List<LocalidadColonia> getColoniasByMunicipio(BigDecimal municipioId);
    public RelacionGeografica getRelacionGeografica(BigDecimal estadoId, BigDecimal municipioId, BigDecimal coloniaId);
}
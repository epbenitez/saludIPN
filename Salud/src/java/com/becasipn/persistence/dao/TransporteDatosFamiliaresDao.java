package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.TransporteDatosFamiliares;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface TransporteDatosFamiliaresDao extends DaoBase<TransporteDatosFamiliares, BigDecimal>{
    public List<TransporteDatosFamiliares> respuestasTransporte (BigDecimal transporteId);
    public Float ingresoPercapita (BigDecimal transporteId);
    public void borrarRespuestasDatosFamiliares(BigDecimal cuestionarioTransporteId);
    public HashMap<String, Float> ingesoEIntegrantes (BigDecimal transporteId);
}
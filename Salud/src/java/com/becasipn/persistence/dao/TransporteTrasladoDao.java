package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.TransporteTraslado;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public interface TransporteTrasladoDao extends DaoBase<TransporteTraslado, BigDecimal>{
    public List<TransporteTraslado> respuestasTraslado(BigDecimal transporteId);
    public void borrarRespuestasTraslado(BigDecimal cuestionarioTransporteId);
    
    public Float gastoTraslado(BigDecimal usuarioId, BigDecimal periodoId);
}
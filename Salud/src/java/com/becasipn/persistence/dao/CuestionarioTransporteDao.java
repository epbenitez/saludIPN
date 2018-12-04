package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.CuestionarioTransporte;
import java.math.BigDecimal;

/**
 *
 * @author Tania G. Sánchez
 */
public interface CuestionarioTransporteDao extends DaoBase<CuestionarioTransporte, BigDecimal>{
    public Boolean tieneEseTransporte(BigDecimal usuarioId, BigDecimal periodoId);
    public Boolean tieneEseTransporte(Alumno a);
    public CuestionarioTransporte respuestasTransporte(BigDecimal usuarioId, BigDecimal periodoId);
    public void borrarCuestionarioTransporte(BigDecimal cuestionarioTransporteId);
}
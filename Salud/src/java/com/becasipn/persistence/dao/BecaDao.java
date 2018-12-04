package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Beca;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author User-03
 */
public interface BecaDao extends DaoBase<Beca, BigDecimal> {
    public List<Beca> getProgramaBecaPorOrigenRecursos(BigDecimal origenDeposito);
    public List<Beca> getTodasExceptuando(List<BigDecimal> becaIds);
    public List<Beca> becasSinTransportePorPeriodoNivel(BigDecimal periodoId, BigDecimal nivelId, Boolean fundacion);
    public List<Beca> becasPorNivel(BigDecimal nivelId);
    
    /**
     * Este método consigue los programa de beca a los que un alumno puede competir,
     * sin tomar en cuenta los requisitos. Esto se hace según el nivel del alumno
     * y el periodo actual
     * Author: Mario Márquez
     *
     * @param nivelId 
     * @param periodoId 
     * @return Lista de objetos Beca
     */
    public List<Beca> programasPorNivelPeriodo(BigDecimal nivelId, BigDecimal periodoId);
}
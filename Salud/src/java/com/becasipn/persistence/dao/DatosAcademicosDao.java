/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2017
 *
 */
package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.DatosAcademicos;
import java.math.BigDecimal;

/**
 *
 * @author Mario Márquez
 */
public interface DatosAcademicosDao extends DaoBase<DatosAcademicos, BigDecimal> {
    public DatosAcademicos datosPorPeriodo(BigDecimal alumnoId, BigDecimal periodoId);
    public DatosAcademicos ultimosDatos(BigDecimal alumnoId);
}
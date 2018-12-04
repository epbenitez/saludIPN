/**
* SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL
* DIRECCION DE SERVICIOS ESTUDIANTILES
* 2016
**/

package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.CicloEscolar;
import com.becasipn.persistence.model.Movimiento;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.OtorgamientoExterno;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public interface OtorgamientoExternoDao extends DaoBase<OtorgamientoExterno,BigDecimal> {

    public List<OtorgamientoExterno> getAlumno(Alumno alumno, CicloEscolar ciclo);
    
    public List<OtorgamientoExterno> getOEByName(String nombre);
    
    public List<OtorgamientoExterno> getAllAlumno(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica);
    
    public List<OtorgamientoExterno> getAllUA(Periodo periodo, Nivel nivel, BigDecimal BecaId);
    
    public List<Object[]> getUnidadesAcademicas(Periodo periodo, Nivel nivel, BigDecimal becaId);
    
    public List<OtorgamientoExterno> getbyUA(Periodo periodo, UnidadAcademica unidadacademica, BigDecimal becaId);
    
    public Boolean tieneOtorgamientoExterno(String curp, BigDecimal cicloId);
    
    public List<Object[]>  reporteEstatus(BigDecimal periodoId, BigDecimal unidadAcademicaId, BigDecimal becaid);
}

/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 07-ago-2015, 12:18:53
 *
 */
package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Carrera;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public interface CarreraDao extends DaoBase<Carrera, BigDecimal> {

    public Carrera getCarrera(String planEstudios, String carrera, String especialidad, BigDecimal unidadAcademica);

    public HashMap<BigDecimal, Integer> getSemestresMaximos();

    public List<String> findByUnidadAacademica(BigDecimal unidadAcademicaId);

    public List<Carrera> findByNivel(BigDecimal nivelId);

    public List<Carrera> findAllGrouped();

    public List<String> findPlan(BigDecimal unidadAcademicaId);

    public List<Carrera> findCarrera(BigDecimal unidadAcademicaId, String planEstudios);

    public List<String> findEspecialidad(BigDecimal unidadAcademicaId, String planEstudios, String carrera);

}

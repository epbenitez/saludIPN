/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public interface AlumnoDao extends DaoBase<Alumno, BigDecimal> {

    public List<Alumno> getByBoleta(String noBoleta);

    public Alumno findByBoleta(String boleta);

    public PaginateUtil solicitudAlumnos(ServerSideUtil ssu);

}

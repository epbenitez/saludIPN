/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.AlumnoDAE;
import java.util.List;
import javax.resource.spi.LocalTransactionException;

/**
 *
 * @author Patricia Benitez
 */
public interface AlumnoDAEDao extends DaoBase<AlumnoDAE, BigDecimal> {

	public List<AlumnoDAE> findByBoleta(String boleta) throws LocalTransactionException;

	public List<AlumnoDAE> findByCurpManutencion(String curp) throws LocalTransactionException;

	public Boolean existeBoletaDAE(String boleta);
}

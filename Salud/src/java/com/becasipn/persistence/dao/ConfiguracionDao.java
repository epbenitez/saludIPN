/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Configuracion;

/**
 *
 * @author Patricia Benitez
 */
public interface ConfiguracionDao extends DaoBase<Configuracion, BigDecimal> {

	public Configuracion secuenciaProcesosAutomaticos();

	public Configuracion otorgamientosSubes();
}

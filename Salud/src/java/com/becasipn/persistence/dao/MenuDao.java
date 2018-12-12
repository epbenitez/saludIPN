/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Menu;

/**
 *
 * @author Patricia Benitez
 */
public interface MenuDao extends DaoBase<Menu, BigDecimal> {

    public String getNombreModulo(String action, StringBuilder menuActivo);
}

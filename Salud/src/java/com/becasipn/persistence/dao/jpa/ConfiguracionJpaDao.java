/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.ConfiguracionDao;
import com.becasipn.persistence.model.Configuracion;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public class ConfiguracionJpaDao extends JpaDaoBase<Configuracion, BigDecimal> implements ConfiguracionDao {

    public ConfiguracionJpaDao() {
        super(Configuracion.class);
    }

    @Override
    public Configuracion secuenciaProcesosAutomaticos() {
	String consulta = "SELECT c FROM Configuracion c WHERE c.propiedad = 'secuenciaProcesosAutomaticos'";
	List<Configuracion> lista = executeQuery(consulta);
	return lista.isEmpty() ? null : lista.get(0);
    }

}

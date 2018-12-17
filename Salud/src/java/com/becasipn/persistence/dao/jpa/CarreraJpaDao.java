/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 07-ago-2015, 12:21:21
 *
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CarreraDao;
import com.becasipn.persistence.model.Carrera;
import java.math.BigDecimal;

/**
 *
 * @author Patricia Benitez
 */
public class CarreraJpaDao extends JpaDaoBase<Carrera, BigDecimal> implements CarreraDao {

    public CarreraJpaDao() {
        super(Carrera.class);
    }

}

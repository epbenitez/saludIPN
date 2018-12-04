package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.DireccionDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Direccion;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class DireccionJpaDao extends JpaDaoBase<Direccion, BigDecimal> implements DireccionDao {

    public DireccionJpaDao() {
        super(Direccion.class);
    }

    // SQLINJECTION Eliminado
    // public Direccion getDireccion(Alumno a) {        
}

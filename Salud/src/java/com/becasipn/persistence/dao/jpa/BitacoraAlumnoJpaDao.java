package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BitacoraAlumnoDao;
import com.becasipn.persistence.model.BitacoraAlumno;
import java.math.BigDecimal;

/**
 * @author Victor Lozano
 * @date 20/04/2016
 */
public class BitacoraAlumnoJpaDao extends JpaDaoBase<BitacoraAlumno, BigDecimal> implements BitacoraAlumnoDao {

    public BitacoraAlumnoJpaDao() {
        super(BitacoraAlumno.class);
    }

}

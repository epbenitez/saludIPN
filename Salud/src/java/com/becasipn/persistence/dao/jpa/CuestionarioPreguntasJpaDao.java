/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CuestionarioPreguntasDao;
import com.becasipn.persistence.model.CuestionarioPreguntas;
import java.math.BigDecimal;

/**
 *
 * @author Patricia Benitez
 */
public class CuestionarioPreguntasJpaDao extends JpaDaoBase<CuestionarioPreguntas, BigDecimal> implements CuestionarioPreguntasDao {

    public CuestionarioPreguntasJpaDao() {
        super(CuestionarioPreguntas.class);
    }

}

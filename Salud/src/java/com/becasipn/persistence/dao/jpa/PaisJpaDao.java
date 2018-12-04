/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.PaisDao;
import com.becasipn.persistence.model.Pais;
import java.math.BigDecimal;

/**
 *
 * @author Patricia Benitez
 */
public class PaisJpaDao extends JpaDaoBase<Pais, BigDecimal> implements PaisDao {

    public PaisJpaDao() {
        super(Pais.class);
    }

}

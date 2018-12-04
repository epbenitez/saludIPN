package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CargoDao;
import com.becasipn.persistence.model.Cargo;
import java.math.BigDecimal;

/**
 *
 * @author Victor Lozano
 */
public class CargoJpaDao extends JpaDaoBase<Cargo, BigDecimal> implements CargoDao {

    public CargoJpaDao() {
        super(Cargo.class);
    }

    @Override
    public Cargo findById(String id) {
        String jpql = "SELECT c FROM Cargo c WHERE c.id = ?1";
        Cargo cargo = executeSingleQuery(jpql, id);

        return cargo;
    }
}

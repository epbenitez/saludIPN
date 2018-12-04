package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BancosDao;
import com.becasipn.persistence.model.Bancos;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public class BancosJpaDao extends JpaDaoBase<Bancos, BigDecimal> implements BancosDao {

    public BancosJpaDao() {
        super(Bancos.class);
    }

    @Override
    public String bancoPorClave(String clave) {
        String consulta = "select b from Bancos b where b.clave = ?1";
        List<Bancos> lista = executeQuery(consulta, clave);
        return lista == null || lista.isEmpty() ? "Error" : lista.get(0).getNombreCorto();
    }
}

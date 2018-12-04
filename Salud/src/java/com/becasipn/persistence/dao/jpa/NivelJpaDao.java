package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.NivelDao;
import com.becasipn.persistence.model.Nivel;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author User-03
 */
public class NivelJpaDao extends JpaDaoBase<Nivel, BigDecimal> implements NivelDao {

    public NivelJpaDao() {
        super(Nivel.class);
    }

    @Override
    public List<Nivel> nivelesActivos() {
        String jpql = "select n from Nivel n where n.activo = 1";
        //select * from cat_nivel where activo = 1
        List<Nivel> lista = executeQuery(jpql);
        return lista;
    }

    @Override
    public Nivel findByNombre(String nombre) {
        String jpql = "select n from Nivel n where n.clave like ?1";
        List<Nivel> lista = executeQuery(jpql, nombre);
        return lista.get(0);
    }
}

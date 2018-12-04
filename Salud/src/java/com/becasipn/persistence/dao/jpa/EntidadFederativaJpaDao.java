package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.EntidadFederativaDao;
import com.becasipn.persistence.model.EntidadFederativa;
import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación de las operaciones CRUD de las entidades federativas.
 *
 * @author Patricia Benitez
 * @version $Rev: 1169 $
 * @since 1.0
 */
public class EntidadFederativaJpaDao extends JpaDaoBase<EntidadFederativa, BigDecimal> implements EntidadFederativaDao {

    /**
     * Crea una instancia de una <code>EntidadFederativaJpaDao</code>.
     */
    public EntidadFederativaJpaDao() {
        super(EntidadFederativa.class);
    }

    /**
     * Obtiene las entidad federativa por nombre
     *
     * @param nombre Nombre de la Entidad Federativa
     * @return Entidad Federativa
     */
    @Override
    public EntidadFederativa findByNombre(String nombre) {
        String jpql = "SELECT estado FROM EntidadFederativa estado WHERE estado.nombre = ?1";
        List<EntidadFederativa> estado = executeQuery(jpql, nombre);

        if (estado != null && estado.size() == 1) {
            return estado.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<EntidadFederativa> findAll() {
        String jpql = "SELECT estado FROM EntidadFederativa estado WHERE estado.id <=32";
        List<EntidadFederativa> estados = executeQuery(jpql);

        return estados == null || estados.isEmpty() ? null : estados;
    }

    @Override
    public EntidadFederativa findByClave(String clave) {
        String jpql = "SELECT e FROM EntidadFederativa e WHERE e.clave = ?1 ";
        List<EntidadFederativa> estado = executeQuery(jpql, clave);

        return estado == null || estado.size() == 0 ? null : estado.get(0);
    }

    @Override
    public BigDecimal findByNombreAcentos(String nombre) {
        String sql = "SELECT id FROM( "
                + "     SELECT UPPER(TRANSLATE(E.NOMBRE,'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')) N,e.id "
                + " FROM CAT_ESTADO E) "
                + " where N= ?1";
        List<Object[]> id = executeNativeQuery(sql, nombre);
        return id == null || id.isEmpty() ? null : new BigDecimal(String.valueOf(id.get(0)));
    }

    @Override
    public List<EntidadFederativa> findTotal() {
        String jpql = "SELECT estado FROM EntidadFederativa estado";
        List<EntidadFederativa> estados = executeQuery(jpql);

        return estados == null || estados.isEmpty() ? null : estados;
    }
}

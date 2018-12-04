/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 04-ago-2015, 13:26:42
 *
 */
package com.becasipn.persistence.dao.jpa;

/**
 *
 * @author Patricia Benitez
 */
import com.becasipn.persistence.dao.DaoBase;
import com.becasipn.util.PaginateUtil;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para ejecutar operaciones CRUD de bitácora
 */
@Transactional
public class JpaDaoBitacora<T, Id extends Serializable> implements DaoBase<T, Id> {

    protected Class<T> clase;

    protected EntityManager entityManager;

    /**
     * Obtiene el valor de la variable entityManager
     *
     * @return el valor de la variable entityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Establece el valor de la variable entityManager
     *
     * @param entityManager nuevo valor de la variable entityManager
     */
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public JpaDaoBitacora(Class<T> clase) {
        this.clase = clase;
    }

    public T save(T obj) {
        entityManager.persist(obj);
        entityManager.flush();
        return obj;
    }

    public T update(T obj) {
        T re = entityManager.merge(obj);
        entityManager.flush();
        return re;
    }

    public void delete(T obj) {
        entityManager.remove(entityManager.contains(obj) ? obj : entityManager.merge(obj));
        entityManager.flush();
    }

    public boolean contains(T obj) {
        return entityManager.contains(obj);
    }

    public List<T> findAll() {
        return entityManager.createQuery("select a from " + clase.getSimpleName() + " a").getResultList();
    }
    
        
    public T findById(Id id) {
        return entityManager.find(clase, id);
    }
    
    @Override
    public List<T> findBy(String field, String value, Boolean isString) {
        return entityManager.createQuery("select a from " + clase.getSimpleName() + " a where a." + field + (isString ? " like " : " = ") + value).getResultList();
    }

    /** Método para paginar del lado del servidor.
     * Author: Jesus Fernandez Flores
     * @return Un mapa el cual tendrá el número de resultados total, y una lista con los resultados paginados.
     */
    
    @Override
    public PaginateUtil findPaginate(int start, int length, Map<String, Object> ... maps) {   
        return null;
    }


    public List<T> executeQuery(String jpql, Object... values) {
        Query q = entityManager.createQuery(jpql);
        int i = 1;
        for (Object object : values) {
            q.setParameter(i, object);
            i++;
        }
        List<T> result = (List<T>) q.getResultList();
        return result;
    }

    public List executeQueryObject(String jpql, Object... values) {
        Query q = entityManager.createQuery(jpql);
        int i = 1;
        for (Object object : values) {
            q.setParameter(i, object);
            i++;
        }
        List result = q.getResultList();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public List<Object[]> executeNativeQuery(String jpql) {
        Query q = entityManager.createNativeQuery(jpql);

        return q.getResultList();
    }

}

package com.becasipn.persistence.dao;

import com.becasipn.util.PaginateUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 * Interfaz para ejecutar operaciones CRUD
 *
 * @param <T>
 * @param <Id>
 * @author Patricia Benitez
 */
public interface DaoBase<T, Id extends Serializable> {

    public T save(T obj);

    public T update(T obj);

    public void delete(T obj);

    public boolean contains(T obj);

    public List<T> findAll();

    public List<T> findBy(String field, String value, Boolean isString);

    public T findById(Id id);

    public PaginateUtil findPaginate(int start, int length, Map<String, Object>... maps);

    public List<T> executeQuery(String jpql, Object... values);

    public EntityManager getEntityManager();

    public List executeQueryObject(String jpql, Object... values);
    

}

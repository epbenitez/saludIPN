package com.becasipn.persistence.dao.jpa;

import com.becasipn.exception.RegexException;
import com.becasipn.persistence.dao.DaoBase;
import com.becasipn.persistence.model.BaseBitacora;
import com.becasipn.persistence.model.BaseEntity;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.becasipn.util.PaginateUtil;
import com.opensymphony.xwork2.ActionContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.WebApplicationContextUtils;
import static com.becasipn.util.StringUtil.addParameters;
import static com.becasipn.util.StringUtil.buildCountQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Implementacion para ejecutar operaciones CRUD
 *
 * @author Patricia Benitez
 * @param <T>
 * @param <Id>
 */
@Transactional
public class JpaDaoBase<T extends BaseEntity, Id extends Serializable> implements DaoBase<T, Id> {

    protected Class<T> clase;
    protected EntityManager entityManager;
    protected static Logger writeFileLog = LogManager.getLogger(JpaDaoBase.class.getName());
    protected String Query;

    /**
     * Obtiene el valor de la variable entityManager
     *
     * @return el valor de la variable entityManager
     */
    @Override
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

    public JpaDaoBase(Class<T> clase) {
        this.clase = clase;
    }

    public static Logger getWriteFileLog() {
        return writeFileLog;
    }

    public static void setWriteFileLog(Logger writeFileLog) {
        JpaDaoBase.writeFileLog = writeFileLog;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String Query) {
        this.Query = Query;
    }

    /**
     * {@inheritDoc}
     *
     * @param obj
     * @return
     */
    @Override
    public T save(T obj) {
        entityManager.persist(obj); //Esta línea se deja, en caso de que se qiera comentar la Bitacora
        entityManager.flush();//Esta línea se deja, en caso de que se qiera comentar la Bitacora

        if (obj instanceof BaseBitacora) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setId(new BigDecimal(2));
            }
        }
        return obj;
    }

    /**
     * {@inheritDoc}
     *
     * @param obj
     * @return
     */
    @Override
    public T update(T obj) {

        // Obtiene valor anterior del objeto. Solo para registrar en bitacora
        T old = entityManager.find(clase, obj == null ? "" : obj.getId());
        if (old == null) {
            return save(obj);
        }

        T re = entityManager.merge(obj);
        if (obj instanceof BaseBitacora) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            String action = (String) ActionContext.getContext().getSession().get("action");
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if (constraintViolations.size() > 0) {
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
                try {
                    throw new RegexException(cv.getMessage());
                } catch (RegexException ex) {
                    java.util.logging.Logger.getLogger(JpaDaoBase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            entityManager.flush();
        }
        return re;
    }

    /**
     * {@inheritDoc}
     *
     * @param obj
     */
    @Override
    public void delete(T obj) {
//        T old = entityManager.find(clase, obj.getId());
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        String urlAction = (String) ActionContext.getContext().getSession().get("urlAction");
        entityManager.flush(); //Esta línea se deja, en caso de que se qiera comentar la Bitacora
    }

    /**
     * {@inheritDoc}
     *
     * @param obj
     * @return
     */
    @Override
    public boolean contains(T obj) {
        return entityManager.contains(obj);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<T> findAll() {
        return entityManager.createQuery("select a from " + clase.getSimpleName() + " a").getResultList();
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public T findById(Id id) {
        return entityManager.find(clase, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param field
     * @param value
     * @param isString
     * @return
     */
    @Override
    public List<T> findBy(String field, String value, Boolean isString) {
        return entityManager.createQuery("select a from " + clase.getSimpleName() + " a where a." + field + (isString ? " like '" : " = ") + value + (isString ? "'" : "")).getResultList();
    }

    /**
     * Método para paginar del lado del servidor. Author: Jesus Fernandez Flores
     *
     * @param start
     * @param length
     * @param maps parámetros para la consulta
     * @return Un mapa el cual tendrá el número de resultados total, y una lista
     * con los resultados paginados.
     */
    @Override
    public PaginateUtil findPaginate(int start, int length, Map<String, Object>... maps) {
        Object[] params = null;
        StringBuilder sbQuery = new StringBuilder("select a from " + clase.getSimpleName() + " a");
        Long noTotal = getCountQuery(buildCountQuery(sbQuery.toString(), Boolean.FALSE), params);
        Long noTotalFiltered = noTotal;
        if (maps != null || maps.length > 0) {
            params = addParameters(sbQuery, Boolean.TRUE, maps);
            noTotalFiltered = getCountQuery(buildCountQuery(sbQuery.toString(), Boolean.FALSE), params);
        }
        List<T> result = executeQueryPaginate(sbQuery.toString(), start, length, params);
        return new PaginateUtil(result, noTotal, noTotalFiltered);
    }

    /**
     * {@inheritDoc}
     *
     * @param jpql
     * @param values
     * @return
     */
    @Override
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

    /**
     *
     * @param jpql
     * @param mapa
     * @param maximoResultados
     * @return
     */
    public List<T> executeQuery(String jpql, Map<String, Object> mapa, Integer maximoResultados) {
        Query q = entityManager.createQuery(jpql);
        List<T> result;

        for (Map.Entry<String, Object> entry : mapa.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        if (maximoResultados == null) {
            result = (List<T>) q.getResultList();
        } else {
            result = (List<T>) q.setMaxResults(maximoResultados).getResultList();
        }

        return result;
    }

    public List<T> executeQuery(int max, String jpql, Object... values) {
        Query q = entityManager.createQuery(jpql);
        int i = 1;
        for (Object object : values) {
            q.setParameter(i, object);
            i++;
        }
        q.setMaxResults(max);
        List<T> result = (List<T>) q.getResultList();
        return result;
    }

    public List<LinkedHashMap<String, Object>> executeQuery(String jpql, Map<String, Object> parametros, List<String> columnas, Integer maximoResultados) {
        Query q = entityManager.createQuery(jpql);
        List<Object[]> result;

        for (Map.Entry<String, Object> param : parametros.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
        }

        if (maximoResultados == null) {
            result = q.getResultList();
        } else {
            result = q.setMaxResults(maximoResultados).getResultList();
        }

        return creaModelo(result, columnas);
    }

    /**
     * {@inheritDoc}
     *
     * @param jpql
     * @return
     */
    public List<Object[]> executeNativeQuery(String jpql) {
        Query q = entityManager.createNativeQuery(jpql);
        return q.getResultList();
    }

    public List<Object[]> executeNativeQuery(String jpql, int start, int length) {
        Query q = entityManager.createNativeQuery(jpql).setFirstResult(start).setMaxResults(length);
        return q.getResultList();
    }

    public List<Object[]> executeNativeQuery(String jpql, Object... values) {
        Query q = entityManager.createNativeQuery(jpql);
        int i = 1;
        for (Object object : values) {
            q.setParameter(i, object);
            i++;
        }
        return q.getResultList();
    }

    /**
     * Obtiene lista de objetos agregando parámetros nombrados Author: Mario
     * Márquez
     *
     * @param sql Query en sql
     * @param mapa Parámetros para la query
     * @param maximoResultados Por si fuera necesario limitar los resultados
     * @return Una lista
     */
    public List<Object[]> executeNativeQuery(String sql, Map<String, Object> mapa, Integer maximoResultados) {
        List<Object[]> result;
        Query q;

        q = entityManager.createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : mapa.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        if (maximoResultados == null) {
            result = q.getResultList();
        } else {
            result = q.setMaxResults(maximoResultados).getResultList();
        }

        return result;
    }

    /**
     * Obtiene lista de objetos correspondiente la clase invocadora agregando
     * parámetros nombrados Author: Mario Márquez
     *
     * @param sql Query en sql
     * @param mapa Parámetros para la query
     * @param clas La clase del objeto que retornará
     * @param maximoResultados Por si fuera necesario limitar los resultados
     * @return Una lista
     */
    public List<T> executeNativeQuery(String sql, Map<String, Object> mapa, Class clas, Integer maximoResultados) {
        List<T> result;
        Query q;

        q = entityManager.createNativeQuery(sql, clas);
        for (Map.Entry<String, Object> entry : mapa.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        if (maximoResultados == null) {
            result = q.getResultList();
        } else {
            result = q.setMaxResults(maximoResultados).getResultList();
        }

        return (List<T>) result;
    }

    public List<T> executeNativeQuery(String sql, Class clase, Object... values) {
        Query q = entityManager.createNativeQuery(sql, clase);
        int i = 1;
        for (Object object : values) {
            q.setParameter(i, object);
            i++;
        }
        List<T> result = q.getResultList();
        return (List<T>) result;
    }

    /**
     * Obtiene rsultados de forma llave valor agregando parámetros nombrados
     * Author: Mario Márquez
     *
     * @param sql Query en sql
     * @param parametros Parámetros para la query
     * @param columnas Lista de cadenas que servirá como llaves para el mapa
     * resultado
     * @param maximoResultados Por si fuera necesario limitar los resultados
     * @return Una lista
     */
    public List<LinkedHashMap<String, Object>> executeNativeQuery(String sql, Map<String, Object> parametros, List<String> columnas, Integer maximoResultados) {

        List<Object[]> result;
        Query q;

        q = entityManager.createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : parametros.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        if (maximoResultados == null) {
            result = q.getResultList();
        } else {
            result = q.setMaxResults(maximoResultados).getResultList();
        }
        return creaModelo(result, columnas);
    }

    public int executeNativeUpdate(String sql) {
        Query q = entityManager.createNativeQuery(sql);
        return q.executeUpdate();
    }

    public int executeNativeUpdate(String sql, Object... values) {
        Query q = entityManager.createNativeQuery(sql);
        int i = 1;
        for (Object value : values) {
            q.setParameter(i, value);
            i++;
        }
        return q.executeUpdate();
    }

    public int executeUpdate(String jpql) {
        Query q = entityManager.createQuery(jpql);
        return q.executeUpdate();
    }

    public int executeUpdate(String jpql, Object... values) {
        Query q = entityManager.createQuery(jpql);
        int i = 1;
        for (Object value : values) {
            q.setParameter(i, value);
            i++;
        }
        return q.executeUpdate();
    }

    public T executeSingleQuery(String jpql, Object... values) {
        Query q = entityManager.createQuery(jpql).setMaxResults(1);
        int i = 1;
        for (Object object : values) {
            q.setParameter(i, object);
            i++;
        }
        T result = (T) q.getSingleResult();
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @param jpql
     * @param values
     * @return
     */
    @Override
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

    public List executeQueryObject(String jpql, Map<String, Object> mapa) {
        Query q = entityManager.createQuery(jpql);

        for (Map.Entry<String, Object> entry : mapa.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        List result = q.getResultList();
        return result;
    }

    public Service getDaos() {
        Service svc = null;
        if (svc == null) {
            try {
                ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
                svc = (Service) applicationContext.getBean("service");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return svc;
    }

    /**
     * Métodos para paginar del lado del servidor usando native querys. Author:
     * Jesus Fernandez Flores
     *
     * @param jpql
     * @param values
     * @return
     */
    //---------------------------------------------------------------------------------------
    public Long getCountNativeQuery(String jpql, Object... values) {
        Query q = entityManager.createNativeQuery(jpql);
        if (values != null) {
            int i = 1;
            for (Object object : values) {
                q.setParameter(i, object);
                i++;
            }
        }
        return Long.parseLong(q.getSingleResult().toString());
    }

    /**
     * Obtiene el count, con parámetros llave valor. Author: Mario Márquez
     *
     * @param sql Query en sql
     * @param mapa Parámetros para la query
     * @return Long Resultado del count
     */
    public Long getCountNativeQueryMap(String sql, Map<String, Object> mapa) {
        Query q = entityManager.createNativeQuery(sql);

        if (mapa != null) {
            for (Map.Entry<String, Object> entry : mapa.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        }

        return Long.parseLong(q.getSingleResult().toString());
    }

    public List<Object[]> executeNativeQueryPaginate(String jpql, int start, int length, Object... values) {
        Query q = entityManager.createNativeQuery(jpql).setFirstResult(start).setMaxResults(length);
        if (values != null) {
            int i = 1;
            for (Object object : values) {
                q.setParameter(i, object);
                i++;
            }
        }
        return q.getResultList();
    }

    /**
     * Obtiene objetos de una clase en específico, con parámetros llave valor,
     * para una query nativa Author: Mario Márquez
     *
     * @param sql Query en sql
     * @param start inicio paginación
     * @param length fin de paginación
     * @param mapa Parámetros para la query
     * @param type La clase esperada
     * @return Long Resultado del count
     */
    public List<T> executeNativeQueryPaginateMap(String sql, int start, int length, Map<String, Object> mapa, Class type) {
        Query q = entityManager.createNativeQuery(sql, type).setFirstResult(start).setMaxResults(length);

        if (mapa != null) {
            for (Map.Entry<String, Object> entry : mapa.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        }

        return (List<T>) q.getResultList();
    }

    public List<Object[]> executeNativeQueryPagClass(String jpql, int start, int length, Class type, Object... values) {
        Query q = entityManager.createNativeQuery(jpql, type).setFirstResult(start).setMaxResults(length);
        if (values != null) {
            int i = 1;
            for (Object object : values) {
                q.setParameter(i, object);
                i++;
            }
        }
        return q.getResultList();
    }

    public List<Object[]> executeNativeQueryPaginateMapped(String jpql, int start, int length, String mapping, Object... values) {
        Query q = entityManager.createNativeQuery(jpql, mapping).setFirstResult(start).setMaxResults(length);
        if (values != null) {
            int i = 1;
            for (Object object : values) {
                q.setParameter(i, object);
                i++;
            }
        }
        return q.getResultList();
    }
    //---------------------------------------------------------------------------------------

    /**
     * Métodos para paginar del lado del servidor usando querys Author: Jesus
     * Fernandez Flores
     *
     * @param jpql
     * @param values
     * @return
     */
    //---------------------------------------------------------------------------------------
    public Long getCountQuery(String jpql, Object... values) {
        Query q = entityManager.createQuery(jpql);
        if (values != null) {
            int i = 1;
            for (Object object : values) {
                q.setParameter(i, object);
                i++;
            }
        }
        return Long.parseLong(q.getSingleResult().toString());
    }

    /**
     * Método para obtener el total de una count query usando un mapa para
     * agregar parámetros Autor Mario márquez
     *
     * @param jpql
     * @param mapa
     * @return
     */
    //---------------------------------------------------------------------------------------
    public Long getCountQuery(String jpql, Map<String, Object> mapa) {
        Query q = entityManager.createQuery(jpql);

        for (Map.Entry<String, Object> entry : mapa.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        return Long.parseLong(q.getSingleResult().toString());
    }

    /**
     *
     * @param jpql
     * @param mapa
     * @return
     */
    public Long getCountQueryNamedParameters(String jpql, Map<String, Object> mapa) {
        Query q = entityManager.createQuery(jpql);

        for (Map.Entry<String, Object> entry : mapa.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }

        return Long.parseLong(q.getSingleResult().toString());
    }

    public List<T> executeQueryPaginate(String jpql, int start, int length, Object... values) {
        Query q = entityManager.createQuery(jpql).setFirstResult(start).setMaxResults(length);
        if (values != null) {
            int i = 1;
            for (Object object : values) {
                q.setParameter(i, object);
                i++;
            }
        }
        List<T> result = (List<T>) q.getResultList();
        return result;
    }
    //---------------------------------------------------------------------------------------

    /**
     * Ejecuta un procedimiento almacenado Author: Jesus Fernandez Flores
     *
     * @param sp Nombre del procedimiento almacenado mapeado
     * @param maps Parametros
     * @return
     */
    public List<Object[]> executeStoredProcedure(String sp, Map<String, Object>... maps) {
        Query q = entityManager.createNamedQuery(sp);
        for (Map<String, Object> map : maps) {
            if (!map.isEmpty()) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        return q.getResultList();
    }

    public List<T> executeStoredProcedureGeneric(String sp, Map<String, Object>... maps) {
        Query q = entityManager.createNamedQuery(sp);
        for (Map<String, Object> map : maps) {
            if (!map.isEmpty()) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        return q.getResultList();
    }

    public Long getStoredProcedureCount(String sp, Map<String, Object>... maps) {
        Query q = entityManager.createNamedQuery(sp);
        for (Map<String, Object> map : maps) {
            if (!map.isEmpty()) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        Object[] temp = (Object[]) q.getSingleResult();
        return Long.parseLong(temp[0].toString());
    }

    /**
     * Agrega criterios a una query Author: Mario Márquez
     *
     * @param sb Nombre del StringBuilder
     * @param criteria Filtros a agregar
     */
    public final void agregaCriterios(StringBuilder sb, List<String> criteria) {
        for (int i = 0; i < criteria.size(); i++) {
            if (i > 0) {
                sb.append(" AND ");
            } else {
                sb.append(" WHERE ");
            }
            sb.append(criteria.get(i));
        }
    }

    /**
     * Crea un Mapa hasheable, que mantiene el orden de los elementos como
     * fueron ingresados. El mapa es creado a partir de una lista de cadenas de
     * texto, y una lista de resultados de tipo lista de objetos. Liga los
     * resultados con cada una des estas cadenas, dentro del mapa llava valor.
     * Author: Mario Márquez
     *
     * @param resultDB Lista de resultados Object[], provenientes de la BD
     * @param columnas Lista de String con el nombre de las columnas
     * @return List<LinkedHashMap<String, Object>> Lista de mapas llave valor
     */
    public final List<LinkedHashMap<String, Object>> creaModelo(List<Object[]> resultDB, List<String> columnas) {
        List<LinkedHashMap<String, Object>> modelst = new ArrayList();

        Integer originalSize = resultDB.size();
        for (int i = 0; i < originalSize; i++) {
            Object[] object = resultDB.get(0);
            LinkedHashMap model = new LinkedHashMap();
            for (int j = 0; j < columnas.size(); j++) {
                model.put(creaLlaveDesdeColumna(columnas.get(j)), object[j]);
            }
            modelst.add(model);
            resultDB.remove(0);
        }

        return modelst;
    }

    /**
     * Extrae una cadena de texto en caso de encontrar comillas, y si no,
     * regresa la cadena completa Se asume que cuando hay comillas es un Alias,
     * y se usará como encabezado del excel (columna) Author: Mario Márquez
     *
     * @param columna String con la información de las columna SQL
     * @return String cadena de texto para ser usada como llave en un mapa
     */
    private String creaLlaveDesdeColumna(String columna) {
        String llave;

        Integer first = columna.indexOf("\"") + 1;
        Integer second = columna.indexOf("\"", first);
        Boolean alias = (first != -1) && (second != -1);

        if (alias) {
            llave = columna.substring(first, second);
        } else {
            llave = columna;
        }

        return llave;
    }

    public final void agregaColumnas(StringBuilder sb, List<String> columnas) {
        for (int i = 0; i < columnas.size(); i++) {
            sb.append(" ");
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(columnas.get(i));
        }
    }

    /**
     * Crea una cadena de texto con un decode básico, para ir de 0 y 1, a no y
     * sí Author: Mario Márquez
     *
     * @param columna String con la información de las columna SQL
     * @param alias String con la información del alias
     * @return String cadena de texto para ser usada en el select de una
     * consulta
     */
    public final String decodeBoolean(String columna, String alias) {
        StringBuilder decode = new StringBuilder();

        decode.append(" Decode(");
        decode.append(columna);
        decode.append(" ,");
        decode.append(" 0, 'No',");
        decode.append(" 1, 'Sí',");
        decode.append(" 'Error'");
        decode.append(" )");
        decode.append(" \"");
        decode.append(alias);
        decode.append("\"");

        return decode.toString();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BitacoraDao;
import com.becasipn.persistence.model.Bitacora;
import com.becasipn.util.PaginateUtil;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Query;

/**
 * Implementación de las operaciones CRUD de la bitácora de modificaciones del
 * sistema (auditoria)
 *
 * Todos los cambios de Alta, Modificación o Baja de los objetos del sistema son
 * registrados. Las pantallas de consulta, solo explotan información relativa a
 * los Alumnos y Personal Académico pero podrían consultarse cambios realizados
 * en cualquier que haya sido persistido por el sistema.
 *
 * @author Patricia Benitezópez <yolanda.lopez@fit.com.mx>
 * @version $Rev: 1169 $
 * @since 1.0
 */
public class BitacoraJpaDao extends JpaDaoBitacora<Bitacora, BigDecimal> implements BitacoraDao {

    public BitacoraJpaDao() {
        super(Bitacora.class);
    }

    /**
     * Obtiene los movimientos de creación, modificación o borrado que se hayan
     * sido realizados sobre un objeto en base a los criterio releccionados
     *
     * @param objetos Tipo de objeto sobre el que se quieren ver los movimientos
     * (Alumno,Tutor,Facilitador)
     * @param fechaIni Rango inicial para la búsqueda que será comparada con la
     * fecha de ejecución del movimiento
     * @param fechaFin Rango final para la búsqueda que será comparada con la
     * fecha de ejecución del movimiento
     * @param objectName Identificador del objeto
     * @param adminUserId Usuario que realizó el movimiento
     * @return Una lista de movimientos de la bitácora
     */
    @Override
    public List<Bitacora> findByParameters(Map objetos, Date fechaIni, Date fechaFin, String objectName, String adminUserId) {

        Query qry = null;
        String jpql = "SELECT bitacora FROM Bitacora bitacora WHERE bitacora.fechaModificacion between ?1 and ?2";

        if (objectName != null) {
            jpql += " AND (( bitacora.objectName LIKE '%" + objectName + "%'";
        }

        if (adminUserId != null) {
            jpql += " AND (( bitacora.userId = '" + adminUserId + "'";
        }

        if (objetos != null && !objetos.isEmpty()) {

            Set datos = objetos.entrySet();
            String objectIds = "";
            String userIds = "";
            int i = 0;
            for (Iterator it = datos.iterator(); it.hasNext(); i++) {
                Map.Entry entry = (Map.Entry) it.next();
                if (i == datos.size() - 1) {
                    objectIds += String.format("'%s'", entry.getKey());
                    userIds += String.format("'%s'", entry.getValue());
                } else {
                    objectIds += String.format("'%s', ", entry.getKey());
                    userIds += String.format("'%s', ", entry.getValue());
                }
            }
            jpql += " AND bitacora.objectId in (" + objectIds;
            jpql += ")) OR (bitacora.userId IN (" + userIds + ")))";

        } else {
            jpql += "))";
        }

        qry = getEntityManager().createQuery(jpql);
        qry.setParameter(1, fechaIni);
        qry.setParameter(2, fechaFin);

        List<Bitacora> lstBitacora = qry.getResultList();

        if (lstBitacora != null) {
            return lstBitacora;
        } else {
            return null;
        }
    }

    @Override
    public List<Bitacora> findBy(String field, String value, Boolean isString) {
        return entityManager.createQuery("select a from " + clase.getSimpleName() + " a where a." + field + (isString ? " like " : " = ") + value).getResultList();
    }
    
    @Override
    public PaginateUtil findPaginate(int start, int length, Map<String, Object> ... maps) {   
        return null;
    }


}

package com.becasipn.business;

import com.becasipn.persistence.model.Accion;
import com.becasipn.persistence.model.Bitacora;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import java.util.Date;

/**
 * Implementa operaciones de lógica de negocio para la consulta del Historial de
 * cambios
 *
 * @author Patricia Benitezópez Monroy <yolanda.lopez@fit.com.mx>
 * @version $Rev: 1212 $
 * @since 1.0
 */
public class BitacoraBO extends BaseBO {

    public BitacoraBO(Service service) {
        super(service);
    }

    /**
     * Registra los movimientos de creación, modificación o borrado de un objeto
     * del sistema. Se guarda el estado anterior del objeto si existe y el nuevo
     * objeto
     *
     * @param user Usuario que realiza el movimiento
     * @param object Nombre del objeto que es modificado
     * @param objectId Identificador del objeto
     * @param descripcion Estado en formato de cadena del objeto modificado
     * @param accion Tipo de Acción (ALTA | MODIFICACION | BAJA)
     */
    public Bitacora saveBitacora(Usuario user, String object, String objectId, String descripcion, Accion accion) {
        if (user == null || accion == null) {
            return null;
        }
        log.debug("::saveBitacora:: registrando el historico del cambio");
        Bitacora bitacora = new Bitacora();

        if (isAdministrativo()) {
            bitacora.setAdministrativo(true);
        } else {
            bitacora.setAdministrativo(false);
        }

        bitacora.setUsuario(user);
        bitacora.setObjectId(objectId);
        bitacora.setObjectName(object);
        bitacora.setDescripcion(descripcion);
        bitacora.setFechaModificacion(new Date());
        bitacora.setAccion(accion);
        return service.getBitacoraDao().save(bitacora);
    }

}

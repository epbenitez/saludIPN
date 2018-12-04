package com.becasipn.actions.catalogos;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.UnidadAcademicaBO;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.UnidadAcademica;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministracionUnidadAcademicaAction extends BaseAction implements MensajesCatalogos {

    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";
    private UnidadAcademica unidadAcademica = new UnidadAcademica();
    private List<PersonalAdministrativo> contactosList = new ArrayList<>();

    /**
     * Lista de elementos del formulario.
     *
     * @return
     */
    public String lista() {
        return SUCCESS;
    }

    /**
     * Formulario de registro de un nuevo elemento.
     *
     * @return
     */
    public String form() {
        return SUCCESS;
    }

    /**
     * Acciones para el botón Guardar.
     *
     * @return
     */
    public String guarda() {
        UnidadAcademicaBO bo = new UnidadAcademicaBO(getDaos());
        if (unidadAcademica.getContactoAlterno() == null || unidadAcademica.getContactoAlterno().getId() == null) {
            PersonalAdministrativo contactoAlterno = null;
            unidadAcademica.setContactoAlterno(contactoAlterno);
        }
        if (bo.guardaUnidadAcademica(unidadAcademica)) {
            addActionMessage(getText("catalogo.guardado.exito"));
        } else {
            addActionError(getText("catalogo.guardado.error"));
        }
        return FORMULARIO;
    }

    public String edicion() {
        if (unidadAcademica == null || unidadAcademica.getId() == null) {
            addActionError(getText("catalogo.guardado.error"));
        }
        //Traemos los contactos que han sido dados 
        contactosList = getDaos().getPersonalAdministrativoDao().personalAdministrativoResponsableBecas(unidadAcademica.getId());
        
        UnidadAcademicaBO bo = new UnidadAcademicaBO(getDaos());
        //Traemos todos los datos de mi objeto
        unidadAcademica = bo.getUnidadAcademica(unidadAcademica.getId());
        return SUCCESS;
    }

    public String eliminar() {
        UnidadAcademicaBO bo = new UnidadAcademicaBO(getDaos());
        if (unidadAcademica == null || unidadAcademica.getId() == null) {
            addActionError(getText("catalogo.eliminado.error"));
        }
        //Traemos todos los datos de mi objeto.
        unidadAcademica = bo.getUnidadAcademica(unidadAcademica.getId());
        //Validamos que no sea utilizada por la tabla Proceso.
        if (unidadAcademica.getId() != null && bo.asociadaProceso(unidadAcademica.getId())) {
            addActionError(getText("catalogo.eliminado.error.unidadAcademica.usada"));
            return LISTA;
        }
        //Validamos que no sea utilizada por la tabla Alumno.
        if (unidadAcademica.getId() != null && bo.asociadaAlumno(unidadAcademica.getId())) {
            addActionError(getText("catalogo.eliminado.error.unidadAcademica.usada"));
            return LISTA;
        }
        //Validamos que no sea utilizada por la tabla OrdenDeposito.
        if (unidadAcademica.getId() != null && bo.asociadaUnidadAcademicaOrdenDeposito(unidadAcademica.getId())) {
            addActionError(getText("catalogo.eliminado.error.unidadAcademica.usada"));
            return LISTA;
        }
        //Validamos que no sea utilizada por la tabla PersonalAdministrativo.
        if (unidadAcademica.getId() != null && bo.asociadaPersonalAdministrativo(unidadAcademica.getId())) {
            addActionError(getText("catalogo.eliminado.error.unidadAcademica.usada"));
            return LISTA;
        }
        //Validamos que no sea utilizada por la tabla PresupuestoUnidadAcademica.
        if (unidadAcademica.getId() != null && bo.asociadaPresupuestoUnidadAcademica(unidadAcademica.getId())) {
            addActionError(getText("catalogo.eliminado.error.unidadAcademica.usada"));
            return LISTA;
        }
        Boolean res = bo.eliminaUnidadAcademica(unidadAcademica);
        if (res) {
            addActionMessage(getText("catalogo.eliminado.exito"));
        } else {
            addActionError(getText("catalogo.eliminado.error"));
        }
        return LISTA;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public List<PersonalAdministrativo> getContactosList() {
        return contactosList;
    }

    public void setContactosList(List<PersonalAdministrativo> contactosList) {
        this.contactosList = contactosList;
    }
    
}

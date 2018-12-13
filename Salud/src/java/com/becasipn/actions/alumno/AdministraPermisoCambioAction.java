package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.persistence.model.Usuario;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
public class AdministraPermisoCambioAction extends BaseAction implements MensajesAlumno {

    public static final String LISTA = "lista";

    private final AlumnoBO alumnoBO;
    private final Usuario usuario;


    public AdministraPermisoCambioAction() {
        alumnoBO = new AlumnoBO(getDaos());
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
    }

    public String lista() {
        return SUCCESS;
    }

}

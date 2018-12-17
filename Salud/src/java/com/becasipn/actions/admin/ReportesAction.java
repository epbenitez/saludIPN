package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.util.List;

/**
 *
 * @author Gustavo A. Alamillo
 */
public class ReportesAction extends BaseAction {

    public List<Periodo> periodos;
    public boolean responsable;
    public UnidadAcademica ua;
    public Cuestionario cuestionario;
    private String periodo;

    public String cuestionarios() {
        if (isAlumno() || !isAuthenticated()) {
            return ERROR;
        }
        responsable = isResponsable();
        if (responsable) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
            ua = personal.getUnidadAcademica();
        }
        periodos = getDaos().getPeriodoDao().findAll();
        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getClave();
        return SUCCESS;
    }

}

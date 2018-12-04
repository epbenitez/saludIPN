package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.TipoBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public class TipoBecaAjaxAction extends JSONAjaxAction {
    
    private BigDecimal uaId;
    private BigDecimal nivel;
    private BigDecimal pkBeca;
    
    public String listado() {
        
        List<TipoBeca> list = getDaos().getTipoBecaDao().findAll();
        for (TipoBeca tipoBeca : list) {
            
            getJsonResult().add("[\"" + tipoBeca.getNombre()
                    + "\", \"" + tipoBeca.getClave()
                    + "\", \"<a title='Editar tipo de beca' class='fancybox fancybox.iframe solo-lectura'  href='/catalogos/edicionTipoBeca.action?tipoBeca.id=" + tipoBeca.getId() + "'> <span class=\\\"fa-stack\\\"><i class=\\\"fa fa-square fa-stack-2x\\\"></i><i class=\\\"fa fa-pencil-square-o fa-stack-1x fa-inverse\\\"></i></span> </a>"
                    + "\", \"<a title='Eliminar tipo de beca' onclick='eliminar(" + tipoBeca.getId() + ")' class='solo-lectura table-link danger'> <span class=\\\"fa-stack\\\"><i class=\\\"fa fa-square fa-stack-2x\\\"></i><i class=\\\"fa fa-trash-o fa-stack-1x fa-inverse\\\"></i></span> </a>\"]");
            
        }
        return SUCCESS_JSON;
    }
    
    public String getProgramaBeca() {
        List<TipoBeca> beca = getDaos().getTipoBecaDao().findByNivel(getDaos().getUnidadAcademicaDao().findById(uaId).getNivel().getId());
        if (beca == null) {
        } else {
            for (TipoBeca tb : beca) {
                getJsonResult().add("[\"" + tb.getId()
                        + "\", \"" + tb.getNombre()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }
    
    public String getBecasPorNivel() {
        List<TipoBeca> becas;
        if (nivel.equals(new BigDecimal(0))) {
            becas = getDaos().getTipoBecaDao().findAll();
        } else {
            becas = getDaos().getTipoBecaDao().findByNivel(nivel);
        }
        
        for (TipoBeca tb : becas) {
            getJsonResult().add("[\"" + tb.getId()
                    + "\", \"" + tb.getNombre()
                    + " \",\"" + tb.getClave() + "\"]");
        }
        return SUCCESS_JSON;
    }
    
    public String get() {
        List<TipoBeca> becas;
        if (pkBeca == null || pkBeca.equals(new BigDecimal(0))) {
            becas = getDaos().getTipoBecaDao().findAll();
            getJsonResult().add("[\"0\", \"-Todas- \",\"0\"]");
        } else {
            becas = getDaos().getTipoBecaDao().getTipoBecaPorBeca(pkBeca);
            getJsonResult().add("[\"0\", \"-Todas " + getDaos().getBecaDao().findById(pkBeca).getNombre() + "- \",\"0\"]");
        }
        for (TipoBeca tb : becas) {
            getJsonResult().add("[\"" + tb.getId()
                    + "\", \"" + tb.getNombre()
                    + " \",\"" + tb.getClave() + "\"]");
        }
        return SUCCESS_JSON;
    }

    /**
     * Función para mostrar los requisitos de las becas
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String requisitos() {
        if (uaId == null) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
            uaId = personal.getUnidadAcademica().getId();
        }
        UnidadAcademica unidadAcademica = getDaos().getUnidadAcademicaDao().findById(uaId);
        List<TipoBecaPeriodo> becasPorNivelPeriodo = getDaos().getTipoBecaPeriodoDao().becasPorNivelPeriodo(unidadAcademica.getNivel().getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
        for (TipoBecaPeriodo beca : becasPorNivelPeriodo) {
            getJsonResult().add("[\"" + beca.getTipoBeca().getNombre()
                    + "\", \"" + (beca.getRegular() == 1 ? "Si" : "No")
                    + "\", \"" + beca.getPromedioMinimo()
                    + "\", \"" + beca.getPromedioMaximo()
                    + "\", \"" + beca.getSemestreMinimo()
                    + "\", \"" + beca.getSemestreMaximo()
                    + "\", \"" + (beca.getIngresoSalarios() == null ? "N/A" : beca.getIngresoSalarios())
                    + "\", \"" + (beca.getModalidad() == null ? "Independiente" : beca.getModalidad().getNombre())
                    + "\", \"" + (beca.getAreasconocimiento() == null ? "Independiente" : beca.getAreasconocimiento().getNombre())
                    + "\", \"" + (beca.getFases() == null ? "N/A" : beca.getFases())
                    + "\"]");
        }
        return SUCCESS_JSON;
    }
    
    public BigDecimal getUaId() {
        return uaId;
    }
    
    public void setUaId(BigDecimal uaId) {
        this.uaId = uaId;
    }
    
    public BigDecimal getPkBeca() {
        return pkBeca;
    }
    
    public void setPkBeca(BigDecimal pkBeca) {
        this.pkBeca = pkBeca;
    }
    
    public BigDecimal getNivel() {
        return nivel;
    }
    
    public void setNivel(BigDecimal nivel) {
        this.nivel = nivel;
    }
    
}

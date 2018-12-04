package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Usre-05
 */
public class TipoBecaPeriodoAjaxAction extends JSONAjaxAction {

    private BigDecimal unidadAcademica;
    private BigDecimal periodoId;
    private BigDecimal nivelId;

    public String listado() {

        List<TipoBecaPeriodo> list = getDaos().getTipoBecaPeriodoDao().findAll();
        for (TipoBecaPeriodo tipoBecaPeriodo : list) {
            // Para mostrar únicamente las becas visibles
            if (tipoBecaPeriodo.getVisible() != null && tipoBecaPeriodo.getVisible()) {
                getJsonResult().add("[\"" + tipoBecaPeriodo.getTipoBeca().getNombre()
                        + "\", \"" + tipoBecaPeriodo.getNivel().getNombre()
                        + "\", \"" + tipoBecaPeriodo.getPeriodo().getClave()
                        + "\", \"" + tipoBecaPeriodo.getMonto()
                        + "\", \"" + "<input  type=checkbox value='" + tipoBecaPeriodo.getId() + "' onclick='transferir(this.checked, this.value);' class='solo-lectura'/>"
                        + "\", \"" + (Objects.equals(tipoBecaPeriodo.getEstatus().getId(), new BigDecimal("1")) ? "<a title='Activo' class='table-link'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-check-square-o fa-stack-1x fa-inverse'></i></span></a>" : "<a title='Inactivo' class='table-link'><span class='fa-stack'><span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-square-o fa-stack-1x fa-inverse'></i></span></a>")
                        + "\", \"<a title='Editar tipo de beca' class='solo-lectura fancybox fancybox.iframe'  href='/catalogos/edicionTipoBecaPeriodo.action?tipoBecaPeriodo.id=" + tipoBecaPeriodo.getId() + "'> <span class=\\\"fa-stack\\\"><i class=\\\"fa fa-square fa-stack-2x\\\"></i><i class=\\\"fa fa-pencil-square-o fa-stack-1x fa-inverse\\\"></i></span> </a>"
                        + "\", \"<a title='Eliminar tipo de beca' onclick='eliminar(" + tipoBecaPeriodo.getId() + ")' class='solo-lectura table-link danger'> <span class=\\\"fa-stack\\\"><i class=\\\"fa fa-square fa-stack-2x\\\"></i><i class=\\\"fa fa-trash-o fa-stack-1x fa-inverse\\\"></i></span> </a>\"]");
            }
        }
        return SUCCESS_JSON;
    }

    /**
     * Acción que obtiene los tipos de beca por nivel
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String get() {
        UnidadAcademica ua = getDaos().getUnidadAcademicaDao().findById(unidadAcademica);
        List<TipoBecaPeriodo> becas = getDaos().getTipoBecaPeriodoDao().becasPorNivelPeriodo(ua.getNivel().getId(), getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());
        if (becas != null) {
            for (TipoBecaPeriodo beca : becas) {
                getJsonResult().add("[\"" + beca.getTipoBeca().getId()
                        + "\", \"" + beca.getTipoBeca().getNombre()
                        + "\"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getBecasPorNivel() {
        UnidadAcademica ua = getDaos().getUnidadAcademicaDao().findById(unidadAcademica);
        List<TipoBecaPeriodo> becas = getDaos().getTipoBecaPeriodoDao().becasPorNivelPeriodo(ua.getNivel().getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
        if (becas != null) {
            for (TipoBecaPeriodo beca : becas) {
                getJsonResult().add("[\"" + beca.getId()
                        + "\", \"" + (beca.getVisible() ? beca.getTipoBeca().getNombre() : beca.getTipoBeca().getNombre() + " (C)")
                        + "\"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String becasEstatusDeposito() {
        List<Beca> becas = getDaos().getTipoBecaPeriodoDao().becasPorNivelPeriodoUa(nivelId, periodoId, unidadAcademica);
        if (becas != null) {
            for (Beca beca : becas) {
                getJsonResult().add("[\"" + beca.getId()
                        + "\", \"" + beca.getNombre()
                        + "\"]");
            }
        }
        return SUCCESS_JSON;
    }

    public BigDecimal getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(BigDecimal unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public BigDecimal getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(BigDecimal periodoId) {
        this.periodoId = periodoId;
    }

    public BigDecimal getNivelId() {
        return nivelId;
    }

    public void setNivelId(BigDecimal nivelId) {
        this.nivelId = nivelId;
    }

}

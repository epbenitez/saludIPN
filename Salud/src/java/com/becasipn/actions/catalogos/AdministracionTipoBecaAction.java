package com.becasipn.actions.catalogos;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.TipoBecaBO;
import com.becasipn.persistence.model.TipoBeca;

/**
 *
 * @author Usre-05
 */
public class AdministracionTipoBecaAction extends BaseAction implements MensajesCatalogos {

    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";
    private TipoBeca tipoBeca = new TipoBeca();

    public String lista() {
        return SUCCESS;
    }

    public String form() {
        return SUCCESS;
    }

    public String guarda() {
        TipoBecaBO bo = new TipoBecaBO(getDaos());

        if (bo.guardaTipoBeca(tipoBeca)) {
            addActionMessage(getText("catalogo.guardado.exito"));
        } else {
            tipoBeca = new TipoBeca();  //LIMPIAMOS EL OBJ TIPO BECA POR QUE AL MOSTRAR EL ERROR E INTENTAR GUARDAR, TRAE EL OBJ ANTERIOR Y ACTUALIZA
            addActionError(getText("catalogo.guardado.error"));
        }

        return FORMULARIO;
    }

    public String edicion() {

        if (tipoBeca == null || tipoBeca.getId() == null) {
            addActionError(getText("catalogo.guardado.error"));
        }

        TipoBecaBO bo = new TipoBecaBO(getDaos());
        tipoBeca = bo.getTipoBeca(tipoBeca.getId());
        System.out.print(tipoBeca.getId());
        return SUCCESS;
    }

    public String eliminar() {
        if (tipoBeca == null || tipoBeca.getId() == null) {
            addActionError(getText("catalogo.guardado.error"));
        }

        TipoBecaBO bo = new TipoBecaBO(getDaos());
        tipoBeca = bo.getTipoBeca(tipoBeca.getId());

        if (tipoBeca.getId() != null && bo.tipoBecaEnUso(tipoBeca.getId())) {
            addActionError(getText("catalogo.eliminado.error.otro"));
            return LISTA;
        }

        Boolean res = bo.eliminaTipoBeca(tipoBeca);
        if (res) {
            addActionMessage(getText("catalogo.guardado.exito"));
        } else {
            addActionError(getText("catalogo.guardado.error"));
        }

        return LISTA;
    }

    public TipoBeca getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(TipoBeca tipoBeca) {
        this.tipoBeca = tipoBeca;
    }
}

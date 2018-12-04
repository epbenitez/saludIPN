package com.becasipn.actions.catalogos;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.TipoBecaPeriodoBO;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.util.Tupla;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public class AdministracionTipoBecaPeriodoAction extends BaseAction implements MensajesCatalogos {

    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";
    private TipoBecaPeriodo tipoBecaPeriodo = new TipoBecaPeriodo();
    private String transferencias;
    private List<Tupla<Integer,String>> validacionInscripcionList;

    public String lista() {
        return SUCCESS;
    }

    public String form() {
        return SUCCESS;
    }

    public String guarda() {
        TipoBecaPeriodoBO bo = new TipoBecaPeriodoBO(getDaos());

        if (bo.guardaTipoBecaPeriodo(tipoBecaPeriodo)) {
            addActionMessage(getText("catalogo.guardado.exito"));
        } else {
            addActionError(getText("catalogo.guardado.error"));
        }        
        tipoBecaPeriodo = bo.getTipoBecaPeriodo(tipoBecaPeriodo.getId());
        return FORMULARIO;
    }

    public String edicion() {

        if (tipoBecaPeriodo == null || tipoBecaPeriodo.getId() == null) {
            addActionError(getText("catalogo.guardado.error"));
        }

        TipoBecaPeriodoBO bo = new TipoBecaPeriodoBO(getDaos());
        tipoBecaPeriodo = bo.getTipoBecaPeriodo(tipoBecaPeriodo.getId());
        validacionInscripcionList= new ArrayList<>();
        validacionInscripcionList.add(new Tupla<Integer, String>(0, "Sin validación de inscripción"));
        validacionInscripcionList.add(new Tupla<Integer, String>(1, "Validación de datos académicos"));
        validacionInscripcionList.add(new Tupla<Integer, String>(2, "Validación de inscripción"));
        validacionInscripcionList.add(new Tupla<Integer, String>(3, "Validación de inscripción y regularidad"));
        return SUCCESS;
    }

    public String eliminar() {
        if (tipoBecaPeriodo == null || tipoBecaPeriodo.getId() == null) {
            addActionError(getText("catalogo.eliminado.error"));
        }

        TipoBecaPeriodoBO bo = new TipoBecaPeriodoBO(getDaos());
        tipoBecaPeriodo = bo.getTipoBecaPeriodo(tipoBecaPeriodo.getId());

        if (tipoBecaPeriodo.getId() != null && bo.tipoBecaPeriodoEnUso(tipoBecaPeriodo.getId())) {
            addActionError(getText("catalogo.eliminado.error.otorgamientos.asociados"));
            return LISTA;
        }
        
        if (tipoBecaPeriodo.getId() != null && bo.tipoBecaPeriodoEnUsoPresupuesto(tipoBecaPeriodo.getId())) {
            addActionError(getText("catalogo.eliminado.error.presupuesto.asociados"));
            return LISTA;
        }
        
        Boolean res = bo.eliminaTipoBecaPeriodo(tipoBecaPeriodo);
        if (res) {
            addActionMessage(getText("catalogo.eliminado.exito"));
        } else {
            addActionError(getText("catalogo.eliminado.error"));
        }

        return LISTA;
    }

    public String transferir() {
        if (transferencias != null && transferencias.length() > 1) {
            transferencias = transferencias.substring(1);
            TipoBecaPeriodoBO bo = new TipoBecaPeriodoBO(getDaos());
            Boolean res = bo.guardaTransferencias(transferencias);
            if (res) {
                addActionMessage(getText("catalogo.transferencias.exito"));
            } else {
                addActionError(getText("catalogo.transferencias.error"));
            }
        }
        return LISTA;
    }

    public TipoBecaPeriodo getTipoBecaPeriodo() {
        return tipoBecaPeriodo;
    }

    public void setTipoBecaPeriodo(TipoBecaPeriodo tipoBecaPeriodo) {
        this.tipoBecaPeriodo = tipoBecaPeriodo;
    }

    public String getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(String transferencias) {
        this.transferencias = transferencias;
    }

    public List<Tupla<Integer, String>> getValidacionInscripcionList() {
        return validacionInscripcionList;
    }

    public void setValidacionInscripcionList(List<Tupla<Integer, String>> validacionInscripcionList) {
        this.validacionInscripcionList = validacionInscripcionList;
    }

}

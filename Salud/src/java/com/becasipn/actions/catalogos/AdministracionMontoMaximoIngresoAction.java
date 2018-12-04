package com.becasipn.actions.catalogos;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.MontoMaximoIngresosBO;
import com.becasipn.persistence.model.MontoMaximoIngresos;
import static com.opensymphony.xwork2.Action.SUCCESS;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public class AdministracionMontoMaximoIngresoAction extends BaseAction implements MensajesCatalogos {

    public static final String FORMULARIO = "formulario";
    private MontoMaximoIngresos montoMaximoIngresos = new MontoMaximoIngresos();
    boolean deshabilitados = false;

    public String lista() {
        return SUCCESS;
    }

    public String formulario() {
        if (montoMaximoIngresos == null || montoMaximoIngresos.getId() == null) {
            montoMaximoIngresos = new MontoMaximoIngresos();
        } else {
            montoMaximoIngresos = getDaos().getMontoMaximoIngresosDao().findById(montoMaximoIngresos.getId());
            deshabilitados = true;
        }
        return SUCCESS;
    }

    public String guarda() {
        MontoMaximoIngresosBO bo = new MontoMaximoIngresosBO(getDaos());
        //No pueden existir dos registros para el mismo periodo y el mismo tipo de ingreso.
        if (montoMaximoIngresos.getId() == null && getDaos().getMontoMaximoIngresosDao().existeRegistro(montoMaximoIngresos.getPeriodo().getId(), montoMaximoIngresos.getDeciles())) {
            addActionError(getText("catalogo.no.monto.maximo.repetido"));
        } else {
            if (bo.guarda(montoMaximoIngresos)) {
                addActionMessage(getText("catalogo.guardado.exito"));
                deshabilitados = true;
            } else {
                addActionError(getText("catalogo.guardado.error"));
            }
        }
        return FORMULARIO;
    }

    public MontoMaximoIngresos getMontoMaximoIngresos() {
        return montoMaximoIngresos;
    }

    public void setMontoMaximoIngresos(MontoMaximoIngresos montoMaximoIngresos) {
        this.montoMaximoIngresos = montoMaximoIngresos;
    }

    public boolean isDeshabilitados() {
        return deshabilitados;
    }

    public void setDeshabilitados(boolean deshabilitados) {
        this.deshabilitados = deshabilitados;
    }
}
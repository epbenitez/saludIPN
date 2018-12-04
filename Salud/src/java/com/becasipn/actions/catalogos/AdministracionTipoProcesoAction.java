package com.becasipn.actions.catalogos;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.TipoProcesoBO;
import com.becasipn.persistence.model.TipoProceso;
import static com.opensymphony.xwork2.Action.SUCCESS;

public class AdministracionTipoProcesoAction extends BaseAction implements MensajesCatalogos {

    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";
    private TipoProceso proceso = new TipoProceso();

    public String lista() {
	return SUCCESS;
    }

    public String form() {
	return SUCCESS;
    }

    public String guarda() {
	TipoProcesoBO bo = new TipoProcesoBO(getDaos());
	if (bo.guardaProceso(proceso)) {
	    addActionMessage(getText("catalogo.guardado.exito"));
	} else {
	    proceso = new TipoProceso();  //LIMPIAMOS EL OBJ TIPO BECA POR QUE AL MOSTRAR EL ERROR E INTENTAR GUARDAR, TRAE EL OBJ ANTERIOR Y ACTUALIZA
	    addActionError(getText("catalogo.guardado.error"));
	}

	return FORMULARIO;
    }

    public String edicion() {
	if (proceso == null || proceso.getId() == null) {
	    addActionError(getText("catalogo.actualizado.error"));
	}

	TipoProcesoBO bo = new TipoProcesoBO(getDaos());
	proceso = bo.getProceso(proceso.getId());
	return SUCCESS;
    }

    public String eliminar() {
	if (proceso == null || proceso.getId() == null) {
	    addActionError(getText("catalogo.eliminado.error"));
	}

	TipoProcesoBO bo = new TipoProcesoBO(getDaos());
	proceso = bo.getProceso(proceso.getId());

	//Validamos que no este siendo usado por algun proceso
	if (proceso.getId() != null && bo.procesoEnUso(proceso.getId())) {
	    addActionError(getText("catalogo.eliminado.error"));
	    return LISTA;
	}

	Boolean res = bo.eliminaProceso(proceso);
	if (res) {
	    addActionMessage(getText("catalogo.eliminado.exito"));
	} else {
	    addActionError(getText("catalogo.eliminado.error"));
	}

	return LISTA;
    }

    public TipoProceso getProceso() {
	return proceso;
    }

    public void setProceso(TipoProceso proceso) {
	this.proceso = proceso;
    }

}

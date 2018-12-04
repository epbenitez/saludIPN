package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.ERROR_JSON;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.ProgressBarManagerUtil;
import com.becasipn.util.Util;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;

public class CargaPadronAjaxAction extends JSONAjaxAction {

	// Variable para sincronizar la barra de progreso
	private ProgressBarManagerUtil pbmu;

	private String opcion;

	public String valores() {
		getJsonResult().add("[\"" + getDaos().getPadronSubesDao().getCargados(getDaos().getPeriodoDao().getPeriodoActivo().getId()) + ","
				+ getDaos().getPadronSubesDao().getSolicitudesCreadas(getDaos().getPeriodoDao().getPeriodoActivo().getId(), new BigDecimal(5)) + ","
				+ getDaos().getPadronSubesDao().getSolicitudesCreadas(getDaos().getPeriodoDao().getPeriodoActivo().getId(), new BigDecimal(4)) + "\"]"
		);
		return SUCCESS_JSON;
	}

	public String sincroniza() {
		if (getPbmu() == null) {
			String result = setPbmu();
			if (result.equals(ERROR_JSON)) {
				return SUCCESS_JSON;
			}
		}
		getJsonResult().add("[\"" + getPbmu().getPbu().getPbarPercentage() + ","
				+ getPbmu().getPbu().getEta() + ","
				+ getPbmu().getPbu().getProcessed() + ","
				+ getPbmu().getPbu().getTotal() + ","
				+ getPbmu().getPbarPercentageGlobal() + ","
				+ getPbmu().getEtaGlobal() + ","
				+ getPbmu().getProcessedGlobal() + ","
				+ getPbmu().getTotalGlobal() + ","
				+ getPbmu().getProcessedSuccess() + ","
				+ getPbmu().getProcessedError() + ","
				+ getPbmu().getPbu().getInfo() + ","
				+ getPbmu().getPbu().getNbar() + ","
				+ getPbmu().getNbarTotal() + "\"]");
		return SUCCESS_JSON;
	}

	public ProgressBarManagerUtil getPbmu() {
		return pbmu;
	}

	public String setPbmu() {
		Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
		this.pbmu = Util.pbmuMap.get(usuario.getId().toString() + opcion);
		if (this.pbmu == null) {
			return ERROR_JSON;
		}
		return SUCCESS_JSON;
	}

	public String getOpcion() {
		return opcion;
	}

	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

}

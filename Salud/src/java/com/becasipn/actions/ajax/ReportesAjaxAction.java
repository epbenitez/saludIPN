package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.business.EstadisticasBO;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.EstadisticaCuestionarios;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;

/**
 *
 * @author Gustavo A. Alamillo
 */
public class ReportesAjaxAction extends JSONAjaxAction {

    private final EstadisticasBO estadisticasBO;

    public Periodo periodo;
    public Cuestionario cuestionario;

    public ReportesAjaxAction() {
        this.estadisticasBO = new EstadisticasBO(getDaos());
    }

    public String cuestionarios() {
        Gson gson = new Gson();
        JsonObject estadisticas = new JsonObject();

        List<EstadisticaCuestionarios> estadisticaTmp = estadisticasBO.estadisticaCuestionarios(periodo, cuestionario);
        estadisticas.add("Cuestionarios", gson.toJsonTree(estadisticaTmp));

        getJsonResult().add(estadisticas.toString());
        return SUCCESS_JSON;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

}

package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.Configuracion;
import com.becasipn.business.ConfiguracionAppBO;
import java.math.BigDecimal;
import java.util.List;

public class ConfiguracionAjaxAction extends JSONAjaxAction {

    private BigDecimal id;
    private String tip;
    private String val;
    private Configuracion configuracion = new Configuracion();

    public String listado() { // HIDDEN VALORES DE EDICION
        List<Configuracion> lista = getDaos().getConfiguracionDao().findAll();
        for (Configuracion config : lista) {
            getJsonResult().add("[\"" + config.getNombre()
                    + "\", \"" + "<div id='" + config.getId() + "_1'>" + config.getTip() + "</div>"
                    + "<div  id='" + config.getId() + "_2' style='display: none'><input type='text' id='" + config.getId() + "_2_1' value='" + config.getTip() + "'></div>"
                    + "\", \"" + "<div id='" + config.getId() + "_3'>" + config.getValor() + "</div>"
                    + "<div  id='" + config.getId() + "_4' style='display: none'><input type='text' id='" + config.getId() + "_4_1' value='" + config.getValor() + "'></div>"
                    + "\", \"" + "<a title='Editar variable' onclick='editar(" + config.getId() + ")' id='" + config.getId() + "_5' class='solo-lectura'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "<a title='Guardar variable' onclick='guardar(" + config.getId() + ")' id='" + config.getId() + "_6' style='display: none'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-floppy-o fa-stack-1x fa-inverse'></i></span></a>"
                    + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String guarda() {
        ConfiguracionAppBO configuracionBO = new ConfiguracionAppBO(getDaos());
        configuracionBO.guardaConfiguracion(id, tip, val);

        //RECARGAMOS LAS VARIABLES EN EL SISTEMA
        reloadVariablesPersonalizadas();
        return SUCCESS_JSON;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }
}

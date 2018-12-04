package com.becasipn.actions.admin;

import com.becasipn.actions.catalogos.*;
import com.becasipn.actions.BaseAction;

/**
 *
 * @author Usre-05
 */
public class AdministracionConfiguracionAction extends BaseAction implements MensajesCatalogos {

    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";

    private String valor;
    private String tip;

    public String lista() {
        return SUCCESS;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

}

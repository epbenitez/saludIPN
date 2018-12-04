package com.becasipn.actions.ajax;

import com.becasipn.business.ExcluirDepositoBO;
import com.becasipn.persistence.model.Otorgamiento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class ExcluirDepositoAlumnosAjaxAction extends JSONAjaxAction {

    private BigDecimal periodoI;
    private String numeroBoleta;
    private String nombre;
    private String aPaterno;
    private String aMaterno;

    public String listado() {
        ExcluirDepositoBO bo = ExcluirDepositoBO.getInstance(getDaos());
        bo.setOtorgamientos(periodoI, numeroBoleta, nombre, aPaterno, aMaterno);
        getJsonResult().add(bo.getJsonResult());

        return SUCCESS_JSON;
    }

    public BigDecimal getPeriodoI() {
        return periodoI;
    }

    public void setPeriodoI(BigDecimal periodoI) {
        this.periodoI = periodoI;
    }

    public String getNumeroBoleta() {
        return numeroBoleta;
    }

    public void setNumeroBoleta(String numeroBoleta) {
        this.numeroBoleta = numeroBoleta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }
}

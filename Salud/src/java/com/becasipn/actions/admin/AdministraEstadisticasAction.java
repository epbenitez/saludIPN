package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AdministraEstadisticasBO;
import java.io.InputStream;

public class AdministraEstadisticasAction extends BaseAction implements MensajesAdmin {

    private String est;
    private String datos = "";
    private String periodo;
    private String nivel;
    private String ua;

    private InputStream excelStream;
    private String contentDisposition;
//----------------------------------------------------------------------------------------------------

    public String formGenero() {
	est = "1";
	periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
	return SUCCESS;
    }

    public String formGenero1() {
	return SUCCESS;
    }

    public String formGenero2() {
	return SUCCESS;
    }
//----------------------------------------------------------------------------------------------------

    public String formDepositos() {
	est = "2";
	periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
	return SUCCESS;
    }

    public String formDepositos1() {
	return SUCCESS;
    }

    public String formDepositos2() {
	return SUCCESS;
    }
//----------------------------------------------------------------------------------------------------

    public String formEstatusT() {
	est = "3";
	periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
	return SUCCESS;
    }

    public String formEstatusT1() {
	return SUCCESS;
    }

    public String formEstatusT2() {
	return SUCCESS;
    }
//----------------------------------------------------------------------------------------------------

    public String lista() {
	return SUCCESS;
    }

    public String formOtorgamientos() {
	est = "1";
	periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
	return SUCCESS;
    }

     public String descargar() {

        AdministraEstadisticasBO aBO = AdministraEstadisticasBO.getInstance(getDaos());
        aBO.setEst(est);
       
        excelStream = aBO.getInfoExcel();
        String titulo = aBO.getTitulo();
        setContentDisposition("attachment; filename=\"" + titulo + "\"");
        
        return "archivo";
    }
//----------------------------------------------------------------------------------------------------

    public String getEst() {
	return est;
    }

    public void setEst(String est) {
	this.est = est;
    }

    public String getPeriodo() {
	return periodo;
    }

    public void setPeriodo(String periodo) {
	this.periodo = periodo;
    }

    public String getDatos() {
	return datos;
    }

    public void setDatos(String datos) {
	this.datos = datos;
    }

    public String getNivel() {
	return nivel;
    }

    public void setNivel(String nivel) {
	this.nivel = nivel;
    }

    public String getUa() {
	return ua;
    }

    public void setUa(String ua) {
	this.ua = ua;
    }

    public InputStream getExcelStream() {
	return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
	this.excelStream = excelStream;
    }

    public String getContentDisposition() {
	return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
	this.contentDisposition = contentDisposition;
    }

}

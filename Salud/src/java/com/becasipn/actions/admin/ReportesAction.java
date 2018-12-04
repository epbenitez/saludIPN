package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.DepositosBO;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.business.SolicitudBecasBO;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.ExcelExport;
import com.opensymphony.xwork2.ActionContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Gustavo A. Alamillo
 */
public class ReportesAction extends BaseAction {

    public List<Periodo> periodos;
    public boolean responsable;
    public UnidadAcademica ua;
    public Cuestionario cuestionario;

    private String periodo;
    private String nivel;
    private String uas;
    private String tbp;
    private String estatus;
    private String mes;
    private BigDecimal movimientos;
    private BigDecimal becas;
    private String limiteInferior;
    private String limiteSuperior;
    private InputStream excelStream;
    private String contentDisposition;

    public String reporte() {
        if (isAlumno() || !isAuthenticated()) {
            return ERROR;
        }
        responsable = isResponsable();
        if (responsable) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
            ua = personal.getUnidadAcademica();
        }
        periodos = getDaos().getPeriodoDao().findAllFrom(new BigDecimal(35));
        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        return SUCCESS;
    }

    public String cuestionarios() {
        if (isAlumno() || !isAuthenticated()) {
            return ERROR;
        }
        responsable = isResponsable();
        if (responsable) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
            ua = personal.getUnidadAcademica();
        }
        periodos = getDaos().getPeriodoDao().findAllFrom(new BigDecimal(35));
        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getClave();
        return SUCCESS;
    }

    public String descarga() {
        if (periodo.equals("x")) {
            periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        }
        if (isResponsable()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
            uas = personal.getUnidadAcademica().getId().toString();
        }
        SolicitudBecasBO sBO = SolicitudBecasBO.getInstance(getDaos());
        sBO.setPeriodoId(periodo);
        sBO.setNivelId(nivel);
        sBO.setuAId(uas);
        sBO.setLowerLimit(limiteInferior);
        sBO.setUpperLimit(limiteSuperior);

        excelStream = sBO.getInfoExcel();
        String titulo = sBO.getTitulo();
        setContentDisposition("attachment; filename=" + titulo);

        return "archivo";
    }

    public String descargaDepositos() {
        DepositosBO bo = DepositosBO.getInstance(getDaos());

        // Valores de prueba
        bo.setPeriodoId(periodo);
        bo.setNivelId(nivel);
        bo.setuAId(uas);
        bo.setMesId(mes);
        bo.setTbId(tbp);

        excelStream = bo.getReporteDepositosInfo();
        if (excelStream == null) {
            addActionError("No existen resultados para los parámetros seleccionados.");
            return "success";
        }
        String titulo = bo.getTitulo();
        setContentDisposition("attachment; filename=" + titulo);

        return "archivo";
    }

    // Función que genera el archivo de excel para descargar los registros del datatable en /reporteCuentas.action
    public String descargaEstatusCuentas() {
        OtorgamientoBO oBO = OtorgamientoBO.getInstance(getDaos());
        oBO.setPeriodoId(periodo);
        oBO.setNivelId(nivel);
        oBO.setuAId(uas);
        oBO.setEstatusId(estatus);

        excelStream = oBO.getInfoExcel();
        String titulo = oBO.getTitulo();
        setContentDisposition("attachment; filename=" + titulo);

        return "archivo";
    }

    public String ver() {
        periodo = getDaos().getPeriodoDao().getPeriodoActivo().getId().toString();
        return SUCCESS;
    }

    public String descargar() {
        List<Object[]> aux = getDaos().getOtorgamientoDao().resumenBecalos(periodo);

        ExcelExport excelExport = new ExcelExport();
        String[] encabezado = new String[]{"CURP", "BOLETA", "APELLIDO PATERNO", "APELLIDO MATERNO", "NOMBRES", "DIA", "MES", "AÑO", "GENERO", "ESTADO CIVIL", "ESTADO", "MUNICIPIO", "LOCALIDAD", "CALLE Y NUMERO", "CP", "CELULAR", "CORREOELECTRONICO", "BECA", "IPN", "UNIDAD ACADEMICA", "CARRERA", "NIVEL", "TIPO", "SEMESTRE", "SEMESTRE", "TOTAL", "SEMESTRES", "PROMEDIO", "MOVIMIENTO"};
        setContentDisposition("attachment; filename=\"reporteBecalos-" + periodo + ".xlsx\"");
        excelStream = excelExport.construyeExcel(encabezado, aux);

        return "archivo";
    }

    public String descargaReporte() {
        OtorgamientoBO oBO = OtorgamientoBO.getInstance(getDaos());
        oBO.setPeriodoId(periodo);
        oBO.setNivelId(nivel);
        oBO.setuAId(uas);
        excelStream = oBO.getInfoExcelProcesoOtorgamiento(getBecas());
        String titulo = oBO.getTitulo();
        setContentDisposition("attachment; filename=" + titulo);

        return "archivo";
    }

    public String descargaTotalBecas() {
        OtorgamientoBO oBO = OtorgamientoBO.getInstance(getDaos());
        oBO.setPeriodoId(periodo);
        oBO.setNivelId(nivel);
        oBO.setuAId(uas);
        excelStream = oBO.getExcelTotalBecas(becas, movimientos);
        if (excelStream == null) {
            return "success";
        }
        String titulo = oBO.getTitulo();
        setContentDisposition("attachment; filename=" + titulo);
        return "archivo";
    }

    public String descargaTotalBecarios() {
        OtorgamientoBO oBO = OtorgamientoBO.getInstance(getDaos());
        oBO.setPeriodoId(periodo);
        oBO.setNivelId(nivel);
        oBO.setuAId(uas);
        excelStream = oBO.getExcelTotalBecarios(becas, movimientos);
        if (excelStream == null) {
            return "success";
        }
        String titulo = oBO.getTitulo();
        setContentDisposition("attachment; filename=" + titulo);
        return "archivo";
    }

    //Descarga Excel de Depósitos con la tabla pivot
    public String descargaReporteDepositos() {
        DepositosBO bo = DepositosBO.getInstance(getDaos());

        bo.setPeriodoId(periodo);
        bo.setNivelId(nivel);
        bo.setuAId(uas);
        bo.setTbId(tbp);

        excelStream = bo.getReporteDepositosTabla();
        if (excelStream == null) {
            addActionError("No existen resultados para los parámetros seleccionados.");
            return "success";
        }
        String titulo = bo.getTitulo();
        setContentDisposition("attachment; filename=" + titulo);

        return "archivo";
    }

    public boolean getResponsable() {
        return responsable;
    }

    public void setResponsable(boolean responsable) {
        this.responsable = responsable;
    }

    public List<Periodo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Periodo> periodos) {
        this.periodos = periodos;
    }

    public UnidadAcademica getUa() {
        return ua;
    }

    public void setUa(UnidadAcademica ua) {
        this.ua = ua;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getUas() {
        return uas;
    }

    public void setUas(String uas) {
        this.uas = uas;
    }

    public String getLimiteInferior() {
        return limiteInferior;
    }

    public void setLimiteInferior(String limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public String getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(String limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public BigDecimal getBecas() {
        return becas;
    }

    public void setBecas(BigDecimal becas) {
        this.becas = becas;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public String getTbp() {
        return tbp;
    }

    public void setTbp(String tbp) {
        this.tbp = tbp;
    }

    public BigDecimal getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(BigDecimal movimientos) {
        this.movimientos = movimientos;
    }

}

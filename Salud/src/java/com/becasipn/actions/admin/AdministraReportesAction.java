package com.becasipn.actions.admin;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.util.ExcelExport;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez Manilla
 */
public class AdministraReportesAction extends BaseReportAction implements MensajesAdmin {
    private InputStream excelStream;
    private String contentDisposition;
    private BigDecimal periodoId;
    private String becas;
    private BigDecimal origenRecursosId;

    public String fundacion() {
        return SUCCESS;
    }

    public String descargar() {
        if (becas == null || "".equals(becas)){
            addActionError(getText("admin.reporte.no.programa.beca"));
            return ERROR;
        }
        List<Object[]> datosReporte = getDaos().getAlumnoDao().reporteFundacion(periodoId, becas, origenRecursosId);
        if (datosReporte.isEmpty() || datosReporte == null) {
            addActionError(getText("admin.reporte.sin.datos"));
            return ERROR;
        }
        //Periodo
        Periodo periodo = getDaos().getPeriodoDao().findById(periodoId);
        //Beca
        String[] becaArreglo = becas.split(",");
        String programaBeca = "";
        for (int i = 0; i < becaArreglo.length; i++) {
            Beca beca = getDaos().getBecaDao().findById(new BigDecimal(becaArreglo[i]));
            programaBeca = programaBeca + beca.getClave() + (i+1 == becaArreglo.length ? "" : "-");
        }
        
        ExcelExport excelExport = new ExcelExport();
        String[] encabezado = new String[]{"CURP", "MATRÍCULA", "APELLIDO PATERNO", "APELLIDO MATERNO", "NOMBRE(S)", "NAC. (DÍA)", "NAC. (MES)", "NAC. (AÑO)", "GENERO"
                , "ESTADO CIVIL", "ESTADO - BECA", "ESTADO / DOMICILIO", "DIR. (MUN o DEL)", "DIR. (COLONIA)", "DIR. (CALLE)", "DIR. (NUMERO INTERNO)", "DIR. (NUMERO EXTERNO)"
                , "C.P.", "TELÉFONO 1", "TELÉFONO 2", "CORREO ELECTRÓNICO 1", "CORREO ELECTRÓNICO 2", "EST. (INSTITUCIÓN)", "EST. (ESC/FAC/PROG)", "CARRERA", "NIVEL_EDUCATIVO"
                , "TIPO DE BECA", "NÚMERO DE SEMESTRE", "SEMESTRE Ó CUATRIMESTRE", "NÚMERO DE DURACIÓN DE ESTUDIOS", "DURACIÓN DE ESTUDIOS", "PROMEDIO ESCOLAR"
                , "ESTATUS DEL BECARIO", "COMENTARIOS", "CCT (Escuela/Facultad)", "PAGA"};
        setContentDisposition("attachment; filename=\"ReporteFundacion" + periodo.getClave() + programaBeca +".xlsx\"");
        excelStream = excelExport.construyeExcel(encabezado, datosReporte);
        return "archivo";
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

    public BigDecimal getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(BigDecimal periodoId) {
        this.periodoId = periodoId;
    }

    public String getBecas() {
        return becas;
    }

    public void setBecas(String becas) {
        this.becas = becas;
    }

    public BigDecimal getOrigenRecursosId() {
        return origenRecursosId;
    }

    public void setOrigenRecursosId(BigDecimal origenRecursosId) {
        this.origenRecursosId = origenRecursosId;
    }
}
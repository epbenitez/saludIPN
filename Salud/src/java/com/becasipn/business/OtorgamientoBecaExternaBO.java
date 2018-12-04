
package com.becasipn.business;

import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.service.Service;
import com.becasipn.util.ExcelExport;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

public class OtorgamientoBecaExternaBO extends XlsLoaderBO {
    
    private String titulo;
    private String uanom;
    private BigDecimal periodoId;
    private BigDecimal nivelId;
    private BigDecimal uAId;
    private BigDecimal becaId;
    
    private OtorgamientoBecaExternaBO(Service service) {
        super(service);
    }
    public static OtorgamientoBecaExternaBO getInstance(Service service) {
        return new OtorgamientoBecaExternaBO(service);
    }
    
    public InputStream getInfoExcel(BigDecimal periodoId,BigDecimal uAId,BigDecimal becaId, String uanom ) {

        if (periodoId != null) {
            List<Object[]>  infoBD = service.getOtorgamientoExternoDao().reporteEstatus(periodoId,  uAId, becaId);
            setTitulo(infoBD.size(), uanom);
            ExcelExport excelExport = new ExcelExport();
            String[] columnas = new String[]{"BOLETA","NOMBRE", "CURP", "UNIDAD ACADÉMICA","BECA SIBEC", "BECA EXTERNA"};
            return excelExport.construyeExcel(columnas, infoBD);
        } else {
            return null;
        }
    }
    
    @Override
    public <T> List<T> processFile(Workbook wb, String lote, Date fecha) throws Exception {
        return null;
    }

    @Override
    public <T> List<T> processFile(Workbook wb) throws Exception {
        return null;
    }

    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo) throws Exception {
        return null;
    }

    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo, Integer accion) throws Exception {
        return null;
    }

    @Override
    public <T> List<T> processFile(Workbook wb, UnidadAcademica unidadAcademica) throws Exception {
        return null;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(int total, String uanom) {
        int k = -1;
        StringBuilder sb = new StringBuilder();
        sb.append("\"Becas Externas ");
        if (uanom != null){
            sb.append(uanom);
        }
        sb.append(" (");
        sb.append(total);
        sb.append(") ");
        sb.append(service.getPeriodoDao().findById(periodoId).getClave());
        sb.append(".xlsx\"");

        this.titulo = sb.toString();
    }

    public BigDecimal getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(String periodoId) {
        this.periodoId = new BigDecimal(periodoId);
    }

    public BigDecimal getNivelId() {
        return nivelId;
    }

    public void setNivelId(String nivelId) {
        this.nivelId = nivelId != null && !nivelId.equals("0") ? new BigDecimal(nivelId) : null;
    }
    public BigDecimal getuAId() {
        return uAId;
    }

    public void setuAId(BigDecimal uAId) {
        this.uAId = uAId;
    }
    
    

    public BigDecimal getBecaId() {
        return becaId;
    }

    public void setBecaId(String becaId) {
        this.becaId = new BigDecimal(becaId);
    }

    public String getUanom() {
        return uanom;
    }

    public void setUanom(String uanom) {
        this.uanom = uanom;
    }

    
}

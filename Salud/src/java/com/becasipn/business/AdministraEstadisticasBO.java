package com.becasipn.business;

import com.becasipn.service.Service;
import com.becasipn.util.ExcelExport;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

public class AdministraEstadisticasBO extends BaseBO {

    private BigDecimal periodoId;
    private String titulo = "";
    private String est;
    private String periodo;

    private AdministraEstadisticasBO(Service service) {
        super(service);
    }

    public static AdministraEstadisticasBO getInstance(Service service) {
        return new AdministraEstadisticasBO(service);
    }

    public BigDecimal getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(String periodo) {
        this.periodoId = new BigDecimal(periodo);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        periodo = service.getPeriodoDao().getPeriodoActivo().getClave();
        this.titulo = titulo + "-" + periodo + ".xlsx";
    }

    public String getEst() {
        return est;
    }

    public void setEst(String est) {
        this.est = est;
    }
    
    

    public InputStream getInfoExcel() {
        
        List<Object[]> infoBD;
        
        switch (est) {
	    case "4"://CUESTIONARIO DE SALUD
                infoBD = service.getSolicitudBecaDao().totalAlumnosCuestionarioCompletoD(new BigDecimal(3));
		this.setTitulo("Total_Alumnos_Censo_salud");
                break;
	    default:
		infoBD = service.getAlumnoDao().totalAlumnosRegistradosD();
                this.setTitulo("Total_Alumnos_Registrados");
		break;
	}
        
        
        ExcelExport excelExport = new ExcelExport();
        String[] encabezado2 = new String[]{"UNIDAD ACADÉMICA", "NUMERO ALUMNOS"};
        return excelExport.construyeExcel(encabezado2, infoBD);
    }

}

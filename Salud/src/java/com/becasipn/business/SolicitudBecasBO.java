package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.service.Service;
import com.becasipn.util.ExcelExport;
import com.becasipn.util.ExcelTitulo;
import com.becasipn.util.UtilFile;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.util.AreaReference;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mario Márquez
 */
public class SolicitudBecasBO extends BaseBO {

    private String titulo;
    private String upperLimit;
    private String lowerLimit;
    private BigDecimal periodoId;
    private BigDecimal nivelId;
    private BigDecimal uAId;
    
    private boolean solicitudAnteriorVigente;

    public SolicitudBecasBO(Service service) {
        super(service);
    }

    public static SolicitudBecasBO getInstance(Service service) {
        return new SolicitudBecasBO(service);
    }

    /**
     * Crea un inputstream de un excel con las columnas especificadas en la
     * query Necesita que al menos el periodo haya sido difinido Author: Mario
     * Márquez
     *
     * @return InputStream Archivo
     */
    public InputStream getInfoExcel() {
        if (periodoId != null) {
            List<Object[]> infoBD = service.getSolicitudBecaDao().reporteSolicitudes(periodoId, nivelId, uAId, upperLimit, lowerLimit);
            if (infoBD != null) {
                ExcelTitulo excelT = new ExcelTitulo.Builder(service, "Solicitudes SIBec", infoBD.size())
                        .periodoId(periodoId).nivelId(nivelId).uAId(uAId).build();
                setTitulo(excelT.getTitulo());
            } else {
                return null;
            }

            String[] encabezado = {"NIVEL ACADÉMICO", "UNIDAD ACADÉMICA", "BOLETA",
                "CURP", "FECHA SOLICITUD", "APELLIDO PATERNO", "APELLIDO MATERNO",
                "NOMBRE", "INSCRITO", "EGRESADO", "PROMEDIO", "SEMESTRE", "REGULAR",
                "CARGA ACADÉMICA", "MODALIDAD", "PROGRAMA BECA SOLICITADA",
                "PERMITE TRANSFERENCIA", "CLASIFICACIÓN SOLICITUD", "MOTIVO RECHAZO",
                "BECA ASIGNADA", "INGRESOS PERCAPITA PESOS", "INGRESO TOTAL MENSUAL",
                "NÚMERO DE INTEGRANTES", "GASTO EN TRANSPORTE", "CORREO ELECTRONICO",
                "CELULAR", "TELEFONO CASA", "BECA PREASIGNADA", "BECA PERIODO ANT",
                "NACIONALIDAD", "CARRERA", "PROSPERA", "MUN POBREZA"};
            ExcelExport excelExport = ExcelExport.getInstance(encabezado, infoBD);

            XSSFWorkbook libro = new XSSFWorkbook();
            XSSFSheet hoja = libro.createSheet("Detalle");
            excelExport.setEncabezado(hoja);

            XSSFSheet hojaTD = libro.createSheet("TD");
            Row renglonTituloPivot = hojaTD.createRow(1);
            renglonTituloPivot.createCell(1).setCellValue("Becas Solicitadas");
            XSSFPivotTable pTSolicitadas = hojaTD.createPivotTable(new AreaReference("Detalle!A1:P" + (infoBD.size() + 1) + ""), new CellReference("B4"));
            pTSolicitadas.addRowLabel(0);
            pTSolicitadas.addRowLabel(1);
            pTSolicitadas.addRowLabel(15);
            pTSolicitadas.addColumnLabel(DataConsolidateFunction.COUNT, 2, "Total Becas Solicitadas");

            renglonTituloPivot.createCell(5).setCellValue("Becas Preasignadas");
            XSSFPivotTable pTPreasignadas = hojaTD.createPivotTable(new AreaReference("Detalle!A1:AB" + (infoBD.size() + 1) + ""), new CellReference("F4"));
            pTPreasignadas.addRowLabel(0);
            pTPreasignadas.addRowLabel(1);
            pTPreasignadas.addRowLabel(27);
            pTPreasignadas.addColumnLabel(DataConsolidateFunction.COUNT, 2, "Total Becas Preasignadas");

            return excelExport.getInputStream(libro, "Detalle");
        } else {
            return null;
        }

    }
    
    /**
     * Este método regresa la lista de solicitudes para un alumno según el periodo que se indique.
     *
     * @param alumno
     * @param periodo Si el periodo es nulo, se usa el periodo activo
     * @return List<SolicitudBeca> 
     * 
     */
    public List<SolicitudBeca> getSolicitudesAlumnoPeriodo(Alumno alumno, Periodo periodo) {
        Periodo periodoActual = service.getPeriodoDao().getPeriodoActivo();
        if (periodo == null) {
            periodo = periodoActual;
        }
        List<SolicitudBeca> solicitudes = service.getSolicitudBecaDao().getSolicitudesPorAlumno(alumno.getId(), periodo.getId());
        
        return solicitudes;
    }
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    private String ordenaFecha(String fecha) {
        String[] dividida = fecha.split("-");
        StringBuilder sb = new StringBuilder();

        sb.append(dividida[2]);
        sb.append("/");
        sb.append(dividida[1]);
        sb.append("/");
        sb.append(dividida[0]);

        return sb.toString();
    }

    public List<SolicitudBeca> SolicitudesPorAlumnoList(BigDecimal alumnoId, BigDecimal periodoId)
    {
        List<SolicitudBeca> list = new ArrayList<>();
        list = service.getSolicitudBecaDao().getSolicitudesSinReconsideracionAlumno(alumnoId, periodoId);
        return list;
    }
    public BigDecimal getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(String periodo) {
        this.periodoId = new BigDecimal(periodo);
    }

    public BigDecimal getNivelId() {
        return nivelId;
    }

    public void setNivelId(String nivel) {
        this.nivelId = nivel != null && !nivel.equals("0") ? new BigDecimal(nivel) : null;
    }

    public BigDecimal getuAId() {
        return uAId;
    }

    public void setuAId(String ua) {
        this.uAId = ua != null && !ua.equals("0") ? new BigDecimal(ua) : null;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        if (upperLimit != null && !upperLimit.isEmpty()) {
            this.upperLimit = ordenaFecha(upperLimit);
        } else {
            this.upperLimit = null;
        }
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        if (lowerLimit != null && !lowerLimit.isEmpty()) {
            this.lowerLimit = ordenaFecha(lowerLimit);
        } else {
            this.lowerLimit = null;
        }
    }

    public boolean isSolicitudAnteriorVigente() {
        return solicitudAnteriorVigente;
    }
}

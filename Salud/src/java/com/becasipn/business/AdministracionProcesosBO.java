package com.becasipn.business;

import com.becasipn.persistence.model.Movimiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.becasipn.util.ExcelExport;
import com.becasipn.util.ExcelTitulo;
import com.becasipn.util.ExcelTitulo.Builder;
import com.becasipn.util.UtilFile;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AdministracionProcesosBO extends BaseBO {

    private String titulo;
    private BigDecimal procesoId;
    // Resumen periodo
    private Periodo periodo;
    private boolean isAlta;
    private boolean isAcotado;
    private Usuario usuario;
    private UnidadAcademica ua;

    private final List<String> mesesPar = Arrays.asList("FEBRERO",
            "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO");
    private final List<String> mesesImpar = Arrays.asList("AGOSTO",
            "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE", "ENERO");

    private AdministracionProcesosBO(Service service) {
        super(service);
    }

    public static AdministracionProcesosBO getInstance(Service service) {
        return new AdministracionProcesosBO(service);
    }

    /**
     * Instancia para el resumen por periodo Author: Mario Márquez
     *
     * @param service
     * @param periodoId
     * @param isAlta
     * @param isAcotado
     * @return AdministracionProcesosBO
     */
    public static AdministracionProcesosBO getInstance(Service service, BigDecimal periodoId, boolean isAlta, boolean isAcotado, Usuario usuario) {
        AdministracionProcesosBO result = new AdministracionProcesosBO(service);
        result.setPeriodo(periodoId);
        result.setIsAlta(isAlta);
        result.setIsAcotado(isAcotado);
        result.setUsuario(usuario);

        return result;
    }

    public InputStream getInfoExcel() {

        Boolean procesoManutencion = service.getProcesoProgramaBecaDao().soloManutencion(procesoId);
        List<Object[]> infoDB = service.getOtorgamientoDao().prelacionProceso(procesoId, procesoManutencion);

        String[] encabezado;
        if (procesoManutencion) {
            encabezado = new String[]{"UA", "BOLETA", "BECA PREASIGNADA",
                "PROMEDIO", "SEMESTRE", "BECA PERIODO ANT",
                "NACIONALIDAD", "MODALIDAD", "REGULAR", "CARGA",
                "CARRERA", "PROSPERA", "MUN POBREZA"};
        } else {
            encabezado = new String[]{"UA", "BOLETA", "BECA PREASIGNADA",
                "INGRESO PER CAPITA", "GASTO TRANS",
                "PROMEDIO", "SEMESTRE", "BECA PERIODO ANT",
                "NACIONALIDAD", "MODALIDAD", "REGULAR", "CARGA",
                "CARRERA", "PROSPERA", "MUN POBREZA"};
        }

        ExcelExport excelExport = new ExcelExport();
        return excelExport.construyeExcel(encabezado, infoDB);
    }

    public void setTituloPrelacion() {
        Date d = new Date();
        SimpleDateFormat formater = new SimpleDateFormat("-ddMMyy-HHmmss");
        String f = formater.format(d);
        this.titulo = "\"listadoPrelacion-" + procesoId + f + ".xlsx\"";
    }

    public String getTitulo() {
        return titulo;
    }

    /**
     * Crea un inputstream de un excel para el reporte resumen por periodo o
     * resumen periodo acotado. Según sea acotado o no, elegirá la consulta
     * correspondiente. La UA es generada en el seter del usuario. Necesita que
     * al menos el periodo haya sido difinido. Author: Mario Márquez
     *
     * @return InputStream
     */
    public InputStream getInfoExcelResumenPeriodo() {
        if (periodo.getId() != null) {
            List<Object[]> infoBD;
            List<String> headers;
            if (isAcotado) {
                headers = getHeadersResumenAcotado();
                infoBD = service.getOtorgamientoDao().otorgamientosPeriodoAcotado(periodo, isAlta, ua.getId());
            } else {
                headers = getHeadersResumen();
                infoBD = service.getOtorgamientoDao().otorgamientosPeriodo(periodo, isAlta);
            }
            if (infoBD != null) {
                StringBuilder tituloSB = new StringBuilder();
                tituloSB.append("Resumen Periodo ");
                if (isAlta) {
                    tituloSB.append("Altas");
                } else {
                    tituloSB.append("Bajas");
                }
                Builder excelTB = new ExcelTitulo.Builder(service, tituloSB.toString(), 
                        infoBD.size()).periodoId(periodo.getId());
                if (isAcotado) {
                    excelTB.uAId(ua.getId());
                }
                ExcelTitulo excelT = excelTB.build();
                setTitulo(excelT.getTitulo());
            } else {
                return null;
            }

            String encabezados[] = headers.toArray(new String[headers.size()]);

            ExcelExport excelExport = ExcelExport.getInstance(encabezados, infoBD);
            
            return excelExport.getInputStream(null, "Detalle");
        } else {
            return null;
        }
    }

    /**
     * Crea los encabezados para el resumen periodo Author: Mario Márquez
     *
     * @return List<String>
     */
    private List<String> getHeadersResumen() {
        List<String> headers = new ArrayList<>(Arrays.asList("NIVEL", "UA", "BOLETA",
                "CURP", "NOMBRE", "APELLIDOS", "GENERO", "SEMESTRE", "PROMEDIO",
                "CARGA", "MODALIDAD", "TURNO", "CORREO", "TELEFONO", "PROGRAMA",
                "BECA", "PROCESO", "ESTATUS P", "MONTO", "CARRERA", getIngresosHeader(),
                "ULTIMO ESTATUS CUENTA", "CUENTA", "PAGO IPN", "EXCLUIR", "OTORGAMIENTO",
                "ESTATUSSUBES", "FOLIOSUBES", "CONVOCATORIASUBES"));
        headers.addAll(getMesesHeader());
        headers.add("TOTAL");

        return headers;
    }

    /**
     * Crea los encabezados para el resumen acotado Author: Mario Márquez
     *
     * @return List<String>
     */
    private List<String> getHeadersResumenAcotado() {
        List<String> headers = new ArrayList<>(Arrays.asList("BOLETA", "CURP", "NOMBRE", "GENERO",
                "SEMESTRE", "PROMEDIO", "MODALIDAD", "PROGRAMA", "BECA",
                "PROCESO", "MONTO", "CARRERA", getIngresosHeader(),
                "ULTIMO ESTATUS CUENTA", "CUENTA", "EXCLUIR", "ESTATUSSUBES",
                "FOLIOSUBES", "CORREO", "TELEFONO"));
        headers.addAll(getMesesHeader());
        headers.add("TOTAL");

        return headers;
    }

    /**
     * Crea el encabezado para las columnas de ingresos según el periodo Author:
     * Mario Márquez
     *
     * @return String
     */
    private String getIngresosHeader() {
        if (periodo.getId().intValue() < 35) {
            return "SALARIOS MIN";
        } else {
            return "INGRESOS PER CAPITA";
        }
    }

    /**
     * Crea el encabezado para las columnas de los meses, según la paridad del
     * periodo Author: Mario Márquez
     *
     * @return List<String>
     */
    private List<String> getMesesHeader() {
        if (periodo.getClave().contains("-2")) {
            return mesesPar;
        } else {
            return mesesImpar;
        }
    }

    /**
     * Crea el título para el reporte excel de resumen periodo Author: Mario
     * Márquez
     *
     * @param total Int total de resultados
     */
    public void setTituloResumen(int total) {
        Date hoy = new Date();
        String fecha = UtilFile.dateToString(hoy, "yyyyMMdd");
        String hora = UtilFile.dateToString(hoy, "HHmm");

        StringBuilder sb = new StringBuilder();
        sb.append("\"Resumen Periodo");
        if (isAlta) {
            sb.append(" Altas (");
        } else {
            sb.append(" Bajas (");
        }
        sb.append(fecha);
        sb.append(") ");
        sb.append(hora);
        sb.append("hrs (");
        sb.append(total);
        sb.append(") ");
        sb.append(periodo.getClave());
        if (isAcotado) {
            sb.append(ua.getNivel().getClave());
            sb.append("-");
            sb.append(ua.getNombreCorto());
        }
        sb.append(".xlsx\"");

        this.titulo = sb.toString();
    }
    
    public List<Movimiento> getResumenMovimientosList() {
        List<Movimiento> resumenMovimientosList = new ArrayList<>();
        
        // Creamos el movimiento dummy
        Movimiento movimientoTodos = new Movimiento();
        movimientoTodos.setNombre("Todos");
        movimientoTodos.setId(new BigDecimal(0));        
        resumenMovimientosList.add(movimientoTodos);
        
        resumenMovimientosList.addAll(service.getMovimientoDao().findAll());
        
        return resumenMovimientosList;
    }

    public void setProcesoId(BigDecimal procesoId) {
        this.procesoId = procesoId;
    }

    private void setPeriodo(BigDecimal periodoId) {
        this.periodo = service.getPeriodoDao().findById(periodoId);
    }

    private void setIsAlta(boolean isAlta) {
        this.isAlta = isAlta;
    }

    private void setIsAcotado(boolean isAcotado) {
        this.isAcotado = isAcotado;
    }

    private void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            this.ua = service.getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
        }
    }

    private void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}

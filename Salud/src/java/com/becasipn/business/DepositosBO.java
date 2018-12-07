package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.ErroresBanamex;
import com.becasipn.persistence.model.EstatusDeposito;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.becasipn.util.ExcelExport;
import com.becasipn.util.ExcelTitulo;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import org.apache.poi.hssf.util.AreaReference;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Patricia Benítez
 */
public class DepositosBO extends BaseBO {

    private String titulo;

    private int periodoId;
    private int nivelId = 0;
    private int uAId;
    private int mesId = 0;
    private int pbId = 0;

    public DepositosBO(Service service) {
        super(service);
    }

    public static DepositosBO getInstance(Service service) {
        return new DepositosBO(service);
    }

    /**
     * Inserta los depositos por alumnos para una nueva orden de deposito
     *
     * @author Tania G. Sánchez
     * @param alumnosLista
     * @param periodoId
     * @param ordenDeposito
     * @param usuario
     */
    public Boolean insertaDeposito(List<Alumno> alumnosLista, BigDecimal periodoId, Usuario usuario) {
        if (alumnosLista == null) {
            return Boolean.FALSE;
        }
        for (Alumno a : alumnosLista) {
            Depositos deposito = new Depositos();
            deposito.setAlumno(a);
            deposito.setMonto(a.getMonto());
            deposito.setFechaDeposito(new Date());
            deposito.setUsuarioModifico(usuario);
            deposito.setFechaModificacion(new Date());
            //Se le asigna el estatusDeposito 1 que es En Espera.
            EstatusDeposito estatusDeposito = new EstatusDeposito();
            estatusDeposito.setId(new BigDecimal(1));
            deposito.setEstatusDeposito(estatusDeposito);
            deposito.setObservaciones(" ");
            //Se busca la tarjeta del alumno.
            TarjetaBancaria tarjetaBancaria = new TarjetaBancaria();
            tarjetaBancaria.setId(a.getTarjetaBancariaId());
            deposito.setTarjetaBancaria(tarjetaBancaria);
            //Se busca el otorgamiento.
            Otorgamiento otorgamiento = new Otorgamiento();
            otorgamiento.setId(a.getOtorgamientoPasadoId());
            deposito.setOtorgamiento(otorgamiento);
            service.getDepositosDao().save(deposito);
            //Se actuzaliza el estatus del deposito anterior
            if (a.getDeposito() != null && a.getDeposito().getId() != null) {
                service.getDepositosDao().update(a.getDeposito());
            }
        }
        return Boolean.TRUE;
    }

    public Boolean guardaDeposito(Depositos d) {
        try {
            if (d.getId() == null) {
                service.getDepositosDao().save(d);

            } else {
                service.getDepositosDao().update(d);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public String graficaEstatusDepositos(BigDecimal ordenId) {
        String datosDeposito = "";
        List<Object[]> lista = service.getDepositosDao().depositosPorEstatus(ordenId);
        for (Object[] d : lista) {
            datosDeposito += "['" + ((String) d[0]) + "', " + ((BigDecimal) d[1]).toString() + "],";
        }
        return datosDeposito;
    }

    public InputStream getReporteDepositosInfo() {
        if (periodoId != 0 && mesId != 0) {
            List<Object[]> infoBD = service.getDepositosDao().reporteDepositosMensual(periodoId, nivelId, uAId, mesId, pbId);
            ExcelTitulo excelT = new ExcelTitulo.Builder(service, "Depositos", infoBD.size()).mes(mesId).periodoId(new BigDecimal(periodoId)).nivelId(new BigDecimal(nivelId)).uAId(new BigDecimal(uAId)).build();
            setTitulo(excelT.getTitulo());

            String[] encabezado;

            encabezado = new String[]{"Nivel académico", "Unidad académica", "Boleta",
                "CURP", "Nombre", "Estatus",
                "Código Error Banamex", "Error Banamex", "Excluir Depósito", "Con Cuenta Bancaria",
                "Estatus de Proceso", "Beca Asignada", "Fecha Depósito", "Forma de Pago"};

            ExcelExport excelExport = new ExcelExport();
            return excelExport.construyeExcel(encabezado, infoBD);
        }
        return null;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getListadoJsonResult(BigDecimal alumnoId, BigDecimal otorgamientoId) {
        List<Depositos> listaDepositos = service.getDepositosDao().depositosAlumno(alumnoId, null, otorgamientoId);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        JsonArrayBuilder jArrayB = Json.createArrayBuilder();

        for (Depositos deposito : listaDepositos) {
            // FIX 28/11/16 - Se checa si es nulo el dato, es necesario corregir en BD
            Otorgamiento o = deposito.getOtorgamiento();
            String s = o == null ? "-" : o.getTipoBecaPeriodo().getTipoBeca().getNombre();

            JsonArrayBuilder jArrayBInner = Json.createArrayBuilder()
                    .add(s)
                    .add(formato.format(deposito.getFechaDeposito()))
                    .add(deposito.getMonto())
                    .add(TarjetaBO.aplicaFormatoNumeroTarjeta(deposito.getTarjetaBancaria()))
                    .add(getStsDColumn(deposito));

            jArrayB.add(jArrayBInner);
        }
        String result = jArrayB.build().toString();

        return result.substring(1, result.length() - 1); // Eliminamos los corchetes externos pues no funcionan bien con la biblioteca de tablas js
    }

    // crea ccolumna paraa tabla
    private String getStsDColumn(Depositos deposito) {
        StringBuilder sb = new StringBuilder();
        EstatusDeposito stsDeposito = deposito.getEstatusDeposito();
        int stsDId = stsDeposito.getId().intValue();
        ErroresBanamex errorBmx = deposito.getErrorBanamex();

        if (stsDeposito != null) {
            sb.append("<span class='estatus-deposito label ").append(getStsDClass(stsDId));
            sb.append(" data-encabezado='").append(getStsDEncabezado(stsDId)).append("'");
            if (stsDId == 4) {
                if (errorBmx != null) {
                    sb.append(" data-descript='").append(getStsDDescripcion(errorBmx));
                    sb.append(" data-sugerencia='").append(getStsDSugerencia(errorBmx));
                } else {
                    String errorNulo = "Error Banamex mal configurado en la base de datos";
                    sb.append(" data-descript='").append(errorNulo).append("'");
                    sb.append(" data-sugerencia='").append(errorNulo).append("'");
                }
            } else if (stsDId == 10) {
                sb.append(" data-descript='Se intentará de nuevo hacer el depósito.'");
                sb.append(" data-sugerencia='No hace falta tomar medidas extras.'");
            }
            sb.append("'>");
            sb.append(stsDeposito.getNombre());
            sb.append("</span>");
        } else if (stsDeposito == null) {
            sb.append("<span class='label label-default'>Estatus Nulo</span>"); // Si el estatus es nulo
        }

        return sb.toString();
    }

    private String getStsDEncabezado(int option) {
        switch (option) {
            case 4:
                return "Depósito rechazado";
            case 10:
                return "Rechazo trabajado";
            default:
                return "";
        }
    }

    private String getStsDSugerencia(ErroresBanamex error) {
        if (error.getAccion() != null && !error.getAccion().isEmpty()) {
            return error.getAccion();
        } else {
            return "Descripción configurada incorrectamente en la base de datos";
        }
    }

    private String getStsDDescripcion(ErroresBanamex error) {
        if (error.getDescripcion() != null && !error.getDescripcion().isEmpty()) {
            return error.getDescripcion();
        } else {
            return "Descripción configurada incorrectamente en la base de datos";
        }
    }

    // crea clase
    private String getStsDClass(int option) {
        switch (option) {
            case 2:
                return "label-success'";
            case 4:
                return "label-danger' style='cursor: pointer;'";
            case 10:
                return "label-warning' style='cursor: pointer;'";
            default:
                return "label-default'";
        }
    }

    /**
     * Revisa si hay depósitos rechazados, entre una lista de depósitos.
     *
     * @author Mario Márquez
     * @param depositos
     * @return boolean
     */
    public static boolean hasRechazados(List<Depositos> depositos) {
        boolean result = false;

        if (depositos != null && !depositos.isEmpty()) {
            for (Depositos deposit : depositos) {
                int id = deposit.getEstatusDeposito().getId().intValue();
                if (id == 4) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public InputStream getReporteDepositosTabla() {
        if (periodoId != 0) {
            String tipo_periodo = service.getPeriodoDao().getClavePeriodo(periodoId);
            String aux = tipo_periodo.substring(5,6);
            
            List<Object[]> infoBD = service.getDepositosDao().ReportePagos(periodoId, nivelId, uAId, pbId, aux);
            if (infoBD != null) {
                ExcelTitulo excelT = new ExcelTitulo.Builder(service, "Reporte de Pagos y Adeudos", infoBD.size())
                        .periodoId(new BigDecimal(periodoId)).nivelId(new BigDecimal(nivelId))
                        .uAId(new BigDecimal(uAId)).build();
                setTitulo(excelT.getTitulo());
            } else {
                return null;
            }

            String[] encabezado;

            if (aux.equals("2")) {
                encabezado = new String[]{"NIVEL ACADÉMICO", "UNIDAD ACADÉMICA", "BOLETA",
                    "CURP", "NOMBRE", "APELLIDOS", "BECA", "TIPO DE BECA", "PROCESO", "ESTATUS PROCESO",
                    "CUENTA", "ULTIMO ESTATUS CUENTA", "EXCLUIR", "IDENTIFICADOR DE OTORGAMIENTO", "MONTO MENSUAL",
                    "MONTO SEMESTRAL", "TOTAL DEPOSITADO", "ADEUDO", "FEBRERO", "FECHA DE DEPOSITO", "MARZO", "FECHA DE DEPOSITO", "ABRIL", "FECHA DE DEPOSITO", "MAYO",
                    "FECHA DE DEPOSITO", "JUNIO", "FECHA DE DEPOSITO", "JULIO", "FECHA DE DEPOSITO"};
            } else {
                encabezado = new String[]{"NIVEL ACADÉMICO", "UNIDAD ACADÉMICA", "BOLETA",
                    "CURP", "NOMBRE", "APELLIDOS", "BECA", "TIPO DE BECA", "PROCESO", "ESTATUS PROCESO",
                    "CUENTA", "ULTIMO ESTATUS CUENTA", "EXCLUIR", "IDENTIFICADOR DE OTORGAMIENTO", "MONTO MENSUAL",
                    "MONTO SEMESTRAL", "TOTAL DEPOSITADO", "ADEUDO", "AGOSTO", "FECHA DE DEPOSITO", "SEPTIEMBRE", "FECHA DE DEPOSITO", "OCTUBRE", "FECHA DE DEPOSITO", "NOVIEMBRE",
                    "FECHA DE DEPOSITO", "DICIEMBRE", "FECHA DE DEPOSITO", "ENERO", "FECHA DE DEPOSITO"};
            }

            ExcelExport excelExport = ExcelExport.getInstance(encabezado, infoBD);

            XSSFWorkbook libro = new XSSFWorkbook();
            XSSFSheet hoja = libro.createSheet("Detalle");
            excelExport.setEncabezado(hoja);

            XSSFSheet pivotSheet = libro.createSheet("TD");
            XSSFPivotTable pivotTable = pivotSheet.createPivotTable(new AreaReference("Detalle!A1:R" + (infoBD.size() + 1) + ""), new CellReference("A1"));

            pivotTable.addRowLabel(0);
            pivotTable.addRowLabel(7);
            pivotTable.addColumnLabel(DataConsolidateFunction.COUNT, 2, "Cuenta de boleta");
            pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 15, "Suma de Monto Semestral");
            pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 16, "Suma de Cantidad Deposita");
            pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 17, "Suma de Saldo Adeudado");

            return excelExport.getInputStream(libro, "Detalle");
        }
        return null;
    }

    /**
     * Obtiene los últimos 2 depósitos aplicados, para mostrarse en el panel
     * alumno
     *
     * @author Mario Márquez
     * @param depositos
     * @return List<Depositos>
     */
    public static List<Depositos> getUltimosAplicados(List<Depositos> depositos) {
        if (depositos != null && !depositos.isEmpty()) {
            for (int i = 0; i < depositos.size(); i++) {
                int depositoId = depositos.get(i).getEstatusDeposito().getId().intValue();
                if (depositoId != 2) {
                    depositos.remove(i);
                }
            }

            int aEliminar = depositos.size() - 2;
            for (int i = 0; i < aEliminar; i++) {
                depositos.remove(depositos.size() - 1);
            }
        }

        return depositos;
    }

    public int getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(String periodoId) {
        this.periodoId = Integer.parseInt(periodoId);
    }

    public int getNivelId() {
        return nivelId;
    }

    public void setNivelId(String nivelId) {
        this.nivelId = nivelId != null && !nivelId.equals("0") ? Integer.parseInt(nivelId) : 0;
    }

    public int getuAId() {
        return uAId;
    }

    public void setuAId(String uAId) {
        this.uAId = uAId != null && !uAId.equals("0") ? Integer.parseInt(uAId) : 0;
    }

    public int getMesId() {
        return mesId;
    }

    public void setMesId(String mesId) {
        this.mesId = Integer.parseInt(mesId);
    }

    public int getPbId() {
        return pbId;
    }

    public void setTbId(String tbId) {
        this.pbId = tbId != null && !tbId.equals("0") ? Integer.parseInt(tbId) : 0;
    }

}

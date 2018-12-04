package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.BitacoraOtorgamientos;
import com.becasipn.persistence.model.IdentificadorOtorgamiento;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.becasipn.util.Util;
import com.opensymphony.xwork2.ActionContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.persistence.PersistenceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Mario Márquez
 */
public class ExcluirDepositoBO extends XlsLoaderBO {

    private Workbook workBook;

    private HashMap<BigDecimal, Integer> relacion;
    private List<Otorgamiento> otorgamientosList;

    private ExcluirDepositoBO(Service service) {
        super(service);
    }

    public static ExcluirDepositoBO getInstance(Service service) {
        return new ExcluirDepositoBO(service);
    }

    public String getJsonResult() {
        JsonArrayBuilder jOBArray = Json.createArrayBuilder();

        for (Otorgamiento otorgamiento : otorgamientosList) {
            String status = otorgamiento.getExcluirDeposito()
                    ? "<span class='label label-danger'>Excluido</span>"
                    : "<span class='label label-primary'>Incluido</span>";
            String boton = "<a title=\"Editar\" class=\"fancybox fancybox.iframe table-link\" href=\"/becas/editarExcluirAlumnosDeposito.action?idOtorgamiento=" + otorgamiento.getId() + "\"><span class=\"fa-stack\"><i class=\"fa fa-square fa-stack-2x\"></i> <i class=\"fa fa-pencil-square-o fa-stack-1x fa-inverse\"></i></span></a>";

            JsonArrayBuilder jOBInnerArray = Json.createArrayBuilder()
                    .add(otorgamiento.getAlumno().getBoleta())
                    .add(otorgamiento.getAlumno().getFullName())
                    .add(otorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre())
                    .add(status)
                    .add(boton);

            jOBArray.add(jOBInnerArray);
        }
        String result = jOBArray.build().toString();

        return result.substring(1, result.length() - 1); // Eliminamos los corchetes externos pues no funcionan bien con la biblioteca de tablas js
    }

    public void setOtorgamientos(BigDecimal periodoId, String numeroBoleta, String nombre,
            String aPaterno, String aMaterno) {

        boolean hayNombre = !nombre.isEmpty() || !aPaterno.isEmpty() || !aMaterno.isEmpty();

        if (!numeroBoleta.isEmpty() && hayNombre) {
            this.otorgamientosList = service.getOtorgamientoDao().buscarAlumnoPeriodoBoletaNombre(periodoId, numeroBoleta, nombre, aPaterno, aMaterno);
        } else if (hayNombre) {
            this.otorgamientosList = service.getOtorgamientoDao().buscarAlumnoPeriodoNombre(periodoId, nombre, aPaterno, aMaterno);
        } else if (!numeroBoleta.isEmpty()) {
            this.otorgamientosList = service.getOtorgamientoDao().buscarAlumnoPeriodoBoleta(periodoId, numeroBoleta);
        } else {
            this.otorgamientosList = service.getOtorgamientoDao().alumnosExcluidosPeriodo(periodoId);
        }
    }

    /**
     * Procesa el archivo de Excel para excluir/inlcuir a depósitos
     *
     * @author Mario Márquez
     * @param idOtorgamientoId
     * @param periodo
     * @param accion
     * @param desdeExcel
     * @return Lista String
     * @throws java.lang.Exception
     */
    public List<String> processFile(BigDecimal periodo, int accion, BigDecimal idOtorgamientoId, boolean desdeExcel) throws Exception {
        if (workBook == null) {
            return null;
        }
        relacion = service.getCarreraDao().getSemestresMaximos();
        Boolean excluir = accion == 1 ? Boolean.TRUE : Boolean.FALSE;
        List<String> logScreen = new LinkedList<>();
        List<Otorgamiento> otorgamientos;
        Otorgamiento otorgamiento;
        BitacoraOtorgamientos bOt;
        Sheet sheet = workBook.getSheetAt(0);
        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            Cell boletaCell = row.getCell(0);
            Cell idOtorgamientoCell = row.getCell(1);
            String idOtorgamientoString;

            if (!desdeExcel) {
                idOtorgamientoString = service.getIdentificadorOtorgamientoDao()
                        .findById(idOtorgamientoId)
                        .getNombre();
            } else {
                idOtorgamientoString = idOtorgamientoCell.toString().trim();
            }
            log.info("Procesando la fila: " + cont + " de " + sheet.getLastRowNum());

            Alumno alumno;
            String boleta = "";
            if (boletaCell.getCellType() == Cell.CELL_TYPE_STRING) {
                String num = boletaCell.toString();
                try {
                    boleta = new BigInteger(num).toString();
                } catch (Exception e) {
                    boleta = num;
                }
            } else if (boletaCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                boleta = String.valueOf(new BigDecimal(boletaCell.getNumericCellValue()));
            }
            alumno = service.getAlumnoDao().findByBoleta(boleta);

            if (alumno == null || alumno.getId() == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("<td>").append(boleta).append("</td>");
                sb.append("<td>").append(" - ").append("</td>");
                sb.append("<td>").append(idOtorgamientoString).append("</td>");
                sb.append("<td><span class='label label-danger'>El número de boleta es incorrecto</span></td>");

                logScreen.add(sb.toString());
            } else {
                otorgamientos = service.getOtorgamientoDao().buscarAlumnoPeriodoBoleta(periodo, alumno.getBoleta());
                if (otorgamientos == null || otorgamientos.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<td>").append(boleta).append("</td>");
                    sb.append("<td>").append(alumno.getFullName()).append("</td>");
                    sb.append("<td>").append(idOtorgamientoString).append("</td>");
                    sb.append("<td><span class='label label-danger'>El alumno no tiene beca para el periodo seleccionado</span></td>");

                    logScreen.add(sb.toString());
                } else {
                    Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
                    for (int o = 0; o < otorgamientos.size(); o++) {
                        boolean asignoIdentificador = false;
                        bOt = new BitacoraOtorgamientos();
                        otorgamiento = otorgamientos.get(o);
                        otorgamiento.setExcluirDeposito(excluir);
                        bOt.setOtorgamiento(otorgamiento);
                        bOt.setExcluirDeposito(excluir);
                        bOt.setUsuarioModifico(usuario);
                        bOt.setFechaModificacion(new Date());
                        if (desdeExcel) {
                            List<IdentificadorOtorgamiento> identificadoresO = service.getIdentificadorOtorgamientoDao().findAll();
                            // Intenta asignar el que corresponda a la celda
                            for (IdentificadorOtorgamiento identificadorOtorgamiento : identificadoresO) {
                                if (Util.equivaleTexto(identificadorOtorgamiento.getNombre(), idOtorgamientoCell.toString())) {
                                    otorgamiento.setIdentificadorOtorgamiento(identificadorOtorgamiento);
                                    bOt.setIdentificadorOtorgamiento(identificadorOtorgamiento);
                                    asignoIdentificador = true;
                                    break;
                                }
                            }
                            // Revisa si un identificador fue asignado, si no, agrega mensaje de error
                            if (!asignoIdentificador) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("<td>").append(boleta).append("</td>");
                                sb.append("<td>").append(alumno.getFullName()).append("</td>");
                                sb.append("<td>").append(idOtorgamientoString).append("</td>");
                                sb.append("<td><span class='label label-danger'>El identificador de otorgamiento es incorrecto</span></td>");

                                logScreen.add(sb.toString());
                            }
                        } else {
                            IdentificadorOtorgamiento identificadorOtorgamiento = service.getIdentificadorOtorgamientoDao().findById(idOtorgamientoId);
                            otorgamiento.setIdentificadorOtorgamiento(identificadorOtorgamiento);
                            bOt.setIdentificadorOtorgamiento(identificadorOtorgamiento);
                            asignoIdentificador = true;
                        }
                        // Guarda los nuevos datos
                        if (asignoIdentificador) {
                            try {
                                service.getBitacoraOtorgamientosDao().save(bOt);
                                service.getOtorgamientoDao().update(otorgamiento);

                                StringBuilder sb = new StringBuilder();
                                sb.append("<td>").append(boleta).append("</td>");
                                sb.append("<td>").append(alumno.getFullName()).append("</td>");
                                sb.append("<td>").append(idOtorgamientoString).append("</td>");
                                sb.append("<td>").append(otorgamiento.getExcluirDeposito()
                                        ? "<span class='label label-warning'>Excluido</span>"
                                        : "<span class='label label-success'>Incluido</span>"
                                ).append("</td>");

                                logScreen.add(sb.toString());
                            } catch (PersistenceException eee) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("<td>").append(boleta).append("</td>");
                                sb.append("<td>").append(alumno.getFullName()).append("</td>");
                                sb.append("<td>").append(idOtorgamientoString).append("</td>");
                                sb.append("<td><span class='label label-danger'>Error al guardar</span></td>");

                                logScreen.add(sb.toString());
                                eee.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return logScreen;
    }

    public void setXlsFile(FileInputStream xlsx, boolean isXlsx) throws IOException {
        if (isXlsx) {
            workBook = readXlsxFile(xlsx);
        } else {
            workBook = readXlsFile(xlsx);
        }
    }

    public String guardar(BigDecimal idOtorgamiento, BigDecimal idOtorgamientoId, int accion) {
        Otorgamiento otorgamiento = service.getOtorgamientoDao().findById(idOtorgamiento);
        Boolean excluir = accion == 1 ? Boolean.TRUE : Boolean.FALSE;
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");

        BitacoraOtorgamientos bOt = new BitacoraOtorgamientos();
        otorgamiento.setExcluirDeposito(excluir);
        bOt.setOtorgamiento(otorgamiento);
        bOt.setExcluirDeposito(excluir);
        bOt.setUsuarioModifico(usuario);
        bOt.setFechaModificacion(new Date());

        IdentificadorOtorgamiento identificadorOtorgamiento = service.getIdentificadorOtorgamientoDao().findById(idOtorgamientoId);
        otorgamiento.setIdentificadorOtorgamiento(identificadorOtorgamiento);
        bOt.setIdentificadorOtorgamiento(identificadorOtorgamiento);

        // Guarda los nuevos datos
        try {
            service.getBitacoraOtorgamientosDao().save(bOt);
            service.getOtorgamientoDao().update(otorgamiento);

            return "";
        } catch (PersistenceException eee) {
            System.out.println(eee);
            return "Error al guardar.";
        }
    }

    /**
     * Genera una lista de todos los identificadores de otorgamiento. El primer
     * elemento es el identificador correspondiente al otorgamiento
     *
     * @author Mario Márquez
     * @param service
     * @param idIdentificadorOtorgamiento
     * @return idIdentificador Id del identificador a colocar al inicio de la
     * lista
     */
    public static List<IdentificadorOtorgamiento> ubicaIdentificadorOtorgamiento(Service service, BigDecimal idIdentificadorOtorgamiento) {
        int ubicacion = idIdentificadorOtorgamiento.subtract(new BigDecimal(1)).toBigInteger().intValue();
        List<IdentificadorOtorgamiento> lista = service.getIdentificadorOtorgamientoDao().findAll();
        lista.add(lista.set(0, lista.remove(ubicacion)));

        return lista;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Mario Márquez
     * @param wb
     * @param periodo
     * @param accion
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public List<String> processFile(Workbook wb, BigDecimal periodo, Integer accion) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Mario Márquez
     * @param <T>
     * @param wb
     * @param lote
     * @param fecha
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, String lote, Date fecha) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Mario Márquez
     * @param wb
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Mario Márquez
     * @param <T>
     * @param wb
     * @param periodo
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Mario Márquez
     * @param <T>
     * @param wb
     * @param unidadAcademica
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, UnidadAcademica unidadAcademica) throws Exception {
        return null;
    }
}

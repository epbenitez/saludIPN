package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.CuestionarioPreguntas;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.PersistenceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Tania Gabriela Sánchez Manilla
 */
public class CuestionarioPreguntasRespuestasBO extends XlsLoaderBO {

    public CuestionarioPreguntasRespuestasBO(Service service) {
        this.service = service;
    }

    /**
     * Procesa el archivo de Excel con los promedios y semestres de los alumnos
     *
     * @author Tania Gabriela Sánchez Manilla
     * @param wb
     * @param periodoId
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public List<String> processFile(Workbook wb, BigDecimal periodoId) throws Exception {
        //Periodo
        List<String> logScreen = new LinkedList<>();
        Sheet sheet = wb.getSheetAt(0);
        CuestionarioRespuestasUsuario cru;
        CuestionarioRespuestas respuesta;
        CuestionarioRespuestas respuestaAnterior;
        Periodo periodo = new Periodo();
        periodo.setId(periodoId);
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setId(new BigDecimal("1")); //1 = ESE
        CuestionarioPreguntas pregunta = new CuestionarioPreguntas();
        pregunta.setId(new BigDecimal("13")); //Pregunta del ingreso per cápita.
        Integer ingreso = 0;
        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            log.info("Procesando la fila: " + cont + " de " + sheet.getLastRowNum());
            try {
                Alumno alumno = null;
                String boleta = "";
                if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
                    String num = row.getCell(0) + "";
                    try {
                        boleta = new BigInteger(num) + "";
                    } catch (Exception e) {
                        boleta = num;
                    }
                    //Validamos si el número de boleta es valido.
                    if (boleta.length() > 10) {
                        logScreen.add("<td>" + boleta + "</td>"
                                + "<td>" + "" + "</td>"
                                + "<td>" + "" + "</td>"
                                + "<td><span class='label label-primary'>El múmero de boleta no es válida.</span></td>");
                    } else {
                        alumno = service.getAlumnoDao().findByBoleta(boleta);
                        //Validamos si existe el alumno en bd.
                        if (alumno == null) {
                            logScreen.add("<td>" + boleta + "</td>"
                                    + "<td>" + "" + "</td>"
                                    + "<td>" + "" + "</td>"
                                    + "<td><span class='label label-danger'>El alumno no se ha registrado.</span></td>");
                        }
                    }
                } else if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    String num = row.getCell(0).getNumericCellValue() + "";
                    boleta = new BigDecimal(num).toBigInteger() + "";
                    //Validamos si el número de boleta es valido.
                    if (boleta.length() > 10) {
                        logScreen.add("<td>" + boleta + "</td>"
                                + "<td>" + "" + "</td>"
                                + "<td>" + "" + "</td>"
                                + "<td><span class='label label-primary'>El múmero de boleta no es válida.</span></td>");
                    } else {
                        alumno = service.getAlumnoDao().findByBoleta(boleta);
                        //Validamos si existe el alumno en bd.
                        if (alumno == null) {
                            logScreen.add("<td>" + boleta + "</td>"
                                    + "<td>" + "" + "</td>"
                                    + "<td>" + "" + "</td>"
                                    + "<td><span class='label label-danger'>El alumno no se ha registrado.</span></td>");
                        }
                    }
                }
                if (alumno != null) {
                    respuesta = new CuestionarioRespuestas();
                    ingreso = Integer.parseInt(row.getCell(1).toString().replace(".0", ""));
                    if (ingreso == 0) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("331"));
                    } else if (ingreso == 1) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("62"));
                    } else if (ingreso == 2) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("63"));
                    } else if (ingreso == 3) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("64"));
                    } else if (ingreso == 4) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("65"));
                    } else if (ingreso == 5) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("66"));
                    } else if (ingreso == 6) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("67"));
                    } else if (ingreso == 7) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("68"));
                    } else if (ingreso == 8) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("69"));
                    } else if (ingreso == 9) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("70"));
                    } else if (ingreso >= 10) {
                        respuesta = service.getCuestionarioRespuestasDao().findById(new BigDecimal("71"));
                    }
                    //Buscamos la respuesta de la pregunta del ingreso per cápita (13).
                    cru = service.getCuestionarioRespuestasUsuarioDao().respuestaPregunta(alumno.getUsuario().getId(), pregunta.getId(), cuestionario.getId(), periodo.getId());
                    if (cru == null) {
                        //Si no existe la respuesta la insertamos en bd.
                        cru = new CuestionarioRespuestasUsuario();
                        cru.setUsuario(alumno.getUsuario());
                        cru.setPregunta(pregunta);
                        cru.setRespuesta(respuesta);
                        cru.setCuestionario(cuestionario);
                        cru.setPeriodo(periodo);
                        service.getCuestionarioRespuestasUsuarioDao().save(cru);
                        logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                                + "<td>" + "" + "</td>"
                                + "<td>" + cru.getRespuesta().getNombre() + "</td>"
                                + "<td><span class='label label-success'>Actualizado correctamente.</span></td>");
                    } else {
                        //Si existe respuesta actualizamos en bd.
                        if (cru.getRespuesta().getId().equals(respuesta.getId())) {
                            logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                                    + "<td>" + cru.getRespuesta().getNombre() + "</td>"
                                    + "<td>" + respuesta.getNombre() + "</td>"
                                    + "<td><span class='label label-primary'>El dato proporcionado era el mismo que le existente.</span></td>");
                        } else {
                            respuestaAnterior = cru.getRespuesta();
                            cru.setRespuesta(respuesta);
                            service.getCuestionarioRespuestasUsuarioDao().update(cru);
                            logScreen.add("<td>" + alumno.getBoleta() + "</td>"
                                    + "<td>" + respuestaAnterior.getNombre() + "</td>"
                                    + "<td>" + cru.getRespuesta().getNombre() + "</td>"
                                    + "<td><span class='label label-success'>Actualizado correctamente.</span></td>");
                        }
                    }

                }
            } catch (PersistenceException eee) {
                eee.printStackTrace();
            } catch (Exception e) {
                //e.printStackTrace();
                String boleta = "";
                if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
                    String num = row.getCell(0) + "";
                    boleta = new BigInteger(num) + "";
                } else if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    String num = row.getCell(0).getNumericCellValue() + "";
                    boleta = new BigDecimal(num).toBigInteger() + "";
                }
                logScreen.add("<td>" + boleta + "</td>"
                        + "<td>" + "" + "</td>"
                        + "<td>" + "" + "</td>"
                        + "<td><span class='label label-primary'>El múmero de boleta no existe</span></td>");
            }
        }
        return logScreen;
    }


    /**
     * Implementa metodo vacio
     *
     * @author Tania Gabriela Sánchez Manilla
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
     * @author Tania Gabriela Sánchez Manilla
     * @param <T>
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
     * @author Tania G. Sánchez
     * @param <T>
     * @param wb
     * @param periodo
     * @param accion
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo, Integer accion) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
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

    public Boolean guardaCRU(CuestionarioRespuestasUsuario cru) {
        try {
            if (cru.getId() == null) {
                service.getCuestionarioRespuestasUsuarioDao().save(cru);
            } else {
                service.getCuestionarioRespuestasUsuarioDao().update(cru);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

package com.becasipn.business;

import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.ErroresBanamex;
import com.becasipn.persistence.model.EstatusDeposito;
import com.becasipn.persistence.model.OrdenDeposito;
import com.becasipn.service.Service;
import com.opensymphony.xwork2.ActionContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Patricia Benítez
 */
public class CargaRespuestasBancariasBO {

    private Service service;
    private BigDecimal ordenId;
    private Integer exitosos = 0;
    private Integer rechazados = 0;
    private Integer noProcesados = 0;
    private Integer correosEnviados = 0;
    private Integer total = 0;
    private Boolean error = false;
    private String mensajeError;
    private List<Depositos> depositosRechazados = new ArrayList<>();

    private String ASUNTO = (String) ActionContext.getContext().getApplication().get("respuestasBancariasCorreoAsunto");
    private String ATENTAMENTE = (String) ActionContext.getContext().getApplication().get("respuestasBancariasCorreoAtentamente");
    private String MENSAJEAPLICADO = (String) ActionContext.getContext().getApplication().get("respuestasBancariasCorreoMensajeAplicado");
    private String MENSAJERECHAZADO = (String) ActionContext.getContext().getApplication().get("respuestasBancariasCorreoMensajeRechazado");

    public CargaRespuestasBancariasBO(Service service) {
        this.service = service;
    }

    public static List<String> processFile(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);
        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line = null;
        List<String> listaLineas = new ArrayList();
        while ((line = br.readLine()) != null) {
            listaLineas.add(line);
        }
        br.close();
        return listaLineas;
    }

    /**
     * Verifica que el valor enviado es un valor válido de acuerdo al catálogo
     * de errores de banamex
     *
     * @param erroresBanamexLst
     * @param err
     * @return
     */
    public Boolean verificaErrorBanamex(List<ErroresBanamex> erroresBanamexLst, BigDecimal error) {
        if (erroresBanamexLst == null || erroresBanamexLst.isEmpty() || error == null) {
            return Boolean.FALSE;
        }
        for (ErroresBanamex e : erroresBanamexLst) {
            if (error.equals(e.getId())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void procesaInfo(List<String> lineas) throws ParseException {
        OrdenDeposito ordenDeposito = new OrdenDeposito();

        List<Depositos> depositosLista = new ArrayList<>();
        Date fecha = new Date();
        List<ErroresBanamex> erroresBanamexLst = service.getErroresBanamexDao().findAll();
        Depositos rechazado;

        // La versión del layout determina si es banca premium
        boolean itsPremium = lineas.get(0).substring(121, 122).equals("C");
        // Valores default
        boolean itsCuenta = false;
        boolean itsCLABE = false;
        boolean naturalezaErronea = false;
        if (itsPremium) {
            itsCuenta = lineas.get(0).substring(79, 81).equals("05");
            itsCLABE = lineas.get(0).substring(79, 81).equals("12");
            if (!itsCLABE && !itsCuenta) {
                naturalezaErronea = true; // Si ni cuenta ni clabe, entonces hay error
                error = true;
                mensajeError += " La naturaleza del archivo no corresponde ni a 'Pagomático', ni a 'Pagos interbancarios CLABE'.<br>";
            }
        }

        // Continúa con el proceso mientras no haya errores en la naturaleza
        if (!naturalezaErronea) {
            // Según sea premium o no, se usa una posición o la otra
            for (String line : lineas) {
                switch (line.substring(0, 1)) {
                    case "1":
                        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
                        String strDate = line.substring(13, 19);
                        fecha = sdf.parse(strDate);
                        String fileDesc;
                        if (itsPremium) {
                            fileDesc = line.substring(59, 79).replaceAll("\\s", "");
                        } else {
                            fileDesc = line.substring(71, 91).replaceAll("\\s", "");
                        }
                        String nomOrden = fileDesc + "_" + strDate;
                        ordenDeposito = service.getOrdenDepositoDao().findById(ordenId);
                        ordenDeposito.setNombreRespuestaBancaria(nomOrden);
                        ordenDeposito.setFechaEjecucion(fecha);
                        break;
                    case "3":
                        Float importe;
                        String tarjeta;
                        String rfrnc;
                        if (itsPremium) {
                            importe = (Float.parseFloat(line.substring(5, 23))) / 100;
                            if (itsCuenta) {
                                tarjeta = line.substring(29, 45);
                            } else {
                                tarjeta = line.substring(27, 45);
                            }
                            rfrnc = line.substring(45, 85).trim();
                        } else {
                            importe = (Float.parseFloat(line.substring(21, 28))) / 100;
                            if (line.substring(28, 30).equals("01")) {
                                tarjeta = line.substring(39, 50);
                            } else {
                                tarjeta = line.substring(32, 50);
                            }
                            rfrnc = line.substring(121, 136);
                        }

                        Depositos depositos = service.getDepositosDao().depositoCargaRespuesta(
                                ordenDeposito.getId(), rfrnc, tarjeta, importe);
                        if (depositos == null) {
                            error = true;
                            mensajeError += " Verifique la línea: <br> " + line + "<br>";
                            break;
                        } else if (depositos.getEstatusDeposito().getId().equals(new BigDecimal(1))) {
                            EstatusDeposito estatusDeposito = new EstatusDeposito();
                            // Verifica si hubo éxito
                            Boolean success;
                            if (itsPremium) {
                                success = (line.substring(229, 230)).equals("3");
                            } else {
                                success = (line.substring(308, 313)).equals("30000");
                            }
                            // Si fue exitoso
                            if (success) {
                                estatusDeposito.setId(new BigDecimal(2));
                                depositos.setEstatusDeposito(estatusDeposito);
                                exitosos++;
                            } else {
                                BigDecimal errId;
                                String err;
                                if (itsPremium) {
                                    err = line.substring(234, 238);
                                } else {
                                    err = line.substring(309, 313);
                                }

                                errId = new BigDecimal(err);

                                if (verificaErrorBanamex(erroresBanamexLst, errId)) {
                                    ErroresBanamex errorBanamex = new ErroresBanamex(errId);
                                    estatusDeposito.setId(new BigDecimal(4));
                                    depositos.setEstatusDeposito(estatusDeposito);
                                    if (itsPremium) {
                                        depositos.setObservaciones("BANAMEX: " + line.substring(238));
                                    } else {
                                        depositos.setObservaciones("BANAMEX: " + line.substring(313));
                                    }
                                    depositos.setErrorBanamex(errorBanamex);
                                    depositosRechazados.add(depositos);
                                    rechazados++;
                                } else {
                                    estatusDeposito.setId(new BigDecimal(1000)); //ESTATUS FICTICIO PARA EVITAR LA ACTUALIZACIÓN MÁS ABAJO
                                    depositos.setEstatusDeposito(estatusDeposito);
                                    depositos.setObservaciones("El código de error encontrado, no es válido (" + err + "), por lo que no se procesó éste depósito.");
                                    noProcesados++;
                                    // ¿Aquí por qué no se aumenta el rechazados? R. Supongo que usa una misma lista en el front
                                    depositosRechazados.add(depositos);
                                }
                            }
                        } else {
                            EstatusDeposito estatusDeposito = new EstatusDeposito();
                            estatusDeposito.setId(new BigDecimal(1000));
                            depositos.setEstatusDeposito(estatusDeposito);
                            depositos.setObservaciones("Este depósito ya antes fue procesado.");
                            noProcesados++;
                            depositosRechazados.add(depositos);
                        }
                        depositos.setFechaDeposito(fecha); // Para este momento ya debería de ser la fecha del renglón
                        depositos.setFechaModificacion(new Date());
                        depositos.setFechaEnvioNotificacion(new Date());
                        depositosLista.add(depositos);
                        total++;
                        break;
                }
            }
        }

        if (!depositosLista.isEmpty()) {
            for (Depositos deposito : depositosLista) {
                if (!deposito.getEstatusDeposito().getId().equals(new BigDecimal(1000))) { //Que no actualice los depósitos con códigos de error no válidos
                    Boolean correoOk = envioCorreoAlumno(deposito);
//                    Boolean correoOk = false;
                    if (correoOk) {
                        correosEnviados++;
                    } else {
                        deposito.setFechaEnvioNotificacion(null);
                    }
                    service.getDepositosDao().update(deposito);
                }
            }
            //Validamos que ya no existan depositos en espera para cambiar el estatus de la órden
            if (!service.getDepositosDao().ordenConDepositosEnEspera(ordenId)) {
                EstatusDeposito estatusDeposito = new EstatusDeposito();
                estatusDeposito.setId(new BigDecimal(2));
                ordenDeposito.setEstatusDeposito(estatusDeposito);
            }
            service.getOrdenDepositoDao().update(ordenDeposito);
        }
    }

    /**
     * Envía un correo electrónico al alumno del depósito especificado. De
     * acuerdo al estatus, envía un mensaje diferente al alumno.
     *
     * @param deposito
     * @return
     */
    public Boolean envioCorreoAlumno(Depositos deposito) {
        if (MENSAJERECHAZADO == null || MENSAJEAPLICADO == null
                || MENSAJERECHAZADO.length() == 0 || MENSAJEAPLICADO.length() == 0) {
            return Boolean.FALSE;
        }
        AlumnoBO bo = new AlumnoBO(service);
        String mensaje = "";
        String correo = deposito.getAlumno().getCorreoElectronico();
        //Rechazado
        if (deposito.getEstatusDeposito().getId().compareTo(new BigDecimal("4")) == 0) {
            ErroresBanamex eb = service.getErroresBanamexDao().findById(deposito.getErrorBanamex().getId());
            mensaje = MENSAJERECHAZADO.replace("{0}", getMes(deposito.getOrdenDeposito().getMes())).replace("{1}", eb.getDescripcion());
        } else {
            mensaje = MENSAJEAPLICADO.replace("{0}", getMes(deposito.getOrdenDeposito().getMes()));
        }
//        System.out.println("mensaje: " + mensaje);
        Map info = bo.infoCorreo(deposito.getAlumno(), ASUNTO, ATENTAMENTE, mensaje, null, null);
        Boolean res = bo.enviarCorreo(correo, info); //bo.enviarCorreoNotificacionDeposito(correo, info, deposito);
        return res;
    }

    private String getMes(int mes) {
        DateFormat formatter = new SimpleDateFormat("MMMM", new Locale("es","MX"));
        GregorianCalendar calendario = new GregorianCalendar();
        calendario.set(Calendar.DAY_OF_MONTH, 1);
        calendario.set(Calendar.MONTH, mes - 1);
        return formatter.format(calendario.getTime());

    }

    public BigDecimal getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(BigDecimal ordenId) {
        this.ordenId = ordenId;
    }

    public Integer getExitosos() {
        return exitosos;
    }

    public Integer getRechazados() {
        return rechazados;
    }

    public Integer getNoProcesados() {
        return noProcesados;
    }

    public Integer getTotal() {
        return total;
    }

    public Boolean getError() {
        return error;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public List<Depositos> getDepositosRechazados() {
        return depositosRechazados;
    }

    public Integer getCorreosEnviados() {
        return correosEnviados;
    }

    public void setCorreosEnviados(Integer correosEnviados) {
        this.correosEnviados = correosEnviados;
    }
}

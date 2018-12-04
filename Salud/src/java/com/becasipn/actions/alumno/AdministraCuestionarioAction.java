package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AdministraCuestionarioBO;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.domain.ParametrosPDF;
import com.becasipn.exception.LoginException;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.CuestionarioPreguntaRespuesta;
import com.becasipn.persistence.model.CuestionarioPreguntas;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.MotivoRechazoSolicitud;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Usuario;
import static com.opensymphony.xwork2.Action.ERROR;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.persistence.PersistenceException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministraCuestionarioAction extends BaseReportAction implements MensajesAlumno {

    public static final String CUESTIONARIO = "cuestionario";
    public static final String PDF = "pdf";
    public static final String PDFMENOR = "pdfMenor";
    public static final String DESHABILITADO = "deshabilitado";
    public static final Integer TOTALPREGUNTASESE = 65;
    private static final int idPreguntaIngresoTotalMensual = 167;
    private static final int idPreguntaIntegrantesFam = 168;
    private static final int idPreguntaGastoTransp = 38;
    List<CuestionarioPreguntaRespuesta> preguntas = new ArrayList<>();
    List<ParametrosPDF> rangosSalarios = new ArrayList<>();
    List<CuestionarioRespuestas> respuestas = new ArrayList<>();
    List<CuestionarioRespuestasUsuario> respuestasUsuario = new ArrayList<>();
    private String numeroDeBoleta;
    private String cuestionarioId;
    private String hResultdos;
    private Integer totalPreguntas;
    private Alumno alumno;
    private String folio;
    private String periodoActual;
    private List<ParametrosPDF> esePdfList1 = new ArrayList<>();
    private List<ParametrosPDF> esePdfList2 = new ArrayList<>();
    private List<ParametrosPDF> esePdfList3 = new ArrayList<>();
    private List<ParametrosPDF> esePdfList4 = new ArrayList<>();
    private List<ParametrosPDF> esePdfList5 = new ArrayList<>();
    private Date hoy = new Date();
    private Boolean contestoSalud;
    private Boolean estatus;
    private Boolean periodoActivo = Boolean.FALSE;
    private BigDecimal alumnoId;
    private Integer transferencia = 1;
    Boolean eseTransporte = Boolean.FALSE;
    private String ingresoPorPersona;
    private String reason;

    public String cuestionario() {

        //Usuario
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        //Alumno
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        if (alumno == null) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        //Periodo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);

        estatus = alumno.getEstatus();
        //Buscar cuestionario correspondiente a el ESE
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));

        DatosAcademicos da = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        if (c == null) {
            addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
        } else if (cuestionarioId == null || cuestionarioId.isEmpty()) {
            addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
        } else {
            BigDecimal cId = new BigDecimal(cuestionarioId);
            preguntas = getDaos().getCuestionarioPreguntaRespuestaDao().findByCuestionario(cId, da == null ? null : da.getUnidadAcademica().getNivel().getId());
            rangosSalarios = getDaos().getTipoBecaPeriodoDao().rangoIngresoPorPersonaPorBeca(periodo.getId(), da == null ? null : da.getUnidadAcademica().getNivel().getId());
            totalPreguntas = getDaos().getCuestionarioPreguntaRespuestaDao().totalPreguntas(c.getId());
            respuestasUsuario = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(cId, u.getId(), periodo.getId());
            SolicitudBeca sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId());
            if (sb != null) {
                transferencia = sb.getPermiteTransferencia();
            }
        }
        return CUESTIONARIO;
    }

    public String guardar() {
        //Alumno
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        alumno = listAlumno.get(0);
        //Periodo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);

        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (usuario == null) {
            return INPUT;
        }

        if (!periodoActivo) {
            if (getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("1"), periodo.getId())) {
                return resultado();
            } else {
                return cuestionario();
            }
        }
        SolicitudBeca sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId());
        if (sb == null) {
            sb = new SolicitudBeca();
        } else if (sb.getFinalizado()) {
            addActionError(getText("misdatos.alumno.estudio.socioeconomico.finalizado"));
            return resultado();
        }
        //Alumno
        sb.setAlumno(alumno);
        //Cuestionario
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        sb.setCuestionario(c);
        //Periodo
        sb.setPeriodo(periodo);
        sb.setFechaModificacion(new Date());
        sb.setFechaIngreso(new Date());
        sb.setFinalizado(Boolean.FALSE);
        sb.setPermiteTransferencia(0);
        //Borramos lo que este previamente guardado del ESE.
        getDaos().getCuestionarioRespuestasUsuarioDao().borrarESEporAlumno(usuario.getId(), periodo.getId());
        if (hResultdos != null && !hResultdos.equals("")) {
            String[] resultados = null;
            String[] valores = null;
            hResultdos = hResultdos.replaceAll("buscador", "");
            resultados = hResultdos.split("@");
            if (resultados.length >= 1) {
                for (String pregunta : resultados) {
                    valores = pregunta.split(",");
                    if (valores.length > 1) {
                        String noPregunta = valores[0];
                        String valor = valores[1];
                        String[] multiple = valor.split("-");
                        if (noPregunta.equals("0")) {
                            Beca b = getDaos().getBecaDao().findById(new BigDecimal(valor));
                            sb.setProgramaBecaSolicitada(b);
                        } else if (noPregunta.equals("-1")) {
                            sb.setPermiteTransferencia(1);
                        } else {
                            for (String m : multiple) {
                                if (noPregunta != null && !noPregunta.isEmpty()) {
                                    switch (Integer.parseInt(noPregunta)) {
                                        case idPreguntaIngresoTotalMensual:
                                            sb.setIngresoTotalMensual(Float.parseFloat(m));
                                            break;
                                        case idPreguntaIntegrantesFam:
                                            sb.setNumeroDeIntegrantes(Integer.parseInt(m));
                                            break;
                                        case idPreguntaGastoTransp:
                                            sb.setGastoEnTransporte(getDaos().getCuestionarioRespuestasDao().findById(new BigDecimal(m)).getNombre());
                                            break;
                                    }
                                }
                                CuestionarioRespuestasUsuario ru = new CuestionarioRespuestasUsuario();
                                CuestionarioPreguntas p = getDaos().getCuestionarioPreguntasDao().findById(new BigDecimal(noPregunta));
                                ru.setPregunta(p);
                                BigDecimal idRespuesta;
                                CuestionarioRespuestas cr = new CuestionarioRespuestas();
                                try {
                                    //Respuesta del catalogo de respuesta
                                    idRespuesta = new BigDecimal(m);
                                    if (espreguntabierta(p.getCuestionarioPreguntaTipo().getId())) {
                                        cr.setId(new BigDecimal(181));
                                        ru.setRespuestaAbierta(valor);
                                        ru.setRespuesta(cr);
                                    } else {
                                        cr = getDaos().getCuestionarioRespuestasDao().findById(idRespuesta);
                                        ru.setRespuesta(cr);
                                    }
                                } catch (Exception e) {
                                    // Cuando no puede convertir la respuesta a 
                                    // BigDecimal, entonces es respuesta Abierta
                                    CuestionarioRespuestas cra = new CuestionarioRespuestas();
                                    cra.setId(new BigDecimal(181));
                                    ru.setRespuesta(cra);
                                    ru.setRespuestaAbierta(m);
                                }
                                ru.setPeriodo(periodo);
                                ru.setUsuario(usuario);
                                ru.setCuestionario(c);
                                getDaos().getCuestionarioRespuestasUsuarioDao().save(ru);

                                //PROSPERA
                                //Si la respuesta a la pregunta ¿Tu familia cuenta con apoyo prospera?(35) es SI(49), actualizamos 
                                //la información contenida en la tabla alumno en el campo beneficiarioprospera
                                if (p.getId().compareTo(new BigDecimal(35)) == 0 && cr.getId().compareTo(new BigDecimal(49)) == 0) {
                                    alumno.setBeneficiarioOportunidades(Boolean.TRUE);
                                    getDaos().getAlumnoDao().update(alumno);
                                }
                            }
                        }
                    }
                }
                Float suma = null;
                Float total = null;
                List<CuestionarioRespuestasUsuario> respuestas = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(new BigDecimal("1"), usuario.getId(), periodo.getId());
                CuestionarioRespuestasUsuario cru = null;
                for (CuestionarioRespuestasUsuario r : respuestas) {
                    if (r.getPregunta().getId().equals(new BigDecimal("167"))) {
                        suma = Float.parseFloat(r.getRespuestaAbierta());
                        if (suma < 1 || suma > 99999) {
                            addActionError(getText("La suma del ingreso mensual de los integrantes de tu familia solo puede tener un valor entre 1 y 99999."));
                            return cuestionario();
                        }
                    } else if (r.getPregunta().getId().equals(new BigDecimal("168"))) {
                        total = Float.parseFloat(r.getRespuestaAbierta());
                        if (total < 1 || total > 99) {
                            addActionError(getText("El total de integrantes de tu familia solo puede tener un valor entre 1 y 99."));
                            return cuestionario();
                        }
                    } else if (r.getPregunta().getId().equals(new BigDecimal("13"))) {
                        cru = r;
                    }
                }
                if (suma != null && total != null) {
                    Float inPer = suma / total;
                    sb.setIngresosPercapitaPesos(inPer);
                }
            }
            if (sb.getId() == null) {
                getDaos().getSolicitudBecaDao().save(sb);
            } else {
                getDaos().getSolicitudBecaDao().update(sb);
            }
        } else {
            addActionError(getText("misdatos.alumno.no.datos"));
            return cuestionario();
        }
        addActionMessage(getText("misdatos.alumno.estudio.socioeconomico.guardado.parcial"));
        return cuestionario();
    }

    public String finalizar() throws LoginException {

        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        alumno = listAlumno.get(0);
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();

        String salarioMinimo = (String) ActionContext.getContext().getApplication().get("montoVigenteSalarioMinimo");

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);

        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (usuario == null) {
            return INPUT;
        }

        if (!periodoActivo) {
            if (getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("1"), periodo.getId())) {
                return resultado();
            } else {
                return cuestionario();
            }
        }
        //Se valida que no le falte alguna pregunta por contestar aunque ya haya finalizado el estudio socioeconómico.
        Integer preguntasContestadas = getDaos().getCuestionarioPreguntaRespuestaDao().preguntasContestadas(new BigDecimal("1"), periodo.getId(), usuario.getId());
        if (preguntasContestadas >= TOTALPREGUNTASESE) {
            if (getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("1"), periodo.getId())) {
                return resultado();
            }
        }
        SolicitudBeca sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId());
        if (sb == null) {
            sb = new SolicitudBeca();
        }
        //Alumno
        sb.setAlumno(alumno);
        //Cuestionario
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        sb.setCuestionario(c);
        //Periodo
        sb.setPeriodo(periodo);
        sb.setFechaModificacion(new Date());
        sb.setFechaIngreso(new Date());
        sb.setFinalizado(Boolean.TRUE);
        sb.setPermiteTransferencia(0);
        sb.setVulnerabilidadSubes(Boolean.FALSE);
        //Total de preguntas
        totalPreguntas = getDaos().getCuestionarioPreguntaRespuestaDao().totalPreguntas(c.getId());
        //Borramos lo que este previamente guardado del ESE.
        getDaos().getCuestionarioRespuestasUsuarioDao().borrarESEporAlumno(usuario.getId(), periodo.getId());
        if (hResultdos != null && !hResultdos.equals("")) {
            String[] resultados = null;
            String[] valores = null;
            hResultdos = hResultdos.replaceAll("buscador", "");
            resultados = hResultdos.split("@");
            if (resultados.length > 1) {
                for (String pregunta : resultados) {
                    valores = pregunta.split(",");
                    if (valores.length > 1) {
                        ////System.out.println(valores[0] + "|" + valores[1]);
                        String noPregunta = valores[0];
                        String valor = valores[1];
                        String[] multiple = valor.split("-");
                        if (noPregunta.equals("0")) {
                            Beca b = getDaos().getBecaDao().findById(new BigDecimal(valor));
                            sb.setProgramaBecaSolicitada(b);
                        } else if (noPregunta.equals("-1")) {
                            sb.setPermiteTransferencia(1);
                        } else {
                            for (String m : multiple) {
                                CuestionarioRespuestasUsuario ru = new CuestionarioRespuestasUsuario();
                                CuestionarioPreguntas p = getDaos().getCuestionarioPreguntasDao().findById(new BigDecimal(noPregunta));
                                ru.setPregunta(p);
                                BigDecimal idRespuesta;
                                CuestionarioRespuestas cr = new CuestionarioRespuestas();
                                try {
                                    //Respuesta del catalogo de respuesta
                                    idRespuesta = new BigDecimal(m);
                                    if (espreguntabierta(p.getCuestionarioPreguntaTipo().getId())) {
                                        cr.setId(new BigDecimal(181));
                                        ru.setRespuestaAbierta(valor);
                                        ru.setRespuesta(cr);
                                    } else {
                                        cr = getDaos().getCuestionarioRespuestasDao().findById(idRespuesta);
                                        ru.setRespuesta(cr);
                                    }
                                } catch (Exception e) {
                                    //Respuesta Abierta
                                    CuestionarioRespuestas cra = new CuestionarioRespuestas();
                                    cra.setId(new BigDecimal(1));
                                    ru.setRespuesta(cra);
                                    ru.setRespuestaAbierta(m);
                                }
                                ru.setPeriodo(periodo);
                                ru.setUsuario(usuario);
                                ru.setCuestionario(c);
                                try {
                                    if (ru.getUsuario() != null && ru.getPregunta() != null && ru.getRespuesta() != null && ru.getCuestionario() != null && ru.getPeriodo() != null) {
                                        getDaos().getCuestionarioRespuestasUsuarioDao().save(ru);
                                    } else {
                                        //INTENTAMOS CACHAR UN ERROR QUE NO HEMOS PODIDO REPLICAR
                                        //Y ENVIAMOS CORREO CON EL ESTADO DE LOS OBJETOS
                                        String body = "\nCuestionarioRespuestasUsuario: " + ru.toString();
                                        sendEmailMonitoreo("SIBEC: CuestionarioRespuestasUsuario", body);
                                    }
                                } catch (PersistenceException e) {
                                    LOG.warn("Ya existe el ESE para el periodo activo y alumno_id" + alumno.getId() + " Exception: " + e.getMessage());
                                } catch (MessagingException ex) {
                                    Logger.getLogger(AdministraCuestionarioAction.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }
                //Se calculan los salarios mínimos
                Float suma = null;
                Float total = null;
                List<CuestionarioRespuestasUsuario> respuestas = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(new BigDecimal("1"), usuario.getId(), periodo.getId());
                CuestionarioRespuestasUsuario cru = null;
                for (CuestionarioRespuestasUsuario r : respuestas) {
                    if (r.getPregunta().getId().equals(new BigDecimal("167"))) {
                        suma = Float.parseFloat(r.getRespuestaAbierta());
                        if (suma < 1 || suma > 99999) {
                            addActionError(getText("La suma del ingreso mensual de los integrantes de tu familia solo puede tener un valor entre 1 y 99999."));
                            return cuestionario();
                        }
                    } else if (r.getPregunta().getId().equals(new BigDecimal("168"))) {
                        total = Float.parseFloat(r.getRespuestaAbierta());
                        if (total < 1 || total > 99) {
                            addActionError(getText("El total de integrantes de tu familia solo puede tener un valor entre 1 y 99."));
                            return cuestionario();
                        }
                    } else if (r.getPregunta().getId().equals(new BigDecimal("13"))) {
                        cru = r;
                    }
                }
                if (suma != null && total != null) {
                    Float inPer = suma / total;
                    sb.setIngresosPercapitaPesos(inPer);
                    Double i = inPer / Double.parseDouble(salarioMinimo);
                    Integer ingresos = i.intValue();
                    //Se busca la respuesta
                    CuestionarioRespuestas cr = new CuestionarioRespuestas();
                    if (ingresos >= 10) {
                        ingresos = 10;
                    }
                    switch (ingresos) {
                        case 0:
                            cr.setId(new BigDecimal("184"));
                            break;
                        case 1:
                            cr.setId(new BigDecimal("62"));
                            break;
                        case 2:
                            cr.setId(new BigDecimal("63"));
                            break;
                        case 3:
                            cr.setId(new BigDecimal("64"));
                            break;
                        case 4:
                            cr.setId(new BigDecimal("65"));
                            break;
                        case 5:
                            cr.setId(new BigDecimal("66"));
                            break;
                        case 6:
                            cr.setId(new BigDecimal("67"));
                            break;
                        case 7:
                            cr.setId(new BigDecimal("68"));
                            break;
                        case 8:
                            cr.setId(new BigDecimal("69"));
                            break;
                        case 9:
                            cr.setId(new BigDecimal("70"));
                            break;
                        case 10:
                            cr.setId(new BigDecimal("71"));
                            break;
                    }
                    if (cru == null) {
                        cru = new CuestionarioRespuestasUsuario();
                        cru.setUsuario(usuario);
                        CuestionarioPreguntas p = getDaos().getCuestionarioPreguntasDao().findById(new BigDecimal("13"));
                        cru.setPregunta(p);
                        cru.setRespuesta(cr);
                        cru.setCuestionario(c);
                        cru.setPeriodo(periodo);
                        //No tiene respuesta se guarda
                        getDaos().getCuestionarioRespuestasUsuarioDao().save(cru);
                    } else {
                        cru.setUsuario(usuario);
                        CuestionarioPreguntas p = getDaos().getCuestionarioPreguntasDao().findById(new BigDecimal("13"));
                        cru.setPregunta(p);
                        cru.setRespuesta(cr);
                        cru.setCuestionario(c);
                        cru.setPeriodo(periodo);
                        //Tiene respuesta se actualiza
                        getDaos().getCuestionarioRespuestasUsuarioDao().update(cru);
                    }
                }
                try {
                    preguntasContestadas = getDaos().getCuestionarioPreguntaRespuestaDao().preguntasContestadas(c.getId(), periodo.getId(), usuario.getId());
                    if (preguntasContestadas >= TOTALPREGUNTASESE) {
                        if (sb.getId() == null) {
                            getDaos().getSolicitudBecaDao().save(sb);
                        } else {
                            getDaos().getSolicitudBecaDao().update(sb);
                        }
                    } else {
                        addActionError(getText("misdatos.alumno.no.finalizar"));
                        sb.setFinalizado(Boolean.FALSE);
                        if (sb.getId() == null) {
                            getDaos().getSolicitudBecaDao().save(sb);
                        } else {
                            getDaos().getSolicitudBecaDao().update(sb);
                        }
                        return cuestionario();
                    }
                } catch (PersistenceException e) {
                    LOG.warn("Ya existe el ESE para el perido activo y alumno_id: " + alumno.getId());
                }
                //Preasignación
                Boolean validarBeca;
                Boolean sinErrorDeporteCultural = Boolean.TRUE; //Varieble que indica si se intenta preasignar una beca deportiva o cultural a una solicitud de beca con programa de beca solicitado, diferente a éstas becas
                MotivoRechazoSolicitud motivo = new MotivoRechazoSolicitud();
                DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
                alumno.setDatosAcademicos(datosAcademicos);
                sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId());
                OtorgamientoBO oBo = new OtorgamientoBO(getDaos());
                TipoBecaPeriodo becaPeriodo = oBo.becaSolicitud(sb, periodo, datosAcademicos, false, false, null, null);
                //Boolean tieneUniversal = getDaos().getOtorgamientoDao().tieneOtorgamientoUniversal(alumno.getId(), periodoActivo.getId());
                //Boolean tieneComplementaria = getDaos().getOtorgamientoDao().tieneOtorgamientoComplementaria(alumno.getId(), periodoActivo.getId());
                if (becaPeriodo != null) {
                    validarBeca = oBo.validarBeca(alumno, becaPeriodo, sb, null, null);
                    sinErrorDeporteCultural = oBo.verificaPreasignacionDeporteCultura(becaPeriodo, sb);
                    if (!validarBeca) {
                        motivo.setId(new BigDecimal(oBo.getRazonNoOtorgamiento()));
                        sb.setRechazoBecaSolicitada(motivo);
                        if (sb.getPermiteTransferencia() == 1) {
                            List<TipoBecaPeriodo> becasAplicables = getDaos().getTipoBecaPeriodoDao().becasAplicables(alumno, sb, false, false);
                            becaPeriodo = null;
                            if (becasAplicables == null || becasAplicables.isEmpty()) {
                                motivo.setId(new BigDecimal(oBo.getRazonNoOtorgamiento()));
                                sb.setRechazoTransferencia(motivo);
                            } else {
                                Iterator<TipoBecaPeriodo> it = becasAplicables.iterator();
                                while (it.hasNext() && !validarBeca) {
                                    becaPeriodo = it.next();
                                    validarBeca = oBo.validarBeca(alumno, becaPeriodo, sb, null, null);
                                    sinErrorDeporteCultural = oBo.verificaPreasignacionDeporteCultura(becaPeriodo, sb);
                                }
                                if (!validarBeca) {
                                    motivo.setId(new BigDecimal(oBo.getRazonNoOtorgamiento()));
                                    sb.setRechazoTransferencia(motivo);
                                } else if (sinErrorDeporteCultural) {
                                    sb.setTipoBecaPreasignada(becaPeriodo);
                                }
                            }
                        }
                    } else if (sinErrorDeporteCultural) {
                        sb.setTipoBecaPreasignada(becaPeriodo);
                    }
                } else if (sb.getPermiteTransferencia() == 1) {
                    motivo.setId(new BigDecimal(24));
                    sb.setRechazoBecaSolicitada(motivo);
                    List<TipoBecaPeriodo> becasAplicables = getDaos().getTipoBecaPeriodoDao().becasAplicables(alumno, sb, false, false);
                    becaPeriodo = null;
                    if (becasAplicables == null || becasAplicables.isEmpty()) {
                        motivo.setId(new BigDecimal(20));
                        sb.setRechazoTransferencia(motivo);
                    } else {
                        Iterator<TipoBecaPeriodo> it = becasAplicables.iterator();
                        validarBeca = false;
                        while (it.hasNext() && !validarBeca) {
                            becaPeriodo = it.next();
                            validarBeca = oBo.validarBeca(alumno, becaPeriodo, sb, null, null);
                            sinErrorDeporteCultural = oBo.verificaPreasignacionDeporteCultura(becaPeriodo, sb);
                        }
                        if (!validarBeca) {
                            motivo.setId(new BigDecimal(oBo.getRazonNoOtorgamiento()));
                            sb.setRechazoTransferencia(motivo);
                        } else if (sinErrorDeporteCultural) {
                            sb.setTipoBecaPreasignada(becaPeriodo);
                        }
                    }
                } else {
                    motivo.setId(new BigDecimal(25));
                    sb.setRechazoBecaSolicitada(motivo);
                }
                getDaos().getSolicitudBecaDao().update(sb);
            }
        } else {
            addActionError(getText("misdatos.alumno.no.datos"));
            return cuestionario();
        }
        try {
            setMenu(usuario);
        } catch (LoginException ex) {
            Logger.getLogger(AdministraCuestionarioAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado();
    }

    public String resultado() {
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();

        //Usuario
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        //Cuestionario
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        //Alumno        
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        alumno = listAlumno.get(0);
        //Periodo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        alumno.setDatosAcademicos(datosAcademicos);
        //Si el alumno no tiene datos no lo dejaremos contestar.
        if (datosAcademicos == null) {
            addActionError(getText("misdatos.alumno.sin.datos.academicos"));
            return ERROR;
        }

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);
        // Si el alumno está en lista negra, no lo dejamos contestar.
        AdministraCuestionarioBO cBO = AdministraCuestionarioBO.getInstance(getDaos());
        if (cBO.tieneBecaExterna(alumno)) {
            reason = "Hemos detectado que ya eres beneficiario de una beca en una institución fuera del Instituto Politécnico Nacional, por lo que de acuerdo a normativas para la asignación de becas, no podemos ofrecerte realizar una solicitud para éste periodo.";
            return DESHABILITADO;
        }
        // Si el alumno es de nuevo ingreso y la variable correspondiente 
        // no permite a los de nuevo ingreso contestar solicitudes, no lo dejamos contestar.
        if (cBO.deberiaIrPorManutencion(alumno)) {
            reason = "Por el momento la convocatoria disponible es para becas Manutención."
                    + " Por favor,"
                    + " <a href='https://www.subes.sep.gob.mx/'>dirígete al sitio de SUBES y realiza tu registro.</a>"
                    + " En caso de resultar beneficiado, podrás darle seguimiento aquí, en tu sesión de SIBec.";
            return DESHABILITADO;
        }
        //Se busca el otorgamiento del periodo anterior.
        Otorgamiento otorgamientoAnterior = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getPeriodoAnterior().getId());
        //Si el alumno tiene la validación de inscripción no le permitira constestar el estudio socioeconomico.
        Boolean validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodo, alumno.getId(), null);
        if (validacionInscripcion && otorgamientoAnterior != null
                && datosAcademicos.getUnidadAcademica().getId().equals(otorgamientoAnterior.getProceso().getUnidadAcademica().getId())) {
            reason = "Este periodo no es necesario contestar tu estudio socioeconómico, debido a que las características de tu beca así lo indican.";
            return DESHABILITADO;
        }
        if (periodoActivo) {
            Integer preguntasContestadas = getDaos().getCuestionarioPreguntaRespuestaDao().preguntasContestadas(c.getId(), periodo.getId(), u.getId());
            if (preguntasContestadas < TOTALPREGUNTASESE) {
                //getDaos().getSolicitudBecaDao().eliminarFinalizarESEporAlumno(c.getId(), periodo.getId(), alumno.getId());
                return cuestionario();
            }
        }
        //Bandera para conocer si el alumno ya contesto el censo de salud.
        //2018/08/16: Dr. Gilberto solicita quitar el censo de saludo como requisito para el proceso de becas.
        //No se elimina la lógica.
        //contestoSalud = getDaos().getSolicitudBecaDao().contestoEncuestaSalud(alumnoId, activeTerm.getId());
        contestoSalud = Boolean.TRUE;
        if (c == null) {
            addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
        } else if (cuestionarioId == null || cuestionarioId.isEmpty()) {
            addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
        } else {
            BigDecimal cId = new BigDecimal(cuestionarioId);
            Boolean ese = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), cId, periodo.getId());
            respuestasUsuario = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(cId, u.getId(), periodo.getId());
            SolicitudBeca sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId());
            if (sb != null) {
                transferencia = sb.getPermiteTransferencia();
                ingresoPorPersona = sb.getIngresosPercapitaPesos() == null ? "" : sb.getIngresosPercapitaPesos().toString();
            }
            if (ese == false) {
                return cuestionario();
            }
        }
        eseTransporte = getDaos().getCuestionarioTransporteDao().tieneEseTransporte(u.getId(), periodo.getId());
        addActionMessage(getText("misdatos.alumno.estudio.socioeconomico.exito"));
        return SUCCESS;
    }

    public String pdf() {

        //Periodo
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();

        //Alumno
        if (isAlumno()) {
            String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
            alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        } else {
            alumno = getDaos().getAlumnoDao().findById(alumnoId);
        }

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);

        //Usuario
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        //Cuestionario
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));

        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        alumno.setDatosAcademicos(datosAcademicos);
        if (isAlumno()) {
            if (periodoActivo) {
                Integer preguntasContestadas = getDaos().getCuestionarioPreguntaRespuestaDao().preguntasContestadas(c.getId(), periodo.getId(), u.getId());
                if (preguntasContestadas < 64) {
                    getDaos().getSolicitudBecaDao().eliminarFinalizarESEporAlumno(c.getId(), periodo.getId(), alumno.getId());
                    addActionError(getText("misdatos.alumno.no.pdf"));
                    return cuestionario();
                }
            }
        }

        SolicitudBeca sb = null;
        //Se busca el otorgamiento del periodo anterior.
        Otorgamiento otorgamientoAnterior = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getPeriodoAnterior().getId());
        //Si el alumno tiene la validación de inscripción no le permitira constestar el estudio socioeconomico.
        Boolean validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodo, alumno.getId(), null);
        if (validacionInscripcion && otorgamientoAnterior != null
                && datosAcademicos.getUnidadAcademica().getId().equals(otorgamientoAnterior.getProceso().getUnidadAcademica().getId())) {
            sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), otorgamientoAnterior.getPeriodo().getId());
            periodo = otorgamientoAnterior.getPeriodo();
        } else {
            sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId());
            periodo = periodo;
        }

        try {
            if (alumno.getNombre() != null) {
                alumno.setNombre(alumno.getNombre().replace("'", "\\\'"));
            }
            if (alumno.getApellidoPaterno() != null) {
                alumno.setApellidoPaterno(alumno.getApellidoPaterno().replace("'", "\\\'"));
            }
            if (alumno.getApellidoMaterno() != null) {
                alumno.setApellidoMaterno(alumno.getApellidoMaterno().replace("'", "\\\'"));
            }
            if (periodo.getClave().length() == 5) {
                periodoActual = periodo.getClave().substring(0, 4) + " / 0" + periodo.getClave().substring(4, 5);
            } else if (periodo.getClave().length() == 6) {
                periodoActual = periodo.getClave().substring(0, 4) + " / 0" + periodo.getClave().substring(5, 6);
            }
            NumberFormat f1 = new DecimalFormat("00");
            NumberFormat f2 = new DecimalFormat("0000000");
            SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
            String hr = sdf.format(hoy);
            Integer h = Integer.parseInt(hr);
            String hexa = Integer.toHexString(h);
            //folio = "ESE" + f1.format(c.getId()) + periodo.getId().toString() + f2.format(alumno.getId()) + hexa;
            ParametrosPDF esePdf = new ParametrosPDF();
            if (c == null) {
                addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
            } else if (cuestionarioId == null || cuestionarioId.isEmpty()) {
                addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
            } else {
                Boolean ese = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), c.getId(), periodo.getId());
                if (isAlumno()) {
                    if (ese == false) {
                        return cuestionario();
                    }
                }
                respuestasUsuario = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(c.getId(), alumno.getUsuario().getId(), periodo.getId());
                transferencia = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId()).getPermiteTransferencia();
                BigDecimal preguntaActual = null;
                int i;
                for (CuestionarioRespuestasUsuario cru : respuestasUsuario) {
                    if (cru.getPregunta().getId().equals(new BigDecimal("13"))) {
                        String s;
                        if (cru.getRespuesta().getId().equals(new BigDecimal("71"))) {
                            s = "10";
                        } else {
                            s = f1.format(Integer.parseInt(cru.getRespuesta().getNombre()));
                        }
                        folio = "ESE" + f1.format(c.getId()) + periodo.getId().toString() + f2.format(alumno.getId()) + "S" + s + hexa.toUpperCase();
                    }
                    if ((cru.getSeccion().getId()).equals(new BigDecimal("1"))) {
                        esePdf = new ParametrosPDF();
                        if (((cru.getPregunta().getId())).equals(preguntaActual)) {
                            esePdf.setParametro1("");
                        } else {
                            preguntaActual = cru.getPregunta().getId();
                            if ((cru.getPregunta().getNombreSinHTML().indexOf("</br>")) != -1) {
                                esePdf.setParametro1(cru.getPregunta().getNombreSinHTML().substring(0, cru.getPregunta().getNombreSinHTML().indexOf("</br>")));
                                esePdf.setParametro2("");
                                esePdfList1.add(esePdf);
                                esePdf = new ParametrosPDF();
                                esePdf.setParametro1(cru.getPregunta().getNombreSinHTML().substring((cru.getPregunta().getNombreSinHTML().indexOf("</br>")) + 5));
                            } else {
                                esePdf.setParametro1(cru.getPregunta().getNombreSinHTML());
                            }
                        }
                        if (espreguntabierta(cru.getPregunta().getCuestionarioPreguntaTipo().getId())) {
                            esePdf.setParametro2(cru.getRespuestaAbierta());
                        } else {
                            esePdf.setParametro2(cru.getRespuesta().getNombre());
                        }
                        esePdfList1.add(esePdf);
                    }
                    if ((cru.getSeccion().getId()).equals(new BigDecimal("2"))) {
                        if (cru.getPregunta().getId().equals(new BigDecimal("13"))) {
                        } else {
                            esePdf = new ParametrosPDF();
                            if (((cru.getPregunta().getId())).equals(preguntaActual)) {
                                esePdf.setParametro1("");
                            } else {
                                preguntaActual = cru.getPregunta().getId();
                                if ((cru.getPregunta().getNombreSinHTML().indexOf("</br>")) != -1) {
                                    esePdf.setParametro1(cru.getPregunta().getNombreSinHTML().substring(0, cru.getPregunta().getNombreSinHTML().indexOf("</br>")));
                                    esePdf.setParametro2("");
                                    esePdfList2.add(esePdf);
                                    esePdf = new ParametrosPDF();
                                    esePdf.setParametro1(cru.getPregunta().getNombreSinHTML().substring((cru.getPregunta().getNombreSinHTML().indexOf("</br>")) + 5));
                                } else if ((cru.getPregunta().getNombreSinHTML().indexOf("</a>")) != -1) {
                                    esePdf.setParametro1(cru.getPregunta().getNombreSinHTML().substring(0, cru.getPregunta().getNombreSinHTML().indexOf("<a")));
                                } else {
                                    esePdf.setParametro1(cru.getPregunta().getNombreSinHTML());
                                }
                            }
                            if (espreguntabierta(cru.getPregunta().getCuestionarioPreguntaTipo().getId())) {
                                esePdf.setParametro2(cru.getRespuestaAbierta());
                            } else {
                                esePdf.setParametro2(cru.getRespuesta().getNombre());
                            }
                            esePdfList2.add(esePdf);
                        }
                    }
                    if ((cru.getSeccion().getId()).equals(new BigDecimal("3"))) {
                        esePdf = new ParametrosPDF();
                        if (((cru.getPregunta().getId())).equals(preguntaActual)) {
                            esePdf.setParametro1("");
                        } else {
                            preguntaActual = cru.getPregunta().getId();
                            if ((cru.getPregunta().getNombreSinHTML().indexOf("</br>")) != -1) {
                                esePdf.setParametro1(cru.getPregunta().getNombreSinHTML().substring(0, cru.getPregunta().getNombreSinHTML().indexOf("</br>")));
                                esePdf.setParametro2("");
                                esePdfList3.add(esePdf);
                                esePdf = new ParametrosPDF();
                                esePdf.setParametro1(cru.getPregunta().getNombreSinHTML().substring((cru.getPregunta().getNombreSinHTML().indexOf("</br>")) + 5));
                            } else {
                                esePdf.setParametro1(cru.getPregunta().getNombreSinHTML());
                            }
                        }
                        if (espreguntabierta(cru.getPregunta().getCuestionarioPreguntaTipo().getId())) {
                            esePdf.setParametro2(cru.getRespuestaAbierta());
                        } else {
                            esePdf.setParametro2(cru.getRespuesta().getNombre());
                        }
                        esePdfList3.add(esePdf);
                    }
                    if ((cru.getSeccion().getId()).equals(new BigDecimal("4"))) {
                        esePdf = new ParametrosPDF();
                        if (((cru.getPregunta().getId())).equals(preguntaActual)) {
                            esePdf.setParametro1("");
                        } else {
                            preguntaActual = cru.getPregunta().getId();
                            if ((cru.getPregunta().getNombreSinHTML().indexOf("</br>")) != -1) {
                                esePdf.setParametro1(cru.getPregunta().getNombreSinHTML().substring(0, cru.getPregunta().getNombreSinHTML().indexOf("</br>")));
                                esePdf.setParametro2("");
                                esePdfList4.add(esePdf);
                                esePdf = new ParametrosPDF();
                                esePdf.setParametro1(cru.getPregunta().getNombreSinHTML().substring((cru.getPregunta().getNombreSinHTML().indexOf("</br>")) + 5));
                            } else {
                                esePdf.setParametro1(cru.getPregunta().getNombreSinHTML());
                            }
                        }
                        if (espreguntabierta(cru.getPregunta().getCuestionarioPreguntaTipo().getId())) {
                            esePdf.setParametro2(cru.getRespuestaAbierta());
                        } else {
                            esePdf.setParametro2(cru.getRespuesta().getNombre());
                        }
                        esePdfList4.add(esePdf);
                    }
                }
                esePdf = new ParametrosPDF();
                esePdf.setParametro1("Beca por la que deseas participar:");
                esePdf.setParametro2(sb.getProgramaBecaSolicitada().getNombre());
                esePdfList5.add(esePdf);
                esePdf = new ParametrosPDF();
                esePdf.setParametro1("Ingresos por persona en tu familia:");
                esePdf.setParametro2(String.format("%.2f", sb.getIngresosPercapitaPesos()));
                esePdfList5.add(esePdf);
            }
        } catch (Exception ex) {
            Logger.getLogger(AdministraCuestionarioAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        ;
        String leyenda = "Acepto que en caso de no poder ser beneficiado con el tipo de beca que solicité, me pueda ser otorgada otra, previo cumplimiento de los requisitos"
                + " (sujeto a disponibilidad de presupuesto). Así mismo que en caso de resultar becario en este ciclo escolar," + (sb.getPermiteTransferencia() == 1 ? "" : " no")
                + " acepto que en el proceso de validación de mi beca se me transfiera a otra si dejo de cumplir con los requisitos de la que se me haya otorgado inicialmente"
                + " (sujeto a disponibilidad de presupuesto).";
        getParametros().put("imagenPlaca", "/com/becasipn/jasper/placasep.JPG");
        getParametros().put("unidadAcademica", datosAcademicos.getUnidadAcademica().getNombre());
        getParametros().put("nombre", alumno.getFullName());
        getParametros().put("boleta", alumno.getBoleta());
        getParametros().put("curp", alumno.getCurp());
        getParametros().put("telefono", alumno.getCelular());
        getParametros().put("correoElectronico", alumno.getCorreoElectronico());
        getParametros().put("estado", alumno.getDireccion().getRelacionGeografica().getEstado().getNombre());
        getParametros().put("delMun", alumno.getDireccion().getRelacionGeografica().getMunicipio().getNombre());
        getParametros().put("modalidad", datosAcademicos.getModalidad().getNombre());
        getParametros().put("periodo", periodoActual);
        getParametros().put("folio", folio);
        getParametros().put("permiteTransferencia", sb.getPermiteTransferencia() == 1 ? "SI" : "NO");
        getParametros().put("leyenda", leyenda);
        JRBeanCollectionDataSource seccion1 = new JRBeanCollectionDataSource(esePdfList1);
        JRBeanCollectionDataSource seccion2 = new JRBeanCollectionDataSource(esePdfList2);
        JRBeanCollectionDataSource seccion3 = new JRBeanCollectionDataSource(esePdfList3);
        JRBeanCollectionDataSource seccion4 = new JRBeanCollectionDataSource(esePdfList4);
        JRBeanCollectionDataSource seccion5 = new JRBeanCollectionDataSource(esePdfList5);
        getParametros().put("ese1", seccion1);
        getParametros().put("ese2", seccion2);
        getParametros().put("ese3", seccion3);
        getParametros().put("ese4", seccion4);
        getParametros().put("ese5", seccion5);
        //Se condiciona el archivo según la edad dado que a los menores de edad se les pedira la firma de padre, madre o tutor.
        if (abo.calcularEdad(alumno.getFechaDeNacimiento()) < 18) {
            return PDFMENOR;
        } else {
            return PDF;
        }
    }

    public boolean espreguntabierta(BigDecimal tipo) {
        return tipo.equals(new BigDecimal("3")) || tipo.equals(new BigDecimal("4")) || tipo.equals(new BigDecimal("5"));
    }

    public List<CuestionarioPreguntaRespuesta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<CuestionarioPreguntaRespuesta> preguntas) {
        this.preguntas = preguntas;
    }

    public List<ParametrosPDF> getRangosSalarios() {
        return rangosSalarios;
    }

    public void setRangosSalarios(List<ParametrosPDF> rangosSalarios) {
        this.rangosSalarios = rangosSalarios;
    }

    public List<CuestionarioRespuestas> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<CuestionarioRespuestas> respuestas) {
        this.respuestas = respuestas;
    }

    public List<CuestionarioRespuestasUsuario> getRespuestasUsuario() {
        return respuestasUsuario;
    }

    public void setRespuestasUsuario(List<CuestionarioRespuestasUsuario> respuestasUsuario) {
        this.respuestasUsuario = respuestasUsuario;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }

    public String getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(String cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
    }

    public String gethResultdos() {
        return hResultdos;
    }

    public void sethResultdos(String hResultdos) {
        this.hResultdos = hResultdos;
    }

    public Integer getTotalPreguntas() {
        return totalPreguntas;
    }

    public void setTotalPreguntas(Integer totalPreguntas) {
        this.totalPreguntas = totalPreguntas;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getPeriodoActual() {
        return periodoActual;
    }

    public void setPeriodoActual(String periodoActual) {
        this.periodoActual = periodoActual;
    }

    public List<ParametrosPDF> getEsePdfList1() {
        return esePdfList1;
    }

    public void setEsePdfList1(List<ParametrosPDF> esePdfList1) {
        this.esePdfList1 = esePdfList1;
    }

    public List<ParametrosPDF> getEsePdfList2() {
        return esePdfList2;
    }

    public void setEsePdfList2(List<ParametrosPDF> esePdfList2) {
        this.esePdfList2 = esePdfList2;
    }

    public List<ParametrosPDF> getEsePdfList3() {
        return esePdfList3;
    }

    public void setEsePdfList3(List<ParametrosPDF> esePdfList3) {
        this.esePdfList3 = esePdfList3;
    }

    public List<ParametrosPDF> getEsePdfList4() {
        return esePdfList4;
    }

    public void setEsePdfList4(List<ParametrosPDF> esePdfList4) {
        this.esePdfList4 = esePdfList4;
    }

    public List<ParametrosPDF> getEsePdfList5() {
        return esePdfList5;
    }

    public void setEsePdfList5(List<ParametrosPDF> esePdfList5) {
        this.esePdfList5 = esePdfList5;
    }

    public Date getHoy() {
        return hoy;
    }

    public void setHoy(Date hoy) {
        this.hoy = hoy;
    }

    public Boolean getContestoSalud() {
        return contestoSalud;
    }

    public void setContestoSalud(Boolean contestoSalud) {
        this.contestoSalud = contestoSalud;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }

    public Boolean getPeriodoActivo() {
        return periodoActivo;
    }

    public void setPeriodoActivo(Boolean periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public BigDecimal getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(BigDecimal alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Integer getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Integer transferencia) {
        this.transferencia = transferencia;
    }

    public Boolean getEseTransporte() {
        return eseTransporte;
    }

    public void setEseTransporte(Boolean eseTransporte) {
        this.eseTransporte = eseTransporte;
    }

    public String getIngresoPorPersona() {
        return ingresoPorPersona;
    }

    public void setIngresoPorPersona(String ingresoPorPersona) {
        this.ingresoPorPersona = ingresoPorPersona;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.exception.LoginException;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.CensoSalud;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.CuestionarioPreguntaRespuesta;
import com.becasipn.persistence.model.CuestionarioPreguntas;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.Usuario;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.PersistenceException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Sistemas DSE
 */
public class AdministraCuestionarioSaludAction extends BaseReportAction implements MensajesAlumno {

    public static final String CUESTIONARIO = "cuestionario";
    public static final String DESHABILITADO = "deshabilitado";
    List<CuestionarioPreguntaRespuesta> preguntas = new ArrayList<>();
    List<CuestionarioRespuestas> respuestas = new ArrayList<>();
    List<CuestionarioRespuestasUsuario> respuestasUsuario = new ArrayList<>();
    private String numeroDeBoleta;
    private String cuestionarioId;
    private String hResultdos;
    private Alumno alumno;
    private String folio;
    private String periodoActual;
    private Date hoy = new Date();
    private Boolean contestoSalud;
    private Boolean estatus;
    private Boolean periodoActivo = Boolean.FALSE;
    private BigDecimal alumnoId;
    private Integer transferencia = 1;
    Boolean eseTransporte = Boolean.FALSE;
    private String ingresoPorPersona;
    private Boolean soloLectura = false;
    private String reason;

    public String adminSalud() {
        return resultado();
    }

    public String cuestionario() {

        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();

        // ------------------ Obtención de datos del Alumno ------------------------------
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        if (alumno == null) {
            reason = "Boleta no encontrada";
            return DESHABILITADO;
        }

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);

        if (!periodoActivo) {
            reason = "El periodo para contestar el Censo de Salud ha finalizado";
            return DESHABILITADO;
        }
        // ------------------ Validación de Censo de Saludo previamente contestado ------------------------------
        if ((getDaos().getCensoSaludDao().contestoEncuestaSalud(alumno.getId(), periodo.getId()))) {
            reason = "Resumen de tu Censo de Salud";
            return resultado();
        }

        // ------------------ Validación de flag "ValidacionInscripcion" ------------------------------
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        //Se busca el otorgamiento del periodo anterior.
        Otorgamiento otorgamientoAnterior = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getPeriodoAnterior().getId());
        //Si el alumno tiene la validación de inscripción no le permitira constestar el estudio socioeconomico.
        Boolean validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodo, alumno.getId(), null);
        if (validacionInscripcion && otorgamientoAnterior != null
                && datosAcademicos.getUnidadAcademica().getId().equals(otorgamientoAnterior.getProceso().getUnidadAcademica().getId())) {
            reason = "Este periodo no es necesario contestar tu censo de salud, debido a que las características de tu beca así lo indican.";
            return DESHABILITADO;
        }

        // ------------------ Validación de ESE previamente contestado ------------------------------
        if (!(getDaos().getSolicitudBecaDao().tieneESECompleto(alumno.getId(), periodo.getId()))) {
            addActionError("Debes de contestar tu ESE antes de responder el Censo de Salud");
            return DESHABILITADO;
        }
        //Buscar cuestionario correspondiente a el Censo de Salud
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        cuestionarioId = "3";
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        if (c == null) {
            addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
        } else if (cuestionarioId == null || cuestionarioId.isEmpty()) {
            addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
        } else {
            BigDecimal cId = new BigDecimal(cuestionarioId);
            preguntas = getDaos().getCuestionarioPreguntaRespuestaDao().findByCuestionario(cId, datosAcademicos == null ? null : datosAcademicos.getUnidadAcademica().getNivel().getId());
            //rangosSalarios = getDaos().getTipoBecaPeriodoDao().rangoIngresoPorPersonaPorBeca(periodo.getId(), datosAcademicos == null ? null : datosAcademicos.getUnidadAcademica().getNivel().getId());
            respuestasUsuario = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(cId, u.getId(), periodo.getId());
            SolicitudBeca sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId());
            if (sb != null) {
                transferencia = sb.getPermiteTransferencia();
            }
        }

        return CUESTIONARIO;
    }

    public String finalizar() throws LoginException {
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();

        // ------------------ Obtención de datos del Alumno ------------------------------
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
        if (alumno == null) {
            reason = "Boleta no encontrada";
            return DESHABILITADO;
        }

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, periodo);

        if (!periodoActivo) {
            if (getDaos().getCensoSaludDao().contestoEncuestaSalud(alumnoId, periodo.getId())) {
                return resultado();
            } else {
                reason = "El periodo para contestar el Censo de Salud ha finalizado";
                return DESHABILITADO;
            }
        }
        CensoSalud cs = getDaos().getCensoSaludDao().getSaludAlumno(alumno.getId(), periodo.getId());
        if (cs == null) {
            cs = new CensoSalud();
        }

        //Alumno
        cs.setAlumno(alumno);
        //Cuestionario
        Cuestionario c = new Cuestionario(new BigDecimal(3));
        cs.setCuestionario(c);
        //Periodo
        cs.setPeriodo(periodo);
        //Fecha de Modificación
        cs.setFechaModificacion(new Date());

        if (hResultdos != null && !hResultdos.equals("")) {
            String[] resultados = null;
            String[] valores = null;
            hResultdos = hResultdos.replaceAll("buscador", "");
            resultados = hResultdos.split("@");
            if (resultados.length > 1) {
                Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
                if (usuario == null) {
                    return INPUT;
                }
                for (String pregunta : resultados) {
                    valores = pregunta.split(",");
                    if (valores.length > 1) {
                        //System.out.println(valores[0] + "|" + valores[1]);
                        String noPregunta = valores[0];
                        String valor = valores[1];
                        String[] multiple = valor.split("-");

                        for (String m : multiple) {
                            CuestionarioRespuestasUsuario ru = new CuestionarioRespuestasUsuario();
                            CuestionarioPreguntas p = getDaos().getCuestionarioPreguntasDao().findById(new BigDecimal(noPregunta));
                            ru.setPregunta(p);
                            BigDecimal idRespuesta;
                            CuestionarioRespuestas cr = new CuestionarioRespuestas();
                            try {
                                //Respuesta del catalogo de respuesta
                                idRespuesta = new BigDecimal(m);
                                if (p.getCuestionarioPreguntaTipo().getId().equals(new BigDecimal(3))) {
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
                                }
                            } catch (PersistenceException e) {
                                LOG.warn("Ya existe el ESE para el periodo activo y alumno_id" + alumno.getId() + " Exception: " + e.getMessage());
                            }
                        }

                    }
                }
                try {
                    if (cs.getId() == null) {
                        getDaos().getCensoSaludDao().save(cs);
                    } else {
                        getDaos().getCensoSaludDao().update(cs);
                    }

                } catch (PersistenceException e) {
                    LOG.warn("Ya existe el Censo de Salud para el perido activo y alumno_id: " + alumno.getId());
                }
            }

        }
        return resultado();
    }

    public String resultado() {
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        Usuario u = new Usuario();
        estatus = true;
        periodoActivo = Boolean.TRUE;

        // ------------------ Obtención de datos del Alumno ------------------------------
        if (alumnoId == null) {
            numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
            alumno = getDaos().getAlumnoDao().findByBoleta(numeroDeBoleta);
            u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        } else {
            alumno = getDaos().getAlumnoDao().findById(alumnoId);
            u = alumno.getUsuario();
        }

        if (alumno == null) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }

        // ------------------ Validación de flag "ValidacionInscripcion" ------------------------------
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        //Se busca el otorgamiento del periodo anterior.
        Otorgamiento otorgamientoAnterior = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getPeriodoAnterior().getId());
        //Si el alumno tiene la validación de inscripción no le permitira constestar el estudio socioeconomico.
        Boolean validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodo, alumno.getId(), null);
        if (validacionInscripcion && otorgamientoAnterior != null
                && datosAcademicos.getUnidadAcademica().getId().equals(otorgamientoAnterior.getProceso().getUnidadAcademica().getId())) {
            reason = "Este periodo no es necesario contestar tu censo de salud, debido a que las características de tu beca así lo indican.";
            return DESHABILITADO;
        }

        //Buscar cuestionario correspondiente a el Censo de Salud
        cuestionarioId = "3";
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        if (c == null) {
            addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
        } else if (cuestionarioId == null || cuestionarioId.isEmpty()) {
            addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
        } else {
            BigDecimal cId = new BigDecimal(cuestionarioId);
            preguntas = getDaos().getCuestionarioPreguntaRespuestaDao().findByCuestionario(cId, datosAcademicos == null ? null : datosAcademicos.getUnidadAcademica().getNivel().getId());
            respuestasUsuario = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(cId, u.getId(), periodo.getId());
        }

        return SUCCESS;
    }

    public List<CuestionarioPreguntaRespuesta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<CuestionarioPreguntaRespuesta> preguntas) {
        this.preguntas = preguntas;
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

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}

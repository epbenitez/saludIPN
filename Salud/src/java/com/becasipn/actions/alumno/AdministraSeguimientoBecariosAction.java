package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.exception.LoginException;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.CuestionarioPreguntaRespuesta;
import com.becasipn.persistence.model.CuestionarioPreguntas;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SeguimientoBecarios;
import com.becasipn.persistence.model.Usuario;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class AdministraSeguimientoBecariosAction extends BaseReportAction implements MensajesAlumno {

    public static final String CUESTIONARIO = "cuestionario";
    public static final String DESHABILITADO = "deshabilitado";
    List<CuestionarioPreguntaRespuesta> preguntas = new ArrayList<>();
    List<CuestionarioRespuestas> respuestas = new ArrayList<>();
    List<CuestionarioRespuestasUsuario> respuestasUsuario = new ArrayList<>();
    private String numeroDeBoleta;
    private String cuestionarioId;
    private String hResultdos;
    private Integer totalPreguntas;
    private Alumno alumno;
    private String periodoActual;
    private Date hoy = new Date();
    private Boolean estatus;
    private Boolean otorgamientoHistorico;
    private Boolean nivelSuperior;
    private Boolean periodoActivo = Boolean.FALSE;
    private BigDecimal alumnoId;
    private String reason;

    public String adminSeguimiento() {
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
        //periodoActivo = abo.solicitudActiva(alumno, periodo);
        periodoActivo = Boolean.TRUE;

        //estatus = true;
        //periodoActivo = Boolean.TRUE;
        // ------------------ Validación de Periodo Activo ------------------------------
        String fInicialStr = (String) ActionContext.getContext().getApplication().get("FechaRegistroInicial");
        String fFinalStr = (String) ActionContext.getContext().getApplication().get("FechaRegistroFinal");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Inicialmente se valida si el registro esta aún activo.
//        try {
//            //Obtiene la fecha Final e Inicial del periodo y la fecha actual.lo compara con la fecha de hoy para establecer si el periodo esta activo o no
//            Date dateInicial = formatter.parse(fInicialStr);
//            Date dateFinal = formatter.parse(fFinalStr);
//            Calendar c = Calendar.getInstance();
//            c.setTime(dateFinal);
//            c.add(Calendar.DATE, 1);
//            dateFinal = c.getTime();
//            Date hoy = new Date();
//            //Compara la fecha Final e Inicial con la fecha de hoy para establecer si el periodo esta activo o no
//            if (hoy.after(dateInicial) && hoy.before(dateFinal)) {
//                periodoActivo = Boolean.TRUE;
//            } else {
//                reason = "El periodo para contestar este cuestionario ha finalizado";
//                return DESHABILITADO;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        //------------------ Validación de Seguimiento previamente contestado ------------------------------
        if ((getDaos().getSeguimientoBecariosDao().contestoSeguimientoBecarios(alumno.getId(), periodo.getId()))) {
            reason = "Resumen de la encuesta de Seguimiento a Becarios";
            return resultado();
        }

        // ------------------ Validación de Otorgamiento Histórico ------------------------------
        otorgamientoHistorico = getDaos().getOtorgamientoDao().tieneOtorgamientoHistorico(alumno.getId());
        System.out.println("Flag OH: " + otorgamientoHistorico);
        if (!otorgamientoHistorico) {
            reason = "Esta encuesta solo está disponible para alumnos que han sido beneficiados con una beca.";
            return DESHABILITADO;
        }

        // ------------------ Validación de flag "ValidacionInscripcion" ------------------------------
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        if (datosAcademicos == null) {
            reason = "No puedes responder este cuestionario debido a que tus datos académicos no han sido actualizados para el periodo actual.";
            return DESHABILITADO;
        }
        //Se busca el otorgamiento del periodo anterior.
        Otorgamiento otorgamientoAnterior = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getPeriodoAnterior().getId());
        //Si el alumno tiene la validación de inscripción no le permitira constestar el estudio socioeconomico.
        Boolean validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(periodo, alumno.getId(), null);
        if (validacionInscripcion && otorgamientoAnterior != null
                && datosAcademicos.getUnidadAcademica().getId().equals(otorgamientoAnterior.getProceso().getUnidadAcademica().getId())) {
            reason = "Este periodo no es necesario contestar el seguimiento de becarios debido a que las características de tu beca así lo indican.";
            return DESHABILITADO;
        }

        // ------------------ Validación de ESE previamente contestado ------------------------------
//        if (!(getDaos().getSolicitudBecaDao().tieneESECompleto(alumno.getId(), periodo.getId()))) {
//            addActionError("Debes de contestar tu ESE antes de responder el Censo de Salud");
//            return DESHABILITADO;
//        }
        //Buscar cuestionario correspondiente a el Censo de Salud
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        //System.out.println(u.getId());
        cuestionarioId = "6";
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        if (c == null) {
            addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
        } else if (cuestionarioId == null || cuestionarioId.isEmpty()) {
            addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
        } else {
            BigDecimal cId = new BigDecimal(cuestionarioId);
            preguntas = getDaos().getCuestionarioPreguntaRespuestaDao().findByCuestionario(cId, datosAcademicos == null ? null : datosAcademicos.getUnidadAcademica().getNivel().getId());
            //rangosSalarios = getDaos().getTipoBecaPeriodoDao().rangoIngresoPorPersonaPorBeca(periodo.getId(), datosAcademicos == null ? null : datosAcademicos.getUnidadAcademica().getNivel().getId());
            totalPreguntas = getDaos().getCuestionarioPreguntaRespuestaDao().totalPreguntas(c.getId());
            respuestasUsuario = getDaos().getCuestionarioRespuestasUsuarioDao().getResultadosUsuario(cId, u.getId(), periodo.getId());
        }

        if (datosAcademicos.getUnidadAcademica().getNivel().getId().compareTo(new BigDecimal(2)) == 0) {
            nivelSuperior = Boolean.TRUE;
        } else {
            nivelSuperior = Boolean.FALSE;
        }

        return CUESTIONARIO;
    }

    public String finalizar() throws LoginException {
        String fInicialStr = (String) ActionContext.getContext().getApplication().get("FechaRegistroInicial");
        String fFinalStr = (String) ActionContext.getContext().getApplication().get("FechaRegistroFinal");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Inicialmente se valida si el registro esta aún activo.
        try {
            //Obtiene la fecha Final e Inicial del periodo y la fecha actual.lo compara con la fecha de hoy para establecer si el periodo esta activo o no
            Date dateInicial = formatter.parse(fInicialStr);
            Date dateFinal = formatter.parse(fFinalStr);
            Calendar c = Calendar.getInstance();
            c.setTime(dateFinal);
            c.add(Calendar.DATE, 1);
            dateFinal = c.getTime();
            Date hoy = new Date();
            //Compara la fecha Final e Inicial con la fecha de hoy para establecer si el periodo esta activo o no
//            if (hoy.after(dateInicial) && hoy.before(dateFinal)) {
//                periodoActivo = Boolean.TRUE;
//            }
            periodoActivo = Boolean.TRUE;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (usuario == null) {
            return INPUT;
        }
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Alumno> listAlumno = getDaos().getAlumnoDao().findBy("boleta", numeroDeBoleta, Boolean.TRUE);
        if (listAlumno == null || listAlumno.isEmpty()) {
            addActionError(getText("registro.boleta.no.encontrada"));
            return ERROR;
        }
        alumno = listAlumno.get(0);
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();

        //TODO: Augusto, tengo duda en ésta sección del código.
        //      Según yo, aquí no debe haber lógica de negocio relacionada al censo de salud
        //      ¿Sera que se quedó debido a un copy/paste?
        if (!periodoActivo) {
            if (getDaos().getCensoSaludDao().contestoEncuestaSalud(alumnoId, periodo.getId())) {
                return resultado();
            } else {
                reason = "El periodo para contestar el Censo de Salud ha finalizado";
                return DESHABILITADO;
            }
        }

        SeguimientoBecarios sb = getDaos().getSeguimientoBecariosDao().getSeguimientoAlumno(alumno.getId(), periodo.getId());
        if (sb == null) {
            sb = new SeguimientoBecarios();
        }

        //Alumno
        sb.setAlumno(alumno);
        //Cuestionario
        Cuestionario c = new Cuestionario(new BigDecimal(6));
        //Periodo
        sb.setPeriodo(periodo);
        //Fecha de Modificación
        sb.setFechaModificacion(new Date());
        //Total de preguntas
        totalPreguntas = getDaos().getCuestionarioPreguntaRespuestaDao().totalPreguntas(c.getId());

        if (hResultdos != null && !hResultdos.equals("")) {
            String[] resultados = null;
            String[] valores = null;
            hResultdos = hResultdos.replaceAll("buscador", "");
            resultados = hResultdos.split("@");
            if (resultados.length > 1) {
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
                    if (sb.getId() == null) {
                        getDaos().getSeguimientoBecariosDao().save(sb);
                    } else {
                        getDaos().getSeguimientoBecariosDao().update(sb);
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
        cuestionarioId = "6";
        Cuestionario c = getDaos().getCuestionarioDao().findById(new BigDecimal(cuestionarioId));
        if (c == null) {
            addActionError(getText("misdatos.alumno.no.existe.cuesionario"));
        } else if (cuestionarioId == null || cuestionarioId.isEmpty()) {
            addActionError(getText("misdatos.alumno.no.especifico.cuestionario"));
        } else {
            BigDecimal cId = new BigDecimal(cuestionarioId);
            preguntas = getDaos().getCuestionarioPreguntaRespuestaDao().findByCuestionario(cId, datosAcademicos == null ? null : datosAcademicos.getUnidadAcademica().getNivel().getId());
            //rangosSalarios = getDaos().getTipoBecaPeriodoDao().rangoIngresoPorPersonaPorBeca(periodo.getId(), datosAcademicos == null ? null : datosAcademicos.getUnidadAcademica().getNivel().getId());
            totalPreguntas = getDaos().getCuestionarioPreguntaRespuestaDao().totalPreguntas(c.getId());
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

    public Boolean getOtorgamientoHistorico() {
        return otorgamientoHistorico;
    }

    public void setOtorgamientoHistorico(Boolean otorgamientoHistorico) {
        this.otorgamientoHistorico = otorgamientoHistorico;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getNivelSuperior() {
        return nivelSuperior;
    }

    public void setNivelSuperior(Boolean nivelSuperior) {
        this.nivelSuperior = nivelSuperior;
    }

}

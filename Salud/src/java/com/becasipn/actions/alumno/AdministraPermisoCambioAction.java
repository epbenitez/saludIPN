package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.CuestionarioPreguntasRespuestasBO;
import com.becasipn.exception.ErrorDaeException;
import com.becasipn.persistence.model.TipoPermisoCambio;
import com.becasipn.persistence.model.BitacoraPermisoCambio;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoDAE;
import com.becasipn.persistence.model.AlumnoDatosBancarios;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.Modalidad;
import com.becasipn.persistence.model.BitacoraAlumno;
import com.becasipn.persistence.model.Carrera;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Documentos;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.ErrorDAE;
import com.becasipn.util.TuplaValidacion;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
public class AdministraPermisoCambioAction extends BaseAction implements MensajesAlumno {

    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";
    public static final String VALIDACION = "validacion";
    public static final String DATOS = "datosDAE";
    public static final String BANCARIOS = "datosBancarios";

    private final AlumnoBO alumnoBO;
    private final Usuario usuario;
    private List<CuestionarioRespuestas> respuestas;

    private Alumno alumno = new Alumno();
    private AlumnoDatosBancarios alumnoDatosBanco = new AlumnoDatosBancarios();
    private AlumnoDAE a = new AlumnoDAE();
    private Documentos documentos = new Documentos();

    private String numeroDeBoleta;
    private String correoNuevo;
    private String correoNuevoC;
    private String correoAnterior;
    private String promedioA;
    private Boolean permisoCambio;
    private Boolean eseF = Boolean.TRUE;
    private Boolean menorEdad = Boolean.FALSE;
    private Boolean validacionInscripcion = Boolean.FALSE;
    private Boolean datosActualizados = Boolean.FALSE;
    private Boolean valDocs = Boolean.TRUE;
    private Boolean otorgamientoActual = Boolean.FALSE;
    private BigDecimal salariosId;
    private BigDecimal salariosA;
    private BigDecimal salariosN;
    private BigDecimal ua;
    private String pe;
    private String crr;
    private String es;
    private String periodoDatos = " - ";
    private String password;

    private String unidadAcademica;
    private String planEstudios;
    private String carrera;
    private String especialidad;
    private String inscripcion;
    private String regularidad;
    private String semestre;
    private String promedio;
    private String materiasReprobadas;
    private String egresado;
    private String egresadoDAE;
    private String modalidad;
    private String carga;
    private String cargaAl;
    private String estatusSolicitud;
    private String observSolicitud;
    private int stsId;
    private boolean clasificacionSolicitudBeca;
    private String checkboxEgresado;
    private String checkboxRegular;
    private String checkboxInscripcion;

    public AdministraPermisoCambioAction() {
        alumnoBO = new AlumnoBO(getDaos());
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
    }

    public String lista() {
        return SUCCESS;
    }

    public String ver() {
        correoAnterior = alumnoBO.getAlumno(numeroDeBoleta).getCorreoElectronico();
        return FORMULARIO;
    }

    public String cambio() {
        if (correoNuevo.equals(correoNuevoC)) {
            Alumno a = alumnoBO.getAlumno(numeroDeBoleta);
            a.setCorreoElectronico(correoNuevo);
            Boolean res = alumnoBO.guardaAlumno(a);

            if (res) {
                TipoPermisoCambio t = getDaos().getTipoPermisoCambioDao().findById(new BigDecimal(1));
                BitacoraPermisoCambio b = new BitacoraPermisoCambio();
                b.setAlumno(a);
                b.setCambioCorreoElectronico(correoAnterior);
                b.setTipoPermisoCambio(t);
                b.setUsuariomodifico(usuario);
                getDaos().getBitacoraPermisoCambioDao().save(b);
                addActionMessage(getText("misdatos.alumno.correo.exito"));
            } else {
                addActionError(getText("registro.guarda.sin.datos"));
            }
        } else {
            addActionError(getText("misdatos.alumno.correo.error"));
        }
        return FORMULARIO;
    }

    public boolean datos() throws ErrorDaeException {
        alumno = alumnoBO.getAlumno(numeroDeBoleta);
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
        alumno.setDatosAcademicos(datosAcademicos);
        if (datosAcademicos == null) {
            datosAcademicos = new DatosAcademicos();
        }
        pe = datosAcademicos.getInscrito() == null ? "-" : datosAcademicos.getInscrito() == 1 ? "si" : "no";
        crr = datosAcademicos.getRegular() == null ? "-" : datosAcademicos.getRegular() == 1 ? "si" : "no";
        setEgresado(datosAcademicos.getEgresado());

        TuplaValidacion tuplaValidacion = alumnoBO.validarAlumno(numeroDeBoleta, true);
        if (tuplaValidacion.getErrorDAE() != null && !tuplaValidacion.getErrorDAE().equals(ErrorDAE.NO_INSCRITO)) {
            unidadAcademica = "-";
            planEstudios = "-";
            carrera = "-";
            especialidad = "-";
            inscripcion = "-";
            regularidad = "-";
            semestre = "-";
            promedio = "-";
            modalidad = "-";
            materiasReprobadas = "-";
            setEgresadoDAE(null);
            return false;
        }
        a = tuplaValidacion.getAlumnoDAE();

        unidadAcademica = getDaos().getUnidadAcademicaDao().getByClave((String.valueOf(Integer.parseInt(a.getEscuela())))).getNombreCorto();
        alumnoBO.validarCarrera(a);
        planEstudios = a.getCarreraObj().getPlanEstudios();
        carrera = a.getCarreraObj().getCarrera();
        especialidad = a.getCarreraObj().getEspecialidad();
        inscripcion = a.getInscrito() == 1 ? "Sí" : "No";
        regularidad = (a.getTipo_alumno()).equals("R") ? "Sí" : "No";        
        materiasReprobadas = String.valueOf(a.getReprobadas());
        setEgresadoDAE(a.getEgresado());
        semestre = a.getSemestre_inscrito().toString();
        promedio = a.getPromedio().toString();
        

        Modalidad m = getDaos().getModalidadDao().findById(AlumnoBO.getModalidadId(a.getModo_ingreso()));

        modalidad = m.getNombre();

        carga = cargaString(a.getCumple_carga_minima());
        cargaAl = cargaString(datosAcademicos.getCumpleCargaMinima());
        return true;
    }

    private String cargaString(Integer c) {
        if (c != null) {
            switch (c) {
                case 0:
                    return "Debajo de la minima";
                case 1:
                    return "Minima";
                case 2:
                    return "Media";
                case 3:
                    return "Maxima";
                default:
                    return "-";
            }
        } else {
            return "-";
        }
    }

    public String datosDAE() throws ErrorDaeException {
        datos();
        return DATOS;
    }
    // Aquí

    public String datosBancarios() throws ErrorDaeException {
        alumno = alumnoBO.getAlumno(numeroDeBoleta);
        alumnoDatosBanco = getDaos().getAlumnoDatosBancariosDao().datosBancarios(alumno.getId());
        AlumnoBO bo = new AlumnoBO(getDaos());
        if (alumnoDatosBanco.getNombre() != null) {
            menorEdad = Boolean.TRUE;
        }
        AlumnoTarjetaBancaria atb = getDaos().getAlumnoTarjetaDao().tarjetaDatosBancarios(alumnoDatosBanco.getId());
        boolean hasAtb = atb != null;
        EstatusTarjetaBancaria statusCC = hasAtb ? atb.getEstatusTarjBanc() : null;
        boolean hasSts = statusCC != null;
        stsId = hasSts ? statusCC.getId().intValue() : -1;

        if (stsId >= 12) {
            if (stsId == 13) {
                estatusSolicitud = statusCC.getNombre();
                observSolicitud = "La cuenta fue liberada";
            } else {
                estatusSolicitud = statusCC.getDescripcion();
                String msj = "Mensaje para alumno: ";
                observSolicitud = msj + statusCC.getObservaciones();
            }
        } else {
            estatusSolicitud = "Datos Capturados";
            observSolicitud = "El alumno capturó sus datos bancarios, pero aún no se ha solicitado una cuenta bancaria";
        }
        return BANCARIOS;
    }

    public String guardaDAE() throws ErrorDaeException {
        Alumno almn = alumnoBO.getAlumno(numeroDeBoleta);
        AlumnoBO abo = new AlumnoBO(getDaos());
        Boolean periodoActivo = abo.solicitudActiva(almn, null);
        String validarDatosDAE = (String) ActionContext.getContext().getApplication().get("ValidarDatosDae");
        if (validarDatosDAE.equals("true")) {
            Boolean tieneOtorgamientoPeriodoActual = getDaos().getOtorgamientoDao().tieneOtorgamientoPeriodoActual(almn.getId());
            if (!tieneOtorgamientoPeriodoActual) {
                try {
                    almn = alumnoBO.datosDAE(almn);
                    TuplaValidacion tuplaValidacion = alumnoBO.validarAlumno(numeroDeBoleta, true);
                    a = tuplaValidacion.getAlumnoDAE();
                    DatosAcademicos da = getDaos().getDatosAcademicosDao().ultimosDatos(almn.getId());
                    if (da.getInfoActualizadaAdmin() == 1 && alumnoBO.existenCambios(da, a)) {
                        addActionError("Los datos académicos no se han podido actualizar, debido a que ya han sido validados previamente");
                    } else {
                        addActionMessage(getText("datosDAE.exito"));
                    }
                } catch (ErrorDaeException ex) {
                    Logger.getLogger(AdministraPermisoCambioAction.class.getName()).log(Level.SEVERE, null, ex);
                    addActionError(getText("datosDAE.error"));
                }
            } else {
                addActionError("El alumno ya tiene un otorgamiento para el periodo en curso, por lo cual la información no puede ser modificada");
            }
        } else {
            addActionError("No se pueden validar los datos con la DAE");//getText("datosDAE.error.periodo"));
        }
        datos();
        return DATOS;
    }

    public String banderaESE() {
        Alumno a = alumnoBO.getAlumno(numeroDeBoleta);
        Periodo p = getDaos().getPeriodoDao().getPeriodoActivo();
        SolicitudBeca cpa = getDaos().getSolicitudBecaDao().getESEAlumno(a.getId(), p.getId());

        try {
            getDaos().getSolicitudBecaDao().delete(cpa);
        } catch (Exception e) {
            addActionError(getText("misdatos.alumno.estudio.socioeconomico.eliminado.error"));
        }

        TipoPermisoCambio t = getDaos().getTipoPermisoCambioDao().findById(new BigDecimal(2));
        BitacoraPermisoCambio b = new BitacoraPermisoCambio();
        b.setAlumno(a);
        b.setTipoPermisoCambio(t);
        b.setUsuariomodifico(usuario);
        getDaos().getBitacoraPermisoCambioDao().save(b);
        addActionMessage(getText("misdatos.alumno.estudio.socioeconomico.eliminado.exito"));
        return LISTA;
    }

    public String validacion() {
        setIframe();
        return SUCCESS;
    }

    public String editar() {
        if (alumno == null || alumno.getId() == null) {
            addActionError(getText("becas.actualizado.error"));
            setIframe();
            return VALIDACION;
        }
        Periodo periodo = getDaos().getPeriodoDao().getPeriodoActivo();
        
        int alumnoInscrito = 0;
        if (alumno.getDatosAcademicos().getInscrito() != null) {
            alumnoInscrito = alumno.getDatosAcademicos().getInscrito();
        }
        int alumnoRegular = 0;
        if (alumno.getDatosAcademicos().getRegular() != null) {
            alumnoRegular = alumno.getDatosAcademicos().getRegular();
        }
        int alumnoEgresado = 0;
        if (alumno.getDatosAcademicos().getEgresado() != null) {
            alumnoEgresado = alumno.getDatosAcademicos().getEgresado();
        }
        int materiasReprobadas = 0;
        if (alumno.getDatosAcademicos().getReprobadas() > 0) {
            materiasReprobadas = alumno.getDatosAcademicos().getReprobadas();
        }
        
        Integer sem = alumno.getDatosAcademicos().getSemestre();
        Double promedioFront = new Double(promedioA);
        Integer cumpleCargaMinima = alumno.getDatosAcademicos().getCumpleCargaMinima();
        Modalidad mod = alumno.getDatosAcademicos().getModalidad();

        if (promedioFront < 0 || promedioFront > 10) {
            addActionError(getText("becas.error.promedio"));
            setIframe();
            return VALIDACION;
        }

        alumno = getDaos().getAlumnoDao().findById(alumno.getId());
        // Si hay datos del periodo actual, se editan esos, si no, se crean unos nuevos
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        if (!getDaos().getOtorgamientoDao().tieneMovimientoPeriodoActual(alumno.getId())) {
            if (datosAcademicos != null) {
                if (datosAcademicos.getCarrera() != null) {
                    if (sem < 0 || sem > datosAcademicos.getCarrera().getNumeroSemestres()) {
                        addActionError(getText("becas.error.semestre"));
                        setIframe();
                        return VALIDACION;
                    }
                } else {
                    addActionError(getText("becas.error.carrera"));
                    setIframe();
                    return VALIDACION;
                }

                datosAcademicos.setInscrito(alumnoInscrito);
                datosAcademicos.setRegular(alumnoRegular);
                datosAcademicos.setSemestre(sem);
                datosAcademicos.setPromedio(promedioFront);
                datosAcademicos.setCumpleCargaMinima(cumpleCargaMinima);
                datosAcademicos.setModalidad(mod);
                datosAcademicos.setInfoActualizadaAdmin(1);
                datosAcademicos.setFechaModificacion(new Date());
                datosAcademicos.setReprobadas(materiasReprobadas);
                datosAcademicos.setEgresado(alumnoEgresado);
                getDaos().getDatosAcademicosDao().update(datosAcademicos);
            } else {
                // Si existe datos academicos para el alumno, se usan como base
                // y se actualizan los datos correspondientes
                datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
                if (datosAcademicos != null) {
                    if (datosAcademicos.getCarrera() != null) {
                        if (sem < 0 || sem > datosAcademicos.getCarrera().getNumeroSemestres()) {
                            addActionError(getText("becas.error.semestre"));
                            setIframe();
                            return VALIDACION;
                        }
                    } else {
                        addActionError(getText("becas.error.carrera"));
                        setIframe();
                        return VALIDACION;
                    }

                    DatosAcademicos nuevosDatosAcademicos = new DatosAcademicos(alumno,
                            periodo, promedioFront, sem, datosAcademicos.getTotalCreditos(),
                            datosAcademicos.getUnidadAcademica(), datosAcademicos.getCarrera(),
                            alumnoRegular, alumnoInscrito, datosAcademicos.getModalidadDAE(),
                            mod, datosAcademicos.getTurno(), materiasReprobadas, alumnoEgresado,
                            cumpleCargaMinima, 1, new Date()
                    );
                    alumno.addDatosAcademicos(nuevosDatosAcademicos);
                } else {
                    addActionError(getText("becas.error.carrera"));
                    setIframe();
                    return VALIDACION;
                }
            }
            if (alumnoBO.guardaAlumno(alumno)) {
                BitacoraAlumno bitacora = new BitacoraAlumno(usuario, new Date(), alumno, getDaos().getPeriodoDao().getPeriodoActivo(), datosAcademicos.getSemestre(), datosAcademicos.getPromedio(), datosAcademicos.getInscrito(), datosAcademicos.getRegular());
                getDaos().getBitacoraAlumnoDao().save(bitacora);
                addActionMessage(getText("becas.actualizado.exito"));
            } else {
                addActionError(getText("becas.actualizado.error"));
            }
        } else {
            addActionError(getText("becas.error.asignacion.tiene.otorgamiento"));
        }
        setIframe();
        return VALIDACION;
    }

    public String salarios() {
        if (alumno == null || alumno.getId() == null) {
            addActionError(getText("becas.actualizado.error"));
            setIframe();
            return VALIDACION;
        }
        alumno = getDaos().getAlumnoDao().findById(alumno.getId());

        if (salariosId == null || salariosN == null) {
            addActionError(getText("becas.actualizado.error"));
            setIframe();
            return VALIDACION;
        }

        CuestionarioRespuestasUsuario cru = getDaos().getCuestionarioRespuestasUsuarioDao().findById(salariosId);
        cru.setRespuesta(getDaos().getCuestionarioRespuestasDao().findById(salariosN));

        CuestionarioPreguntasRespuestasBO bo = new CuestionarioPreguntasRespuestasBO(getDaos());

        if (!getDaos().getOtorgamientoDao().tieneMovimientoPeriodoActual(alumno.getId())) {
            if (bo.guardaCRU(cru)) {
                addActionMessage(getText("becas.actualizado.exito"));
            } else {
                addActionError(getText("becas.actualizado.error"));
            }
        } else {
            addActionError(getText("becas.error.asignacion.tiene.otorgamiento"));
        }
        setIframe();
        return VALIDACION;
    }

    public String unidadCarrera() {
        if (alumno == null || alumno.getId() == null) {
            addActionError(getText("becas.actualizado.error"));
            setIframe();
            return VALIDACION;
        }
        Periodo periodo = getDaos().getPeriodoDao().getPeriodoActivo();
        ua = alumno.getDatosAcademicos().getUnidadAcademica().getId();
        pe = alumno.getDatosAcademicos().getCarrera().getPlanEstudios();
        crr = alumno.getDatosAcademicos().getCarrera().getClaveCarrera();
        es = alumno.getDatosAcademicos().getCarrera().getEspecialidad();
        
        Carrera carreraNueva = getDaos().getCarreraDao().getCarrera(pe, crr, es, ua);
        if (carreraNueva == null) {
            addActionError(getText("becas.actualizado.error.carrera"));
            setIframe();
            return VALIDACION;
        }

        alumno = getDaos().getAlumnoDao().findById(alumno.getId());
        // Si hay datos del periodo actual, se editan esos, si no, se crean unos nuevos
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
        if (!getDaos().getOtorgamientoDao().tieneMovimientoPeriodoActual(alumno.getId())) {
            if (datosAcademicos != null) {
                datosAcademicos.setUnidadAcademica(getDaos().getUnidadAcademicaDao().findById(ua));
                datosAcademicos.setCarrera(carreraNueva);
                datosAcademicos.setInfoActualizadaAdmin(1);
                datosAcademicos.setFechaModificacion(new Date());

                getDaos().getDatosAcademicosDao().update(datosAcademicos);
            } else {
                // Si existe datos academicos para el alumno, se usan como base
                // y se actualizan los datos correspondientes
                datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
                DatosAcademicos nuevosDatosAcademicos = new DatosAcademicos();
                nuevosDatosAcademicos.setInfoActualizadaAdmin(1);
                if (datosAcademicos != null) {
                    nuevosDatosAcademicos = new DatosAcademicos(alumno,
                            periodo, datosAcademicos.getPromedio(), datosAcademicos.getSemestre(),
                            datosAcademicos.getTotalCreditos(), getDaos().getUnidadAcademicaDao().findById(ua),
                            carreraNueva,
                            datosAcademicos.getRegular(), datosAcademicos.getInscrito(),
                            datosAcademicos.getModalidadDAE(), datosAcademicos.getModalidad(),
                            datosAcademicos.getTurno(), datosAcademicos.getReprobadas(), datosAcademicos.getEgresado(),
                            datosAcademicos.getCumpleCargaMinima(), 1, new Date()
                    );
                } else {
                    nuevosDatosAcademicos.setUnidadAcademica(getDaos().getUnidadAcademicaDao().findById(ua));
                    nuevosDatosAcademicos.setCarrera(carreraNueva);
                    nuevosDatosAcademicos.setPeriodo(periodo);
                    nuevosDatosAcademicos.setFechaModificacion(new Date());
                }
                alumno.addDatosAcademicos(nuevosDatosAcademicos);
            }
            if (alumnoBO.guardaAlumno(alumno)) {
                addActionMessage(getText("becas.actualizado.exito"));
            } else {
                addActionError(getText("becas.actualizado.error"));
            }
        } else {
            addActionError(getText("becas.error.asignacion.tiene.otorgamiento"));
        }
        setIframe();
        return VALIDACION;
    }

    public void setIframe() {
        alumno = alumnoBO.getAlumno(alumno.getBoleta());
        promedioA = "0";
        ua = BigDecimal.ZERO;
        pe = "";
        crr = "";
        es = "";
        semestre = "";
        password = "";

        if (alumno == null || alumno.getBoleta() == null) {
            addActionError(getText("becas.error.visualizar"));
        }
        BigDecimal periodoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodoId);
        if (datosAcademicos != null) {
            datosActualizados = Boolean.TRUE;
        } else {
            datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
        }

        if (datosAcademicos == null) {
            datosAcademicos = new DatosAcademicos();
            datosAcademicos.setPromedio(new Double(0));
            datosAcademicos.setUnidadAcademica(new UnidadAcademica());
            datosAcademicos.setCarrera(new Carrera());
        } else if (datosAcademicos.getPeriodo() != null) {
            periodoDatos = datosAcademicos.getPeriodo().getClave();
        }
        alumno.setDatosAcademicos(datosAcademicos);
        promedioA = datosAcademicos.getPromedio().toString();
        password = alumno.getUsuario().getPassword();

        documentos = getDaos().getDocumentosDao().documentosAlumnoPeriodoActual(alumno);
        validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(getDaos().getPeriodoDao().getPeriodoActivo(), alumno.getId(), null);
        List<SolicitudBeca> solicitudesAlumno = getDaos().getSolicitudBecaDao().getSolicitudesPorAlumno(alumno.getId(), periodoId);
        if (solicitudesAlumno != null && !solicitudesAlumno.isEmpty()) {
            for (SolicitudBeca solicitud : solicitudesAlumno) {
                if (solicitud.getClasificacionSolicitud() != null) {
                    clasificacionSolicitudBeca = true;
                }
            }
        }

        if (documentos == null) {
            if (validacionInscripcion) {
                documentos = getDaos().getDocumentosDao().documentosAlumnoPeriodo(alumno, getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());
                if (documentos == null) {
                    valDocs = false;
                }
            } else {
                documentos = new Documentos(getDaos().getPeriodoDao().getPeriodoActivo(), alumno);
            }
        }

        Boolean o = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("1"), getDaos().getPeriodoDao().getPeriodoActivo().getId());
        CuestionarioRespuestasUsuario cru = getDaos().getCuestionarioRespuestasUsuarioDao().getPreguntaUsuarioActual(alumno.getUsuario().getId(), new BigDecimal(13));
        if (o || cru != null) {
            eseF = true;
            salariosId = cru.getId();
            salariosA = cru.getRespuesta().getId();
            salariosN = salariosA;
        } else if (validacionInscripcion) {
            o = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("1"), getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());
            cru = getDaos().getCuestionarioRespuestasUsuarioDao().respuestaPregunta(alumno.getUsuario().getId(), new BigDecimal(13), new BigDecimal("1"), getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());
            if (o || cru != null) {
                eseF = true;
                salariosId = cru.getId();
                salariosA = cru.getRespuesta().getId();
                salariosN = salariosA;
            } else {
                eseF = false;
            }
        } else {
            eseF = false;
        }
        otorgamientoActual = getDaos().getOtorgamientoDao().tieneMovimientoPeriodoActual(alumno.getId());
    }

    public String getCorreoNuevo() {
        return correoNuevo;
    }

    public void setCorreoNuevo(String correoNuevo) {
        this.correoNuevo = correoNuevo;
    }

    public String getCorreoNuevoC() {
        return correoNuevoC;
    }

    public void setCorreoNuevoC(String correoNuevoC) {
        this.correoNuevoC = correoNuevoC;
    }

    public String getCorreoAnterior() {
        return correoAnterior;
    }

    public void setCorreoAnterior(String correoAnterior) {
        this.correoAnterior = correoAnterior;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Documentos getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Documentos documentos) {
        this.documentos = documentos;
    }

    public List<CuestionarioRespuestas> getRespuestas() {
        respuestas = getDaos().getCuestionarioRespuestasDao().respuestasPregunta(new BigDecimal(13));
        return respuestas;
    }

    public BigDecimal getSalariosId() {
        return salariosId;
    }

    public void setSalariosId(BigDecimal salariosId) {
        this.salariosId = salariosId;
    }

    public BigDecimal getSalariosA() {
        return salariosA;
    }

    public void setSalariosA(BigDecimal salariosA) {
        this.salariosA = salariosA;
    }

    public BigDecimal getSalariosN() {
        return salariosN;
    }

    public void setSalariosN(BigDecimal salariosN) {
        this.salariosN = salariosN;
    }

    public BigDecimal getUa() {
        return ua;
    }

    public void setUa(BigDecimal ua) {
        this.ua = ua;
    }

    public String getPe() {
        return pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public String getCrr() {
        return crr;
    }

    public void setCrr(String crr) {
        this.crr = crr;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getPromedioA() {
        return promedioA;
    }

    public void setPromedioA(String promedioA) {
        this.promedioA = promedioA;
    }

    public AlumnoDAE getA() {
        return a;
    }

    public void setA(AlumnoDAE a) {
        this.a = a;
    }

    public String getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(String unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public String getPlanEstudios() {
        return planEstudios;
    }

    public void setPlanEstudios(String planEstudios) {
        this.planEstudios = planEstudios;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(String inscripcion) {
        this.inscripcion = inscripcion;
    }

    public String getRegularidad() {
        return regularidad;
    }

    public void setRegularidad(String regularidad) {
        this.regularidad = regularidad;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public String getCargaAl() {
        return cargaAl;
    }

    public void setCargaAl(String cargaAl) {
        this.cargaAl = cargaAl;
    }

    public AlumnoDatosBancarios getAlumnoDatosBanco() {
        return alumnoDatosBanco;
    }

    public void setAlumnoDatosBanco(AlumnoDatosBancarios alumnoDatosBanco) {
        this.alumnoDatosBanco = alumnoDatosBanco;
    }

    public Boolean getPermisoCambio() {
        return permisoCambio;
    }

    public void setPermisoCambio(Boolean permisoCambio) {
        this.permisoCambio = permisoCambio;
    }

    public Boolean getEseF() {
        return eseF;
    }

    public void setEseF(Boolean eseF) {
        this.eseF = eseF;
    }

    public Boolean getValidacionInscripcion() {
        return validacionInscripcion;
    }

    public void setValidacionInscripcion(Boolean validacionInscripcion) {
        this.validacionInscripcion = validacionInscripcion;
    }

    public Boolean getValDocs() {
        return valDocs;
    }

    public void setValDocs(Boolean valDocs) {
        this.valDocs = valDocs;
    }

    public Boolean getMenorEdad() {
        return menorEdad;
    }

    public void setMenorEdad(Boolean menorEdad) {
        this.menorEdad = menorEdad;
    }

    public String getEstatusSolicitud() {
        return estatusSolicitud;
    }

    public void setEstatusSolicitud(String estatusSolicitud) {
        this.estatusSolicitud = estatusSolicitud;
    }

    public String getObservSolicitud() {
        return observSolicitud;
    }

    public void setObservSolicitud(String observSolicitud) {
        this.observSolicitud = observSolicitud;
    }

    public int getStsId() {
        return stsId;
    }

    public void setStsId(int stsId) {
        this.stsId = stsId;
    }

    public String getPromedio() {
        return promedio;
    }

    public void setPromedio(String promedio) {
        this.promedio = promedio;
    }

    public Boolean getDatosActualizados() {
        return datosActualizados;
    }

    public void setDatosActualizados(Boolean datosActualizados) {
        this.datosActualizados = datosActualizados;
    }

    public String getPeriodoDatos() {
        return periodoDatos;
    }

    public void setPeriodoDatos(String periodoDatos) {
        this.periodoDatos = periodoDatos;
    }

    public Boolean getOtorgamientoActual() {
        return otorgamientoActual;
    }

    public void setOtorgamientoActual(Boolean OtorgamientoActual) {
        this.otorgamientoActual = OtorgamientoActual;
    }

    public boolean isClasificacionSolicitudBeca() {
        return clasificacionSolicitudBeca;
    }

    public void setClasificacionSolicitudBeca(boolean clasificacionSolicitudBeca) {
        this.clasificacionSolicitudBeca = clasificacionSolicitudBeca;
    }

    public String getCheckboxEgresado() {
        return checkboxEgresado;
    }

    public void setCheckboxEgresado(String checkboxEgresado) {
        this.checkboxEgresado = checkboxEgresado;
        if (checkboxEgresado.equals("on")) {
            alumno.getDatosAcademicos().setEgresado(1);
        } else {
            alumno.getDatosAcademicos().setEgresado(0);
        }
    }

    public void setCheckboxRegular(String checkboxRegular) {
        this.checkboxRegular = checkboxRegular;
        if (checkboxRegular.equals("on")) {
            alumno.getDatosAcademicos().setRegular(1);
        } else {
            alumno.getDatosAcademicos().setRegular(0);
        }
    }

    public void setCheckboxInscripcion(String checkboxInscripcion) {
        this.checkboxInscripcion = checkboxInscripcion;
        if (checkboxInscripcion.equals("on")) {
            alumno.getDatosAcademicos().setInscrito(1);
        } else {
            alumno.getDatosAcademicos().setInscrito(0);
        }
    }
    
    public String getEgresado() {
        return egresado;
    }

    public void setEgresado(Integer egresado) {
        this.egresado = egresado == null ? "-" : egresado == 1 ? "Sí" : "No";
    }

    public String getEgresadoDAE() {
        return egresadoDAE;
    }

    public void setEgresadoDAE(Integer egresado) {
        this.egresadoDAE = egresado == null ? "-" : egresado == 1 ? "Sí" : "No";
    }

    public String getMateriasReprobadas() {
        return materiasReprobadas;
    }

    public void setMateriasReprobadas(String materiasReprobadas) {
        this.materiasReprobadas = materiasReprobadas;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}

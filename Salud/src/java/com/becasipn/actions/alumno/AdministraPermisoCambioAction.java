package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.business.CuestionarioPreguntasRespuestasBO;
import com.becasipn.exception.ErrorDaeException;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Modalidad;
import com.becasipn.persistence.model.Carrera;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
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

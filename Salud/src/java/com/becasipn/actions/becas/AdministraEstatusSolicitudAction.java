package com.becasipn.actions.becas;

import com.becasipn.actions.BaseAction;
import com.becasipn.actions.alumno.MensajesAlumno;
import com.becasipn.business.EstatusSolicitudBO;
import com.becasipn.business.SolicitudBecasBO;
import com.becasipn.domain.RequisitosPrograma;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.OtorgamientoBajasDetalle;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.util.Util;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Usre-05
 */
public class AdministraEstatusSolicitudAction extends BaseAction implements MensajesAlumno {

    public static final String LISTA = "lista";
    private String numeroDeBoleta;
    private Alumno alumno = new Alumno();
    private Periodo periodo = new Periodo();
    private Otorgamiento otorgamiento = new Otorgamiento();
    private OtorgamientoBajasDetalle otorgamientoBajasDetalle = new OtorgamientoBajasDetalle();
    private List<RequisitosPrograma> stsList = new ArrayList<>();
    private String becaAnterior;
    private Boolean asignado = false;
    private Boolean procesosAlta = false;
    private Boolean mostrarDatosBeca = false;
    private Boolean periodoActivo = Boolean.FALSE;
    private Boolean esAlumno = Boolean.FALSE;
    private Boolean datosActual = Boolean.TRUE;
    private Boolean solicitudAnteriorVigente = Boolean.FALSE;
    private Boolean conSolicitud = Boolean.FALSE; // Con solicitud en el periodo actual
    private String curp;
    private String nombre;
    private String apPaterno;
    private String apMaterno;

    public String lista() {
        return LISTA;
    }

    public String ver() {
        if (isAlumno()) {
            esAlumno = Boolean.TRUE;
            numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        List<Alumno> listAlumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta);
        alumno = listAlumno.get(0);
        periodo = getDaos().getPeriodoDao().getPeriodoActivo();

        Otorgamiento otorgamientoAnterior = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getPeriodoAnterior().getId());
        if (otorgamientoAnterior == null || otorgamientoAnterior.getId() == null) {
            becaAnterior = "Sin beca en periodo anterior";
        } else {
            becaAnterior = otorgamientoAnterior.getTipoBecaPeriodo().getTipoBeca().getNombre();
        }
        asignado = getDaos().getOtorgamientoDao().existeAlumnoAsignado(alumno.getId());
        mostrarDatosBeca = Util.fechaDatos((String) ActionContext.getContext().getApplication().get("PublicacionResultadosOtorgamientos"));
        if (asignado) {
            otorgamiento = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), periodo.getId());
        }

        SolicitudBecasBO sBBO = SolicitudBecasBO.getInstance(getDaos());
        List<SolicitudBeca> solicitudes = sBBO.getSolicitudesAlumnoPeriodo(alumno, null);
        conSolicitud = solicitudes != null && !solicitudes.isEmpty();
        
        // Crea datos del estatus de la solicitud
        EstatusSolicitudBO bo = new EstatusSolicitudBO(getDaos(), solicitudes);
        solicitudAnteriorVigente = bo.isSolicitudAnteriorVigente(conSolicitud, alumno);
        stsList = bo.getRequisitos();

        DatosAcademicos datosAcademicos;
        if (conSolicitud) {
            datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), solicitudes.get(0).getPeriodo().getId());
        } else {
            datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
        }
        // Si los datos académicos no están actualziados
        // Se asigna el periodo de los datos academicos
        // en caso de que estos sean de otro periodo
        if (!datosAcademicos.getPeriodo().equals(periodo)) {
            datosActual = false;
            periodo = datosAcademicos.getPeriodo();
        }
        alumno.setDatosAcademicos(datosAcademicos);

        return SUCCESS;
    }

    public boolean compararIngresos(Object[] o) {
        Boolean res = false;
        BigDecimal a = (BigDecimal) o[1];//ID RESPUESTA
        BigDecimal b = (BigDecimal) o[2];//INGRESOS EN TBP

        switch (a.intValue()) {
            case 62:
                if (b.compareTo(new BigDecimal(1)) >= 0) {
                    res = true;
                }
                break;
            case 63:
                if (b.compareTo(new BigDecimal(2)) >= 0) {
                    res = true;
                }
                break;
            case 64:
                if (b.compareTo(new BigDecimal(3)) >= 0) {
                    res = true;
                }
                break;
            case 65:
                if (b.compareTo(new BigDecimal(4)) >= 0) {
                    res = true;
                }
                break;
            case 66:
                if (b.compareTo(new BigDecimal(5)) >= 0) {
                    res = true;
                }
                break;
            case 67:
                if (b.compareTo(new BigDecimal(6)) >= 0) {
                    res = true;
                }
                break;
            case 68:
                if (b.compareTo(new BigDecimal(7)) >= 0) {
                    res = true;
                }
                break;
            case 69:
                if (b.compareTo(new BigDecimal(8)) >= 0) {
                    res = true;
                }
                break;
            case 70:
                if (b.compareTo(new BigDecimal(9)) >= 0) {
                    res = true;
                }
                break;
            default:
                res = false;
                break;
        }
        return res;
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

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Otorgamiento getOtorgamiento() {
        return otorgamiento;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
    }

    public String getBecaAnterior() {
        return becaAnterior;
    }

    public void setBecaAnterior(String becaAnterior) {
        this.becaAnterior = becaAnterior;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public OtorgamientoBajasDetalle getOtorgamientoBajasDetalle() {
        return otorgamientoBajasDetalle;
    }

    public void setOtorgamientoBajasDetalle(OtorgamientoBajasDetalle otorgamientoBajasDetalle) {
        this.otorgamientoBajasDetalle = otorgamientoBajasDetalle;
    }

    public Boolean getProcesosAlta() {
        return procesosAlta;
    }

    public void setProcesosAlta(Boolean procesosAlta) {
        this.procesosAlta = procesosAlta;
    }

    public Boolean getAsignado() {
        return asignado;
    }

    public void setAsignado(Boolean asignado) {
        this.asignado = asignado;
    }

    public Boolean getMostrarDatosBeca() {
        return mostrarDatosBeca;
    }

    public void setMostrarDatosBeca(Boolean mostrarDatosBeca) {
        this.mostrarDatosBeca = mostrarDatosBeca;
    }

    public Boolean getPeriodoActivo() {
        return periodoActivo;
    }

    public void setPeriodoActivo(Boolean periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public Boolean getEsAlumno() {
        return esAlumno;
    }

    public void setEsAlumno(Boolean esAlumno) {
        this.esAlumno = esAlumno;
    }

    public Boolean getDatosActual() {
        return datosActual;
    }

    public void setDatosActual(Boolean datosActual) {
        this.datosActual = datosActual;
    }

    public List<RequisitosPrograma> getStsList() {
        return stsList;
    }

    public void setStsList(List<RequisitosPrograma> stsList) {
        this.stsList = stsList;
    }

    public Boolean getConSolicitud() {
        return conSolicitud;
    }

    public void setConSolicitud(Boolean conSolicitud) {
        this.conSolicitud = conSolicitud;
    }

    public Boolean getSolicitudAnteriorVigente() {
        return solicitudAnteriorVigente;
    }

    public void setSolicitudAnteriorVigente(Boolean solicitudAnteriorVigente) {
        this.solicitudAnteriorVigente = solicitudAnteriorVigente;
    }

}

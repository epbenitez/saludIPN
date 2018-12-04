package com.becasipn.actions.becas;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AlumnoBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Victor Lozano
 * @date 9/05/2016
 */
public class AdministracionBecasDisponiblesAction extends BaseAction implements MensajesBecas {

    private String periodo;
    private String becas;
    private int estatus;
    private boolean error = false;
    private String numeroDeBoleta;
    private BigDecimal otorgamientoId;
    private Boolean mostrarDatosBeca = false;
    private Boolean periodoActivo = Boolean.FALSE;
    private Boolean esAlumno = Boolean.FALSE;
    private Alumno alumno;
    private Otorgamiento otorgamiento;
    private Boolean mostrarOtorgamiento;
    private Boolean verOtorgamiento = Boolean.FALSE;
    Boolean puedeManutencion = Boolean.FALSE;

    public String verAlumno() {
        verBeca();
        return "ver";
    }

    public String ver() {
        verBeca();
        return SUCCESS;
    }

    private void verBeca() {
        Alumno alumno;

        mostrarDatosBeca = fechaDatos();
        if (isAlumno()) {
            esAlumno = Boolean.TRUE;
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            alumno = getDaos().getAlumnoDao().getByUsuario(usuario.getId());
        } else {
            alumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta).get(0);
        }

        Periodo pActual = getDaos().getPeriodoDao().getPeriodoActivo();

        AlumnoBO abo = new AlumnoBO(getDaos());
        periodoActivo = abo.solicitudActiva(alumno, pActual);

        if (getDaos().getOtorgamientoDao().existeAlumnoAsignado(alumno.getId()) && mostrarDatosBeca /*¿?*/) {
            becas = "<ul class='list-group'>"
                    + "<li class='list-group-item'>"
                    + "<span class='badge badge-info'>"
                    + "<span class='fa fa-graduation-cap'></span>"
                    + "</span>"
                    + getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId()).getTipoBecaPeriodo().getTipoBeca().getNombre()
                    + "</li>"
                    + "</ul>";
            estatus = 1;
        } else {
            List<TipoBecaPeriodo> listaBecas = getDaos().getTipoBecaPeriodoDao().becasAplicables(alumno, null, false, false);
            if (listaBecas == null || listaBecas.isEmpty()) {
                if (isAlumno()) {
                    becas = "<p>No cumples los requisitos para alguna de las becas disponibles. Para mayor información consulta</p><p class='text-center'><a href='/misdatos/verEstatusSolicitud.action' class='btn btn-primary btn-lg' >Estatus de solicitud de beca</a></p>";
                } else {
                    becas = "<p>El alumno no cumple los requisitos para alguna de las becas disponibles. Para mayor información consulte</p><p class='text-center'><a href='/becas/verEstatusSolicitud.action?numeroDeBoleta=" + alumno.getBoleta() + "' class='btn btn-primary btn-lg' >Estatus de solicitud</a></p>";
                }
                error = true;
            } else {
                for (TipoBecaPeriodo beca : listaBecas) {
                    if (beca.getTipoBeca().getBeca().getClave().equals("TM") || beca.getTipoBeca().getBeca().getClave().equals("M")) {
                        puedeManutencion = Boolean.TRUE;
                    }
                }
                becas = "<ul class='list-group'>";
                for (TipoBecaPeriodo beca : listaBecas) {
                    becas += "<li class='list-group-item'>"
                            + "<span class='badge badge-info'>"
                            + "<span class='fa fa-graduation-cap'></span>"
                            + "</span>"
                            + beca.getTipoBeca().getNombre() + "</li>";
                }
                becas += "</ul>";
            }
            estatus = 0;
        }

        periodo = pActual.getDescripcion();
    }

    public String detalle() {
        alumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta).get(0);
        alumno.setDatosAcademicos(getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId()));
        return SUCCESS;
    }

    public String detalleOtorgamiento() {
        otorgamiento = getDaos().getOtorgamientoDao().findById(otorgamientoId);
        alumno = otorgamiento.getAlumno();
        DatosAcademicos da = otorgamiento.getDatosAcademicos();
        alumno.setDatosAcademicos(da == null ? getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId()) : da);
        return SUCCESS;
    }

    public String mostrar() {
        String fPublicacionStr = (String) ActionContext.getContext().getApplication().get("PublicacionResultadosOtorgamientos");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //Inicialmente se que la fecha de publicacion ya haya pasado.
        try {
            //Obtiene la fecha de publicación.
            Date fechaPublicacion = formatter.parse(fPublicacionStr);
            Date hoy = new Date();
            //Compara la de publicación con la fecha de hoy para establecer si puede ver los otorgamientos o no.
            if (hoy.after(fechaPublicacion)) {
                verOtorgamiento = Boolean.TRUE;
                System.out.println("Paso1");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        alumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta).get(0);
        DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
        alumno.setDatosAcademicos(datosAcademicos);
        if (verOtorgamiento) {
            System.out.println("Paso2");
            mostrarOtorgamiento = getDaos().getOtorgamientoDao().mostrarOtorgamiento(alumno.getId());
            if (mostrarOtorgamiento) {
                Otorgamiento o = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
                if (o != null && o.getTipoBecaPeriodo() != null) {
                    becas = o.getTipoBecaPeriodo().getTipoBeca().getId().toString();
                }
            }
        }

        return SUCCESS;
    }

    public Boolean fechaDatos() {
        String fechaDatos = (String) ActionContext.getContext().getApplication().get("PublicacionResultadosOtorgamientos");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = formatter.parse(fechaDatos);
        } catch (Exception e) {
            d = null;
        }
        return new Date().after(d);
    }

    /**
     * Funcion default para la busqueda de becas disponibles
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String lista() {
        return SUCCESS;
    }

    public String listado() {
        return SUCCESS;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getBecas() {
        return becas;
    }

    public void setBecas(String becas) {
        this.becas = becas;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
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

    public Boolean getMostrarOtorgamiento() {
        return mostrarOtorgamiento;
    }

    public void setMostrarOtorgamiento(Boolean mostrarOtorgamiento) {
        this.mostrarOtorgamiento = mostrarOtorgamiento;
    }

    public Boolean getVerOtorgamiento() {
        return verOtorgamiento;
    }

    public void setVerOtorgamiento(Boolean verOtorgamiento) {
        this.verOtorgamiento = verOtorgamiento;
    }

    public Boolean getPuedeManutencion() {
        return puedeManutencion;
    }

    public void setPuedeManutencion(Boolean puedeManutencion) {
        this.puedeManutencion = puedeManutencion;
    }

    public Otorgamiento getOtorgamiento() {
        return otorgamiento;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
    }

    public BigDecimal getOtorgamientoId() {
        return otorgamientoId;
    }

    public void setOtorgamientoId(BigDecimal otorgamientoId) {
        this.otorgamientoId = otorgamientoId;
    }

}

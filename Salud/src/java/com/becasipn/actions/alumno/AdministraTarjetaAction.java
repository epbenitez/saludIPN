package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.TarjetaBO;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
public class AdministraTarjetaAction extends BaseAction implements MensajesAlumno {

    private static final String SUCCESS_REPORT = "success_report";
    private static final String ERROR_REPORT = "error_report";

    private final TarjetaBO tarjetaBO;
    private final Usuario usuario;

    private AlumnoTarjetaBancaria tarjetaAlumno;
    private String numTarjeta;
    private String numReporte;
    private String motivo;

    private Boolean warning = Boolean.FALSE;

    /**
     * Constructor público que inicializa el DAO de la tarjeta y obtiene el
     * alumno de la sesión en contexto.
     */
    public AdministraTarjetaAction() {
        tarjetaBO = new TarjetaBO(getDaos());
        usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
    }

    /**
     * Realiza la pre-validación para que el alumno pueda activar su tarjeta.
     *
     * @return <b><i>SUCCESS</i></b> Cuando la verificación pasa (El alumno
     * tiene una tarjeta que puede ser activada).<br>
     * <b><i>ERROR</i></b> Cuando se presenta alguna de las siguientes
     * situaciones:
     * <ul>
     * <li> El alumno no ha iniciado sesión en el sistema </li>
     * <li> El alumno tiene una tarjeta, pero esta ya esta activa ó aún no se
     * cumplen las condiciones necesarias para que sea activada.</li>
     * </ul>
     */
    public String activar() {

        if (null == usuario) {
            addActionError(getText("activarTarjeta.error.sinTarjeta"));
            return ERROR;
        }
        
        tarjetaAlumno = tarjetaBO.obtenerTarjeta(usuario.getId());

        if (null == tarjetaAlumno) {
            addActionError(getText("activarTarjeta.error.sinTarjeta"));
            warning = Boolean.TRUE;
            return ERROR;
        }

        if (!tarjetaBO.sePuedeActivar(tarjetaAlumno.getTarjetaBancaria().getBitacoraTarjetaBancarias())) {
            addActionError(getText("activarTarjeta.error.noSePuedeActivar"));
            warning = Boolean.TRUE;
            return ERROR;
        }

        clearErrors();
        return SUCCESS;
    }

    /**
     * Verifica una tarjeta (Cambiar el estado de <i>Entregada al alumno</i> a
     * el estado de
     * <i>Verificada por el alumno</i>) El número de tarjeta introducido por el
     * alumno no debe ser vacio ó nulo, además de que el último estado en la
     * bitacora de la tarjeta debe ser uno anterior a <b>Entregada al alumno</b>
     *
     * @return <b><i>SUCCESS</i></b> Cuando la activación de la tarjeta fue
     * realizada con éxito.<br>
     * <b><i>ERROR</i></b> Cuando se presenta alguna de las siguientes
     * situaciones:
     * <ul>
     * <li> El alumno ingreso un número de tarjeta vacio </li>
     * <li> La tarjeta no se puede activar debido a que ya esta activa </li>
     * <li> El número ingresado y el que se tiene en el sistema no coincide
     * </li>
     * </ul>
     */
    public String verificar() {

        if (numTarjeta == null || numTarjeta.isEmpty()) {
            addActionError(getText("activarTarjeta.error.noSePuedeActivar"));
            return ERROR;
        }

        if (!tarjetaBO.sePuedeActivar(usuario.getId())) {
            addActionError(getText("activarTarjeta.error.noSePuedeActivar"));
            return ERROR;
        }

        if (!tarjetaBO.guardarTarjetaAlumno(numTarjeta, usuario.getId())) {
            addActionError(getText("activarTarjeta.error.noCoincide"));
            return ERROR;
        } else {
            addActionMessage(getText("activarTarjeta.success.verificada"));
        }

        clearActionErrors();
        return SUCCESS;
    }

    public String reportar() {

        tarjetaAlumno = tarjetaBO.obtenerTarjeta(usuario.getId());

        //Tiene tarjeta
        if (null == tarjetaAlumno) {
            addActionError(getText("activarTarjeta.error.sinTarjeta"));
            warning = Boolean.TRUE;
            return ERROR_REPORT;
        }

        //Tenga tarjeta activa y se pueda dar de baja
        if (!tarjetaBO.sePuedeBorrar(tarjetaAlumno.getTarjetaBancaria().getBitacoraTarjetaBancarias())) {
            addActionError(getText("activarTarjeta.error.noSePuedeBorrar"));
            warning = Boolean.TRUE;
            return ERROR_REPORT;
        }
        return SUCCESS_REPORT;
    }

    public String suspender() {

        //No se cargo la tarjeta
        if (null == tarjetaAlumno) {
            addActionError(getText("reportarTarjeta.error.redireccion"));
            return ERROR_REPORT;
        }

        //Número de reporte vacio
        if (null == numReporte || numReporte.isEmpty()) {
            addActionError(getText("reportarTarjeta.error.numReporte"));
            return ERROR_REPORT;
        }
        
         //Motivo vacio
        if (null == motivo || motivo.isEmpty()) {
            addActionError(getText("reportarTarjeta.error.motivo"));
            return ERROR_REPORT;
        }

        //Tenga tarjeta activa y se pueda dar de baja
        if (!tarjetaBO.sePuedeBorrar(tarjetaAlumno)) {
            addActionError(getText("activarTarjeta.error.noSePuedeBorrar"));
            return ERROR_REPORT;
        }
        
        tarjetaBO.suspender(tarjetaAlumno, numReporte,motivo);
        addActionMessage(getText("reportarTarjeta.success.reportada"));
        return SUCCESS_REPORT;
    }

    /* ******************
     * GETTER & SETTERS *
     ********************/
    public AlumnoTarjetaBancaria getTarjeta() {
        return tarjetaAlumno;
    }

    public void setTarjeta(AlumnoTarjetaBancaria tarjeta) {
        this.tarjetaAlumno = tarjeta;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public String getNumReporte() {
        return numReporte;
    }

    public void setNumReporte(String numReporte) {
        this.numReporte = numReporte;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    
    public AlumnoTarjetaBancaria getTarjetaAlumno() {
        return tarjetaAlumno;
    }

    public void setTarjetaAlumno(AlumnoTarjetaBancaria tarjetaAlumno) {
        this.tarjetaAlumno = tarjetaAlumno;
    }

    public Boolean getWarning() {
        return warning;
    }

    public void setWarning(Boolean warning) {
        this.warning = warning;
    }

}

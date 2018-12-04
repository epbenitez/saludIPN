package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.CuentaBO;
import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.persistence.model.Usuario;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.text.SimpleDateFormat;

/**
 *
 * @author Rafael Cárdeneas Reséndiz
 */
public class AdministraCuentaAction extends BaseAction implements MensajesAdmin {
    public static final String LISTA = "lista";
    private SolicitudCuentas solicitud;
    private String usuarioCancelacion, fechaCancelacion;
    private String datosGrafica= "";

    public String lista() {
        return SUCCESS;
    }

    public String detalle() {
        CuentaBO solicitudCuentaBO = new CuentaBO(getDaos());
        datosGrafica = solicitudCuentaBO.graficaEstatusCuentas(solicitud.getId());
        solicitud = getDaos().getSolicitudCuentasDao().findById(solicitud.getId());
        //Se agregan los datos de la gráfica que se muestra en pantalla
        return SUCCESS;
    }

    public String estatus() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SolicitudCuentas sc = getDaos().getSolicitudCuentasDao().findById(solicitud.getId());
        usuarioCancelacion = sc.getUsuarioCancelacion().getUsuario();
        fechaCancelacion = sdf.format(sc.getFechaCancelacion());
        return SUCCESS;
    }

    public String finaliza() {
        CuentaBO cbo = new CuentaBO(getDaos());
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        SolicitudCuentas sc = getDaos().getSolicitudCuentasDao().findById(solicitud.getId());
        cbo.finalizar(sc, usuario);
        return LISTA;
    }

    public SolicitudCuentas getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudCuentas solicitud) {
        this.solicitud = solicitud;
    }

    public String getDatosGrafica() {
        return datosGrafica;
    }

    public void setDatosGrafica(String datosGrafica) {
        this.datosGrafica = datosGrafica;
    }
    
    public String getUsuarioCancelacion() {
        return usuarioCancelacion;
    }

    public void setUsuarioCancelacion(String usuarioCancelacion) {
        this.usuarioCancelacion = usuarioCancelacion;
    }

    public String getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }
}

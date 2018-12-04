package com.becasipn.actions.becas;

import com.becasipn.actions.BaseReportAction;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.business.OtorgamientoBajasDetalleBO;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.Util;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public class AdministracionBecasBajasAction extends BaseReportAction implements MensajesBecas {

    public static final String LISTA = "lista";
    public static final String FORMULARIO = "formulario";

    private Usuario usuario;
    private UnidadAcademica UA;

    private BigDecimal privilegio;
    private String alumnosError;
    private String bajas;
    private String tipoBeca;
    private String proceso;
    private String idTipoBaja;
    private String observaciones;

    private Boolean alta;
    private String unidadAcademica;
    private String boleta;

    private List<Otorgamiento> datos = new ArrayList<>();
    private List<TipoBecaPeriodo> becasPeriodo = new ArrayList<>();

    public String lista() {
	inicializar();
	return SUCCESS;
    }

    public String correos() {
	if (isFuncionario() || isResponsable()) {
	    usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
	    unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
	}
	return SUCCESS;
    }

    public String form() {
	if (isFuncionario() || isResponsable()) {
	    usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
	    unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
	}
	return SUCCESS;
    }

    public String darBja() {
	if (isFuncionario() || isResponsable()) {
	    usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
	    unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
	}
	return darBaja();
    }

    public String darBaja() {
	if (isFuncionario() || isResponsable()) {
	    usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
	    unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
	}
	if (bajas != null && bajas.length() > 1) {
	    bajas = bajas.substring(1);
	    OtorgamientoBajasDetalleBO bo = new OtorgamientoBajasDetalleBO(getDaos());
	    OtorgamientoBO bs = new OtorgamientoBO(getDaos());

	    BigDecimal ax = null;
	    if (tipoBeca.equals("6") && Util.CampoValidoAJAX(idTipoBaja)) {
		ax = new BigDecimal(idTipoBaja);
	    }

	    Boolean res = true;//bo.guardaBajasDetalle(bajas, ax, new BigDecimal(tipoBeca), observaciones, new BigDecimal(proceso));
	    Boolean rsp = bs.guardaBajas(bajas);
	    if (res && rsp) {
		addActionMessage(getText("becas.guardado.exito"));
	    } else {
		addActionError(getText("becas.guardado.error"));
	    }
	} else {
	    addActionError(getText("becas.bajas.error"));
	}
	if (tipoBeca.equals("6") && Util.CampoValidoAJAX(idTipoBaja)) {
	    return FORMULARIO;
	} else {
	    return LISTA;
	}
    }

    private void inicializar() {
	usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
	privilegio = getDaos().getUsuarioPrivilegioDao().findByUsuario(usuario.getId()).getPrivilegio().getId();
	if (isFuncionario() || isResponsable()) {
	    unidadAcademica = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica().getId().toString();
	}
	BigDecimal nivelId;
	PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
	if (isResponsable()) {
	    nivelId = personal.getUnidadAcademica().getNivel().getId();
	    UA = personal.getUnidadAcademica();
	} else {
	    nivelId = null;
	}
	becasPeriodo = getDaos().getTipoBecaPeriodoDao().becasPorNivelPeriodo(nivelId, getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());
	alumnosError = "";
    }

    public String automatico() {
	inicializar();
	return SUCCESS;
    }

    @Override
    public String toString() {
	return "AdministracionBecasBajasAction{" + "bajas=" + bajas + ", tipoBeca=" + tipoBeca + ", idTipoBaja=" + idTipoBaja + ", observaciones=" + observaciones + ", alta=" + alta + ", unidad=" + unidadAcademica + ", boleta=" + boleta + '}';
    }

    public String getBajas() {
	return bajas;
    }

    public void setBajas(String bajas) {
	this.bajas = bajas;
    }

    public String getTipoBeca() {
	return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca) {
	this.tipoBeca = tipoBeca;
    }

    public String getIdTipoBaja() {
	return idTipoBaja;
    }

    public void setIdTipoBaja(String idTipoBaja) {
	this.idTipoBaja = idTipoBaja;
    }

    public String getObservaciones() {
	return observaciones;
    }

    public void setObservaciones(String observaciones) {
	this.observaciones = observaciones;
    }

    public Boolean getAlta() {
	return alta;
    }

    public void setAlta(Boolean alta) {
	this.alta = alta;
    }

    public String getUnidadAcademica() {
	return unidadAcademica;
    }

    public void setUnidadAcademica(String unidadAcademica) {
	this.unidadAcademica = unidadAcademica;
    }

    public String getBoleta() {
	return boleta;
    }

    public void setBoleta(String boleta) {
	this.boleta = boleta;
    }

    public String getProceso() {
	return proceso;
    }

    public void setProceso(String proceso) {
	this.proceso = proceso;
    }

    public List<Otorgamiento> getDatos() {
	return datos;
    }

    public void setDatos(List<Otorgamiento> datos) {
	this.datos = datos;
    }

    public UnidadAcademica getUA() {
	return UA;
    }

    public void setUA(UnidadAcademica UA) {
	this.UA = UA;
    }

    public Usuario getUsuario() {
	return usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public BigDecimal getPrivilegio() {
	return privilegio;
    }

    public void setPrivilegio(BigDecimal privilegio) {
	this.privilegio = privilegio;
    }

    public String getAlumnosError() {
	return alumnosError;
    }

    public void setAlumnosError(String alumnosError) {
	this.alumnosError = alumnosError;
    }

    public List<TipoBecaPeriodo> getBecasPeriodo() {
	return becasPeriodo;
    }

    public void setBecasPeriodo(List<TipoBecaPeriodo> becasPeriodo) {
	this.becasPeriodo = becasPeriodo;
    }

}

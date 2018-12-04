package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.Otorgamiento;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_PAGINATED_JSON;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.business.OtorgamientoBajasDetalleBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.Util;
import static com.becasipn.util.Util.CampoValidoAJAX;
import com.opensymphony.xwork2.ActionContext;

public class BecasBajasAjaxAction extends JSONAjaxAction {

    private Boolean alta;
    private String unidad;
    private String boleta;
    private String tipoBeca;
    private BigDecimal procesoid;

    private String alumnos;
    private String oaIDs;
    private String tipoBaja;
    private String observaciones;
    private String prc;

    public String listadoBajasDiversas() {
	setSsu();
	OtorgamientoBO bo = new OtorgamientoBO(getDaos());
	PaginateUtil ax = getDaos().getOtorgamientoDao().otorgamientosPeriodoAnterior(ssu);
	ax.setResultados(bo.asignarElementos(ax.getResultados()));
	setPu(ax);
	List<Otorgamiento> lista = getPu().getResultados();
	if (lista.size() > 0) {
	    for (Otorgamiento otorgamiento : lista) {
//		otorgamiento = getDaos().getOtorgamientoDao().findById(otorgamiento.getId());
		Alumno alumno = otorgamiento.getAlumno();
		DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
		getJsonResult().add("[\"" + "<input class='chck' type=checkbox id='" + otorgamiento.getId() + "' value='" + otorgamiento.getId() + "' oaId='" + otorgamiento.getId() + "'/>"
			+ labelAlumno(otorgamiento.getAlta(), alumno.getBoleta())
			+ "\", \"" + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre()
			+ "\", \"" + datosAcademicos.getCarrera().getCarrera()
                        + "\", \"" + otorgamiento.getPeriodo().getClave()
			+ "\", \"" + datosAcademicos.getPromedio()
			+ "\", \"" + datosAcademicos.getSemestre()
			+ "\", \"" + otorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre()
			+ "\", \"" + labelInscrito(datosAcademicos.getInscrito())
			+ "\", \"" + labelRegular(datosAcademicos.getRegular())
			+ " \"]");
	    }
	}
	return SUCCESS_PAGINATED_JSON;
    }

    public String listadoBajasPasantia() {
	setSsu();
	OtorgamientoBO bo = new OtorgamientoBO(getDaos());
	PaginateUtil ax = getDaos().getOtorgamientoDao().otorgamientosPasantia(ssu);
	ax.setResultados(bo.asignarElementos(ax.getResultados()));
	setPu(ax);
	List<Otorgamiento> lista = getPu().getResultados();
	if (lista.size() > 0) {
	    for (Otorgamiento otorgamiento : lista) {
//		otorgamiento = getDaos().getOtorgamientoDao().findById(otorgamiento.getId());
		Alumno alumno = otorgamiento.getAlumno();
		DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
		getJsonResult().add("[\"" + "<input class='chck' type=checkbox id='" + otorgamiento.getId() + "' value='" + otorgamiento.getId() + "' oaId='" + otorgamiento.getId() + "'/>"
			+ labelAlumno(alta, alumno.getBoleta())
			+ "\", \"" + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre()
			+ "\", \"" + datosAcademicos.getCarrera().getCarrera()
                        + "\", \"" + otorgamiento.getPeriodo().getClave()
			+ "\", \"" + datosAcademicos.getPromedio()
			+ "\", \"" + datosAcademicos.getSemestre()
			+ "\", \"" + otorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre()
			+ "\", \"" + labelInscrito(datosAcademicos.getInscrito())
			+ "\", \"" + labelRegular(datosAcademicos.getRegular())
			+ " \"]");
	    }
	}
	return SUCCESS_PAGINATED_JSON;
    }

    public String listadoBajasIncumplimiento() {
	setSsu();
	OtorgamientoBO bo = new OtorgamientoBO(getDaos());
	PaginateUtil ax = getDaos().getOtorgamientoDao().otorgamientosIncumplimiento(ssu);
	ax.setResultados(bo.asignarElementos(ax.getResultados()));
	setPu(ax);
	List<Otorgamiento> lista = getPu().getResultados();
	if (lista.size() > 0) {
	    for (Otorgamiento otorgamiento : lista) {
//		otorgamiento = getDaos().getOtorgamientoDao().findById(otorgamiento.getId());
		Alumno alumno = otorgamiento.getAlumno();
		DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
		getJsonResult().add("[\"" + "<input class='chck' type=checkbox id='" + otorgamiento.getId() + "' value='" + otorgamiento.getId() + "' oaId='" + otorgamiento.getId() + "'/>"
			+ labelAlumno(alta, alumno.getBoleta())
			+ "\", \"" + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre()
			+ "\", \"" + datosAcademicos.getCarrera().getCarrera()
                        + "\", \"" + otorgamiento.getPeriodo().getClave()
			+ "\", \"" + datosAcademicos.getPromedio()
			+ "\", \"" + datosAcademicos.getSemestre()
			+ "\", \"" + otorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre()
			+ "\", \"" + labelInscrito(datosAcademicos.getInscrito())
			+ "\", \"" + labelRegular(datosAcademicos.getRegular())
			+ " \"]");
	    }
	}
	return SUCCESS_PAGINATED_JSON;
    }

    public String cargaMenu() {
	List<Proceso> lista = getDaos().getProcesoDao().procesosBajasPorUnidadAcademica(new BigDecimal(unidad));
	String aux = "";
	for (Proceso p : lista) {
	    Date hoy = new Date();
	    Boolean bol = !(hoy.before(p.getFechaInicial()) || hoy.after(p.getFechaFinal()));
	    String ax;
	    if (bol) {
		ax = p.getProcesoEstatus().getId().toString();
	    } else {
		ax = "0";
	    }
	    aux = aux + p.getId()
		    + "," + ax
		    + "," + p.getUnidadAcademica().getId()
		    + "," + p.getTipoProceso().getNombre()
		    + "," + p.getTipoProceso().getMovimiento().getId()
		    + "/";
	}
	getJsonResult().add("[\"" + aux + " \"]");
	return SUCCESS_JSON;
    }

    private String labelInscrito(Integer inscrito) {
	String res;
	if (inscrito != null && inscrito == 1) {
	    res = "<span class='label label-success'>Inscrito</span>";
	} else {
	    res = "<span class='label label-danger'>No Inscrito</span>";
	}
	return res;
    }

    private String labelAlumno(Boolean a, String b) {
	String res;
	if (a) {
	    res = "\", \"<a title='Estatus de solicitud del alumno' class='fancybox fancybox.iframe table-link'  href='/becas/verEstatusSolicitud.action?numeroDeBoleta=" + b + "'>" + b + "</a>";
	} else {
	    res = "\", \"" + b;
	}
	return res;
    }

    private String labelRegular(Integer regular) {
	String res;
	if (regular != null && regular == 1) {
	    res = "<span class='label label-success'>Regular</span>";
	} else {
	    res = "<span class='label label-danger'>No Regular</span>";
	}
	return res;
    }

    private String getBecaAnterior(BigDecimal id) {
	String ba = getDaos().getOtorgamientoDao().findById(id).getTipoBecaPeriodo().getTipoBeca().getNombre();
	if (ba == null) {
	    ba = "---";
	}
	return ba;
    }

    public String darBaja() {
	if (oaIDs == null || oaIDs.equals("")) {
	    getJsonResult().add("{\"error\": \"Debes seleccionar al menos un alumno para poder dar de baja.\"}");
	    return SUCCESS_JSON;
	}
	OtorgamientoBajasDetalleBO bo = new OtorgamientoBajasDetalleBO(getDaos());
	Proceso p = getDaos().getProcesoDao().findById(new BigDecimal(prc));

//	BigDecimal procesoPasantia = getDaos().getProcesoDao().procesoAutoByMovimiento(new BigDecimal("5")).getId();
//	BigDecimal procesoIncumplimiento = getDaos().getProcesoDao().procesoAutoByMovimiento(new BigDecimal("4")).getId();
//	if ((p.getTipoProceso().getId().equals(procesoPasantia) || p.getTipoProceso().getId().equals(procesoIncumplimiento)) && alta) {
//	    getJsonResult().add("{\"error\": \"No se puede dar de baja dentro de un proceso automatico.\"}");
//	    return SUCCESS_JSON;
//	}
	BigDecimal ax = null;
	if (p.getTipoProceso().getMovimiento().getId().equals(new BigDecimal(6)) && Util.CampoValidoAJAX(tipoBaja)) {
	    ax = new BigDecimal(tipoBaja);
	}

	bo.guardaBajasDetalle(oaIDs, ax, observaciones, p.getId(), p.getTipoProceso().getMovimiento().getId(), alta);

	getJsonResult().add("{\"bien\":" + bo.getAlumnosOtorgados().size() + "}");
	getJsonResult().add("{\"mal\":" + bo.getAlumnosConError().size() + "}");
	getJsonResult().addAll(bo.getAlumnosConError());

	return SUCCESS_JSON;
    }

    public String darBajaAuto() {
	if (tipoBaja == null || tipoBaja.equals("")) {
	    getJsonResult().add("{\"error\": \"Esta operacion no es permitida.\"}");
	    return SUCCESS_JSON;
	}
	OtorgamientoBajasDetalleBO bo = new OtorgamientoBajasDetalleBO(getDaos());
	bo.guardaBajasDetalleAuto(tipoBaja, Boolean.TRUE);

	if (bo.getAlumnosConError().size() <= 0) {
	    reloadVariablesPersonalizadas();
	}

	getJsonResult().add("{\"bien\":" + bo.getAlumnosOtorgados().size() + "}");
	getJsonResult().add("{\"mal\":" + bo.getAlumnosConError().size() + "}");
	getJsonResult().addAll(bo.getAlumnosConError());
	return SUCCESS_JSON;
    }

    public String automaticas() {
	String autoSec = (String) ActionContext.getContext().getApplication().get("secuenciaProcesosAutomaticos");
	Integer i = Integer.valueOf(autoSec);
	switch (i) {
	    case 1:
		getJsonResult().add("[\""//ACTIVO
			+ "<div class='col-lg-4 col-md-4 col-xs-6 rectangulo' onclick='activo(this)' "
			+ "style='background: #ACC18B url(/resources/img/asignacionManual/bajas-pasantia.png) no-repeat;' "
			+ "data-activo='1' "
			+ "data-baja='1' "
			+ "><span>Bajas automaticas de pasantia</span></div>"
			+ "\"]"
		);
		getJsonResult().add("[\""//INACTIVO
			+ "<div class='col-lg-4 col-md-4 col-xs-6 rectangulo' onclick='activo(this)'"
			+ "style='background: #DB4D6C url(/resources/img/asignacionManual/bajas-incumplimiento.png) no-repeat;' "
			+ "data-activo='0' "
			+ "data-baja='2' "
			+ "><span>Bajas automaticas de incumplimiento</span></div>"
			+ "\"]"
		);
		break;
	    case 3:
		getJsonResult().add("[\""//INACTIVO
			+ "<div class='col-lg-4 col-md-4 col-xs-6 rectangulo' onclick='activo(this)'"
			+ "style='background: #DB4D6C url(/resources/img/asignacionManual/bajas-pasantia.png) no-repeat;' "
			+ "data-activo='0' "
			+ "data-baja='1' "
			+ "><span>Bajas automaticas de pasantia</span></div>"
			+ "\"]"
		);
		getJsonResult().add("[\""//ACTIVO
			+ "<div class='col-lg-4 col-md-4 col-xs-6 rectangulo' onclick='activo(this)' "
			+ "style='background: #ACC18B url(/resources/img/asignacionManual/bajas-incumplimiento.png) no-repeat;' "
			+ "data-activo='1' "
			+ "data-baja='2' "
			+ "><span>Bajas automaticas de incumplimiento</span></div>"
			+ "\"]"
		);
		break;
	    default:
		getJsonResult().add("[\""//INACTIVO
			+ "<div class='col-lg-4 col-md-4 col-xs-6 rectangulo' onclick='activo(this)'"
			+ "style='background: #DB4D6C url(/resources/img/asignacionManual/bajas-pasantia.png) no-repeat;' "
			+ "data-activo='0' "
			+ "data-baja='1' "
			+ "><span>Bajas automaticas de pasantia</span></div>"
			+ "\"]"
		);
		getJsonResult().add("[\""//INACTIVO
			+ "<div class='col-lg-4 col-md-4 col-xs-6 rectangulo' onclick='activo(this)'"
			+ "style='background: #DB4D6C url(/resources/img/asignacionManual/bajas-incumplimiento.png) no-repeat;' "
			+ "data-activo='0' "
			+ "data-baja='2' "
			+ "><span>Bajas automaticas de incumplimiento</span></div>"
			+ "\"]"
		);
		break;
	}
	return SUCCESS_JSON;
    }

    public String mostrarDatos() {
	Integer p = Integer.valueOf(prc);
	Integer b = Integer.valueOf(tipoBaja);
	switch (b) {
	    case 1:
		getJsonResult().add("[\""
			+ "<button id='pasantiaButton' type='button' class='btn btn-primary pull-right col-lg-4' onclick='darBajaAuto(5)'>Aplicar</button>"
			+ "\"]");
		break;
	    case 2:
		getJsonResult().add("[\""
			+ "<button id='incumplimientoButton' type='button' class='btn btn-primary pull-right col-lg-4' onclick='darBajaAuto(4)'>Aplicar</button>"
			+ "\"]");
		break;
	    default:
		getJsonResult().add("[\""
			+ "<div class='col-lg-12 col-md-12 col-xs-12'><span>No existen datos</span></div>"
			+ "\"]");
		break;
	}
	return SUCCESS_JSON;
    }

    public Boolean getAlta() {
	return alta;
    }

    public void setAlta(Boolean alta) {
	this.alta = alta;
	ssu.parametrosServidor.put("ALTA_", alta);
    }

    public String getUnidad() {
	return unidad;
    }

    public void setUnidad(String unidad) {
	this.unidad = unidad;
	if (CampoValidoAJAX(unidad)) {
	    ssu.parametrosServidor.put("UNIDADACADEMICA", new BigDecimal(this.unidad));
	}
    }

    public BigDecimal getProcesoid() {
	return procesoid;
    }

    public void setProcesoid(BigDecimal procesoid) {
	this.procesoid = procesoid;
	if (CampoValidoAJAX(procesoid)) {
	    ssu.parametrosServidor.put("PROCESOID", this.procesoid);
	}
    }

    public String getBoleta() {
	return boleta;
    }

    public void setBoleta(String boleta) {
	this.boleta = boleta;
	if (CampoValidoAJAX(boleta)) {
	    ssu.parametros.put("ALUMNOBOLETA", this.boleta);
	}
	ssu.parametrosServidor.put("ALUMNOBOLETA", null);
    }

    public String getTipoBeca() {
	return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca) {
	this.tipoBeca = tipoBeca;
	if (CampoValidoAJAX(tipoBeca)) {
	    ssu.parametros.put("TIPOBECA", new BigDecimal(this.tipoBeca));
	}
	ssu.parametrosServidor.put("TIPOBECA", null);
    }

    public String getAlumnos() {
	return alumnos;
    }

    public void setAlumnos(String alumnos) {
	this.alumnos = alumnos;
    }

    public String getOaIDs() {
	return oaIDs;
    }

    public void setOaIDs(String oaIDs) {
	this.oaIDs = oaIDs;
    }

    public String getTipoBaja() {
	return tipoBaja;
    }

    public void setTipoBaja(String tipoBaja) {
	this.tipoBaja = tipoBaja;
    }

    public String getObservaciones() {
	return observaciones;
    }

    public void setObservaciones(String observaciones) {
	this.observaciones = observaciones;
    }

    public String getPrc() {
	return prc;
    }

    public void setPrc(String prc) {
	this.prc = prc;
    }

}

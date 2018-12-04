package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.domain.AsignacionResumen;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.util.StringUtil;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministracionAsignacionAutomaticaAction extends BaseAction implements MensajesAdmin {

    public static final String NUEVOS = "nuevos";
    public static final String REVALIDANTES = "revalidantes";
    public static final String REVIERTE = "revertir";
    private Integer alumnos0RevalidantesNMS;
    private Integer alumnos0RevalidantesNS;
    private Integer alumnos0NuevosNMS;
    private Integer alumnos0NuevosNS;
    private List<Nivel> niveles;
    private Integer nivel;
    private Integer movimiento;
    private List<AsignacionResumen> resumen = new ArrayList<>();
    private Integer alumnosNoAsignados = 0;

    public String form() {
	cargarListas();
	return SUCCESS;
    }

    public String genera() {

	OtorgamientoBO bo = new OtorgamientoBO(getDaos());
	switch (movimiento) {
	    case 1:
		resumen = bo.asignacionAutomaticaNuevos(nivel);
		if (resumen == null || resumen.isEmpty()) {
		    addActionError(getText("admin.asignacion.automatica.error"));
		} else {
		    addActionMessage(getText("admin.asignacion.automatica.exito"));
		}
		alumnosNoAsignados = getDaos().getAlumnoDao().alumnosNoAsinadosAutomaticamente();
		return NUEVOS;
	    case 2:
		resumen = bo.asignacionAutomaticaRevalidantes(nivel);
		if (resumen == null || resumen.isEmpty()) {
		    addActionError(getText("admin.asignacion.automatica.error"));
		} else {
		    addActionMessage(getText("admin.asignacion.automatica.exito"));
		}
		return REVALIDANTES;
	    case 3:
		resumen = bo.asignacionAutomaticaOtorgamientosSeleccionados(nivel);
		if (resumen == null || resumen.isEmpty()) {
		    addActionError(getText("admin.asignacion.automatica.error"));
		} else {
		    addActionMessage(getText("admin.asignacion.automatica.exito"));
		}
		return REVALIDANTES;
	    default:
		return ERROR;
	}
    }

    public String revierte() {
	OtorgamientoBO bo = new OtorgamientoBO(getDaos());
	try {
	    cargarListas();
	    int revertidos = bo.revertirAsignacion();
	    addActionMessage(StringUtil.formatMessage(getText("admin.revertir.otorgamientos"), revertidos));
	    return REVIERTE;
	} catch (Exception e) {
	    addActionError(StringUtil.formatMessage(getText("admin.revertir.otorgamientos"), e.getMessage()));
	    return ERROR;
	}
    }

    public List<Nivel> getNiveles() {
	return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
	this.niveles = niveles;
    }

    public Integer getNivel() {
	return nivel;
    }

    public void setNivel(Integer nivel) {
	this.nivel = nivel;
    }

    public Integer getAlumnos0RevalidantesNMS() {
	return alumnos0RevalidantesNMS;
    }

    public void setAlumnos0RevalidantesNMS(Integer alumnos0RevalidantesNMS) {
	this.alumnos0RevalidantesNMS = alumnos0RevalidantesNMS;
    }

    public Integer getAlumnos0RevalidantesNS() {
	return alumnos0RevalidantesNS;
    }

    public void setAlumnos0RevalidantesNS(Integer alumnos0RevalidantesNS) {
	this.alumnos0RevalidantesNS = alumnos0RevalidantesNS;
    }

    public Integer getAlumnos0NuevosNMS() {
	return alumnos0NuevosNMS;
    }

    public void setAlumnos0NuevosNMS(Integer alumnos0NuevosNMS) {
	this.alumnos0NuevosNMS = alumnos0NuevosNMS;
    }

    public Integer getAlumnos0NuevosNS() {
	return alumnos0NuevosNS;
    }

    public void setAlumnos0NuevosNS(Integer alumnos0NuevosNS) {
	this.alumnos0NuevosNS = alumnos0NuevosNS;
    }

    public Integer getMovimiento() {
	return movimiento;
    }

    public void setMovimiento(Integer movimiento) {
	this.movimiento = movimiento;
    }

    public List<AsignacionResumen> getResumen() {
	return resumen;
    }

    public void setResumen(List<AsignacionResumen> resumen) {
	this.resumen = resumen;
    }

    public Integer getAlumnosNoAsignados() {
	return alumnosNoAsignados;
    }

    public void setAlumnosNoAsignados(Integer alumnosNoAsignados) {
	this.alumnosNoAsignados = alumnosNoAsignados;
    }

    private void cargarListas() {
	niveles = getDaos().getNivelDao().nivelesActivos();
	alumnos0NuevosNMS = getDaos().getAlumnoDao().alumnos0sNuevos(true);
	alumnos0NuevosNS = getDaos().getAlumnoDao().alumnos0sNuevos(false);

	alumnos0RevalidantesNMS = getDaos().getAlumnoDao().alumnos0sRevalidantes(true);
	alumnos0RevalidantesNS = getDaos().getAlumnoDao().alumnos0sRevalidantes(false);
    }

}

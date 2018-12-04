package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.ERROR_JSON;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import com.becasipn.util.ProgressBarManagerUtil;
import com.becasipn.util.Util;
import static com.becasipn.util.Util.CampoValidoAJAX;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class EnvioCorreosAjaxAction extends JSONAjaxAction {

    private Integer opcion;
    
    // Variable para sincronizar la barra de progreso
    private ProgressBarManagerUtil pbmu;
    private BigDecimal nivel;
    private BigDecimal unidadAcademica;
    private BigDecimal beca;
    private BigDecimal tipoBeca;
    private BigDecimal movimiento;
    private BigDecimal proceso;
    private BigDecimal periodo;
    private Integer option;
    private String alumnosL;

    public String lista() {
	setSsu();
	setPu(getDaos().getAlumnoDao().alumnosFiltros(ssu));
	List<Alumno> lista = pu.getResultados();
	for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            String nameU = "No cuenta con UA";            
            if (datosAcademicos != null && datosAcademicos.getUnidadAcademica() != null)
                nameU = datosAcademicos.getUnidadAcademica().getNombreCorto();
            
	    getJsonResult().add("[\"<div class='checkbox-nice'>" 
                    + "<input class='chck' type='checkbox' checked id='" + alumno.getId() + "' value='" + alumno.getId() + "' onclick='excluir(this.checked, this.value);' />"
                    + "<label for='" + alumno.getId() +"'> </label>"
                    + "</div>"
		    + "\", \"" + alumno.getBoleta()
		    + "\", \"" + alumno.getFullName()
		    + "\", \"" + nameU
		    + " \"]");
	}
	return SUCCESS_PAGINATED_JSON;
    }

    public String listaEsp() {
	setSsu();
	setPu(getDaos().getAlumnoDao().alumnosL(ssu, alumnosL));
	List<Alumno> lista = pu.getResultados();
	for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
	    getJsonResult().add("[\"" + alumno.getBoleta()
		    + "\", \"" + alumno.getFullName()
		    + "\", \"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
		    + " \"]");
	}
	return SUCCESS_PAGINATED_JSON;
    }

    public String carga() {
	String n = nivel.equals(BigDecimal.ZERO) ? "" : nivel.toString();        
	String u = unidadAcademica.equals(BigDecimal.ZERO) ? "" : unidadAcademica.toString();
	String aux = getDaos().getAlumnoDao().alumnosRegistradosC(n, u)
		+ "," + getDaos().getAlumnoDao().alumnosRevalidantesNoRegistradosC(n, u)
		+ "," + getDaos().getAlumnoDao().alumnosESEincompletoC(n, u)
		+ "," + getDaos().getAlumnoDao().alumnosDatosIncorrectosC(n, u, periodo);
	getJsonResult().add("[\"" + aux + " \"]");
	return SUCCESS_JSON;
    }

    
    public String sincroniza() {
        if (getPbmu() == null) {
            String result = setPbmu();
            if (result.equals(ERROR_JSON)) {
                return SUCCESS_JSON;
            }
        }
        getJsonResult().add("[\"" + getPbmu().getPbu().getPbarPercentage() + ","
                + getPbmu().getPbu().getEta() + ","
                + getPbmu().getPbu().getProcessed() + ","
                + getPbmu().getPbu().getTotal() + ","
                + getPbmu().getPbarPercentageGlobal() + ","
                + getPbmu().getEtaGlobal() + ","
                + getPbmu().getProcessedGlobal() + ","
                + getPbmu().getTotalGlobal() + ","
                + getPbmu().getProcessedSuccess() + ","
                + getPbmu().getProcessedError() + ","
                + getPbmu().getPbu().getInfo() + ","
                + getPbmu().getPbu().getNbar() + ","
                + getPbmu().getNbarTotal() + "\"]");
        return SUCCESS_JSON;
    }

    public Integer getOpcion() {
	return opcion;
    }

    public void setOpcion(Integer opcion) {
	this.opcion = opcion;
    }

    public BigDecimal getNivel() {
	return nivel;
    }

    public void setNivel(BigDecimal nivel) {
	this.nivel = nivel;
	if (CampoValidoAJAX(nivel)) {
	    ssu.parametros.put("ua.NIVEL_ID", this.nivel);
	}
    }

    public BigDecimal getUnidadAcademica() {
	return unidadAcademica;
    }

    public void setUnidadAcademica(BigDecimal unidadAcademica) {
	this.unidadAcademica = unidadAcademica;
	if (CampoValidoAJAX(unidadAcademica)) {
	    if (!checkResponsableOrFuncionario()) {
		ssu.parametros.put("ua.ID", this.unidadAcademica);
	    }
	}
    }

    public BigDecimal getBeca() {
	return beca;
    }

    public void setBeca(BigDecimal beca) {
	this.beca = beca;
	if (CampoValidoAJAX(beca)) {
	    ssu.parametros.put("tb.BECA_ID", this.beca);
	}
    }

    public BigDecimal getTipoBeca() {
	return tipoBeca;
    }

    public void setTipoBeca(BigDecimal tipoBeca) {
	this.tipoBeca = tipoBeca;
	if (CampoValidoAJAX(tipoBeca)) {
	    ssu.parametros.put("tbp.TIPOBECA_ID", this.tipoBeca);
	}
    }

    public BigDecimal getMovimiento() {
	return movimiento;
    }

    public void setMovimiento(BigDecimal movimiento) {
	this.movimiento = movimiento;
	if (CampoValidoAJAX(movimiento)) {
	    ssu.parametros.put("tp.MOVIMIENTO_ID", this.movimiento);
	}
    }

    public BigDecimal getProceso() {
	return proceso;
    }

    public void setProceso(BigDecimal proceso) {
	this.proceso = proceso;
	if (CampoValidoAJAX(proceso)) {
	    ssu.parametros.put("tp.id", this.proceso);
	}
    }

    public String getAlumnosL() {
	return alumnosL;
    }

    public void setAlumnosL(String alumnosL) {
	this.alumnosL = alumnosL;
    }

    public BigDecimal getPeriodo() {
        return periodo;
    }

    public void setPeriodo(BigDecimal periodo) {
        this.periodo = periodo;
    }

    private Boolean checkResponsableOrFuncionario() {
	BigDecimal ROLE_RESPONSABLE_UA = new BigDecimal("5");
	BigDecimal ROLE_FUNCIONARIO_UA = new BigDecimal("6");
	Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
	Set<UsuarioPrivilegio> privilegios = usuario.getPrivilegios();
	for (UsuarioPrivilegio privilegio : privilegios) {
	    if (privilegio.getPrivilegio().getId().equals(ROLE_RESPONSABLE_UA) || privilegio.getPrivilegio().getId().equals(ROLE_FUNCIONARIO_UA)) {
		PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
		ssu.parametros.put("ua.ID", personal.getUnidadAcademica().getId());
		return true;
	    }
	}
	return false;
    }
    
    public ProgressBarManagerUtil getPbmu() {
        return pbmu;
    }

    public String setPbmu() {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        this.pbmu = Util.pbmuMap.get(usuario.getId().toString()+option.toString());
        if (this.pbmu == null) {
            return ERROR_JSON;
        }
        return SUCCESS_JSON;
    }

    public Integer getOption() {
        return option;
    }

    public void setOption(Integer option) {
        this.option = option;
    }
    
    
}

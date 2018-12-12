/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 28-ago-2015, 10:32:25
 *
 */
package com.becasipn.actions.ajax;

import com.becasipn.actions.alumno.AdministraAlumnoAction;
import com.becasipn.actions.alumno.MensajesAlumno;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public class AlumnoAjaxAction extends JSONAjaxAction implements MensajesAlumno {

    private BigDecimal alumnoId;
    private String correoElectronico;
    private String numero;
    private String pss;

    public String muestraListado() {
        List<Alumno> alumnoList = getDaos().getAlumnoDao().findAll();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        for (Alumno alumno : alumnoList) {
            getJsonResult().add("[\"" + alumno.getId()
                    + "\", \"" + alumno.getFullName()
                    + "\", \"" + alumno.getBoleta()
                    + "\", \"" + alumno.getUsuario().getUsuario()
                    + "\", \"" + alumno.getCurp()
                    + "\", \"" + sdf.format(alumno.getFechaAlta())
                    + "\", \"<a class='fancybox fancybox.iframe'  href='/tickets/detalleTicket.action?alumnoId=" + alumno.getId() + "'> Ver </a>\"]");
        }
        return SUCCESS_JSON;
    }

    public String actualizaCorreoElectronico() {
        if (correoElectronico == null || correoElectronico.isEmpty()) {
            getJsonResult().add("\"Debe definir un valor en el correo electrónico, por favor, verifique.\"");
            return ERROR_JSON;
        }
        if (alumnoId == null || alumnoId.toString().isEmpty() || correoElectronico == null || correoElectronico.isEmpty()) {
            getJsonResult().add("\"No se proporcionó información completa para realizar la operación. Por favor, recargue su página e intente de nuevo.\"");
            return ERROR_JSON;
        }
        Alumno a = getDaos().getAlumnoDao().findById(alumnoId);
        if (a == null) {
            getJsonResult().add("\"No fue encontrado el alumno en base de datos. Por favor, recargue su página e intente de nuevo.\"");
            return ERROR_JSON;
        } else {
            a.setCorreoElectronico(correoElectronico);
            getDaos().getAlumnoDao().update(a);
        }

        AdministraAlumnoAction aa = new AdministraAlumnoAction();

        getJsonResult().add("\"OK\"");
        return SUCCESS_JSON;
    }

    
    public String comparar() {
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        getJsonResult().add("[\"" + pss.equals(usuario.getPassword()) + "\"]");
        return SUCCESS_JSON;
    }

    public BigDecimal getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(BigDecimal alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPss() {
        return pss;
    }

    public void setPss(String pss) {
        this.pss = pss;
    }
}

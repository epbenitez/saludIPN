package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import static com.becasipn.util.Util.CampoValidoAJAX;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EstatusSolicitudAjaxAction extends JSONAjaxAction {

    private String curp;
    private String numeroDeBoleta;
    private String nombre;
    private String apPaterno;
    private String apMaterno;

    public String listado() {
        if (ssu.parametros.isEmpty()) {
            return SUCCESS_JSON;
        }
        setSsu();
        checkResponsableOrFuncionario();
        setPu(getDaos().getAlumnoDao().solicitudAlumnos(ssu));
        List<Alumno> lista = pu.getResultados();
        String nombreCorto = "";
        for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            if (datosAcademicos != null)
                nombreCorto = datosAcademicos.getUnidadAcademica().getNombreCorto();
            getJsonResult().add("[\"" + alumno.getBoleta()
                    + "\", \"" + alumno.getFullName()
                    + "\", \"" + nombreCorto
                    + "\", \"<a title='Estatus de solicitud del alumno' class='fancybox fancybox.iframe table-link'  href='/becas/verEstatusSolicitud.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + " \"]");
        }
        return SUCCESS_PAGINATED_JSON;
    }

    private String getDetalles(Alumno alumno) {
        String res;
        Otorgamiento ot = new Otorgamiento();
        Boolean o = getDaos().getOtorgamientoDao().existeAlumnoAsignado(alumno.getId());
        if (o) {
            ot = getDaos().getOtorgamientoDao().getOtorgamientoAlumno(alumno.getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
            if (ot.getAlta()) {
//                res = "<span class='label label-success'>Alta</span>";
                res = "Alta";
            } else {
//                res = "<span class='label label-danger'>Baja</span>";
                res = "Baja";
            }
        } else {
            Boolean oa = getDaos().getOtorgamientoBajasDetalleDao().bajaPeriodoActual(alumno.getId());
            if (oa) {
//                res = "<span class='label label-danger'>Baja</span>";
                res = "Baja";
            } else {
                res = "<span class='label label-warning'>Pendiente</span>";
                res = "Pendiente";
            }
        }
        return res;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
        if (CampoValidoAJAX(numeroDeBoleta)) {
            ssu.parametros.put("a.boleta", this.numeroDeBoleta.toUpperCase());
        }
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
        if (CampoValidoAJAX(curp)) {
            ssu.parametros.put("a.curp", this.curp.toUpperCase());
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        if (CampoValidoAJAX(nombre)) {
            ssu.parametros.put("a.nombre", this.nombre.toUpperCase());
        }
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
        if (CampoValidoAJAX(apPaterno)) {
            ssu.parametros.put("a.apellidoPaterno", this.apPaterno.toUpperCase());
        }
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
        if (CampoValidoAJAX(apMaterno)) {
            ssu.parametros.put("a.apellidoMaterno", this.apMaterno.toUpperCase());
        }
    }

    private void checkResponsableOrFuncionario() {
        BigDecimal ROLE_RESPONSABLE_UA = new BigDecimal("5");
        BigDecimal ROLE_FUNCIONARIO_UA = new BigDecimal("6");
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        Set<UsuarioPrivilegio> privilegios = usuario.getPrivilegios();
        for (UsuarioPrivilegio privilegio : privilegios) {
            if (privilegio.getPrivilegio().getId().equals(ROLE_RESPONSABLE_UA) || privilegio.getPrivilegio().getId().equals(ROLE_FUNCIONARIO_UA)) {
                PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
                ssu.parametros.put("da.unidadAcademica.id", personal.getUnidadAcademica().getId());
            }
        }
    }
}

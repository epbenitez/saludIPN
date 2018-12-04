package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.business.AdministraBecaTransporteBO;
import com.becasipn.business.AdministraCuestionarioBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Carrera;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import static com.becasipn.util.Util.CampoValidoAJAX;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class PermisoCambioAjaxAction extends JSONAjaxAction {

    private String numeroDeBoleta;
    private String curp;
    private String nombre;
    private String apPaterno;
    private String apMaterno;

    private BigDecimal ua;
    private String plan;
    private String crr;

    public String listado() {
        if (ssu.parametros.isEmpty()) {
            return SUCCESS_JSON;
        }
        setSsu();
        setPu(getDaos().getAlumnoDao().solicitudAlumnos(ssu));
        List<Alumno> lista = pu.getResultados();
        String nombreCorto = "No cuenta con UA";
        for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            if (datosAcademicos != null) {
                if (datosAcademicos.getUnidadAcademica() != null) {
                    nombreCorto = datosAcademicos.getUnidadAcademica().getNombreCorto();
                }
            }

            getJsonResult().add("[\"" + alumno.getBoleta()
                    + "\", \"" + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre()
                    + "\", \"" + nombreCorto
                    + getMail(alumno)
                    + getDocumentos(alumno)
                    + getDatosDAE(alumno)
                    + "\", \"<a title='Datos Bancarios' class='fancybox fancybox.iframe table-link'  href='/tarjeta/bitacoraMonitoreoTarjetaBancaria.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-money  fa-stack-1x fa-inverse'></i></span></a>"
                    + getDetalles(alumno)
                    + getESE(alumno)
                    + getPermiteTransferencia(alumno)
                    + " \"]"
            );
        }
        return SUCCESS_PAGINATED_JSON;
    }

    private String getDetalles(Alumno alumno) {
        String res;
        Boolean o = alumno.getEstatus();
        if (o) {
            res = "\", \"<span style='color:green' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-check fa-stack-1x fa-inverse'></i></span>";
        } else {
            res = "\", \"<span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-times fa-stack-1x fa-inverse'></i></span>";
        }
        return res;
    }

    private String getPermiteTransferencia(Alumno alumno) {
        String res = "\", \"<span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-times fa-stack-1x fa-inverse'></i></span>";
        SolicitudBeca sb = getDaos().getSolicitudBecaDao().getESEAlumno(alumno.getId(), getDaos().getPeriodoDao().getPeriodoActivo().getId());
        if (sb != null) {
            if (sb.getPermiteTransferencia() != null) {
                if (sb.getPermiteTransferencia() == 1) {
                    res = "\", \"<span style='color:green' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-check fa-stack-1x fa-inverse'></i></span>";
                }
            }
        }
        return res;
    }

    private Boolean checkResponsableOrFuncionario() {
        BigDecimal ROLE_RESPONSABLE_UA = new BigDecimal("5");
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        Set<UsuarioPrivilegio> privilegios = usuario.getPrivilegios();
        for (UsuarioPrivilegio privilegio : privilegios) {
            if (privilegio.getPrivilegio().getId().equals(ROLE_RESPONSABLE_UA)) {
                return true;
            }
        }
        return false;
    }

    private String getESE(Alumno alumno) {
        String res = "\", \"";
        Periodo p = getDaos().getPeriodoDao().getPeriodoActivo();

        if (getDaos().getCuestionarioDao().findById(new BigDecimal(1)).getActivo()) {
            Boolean o = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("1"), p.getId());

            if (o) {
                res += "<a title='ESE' class='fancybox fancybox.iframe table-link'  href='/admin/eseCuestionario.action?cuestionarioId=1&alumnoId=" + alumno.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-file-text-o fa-stack-1x fa-inverse'></i></span></a>";
            } else {
                Boolean validacionInscripcion = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(getDaos().getPeriodoDao().getPeriodoActivo(), alumno.getId(), null);
                o = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("1"), p.getPeriodoAnterior().getId());
                if (o && validacionInscripcion) {
                    res += "<a title='ESE' class='fancybox fancybox.iframe table-link'  href='/admin/eseCuestionario.action?cuestionarioId=1&alumnoId=" + alumno.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-file-text-o fa-stack-1x fa-inverse'></i></span></a>";
                } else {
                    res += "<a title='ESE' onclick='ESENoActivo()' class='table-link danger'> <span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-file-text-o fa-stack-1x fa-inverse'></i></span></a>";
                }
            }
        }
        if (getDaos().getCuestionarioDao().findById(new BigDecimal(3)).getActivo()) {
            Boolean ox = getDaos().getCensoSaludDao().contestoEncuestaSalud(alumno.getId(), p.getId());
            if (ox) {
                res += "<a title='Censo Salud' class='fancybox fancybox.iframe table-link'  href='/misdatos/adminSaludCuestionarioSalud.action?alumnoId=" + alumno.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-medkit fa-stack-1x fa-inverse'></i></span></a>";
            } else {
                res += "<a title='CS' onclick='ESENoActivo()' class='table-link danger'> <span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-medkit fa-stack-1x fa-inverse'></i></span></a>";
            }
        }
        if (getDaos().getCuestionarioDao().findById(new BigDecimal(6)).getActivo()) {
            Boolean ox = getDaos().getCensoSaludDao().contestoEncuestaSalud(alumno.getId(), p.getId());
            if (ox) {
                res += "<a title='Seguimiento Becarios' class='fancybox fancybox.iframe table-link'  href='/misdatos/adminSeguimientoSeguimientoBecarios.action?alumnoId=" + alumno.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-search fa-stack-1x fa-inverse'></i></span></a>";
            } else {
                res += "<a title='Seguimiento Becarios' onclick='ESENoActivo()' class='table-link danger'> <span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-search fa-stack-1x fa-inverse'></i></span></a>";
            }
        }
        if (getDaos().getCuestionarioDao().findById(new BigDecimal(2)).getActivo()) {
            Boolean oxx = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("2"), p.getId());

            if (oxx) {
                res += "<a title='ESET' class='fancybox fancybox.iframe table-link'  href='/admin/esetCuestionario.action?cuestionarioId=2&alumnoId=" + alumno.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-bus fa-stack-1x fa-inverse'></i></span></a>";
            } else {
                Boolean validacionInscripcionESET = getDaos().getOtorgamientoDao().tieneValidacionInscripcion(p, alumno.getId(), true);
                oxx = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("2"), p.getPeriodoAnterior().getId());
                if (oxx && validacionInscripcionESET) {
                    res += "<a title='ESET' class='fancybox fancybox.iframe table-link'  href='/admin/esetCuestionario.action?cuestionarioId=2&alumnoId=" + alumno.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-bus fa-stack-1x fa-inverse'></i></span></a>";
                } else {
                    res += "<a title='ESET' onclick='ESENoActivo()' class='table-link danger'> <span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-bus fa-stack-1x fa-inverse'></i></span></a>";
                }

            }
        }
        return res;
    }

    private String getMail(Alumno alumno) {
        String res;
        if (checkResponsableOrFuncionario()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            UnidadAcademica ua = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            Boolean o = datosAcademicos.getUnidadAcademica().getId().equals(ua.getId());
            if (o) {
                res = "\", \"<a title='Cambio de correo' class='fancybox fancybox.iframe table-link'  href='/misdatos/verPermisoCambio.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-envelope-o fa-stack-1x fa-inverse'></i></span></a>";
            } else {
                res = "\", \"<a title='Cambio de correo' onclick='CorreoNoActivo()' class='table-link danger'><span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-envelope-o fa-stack-1x fa-inverse'></i></span></a>";
            }
        } else {
            res = "\", \"<a title='Cambio de correo' class='fancybox fancybox.iframe table-link'  href='/misdatos/verPermisoCambio.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-envelope-o fa-stack-1x fa-inverse'></i></span></a>";
        }
        return res;
    }

    private String getDatosDAE(Alumno alumno) {
        String res;
        if (checkResponsableOrFuncionario()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            UnidadAcademica ua = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
            DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
            Boolean o = datosAcademicos.getUnidadAcademica().getId().equals(ua.getId());
            if (o) {
                res = "\", \"<a title='Datos DAE' class='fancybox fancybox.iframe table-link'  href='/misdatos/datosDAEPermisoCambio.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-files-o fa-stack-1x fa-inverse'></i></span></a>";
            } else {
                res = "\", \"<a title='Datos DAE' onclick='CorreoNoActivo()' class='table-link danger'><span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-files-o fa-stack-1x fa-inverse'></i></span></a>";
            }
        } else {
            res = "\", \"<a title='Datos DAE' class='fancybox fancybox.iframe table-link'  href='/misdatos/datosDAEPermisoCambio.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-files-o fa-stack-1x fa-inverse'></i></span></a>";
        }
        return res;
    }

    private String getDocumentos(Alumno alumno) {
        String res;
        if (checkResponsableOrFuncionario()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            UnidadAcademica ua = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
            DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
            Boolean o = datosAcademicos.getUnidadAcademica().getId().equals(ua.getId());
            if (o) {
                res = "\", \"<a title='Validacion de documentos' class='fancybox fancybox.iframe table-link'  href='/misdatos/validacionPermisoCambio.action?alumno.boleta=" + alumno.getBoleta() + "&permisoCambio=true'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-clipboard fa-stack-1x fa-inverse'></i></span></a>";
            } else {
                res = "\", \"<a title='Validacion de documentos' onclick='CorreoNoActivo()' class='table-link danger'><span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-clipboard fa-stack-1x fa-inverse'></i></span></a>";
            }
        } else {
            res = "\", \"<a title='Validacion de documentos' class='fancybox fancybox.iframe table-link'  href='/misdatos/validacionPermisoCambio.action?alumno.boleta=" + alumno.getBoleta() + "&permisoCambio=true'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-clipboard fa-stack-1x fa-inverse'></i></span></a>";
        }
        return res;
    }

    public String getPlanE() {
        List<String> planes = getDaos().getCarreraDao().findPlan(ua);
        for (String pl : planes) {
            getJsonResult().add("[\"" + pl
                    + "\", \"" + pl + "\"]");
        }
        return SUCCESS_JSON;
    }

    public String getCarrera() {
        List<Carrera> planes = getDaos().getCarreraDao().findCarrera(ua, plan);
        for (Carrera pl : planes) {
            getJsonResult().add("[\"" + pl.getClaveCarrera()
                    + "\", \"" + pl.getCarrera() + "\"]");
        }
        return SUCCESS_JSON;
    }

    public String getEspecialidad() {
        List<String> planes = getDaos().getCarreraDao().findEspecialidad(ua, plan, crr);
        for (String pl : planes) {
            getJsonResult().add("[\"" + pl
                    + "\", \"" + pl + "\"]");
        }
        return SUCCESS_JSON;
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

    public BigDecimal getUa() {
        return ua;
    }

    public void setUa(BigDecimal ua) {
        this.ua = ua;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getCrr() {
        return crr;
    }

    public void setCrr(String crr) {
        this.crr = crr;
    }

}

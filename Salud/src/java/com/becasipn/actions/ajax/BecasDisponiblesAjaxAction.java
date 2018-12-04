package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.business.PeriodoBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.OtorgamientoBajasDetalle;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.UsuarioPrivilegio;
import com.becasipn.persistence.model.VWConteoDepositos;
import static com.becasipn.util.Util.CampoValidoAJAX;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Victor Lozano
 * @date 4/07/2016
 */
public class BecasDisponiblesAjaxAction extends JSONAjaxAction {

    private String curp;
    private String numeroDeBoleta;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private Boolean verOtorgamiento = Boolean.FALSE;
    private BigDecimal otorgamientoId;

    /**
     * Lista los alumnos que cumplen con los criterios de busqueda
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String listado() {
        if (ssu.parametros.isEmpty()) {
            return SUCCESS_JSON;
        }
        setSsu();
        setPu(getDaos().getAlumnoDao().busquedaAlumnos(ssu));
        List<Alumno> lista = getPu().getResultados();
        String nombreCorto = "No cuenta con UA";
        for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            if (datosAcademicos != null) {
                nombreCorto = datosAcademicos.getUnidadAcademica().getNombreCorto();
            }
            getJsonResult().add("[\"" + alumno.getBoleta()
                    + "\", \"" + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre()
                    + "\", \"" + nombreCorto
                    + "\", \"<a title='Becas disponibles' class='fancybox fancybox.iframe table-link'  href='/becas/verAlumnoBecasDisponibles.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + " \"]");
        }
        return SUCCESS_PAGINATED_JSON;
    }

    public String lista() {
        if (ssu.parametros.isEmpty()) {
            return SUCCESS_JSON;
        }
        setSsu();
        setPu(getDaos().getAlumnoDao().busquedaAlumnos(ssu));
        List<Alumno> lista = getPu().getResultados();
        for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            if (datosAcademicos != null) {
                getJsonResult().add("[\"" + alumno.getBoleta()
                        + "\", \"" + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre()
                        + "\", \"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
                        + detalle(alumno)
                        + " \"]");
            } else {
                getJsonResult().add("[\"" + alumno.getBoleta()
                        + "\", \"" + alumno.getNombre() + " " + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno()
                        + "\", \"" + "Sin unidad académica"
                        + detalle(alumno)
                        + " \"]");
            }

        }
        return SUCCESS_PAGINATED_JSON;
    }

    public String list() {
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
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Alumno alumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta).get(0);
        List<Otorgamiento> list = getDaos().getOtorgamientoDao().getOtorgamientosAlumno(alumno.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        VWConteoDepositos conteoDepositos;
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        Boolean mostrarOtorgamiento = Boolean.TRUE;
        Boolean mostrarBaja = Boolean.TRUE;
        if (list != null) {
            for (Otorgamiento o : list) {
                if (isAlumno()) {
                    if (o.getPeriodo().getId().equals(periodo.getId())) {
                        if (verOtorgamiento) {
                            mostrarOtorgamiento = getDaos().getOtorgamientoDao().mostrarOtorgamiento(alumno.getId());
                        } else {
                            mostrarOtorgamiento = Boolean.FALSE;
                        }
                    } else {
                        mostrarOtorgamiento = Boolean.TRUE;
                    }
                }
                if (mostrarOtorgamiento) {
                    if (o.getAlta()) {
                        Integer semestre = 0;
                        Double promedio = 0.0;
                        if (o.getDatosAcademicos() != null) {
                            semestre = o.getDatosAcademicos().getSemestre();
                            promedio = o.getDatosAcademicos().getPromedio();
                        }
                        conteoDepositos = getDaos().getVwConteoDepositosDao().depositosOtorgamiento(o.getId());
                        getJsonResult().add("["
                                + "\"" + (isAlumno() == false ? "<a title='Detalle de movimientos del otorgamiento' class='fancybox fancybox.iframe'  href='/misdatos/detalleOtorgamientoHistorialBecas.action?otorgamientoId=" + o.getId() + "'> <span class='fa-stack'>" : "")
                                + o.getPeriodo().getClave()
                                + (isAlumno() == false ? "</span></a>" : "")
                                + "\", \"" + (o.getProceso() == null ? "-" : o.getProceso().getUnidadAcademica().getNombreCorto())
                                + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                                + "\", \"" + "<a class='table-link' data-toggle='tooltip' data-placement='up' title='Estatus'><span class='label label-success'>Alta</span></a>"
                                + "\", \"" + (o.getProceso() == null ? "-" : o.getProceso().getTipoProceso().getMovimiento().getNombre())
                                + "\", \"" + (o.getProceso() == null ? "-" : o.getProceso().getTipoProceso().getNombre())
                                + "\", \"" + (conteoDepositos == null
                                        ? "" : "<a title='Consulta de depositos' class='fancybox fancybox.iframe' href='/depositos/formAdministraDepositos.action?numeroDeBoleta=" + o.getAlumno().getBoleta() + "&idOtorgamiento=" + o.getId() + "'> <span class='fa-stack'>" + conteoDepositos.getConteoDepositos() + "</span></a>")
                                //+ "\", \"" + (conteoDepositos == null
                                //		? "" : conteoDepositos.getConteoDepositos() == 0 ? "<a title='Sin depositos' class='fancybox fancybox.iframe'> <span class='fa-stack'>" + conteoDepositos.getConteoDepositos() + "</span></a>"
                                //				: "<a title='Consulta de depositos' class='fancybox fancybox.iframe' href='/depositos/formAdministraDepositos.action?numeroDeBoleta=" + o.getAlumno().getBoleta() + "&idOtorgamiento=" + o.getId() + "'> <span class='fa-stack'>" + conteoDepositos.getConteoDepositos() + "</span></a>")
                                + "\", \"" + (o.getFecha() == null ? "" : sdf.format(o.getFecha()))
                                + "\", \"" + semestre
                                + "\", \"" + promedio
                                + (isAlumno() ? "" : "\", \"" + (o.getExcluirDeposito() ? "<b>Si</b> " : "<b>No</b>") + (o.getIdentificadorOtorgamiento() == null ? "" : "/<br>" + o.getIdentificadorOtorgamiento().getNombre()))
                                + "\"]");
                    } else {
                        OtorgamientoBajasDetalle obd = getDaos().getOtorgamientoBajasDetalleDao().getByOtorgamientoId(o.getId());
                        if (isAlumno()) {
                            if (verOtorgamiento) {
                                mostrarBaja = getDaos().getOtorgamientoBajasDetalleDao().mostrarBaja(o.getId());
                            } else {
                                mostrarBaja = Boolean.FALSE;
                            }
                        }
                        if (obd == null) {
                            getJsonResult().add("["
                                    + "\"" + (isAlumno() == false ? "<a title='Detalle de movimientos del otorgamiento' class='fancybox fancybox.iframe'  href='/misdatos/detalleOtorgamientoHistorialBecas.action?otorgamientoId=" + o.getId() + "'> <span class='fa-stack'>" : "")
                                    + o.getPeriodo().getClave()
                                    + (isAlumno() == false ? "</span></a>" : "") + "\", \"" + o.getProceso().getUnidadAcademica().getNombreCorto()
                                    + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                                    + "\", \"" + "<a class='table-link' data-toggle='tooltip' data-placement='up' title='Estatus'><span class='label label-danger'>Baja</span></a>"
                                    + "\", \"" + "Sin detalle"
                                    + "\", \"" + ""
                                    + "\", \"" + ""
                                    + "\", \"" + ""
                                    + "\", \"" + ""
                                    + "\", \"" + ""
                                    + "\", \"" + ""
                                    + "\"]");
                        } else {
                            if (mostrarBaja) {
                                if (o.getPeriodo().getId().equals(obd.getPeriodo().getId())) {
                                    conteoDepositos = getDaos().getVwConteoDepositosDao().depositosOtorgamiento(o.getId());
                                } else {
                                    conteoDepositos = new VWConteoDepositos();
                                    conteoDepositos.setConteoDepositos(0);
                                }
                                getJsonResult().add("["
                                        + "\"" + (isAlumno() == false ? "<a title='Detalle de movimientos del otorgamiento' class='fancybox fancybox.iframe'  href='/misdatos/detalleOtorgamientoHistorialBecas.action?otorgamientoId=" + o.getId() + "'> <span class='fa-stack'>" : "")
                                        + obd.getPeriodo().getClave()
                                        + (isAlumno() == false ? "</span></a>" : "") + "\", \"" + o.getProceso().getUnidadAcademica().getNombreCorto()
                                        + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                                        + "\", \"" + "<a class='table-link' data-toggle='tooltip' data-placement='up' title='Estatus'><span class='label label-danger'>Baja</span></a>"
                                        + "\", \"" + (obd.getProceso() == null ? "No especificado" : obd.getProceso().getTipoProceso().getMovimiento().getNombre())
                                        + (obd.getTipoBajasDetalle() == null ? "" : " / " + obd.getTipoBajasDetalle().getNombre())
                                        + "\", \"" + (obd.getProceso() == null ? "No especificado" : obd.getProceso().getTipoProceso().getNombre())
                                        + "\", \"" + (conteoDepositos == null
                                                ? "" : "<a title='Consulta de depositos' class='fancybox fancybox.iframe' href='/depositos/formAdministraDepositos.action?numeroDeBoleta=" + o.getAlumno().getBoleta() + "&idOtorgamiento=" + o.getId() + "'> <span class='fa-stack'>" + conteoDepositos.getConteoDepositos() + "</span></a>")
                                        // + "\", \"" + (conteoDepositos == null
                                        //		? "" : conteoDepositos.getConteoDepositos() == 0 ? "<a title='Sin depositos' class='fancybox fancybox.iframe'> <span class='fa-stack'>" + conteoDepositos.getConteoDepositos() + "</span></a>"
                                        //				: "<a title='Consulta de depositos' class='fancybox fancybox.iframe' href='/depositos/formAdministraDepositos.action?numeroDeBoleta=" + o.getAlumno().getBoleta() + "&idOtorgamiento=" + o.getId() + "'> <span class='fa-stack'>" + conteoDepositos.getConteoDepositos() + "</span></a>")

                                        + "\", \"" + (obd.getFechaBaja() == null ? "" : sdf.format(obd.getFechaBaja()))
                                        + "\", \"" + ""
                                        + "\", \"" + ""
                                        + "\", \"" + ""
                                        + "\"]");
                            }
                        }
                        if (obd==null || !o.getPeriodo().getId().equals(obd.getPeriodo().getId())) {
                            conteoDepositos = getDaos().getVwConteoDepositosDao().depositosOtorgamiento(o.getId());
                            getJsonResult().add("["
                                    + "\"" + (isAlumno() == false ? "<a title='Detalle de movimientos del otorgamiento' class='fancybox fancybox.iframe'  href='/misdatos/detalleOtorgamientoHistorialBecas.action?otorgamientoId=" + o.getId() + "'> <span class='fa-stack'>" : "")
                                    + o.getPeriodo().getClave()
                                    + (isAlumno() == false ? "</span></a>" : "") + "\", \"" + o.getProceso().getUnidadAcademica().getNombreCorto()
                                    + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                                    + "\", \"" + "<a class='table-link' data-toggle='tooltip' data-placement='up' title='Estatus'><span class='label label-success'>Alta</span></a>"
                                    + "\", \"" + o.getProceso().getTipoProceso().getMovimiento().getNombre()
                                    + "\", \"" + o.getProceso().getTipoProceso().getNombre()
                                    + "\", \"" + (conteoDepositos == null
                                            ? "" : "<a title='Consulta de depositos' class='fancybox fancybox.iframe' href='/depositos/formAdministraDepositos.action?numeroDeBoleta=" + o.getAlumno().getBoleta() + "&idOtorgamiento=" + o.getId() + "'> <span class='fa-stack'>" + conteoDepositos.getConteoDepositos() + "</span></a>")
                                    //+ "\", \"" + (conteoDepositos == null
                                    //		? "" : conteoDepositos.getConteoDepositos() == 0 ? "<a title='Sin depositos' class='fancybox fancybox.iframe'> <span class='fa-stack'>" + conteoDepositos.getConteoDepositos() + "</span></a>"
                                    //				: "<a title='Consulta de depositos' class='fancybox fancybox.iframe' href='/depositos/formAdministraDepositos.action?numeroDeBoleta=" + o.getAlumno().getBoleta() + "&idOtorgamiento=" + o.getId() + "'> <span class='fa-stack'>" + conteoDepositos.getConteoDepositos() + "</span></a>")

                                    + "\", \"" + (o.getFecha() == null ? "" : sdf.format(o.getFecha()))
                                    + "\", \"" + o.getDatosAcademicos().getSemestre()
                                    + "\", \"" + o.getDatosAcademicos().getPromedio()
                                    + (isAlumno() ? "" : "\", \"" + (o.getExcluirDeposito() ? "<b>Si</b> " : "<b>No</b>") + (o.getIdentificadorOtorgamiento() == null ? "" : "/<br>" + o.getIdentificadorOtorgamiento().getNombre()))
                                    + "\"]");
                        }

                    }
                }
            }
        }
        return SUCCESS_JSON;
    }

    public String histo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy / hh:mm aaa");
        Otorgamiento o = getDaos().getOtorgamientoDao().findById(otorgamientoId);

        getJsonResult().add("["
                + "\"" + "<a class='table-link' data-toggle='tooltip' data-placement='up' title='Estatus'><span class='label label-success'>Alta</span></a>"
                + "\", \"" + (o.getPeriodo().getClave())
                + "\", \"" + (o.getProceso() == null ? "-" : o.getProceso().getTipoProceso().getMovimiento().getNombre())
                + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                + "\", \"" + (o.getProceso() == null ? "-" : o.getProceso().getUnidadAcademica().getNombreCorto())
                + "\", \"" + (o.getProceso() == null ? "-" : o.getProceso().getTipoProceso().getNombre())
                + "\", \"" + (o.getFecha() == null ? "" : sdf.format(o.getFecha()))
                + "\"]");

        List<OtorgamientoBajasDetalle> list = getDaos().getOtorgamientoBajasDetalleDao().getByOtorgamientoIdL(otorgamientoId);
        if (list != null) {
            for (OtorgamientoBajasDetalle obd : list) {
                String lbl = obd.getMovimiento() != null ? "Baja" : "Reversion";
                getJsonResult().add("["
                        + "\"" + "<a class='table-link' data-toggle='tooltip' data-placement='up' title='Estatus'><span class='label label-danger'>" + lbl + "</span></a>"
                        + "\", \"" + (obd.getPeriodo().getClave())
                        + "\", \"" + (obd.getMovimiento() == null ? "-" : obd.getMovimiento().getNombre())
                        + "\", \"" + obd.getOtorgamiento().getTipoBecaPeriodo().getTipoBeca().getNombre()
                        + "\", \"" + (obd.getProceso() == null ? "-" : obd.getProceso().getUnidadAcademica().getNombreCorto())
                        + "\", \"" + (obd.getProceso() == null ? "-" : obd.getProceso().getTipoProceso().getNombre())
                        + "\", \"" + (obd.getFechaBaja() == null ? "" : sdf.format(obd.getFechaBaja()))
                        + "\"]");
            }
        }
        return SUCCESS_JSON;
    }

    private String detalle(Alumno alumno) {
        String res;
        if (checkResponsableOrFuncionario()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            UnidadAcademica ua = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId()).getUnidadAcademica();
            DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
            Boolean o = datosAcademicos.getUnidadAcademica().getId().equals(ua.getId());
            if (o) {
                res = "\", \"<a title='Historial' class='fancybox fancybox.iframe table-link'  href='/misdatos/detalleHistorialBecas.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>";
            } else {
                res = "\", \"<a title='Historial' onclick='noCoincide()' class='table-link danger'><span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>";
            }
        } else {
            res = "\", \"<a title='Historial' class='fancybox fancybox.iframe table-link'  href='/misdatos/detalleHistorialBecas.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>";
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

    public Boolean getVerOtorgamiento() {
        return verOtorgamiento;
    }

    public void setVerOtorgamiento(Boolean verOtorgamiento) {
        this.verOtorgamiento = verOtorgamiento;
    }

    public BigDecimal getOtorgamientoId() {
        return otorgamientoId;
    }

    public void setOtorgamientoId(BigDecimal otorgamientoId) {
        this.otorgamientoId = otorgamientoId;
    }

}

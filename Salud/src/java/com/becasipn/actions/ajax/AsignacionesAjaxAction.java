package com.becasipn.actions.ajax;

import com.becasipn.business.OtorgamientoBO;
import com.becasipn.business.TipoBecaPeriodoBO;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.MotivoRechazoSolicitud;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.ProgressBarManagerUtil;
import com.becasipn.util.Util;
import static com.becasipn.util.Util.CampoValidoAJAX;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AsignacionesAjaxAction extends JSONAjaxAction {

    protected Logger log = LogManager.getLogger(this.getClass().getName());
    private int tab;
    private String scIDs;
    private Proceso proceso;
    private int tipoproceso;
    //Parámetros de búsqueda
    private String search_boleta;
    private int search_documentacion;
    private int antecedente;
    private int preasignado;
    private BigDecimal unidadAcademicaId;
    private String tipoBeca;
    //Variable para sincronizar la barra de progreso
    private ProgressBarManagerUtil pbmu;
    private Integer sinc_nivel;
    private Integer sinc_tipomov;
    private Integer activo;
    private String alumnos;
    private String solicitudes;
    private BigDecimal motivoRechazo;
    private String alumnoId;
    private String solicitudId;
    private String programaBecaId;

    /**
     * Devuelve el listado "default" de alumnos suceptibles a un otorgamiento
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String listado() {
        setSsu();
        setParams();
        setPu(getDaos().getAlumnoDao().findAsignaciones(tipoproceso, tab, ssu));
        List<Object[]> lista = getPu().getResultados();
        StringBuilder sb;

        for (Object[] solicitud : lista) {
            if (tipoproceso == 1 && tab==0) {
                //Si el proceso es de tipo Transferencia y la pestaña es Candidatos, no debe mostrar el check masivo
                sb = new StringBuilder("[\"");
            } else {
                sb = new StringBuilder("[\"<input class='alumno' value='");
                sb.append(solicitud[1]).append("' type='checkbox' ")
                        .append("' id='checkbox")
                        .append(solicitud[1])
                        .append("'")
                        .append(" scid='")
                        .append(solicitud[0])
                        .append("' ")
                        //if(tab == 0)
                        //    sb.append(solicitud[13] == null ? "data-toggle='popover' title='Nota' data-placement='left' data-trigger='hover' data-content='El alumno no tiene una preasignación.' disabled" : "");
                        .append("/>");
            }
            sb.append("\",\"<a title='Estatus de solicitud del alumno' class='fancybox fancybox.iframe table-link ")
                    .append("tab").append(tab)
                    //.append("'  href='/becas/formAsignaciones.action?")
                    .append("'  href='/becas/verEstatusSolicitud.action?numeroDeBoleta=")
                    .append(solicitud[2])
                    .append("'>")
                    .append(solicitud[2])
                    .append("</a>")
                    .append("\", \"").append(solicitud[3])
                    .append("\", \"").append(solicitud[4] == null ? "N/A" : solicitud[4])
                    .append("\", \"").append(solicitud[5])
                    .append("\", \"").append(solicitud[6])
                    .append("\", \"").append((solicitud[7] != null && solicitud[7].equals(BigDecimal.ONE)) ? "Si" : "No")
                    .append("\", \"").append((solicitud[8] != null && solicitud[8].equals(BigDecimal.ONE)) ? "Si" : "No")
                    .append("\", \"").append((solicitud[16] == null ) ? "-" : solicitud[16].toString())
                    .append("\", \"").append(solicitud[9].equals(BigDecimal.ONE) ? "Si" : "No");
            if (tab == 0) {
                sb.append("\", \"").append(solicitud[12].equals(BigDecimal.ONE) ? "Si" : "No");
            }
            sb.append("\", \"").append(solicitud[11])
                    .append("\", \"").append(solicitud[14] == null ? "N/A" : solicitud[14])
                    .append("\", \"").append("<a onclick='addProceso(this)' title='Detalle del alumno' class='fancybox fancybox.iframe ")
                    .append("tab").append(tab).append("'  href='/becas/formAsignaciones.action?")
                    .append("solicitudBeca.id=")
                    .append(solicitud[0])
                    .append("&alumno.boleta=")
                    .append(solicitud[2]).append("'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-cogs  fa-stack-1x fa-inverse'></i></span></a>\"]");
            getJsonResult().add(sb.toString());
        }
        return SUCCESS_PAGINATED_JSON;
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

    public String rechazarMasivo() {
        if (proceso == null) {
            getJsonResult().add("{\"error\": \"No se ha recibido proceso\"}");
            return SUCCESS_JSON;
        } else {
            proceso = getDaos().getProcesoDao().findById(proceso.getId());
        }

        if (solicitudes.equals("") || solicitudes == null) {
            getJsonResult().add("{\"error\": \"Debes seleccionar al menos un alumno para rechazar masivamente.\"}");
            return SUCCESS_JSON;
        }

        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        List<String> solicitudesString = Arrays.asList(solicitudes.split(","));
        MotivoRechazoSolicitud motivo = getDaos().getMotivoRechazoSolicitudDao().findById(motivoRechazo);

        OtorgamientoBO otorgamientoBO = new OtorgamientoBO(getDaos());
        otorgamientoBO.rechazoMasivo(proceso, solicitudesString, motivo, usuario);

        getJsonResult().add("{\"bien\":" + otorgamientoBO.getAlumnosOtorgados().size() + "}");
        getJsonResult().add("{\"mal\":" + otorgamientoBO.getAlumnosConError().size() + "}");
        getJsonResult().addAll(otorgamientoBO.getAlumnosConError());

        return SUCCESS_JSON;
    }

    public String esperaMasivo() {
        if (proceso == null) {
            getJsonResult().add("{\"error\": \"No se ha recibido proceso\"}");
            return SUCCESS_JSON;
        } else {
            proceso = getDaos().getProcesoDao().findById(proceso.getId());
        }

        if (solicitudes.equals("") || solicitudes == null) {
            getJsonResult().add("{\"error\": \"Debes seleccionar al menos un alumno para enviar a la lista de espera masivamente.\"}");
            return SUCCESS_JSON;
        }

        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        List<String> solicitudesString = Arrays.asList(solicitudes.split(","));

        OtorgamientoBO otorgamientoBO = new OtorgamientoBO(getDaos());
        otorgamientoBO.esperaMasivo(proceso, solicitudesString, usuario);

        getJsonResult().add("{\"bien\":" + otorgamientoBO.getAlumnosOtorgados().size() + "}");
        getJsonResult().add("{\"mal\":" + otorgamientoBO.getAlumnosConError().size() + "}");
        getJsonResult().addAll(otorgamientoBO.getAlumnosConError());

        return SUCCESS_JSON;
    }

    public String asignarMasivo() {
        if (proceso == null) {
            getJsonResult().add("{\"error\": \"No se ha recibido proceso\"}");
            return SUCCESS_JSON;
        } else {
            proceso = getDaos().getProcesoDao().findById(proceso.getId());
        }

        if (solicitudes.equals("") || solicitudes == null) {
            getJsonResult().add("{\"error\": \"Debes seleccionar al menos un alumno para asignar masivamente.\"}");
            return SUCCESS_JSON;
        }

        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        List<String> vectorAlumnos = Arrays.asList(solicitudes.split(","));

        OtorgamientoBO otorgamientoBO = new OtorgamientoBO(getDaos());
        otorgamientoBO.asignacionMasiva(proceso, vectorAlumnos, usuario);

        getJsonResult().add("{\"bien\":" + otorgamientoBO.getAlumnosOtorgados().size() + "}");
        getJsonResult().add("{\"mal\":" + otorgamientoBO.getAlumnosConError().size() + "}");
        getJsonResult().addAll(otorgamientoBO.getAlumnosConError());

        return SUCCESS_JSON;
    }

    public String revertirMasivo() {

        if (alumnos.equals("") || alumnos == null) {
            getJsonResult().add("{\"error\": \"Debes seleccionar al menos un alumno para revertir masivamente.\"}");
            return SUCCESS_JSON;
        }

        List<String> alumnosId = Arrays.asList(alumnos.split(","));
        List<String> solicitudesIds = Arrays.asList(scIDs.split(","));

        OtorgamientoBO otorgamientoBO = new OtorgamientoBO(getDaos());
        otorgamientoBO.reversionMasiva(alumnosId, solicitudesIds, tab, proceso);

        getJsonResult().add("{\"bien\":" + otorgamientoBO.getAlumnosOtorgados().size() + "}");
        getJsonResult().add("{\"mal\":" + otorgamientoBO.getAlumnosConError().size() + "}");
        getJsonResult().addAll(otorgamientoBO.getAlumnosConError());

        return SUCCESS_JSON;
    }

    public String getBecasPorNivel() {
        UnidadAcademica unidadAcademica = getDaos().getUnidadAcademicaDao().findById(unidadAcademicaId);
        List<TipoBecaPeriodo> becasPeriodo = getDaos().getTipoBecaPeriodoDao().becasPorNivelPeriodo(unidadAcademica.getNivel().getId(),
                getDaos().getPeriodoDao().getPeriodoActivo().getPeriodoAnterior().getId());
        Collections.sort(becasPeriodo);

        if (becasPeriodo == null) {
        } else {
            for (TipoBecaPeriodo tbp : becasPeriodo) {
                getJsonResult().add("[\"" + tbp.getTipoBeca().getId()
                        + "\", \"" + tbp.getTipoBeca().getNombre()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }
    
    public String getRequisitos() {
        StringBuilder sb = new StringBuilder();
        
        TipoBecaPeriodoBO tbpBO = TipoBecaPeriodoBO.getInstance(getDaos(), alumnoId, solicitudId, programaBecaId);
        getJsonResult().add(tbpBO.getRequisitos().toJson().toString());
        
        return SUCCESS_JSON;
    }

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public int getTipoproceso() {
        return tipoproceso;
    }

    public void setTipoproceso(int tipoproceso) {
        this.tipoproceso = tipoproceso;
    }

    public String getSearch_boleta() {
        return search_boleta;
    }

    public void setSearch_boleta(String search_boleta) {
        this.search_boleta = search_boleta;
    }

    public int getSearch_documentacion() {
        return search_documentacion;
    }

    public void setSearch_documentacion(int search_documentacion) {
        this.search_documentacion = search_documentacion;
    }

    public BigDecimal getUnidadAcademicaId() {
        return unidadAcademicaId;
    }

    public void setUnidadAcademicaId(BigDecimal unidadAcademicaId) {
        if (!isResponsable()) {
            this.unidadAcademicaId = unidadAcademicaId;
        } else {

            Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo pa = getDaos().getPersonalAdministrativoDao().findByUsuario(u.getId());
            this.unidadAcademicaId = pa.getUnidadAcademica().getId();
        }
        if (CampoValidoAJAX(this.unidadAcademicaId)) {
            ssu.parametrosServidor.put("UNIDADACADEMICA", this.unidadAcademicaId);
        }
    }

    public ProgressBarManagerUtil getPbmu() {
        return pbmu;
    }

    public String setPbmu() {
        this.pbmu = Util.pbmuMap.get(Util.getNivel(getSinc_tipomov(), getSinc_nivel()));
        if (this.pbmu == null) {
            return ERROR_JSON;
        }
        return SUCCESS_JSON;
    }

    public Integer getSinc_nivel() {
        return sinc_nivel;
    }

    public void setSinc_nivel(Integer sinc_nivel) {
        this.sinc_nivel = sinc_nivel;
    }

    public Integer getSinc_tipomov() {
        return sinc_tipomov;
    }

    public void setSinc_tipomov(Integer sinc_tipomov) {
        this.sinc_tipomov = sinc_tipomov;
    }

    public String getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    private void setParams() {
        if (CampoValidoAJAX(this.tipoBeca)) {
            ssu.parametros.put("TIPOBECA", this.tipoBeca);
        }

        if (CampoValidoAJAX(this.search_boleta)) {
            ssu.parametros.put("ALUMNOBOLETA", this.search_boleta);
        }

        if (CampoValidoAJAX(this.preasignado)) {
            ssu.parametros.put("PREASIGNADO", this.preasignado);
        }

        if (CampoValidoAJAX(this.antecedente)) {
            ssu.parametros.put("ANTECEDENTE", this.antecedente);
        }

        if (CampoValidoAJAX(this.proceso)) {
            ssu.parametrosServidor.put("PROCESO", this.proceso.getId());
        }

        switch (this.search_documentacion) {
            case 1:
                ssu.parametros.put("DOCUMENTOSCOMPLETOS", Boolean.TRUE);
                break;
            case 2:
                ssu.parametros.put("DOCUMENTOSCOMPLETOS", Boolean.FALSE);
                break;
            default:
                ssu.parametros.put("DOCUMENTOSCOMPLETOS", null);
                break;
        }
        if (ssu.getSortCol() == 0) {
            ssu.setSortCol(1);
        }
        if (ssu.getSortDir() == null) {
            ssu.setSortDir("asc");
        }
        setUnidadAcademicaId(unidadAcademicaId);
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(String alumnos) {
        this.alumnos = alumnos;
    }

    public String getScIDs() {
        return scIDs;
    }

    public void setScIDs(String scIDs) {
        this.scIDs = scIDs;
    }

    public int getAntecedente() {
        return antecedente;
    }

    public void setAntecedente(int antecedente) {
        this.antecedente = antecedente;
    }

    public int getPreasignado() {
        return preasignado;
    }

    public void setPreasignado(int preasignado) {
        this.preasignado = preasignado;
    }

    public String getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(String solicitudes) {
        this.solicitudes = solicitudes;
    }

    public BigDecimal getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(BigDecimal motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public void setAlumnoId(String alumnoId) {
        this.alumnoId = alumnoId;
    }

    public void setSolicitudId(String solicitudId) {
        this.solicitudId = solicitudId;
    }

    public void setProgramaBecaId(String programaBecaId) {
        this.programaBecaId = programaBecaId;
    }
}

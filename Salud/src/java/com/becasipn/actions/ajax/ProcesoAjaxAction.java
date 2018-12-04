package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import static com.becasipn.util.Util.CampoValidoAJAX;
import com.becasipn.business.ProcesoBO;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.ProcesoEstatus;
import com.becasipn.util.UtilFile;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ProcesoAjaxAction extends JSONAjaxAction {

    private BigDecimal id_proceso;
    private BigDecimal id_estatus;
    // Filtros
    private int search_id_periodo;
    private int search_id_proceso;
    private int search_id_ua;
    private int search_id_estatus;

    private int periodoId;
    private int uaId;
    private int movimientoId;
    private int estatusId;

    private BigDecimal unidadAcademica;
    private String periodo;
    private String movimiento;
    private String ua;

    /**
     * Devuelve el listado de procesos registrados en el sistema
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String listado() {
        setSsu();
        setPu(getDaos().getProcesoDao().findAllProcesos(ssu));
        List<Proceso> lista = getPu().getResultados();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        for (Proceso proceso : lista) {
            getJsonResult().add("[\"" + proceso.getUnidadAcademica().getNombreCorto()
                    + "\", \"" + proceso.getPeriodo().getClave()
                    + "\", \"" + proceso.getTipoProceso().getNombre()
                    + "\", \"" + (proceso.getFechaInicial() == null ? "" : formato.format(proceso.getFechaInicial()))
                    + "\", \"" + (proceso.getFechaFinal() == null ? "" : proceso.getFechaFinal().before(new Date()) ? "<span class='label label-danger'>" + formato.format(proceso.getFechaFinal()) + "</span>" : formato.format(proceso.getFechaFinal()))
                    + "\", \"<span class=" + (proceso.getProcesoEstatus().getId().compareTo(new BigDecimal(1)) == 0 ? "'label label-primary'>" : "'label label-info'>") + proceso.getProcesoEstatus().getNombre() + "</span>"
                    + "\", \"<a class='fancybox fancybox.iframe'  title='Bitácora del proceso' href='/admin/bitacoraProceso.action?proceso.id=" + proceso.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-calendar fa-stack-1x fa-inverse'></i> </a>"
                    + "\", \"" + (isAnalista() || isJefe()
                            ? "<a class='fancybox fancybox.iframe'  title='Editar proceso' href='/admin/edicionProceso.action?proceso.id=" + proceso.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i> </a>"
                            : "<a title='Operacion no permitida' class='table-link danger'><span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>")
                    + "\", \"" + (isAnalista() || isJefe()
                            ? "<a class='table-link danger' href='#' title='Eliminar proceso' onclick='eliminar(" + proceso.getId() + ")'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i> </a>"
                            : "<a title='Operacion no permitida' class='table-link danger'><span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i></span></a>")
                    + "\"]");
        }
        return SUCCESS_PAGINATED_JSON;
    }

    public String grafica() {
        List<BigDecimal> estatusList = getDaos().getProcesoDao().estatusProcesos(periodoId, uaId, movimientoId, estatusId);

        for (BigDecimal estatus : estatusList) {
            getJsonResult().add("[\"" + estatus.toString()
                    + "\"]");
        }

        return SUCCESS_JSON;
    }

    public String listadoValidacion() {
        if (ssu.parametros.isEmpty()) {
            return SUCCESS_JSON;
        }
        setSsu();
        setPu(getDaos().getProcesoDao().validacionProcesosList(ssu, search_id_periodo, search_id_ua, search_id_proceso, search_id_estatus));
        List<Proceso> lista = pu.getResultados();
        ProcesoBO pBO = new ProcesoBO(getDaos());
        Periodo currentTerm = getDaos().getPeriodoDao().getPeriodoActivo();
        for (Proceso proceso : lista) {
            getJsonResult().add("[\"" + proceso.getUnidadAcademica().getNombreCorto()
                    + "\", \"" + proceso.getTipoProceso().getNombre()
                    + "\", \"" + (proceso.getFechaInicial() == null ? "" : UtilFile.dateToString(proceso.getFechaInicial(), "dd/MM/yyyy"))
                    + "\", \"" + (proceso.getFechaFinal() == null ? "" : proceso.getFechaFinal().before(new Date()) ? "<span class='label label-danger'>" + (UtilFile.dateToString(proceso.getFechaFinal(), "dd/MM/yyyy")) + "</span>" : (UtilFile.dateToString(proceso.getFechaFinal(), "dd/MM/yyyy")))
                    + pBO.creaCheckboxes(proceso.getProcesoEstatus().getId(), proceso.getId())
                    + "\", \"<a title='Bitácora del proceso' class='fancybox fancybox.iframe'  href='/admin/bitacoraProceso.action?proceso.id=" + proceso.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-tasks fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"" + (isAnalista() || isJefe()
                            ? "<a class='solo-lectura fancybox fancybox.iframe'  title='Editar proceso' href='/admin/edicionProceso.action?proceso.id=" + proceso.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i> </a>"
                            : "<a title='Operacion no permitida' class='table-link danger'><span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>")
                    + "\", \"" + (isAnalista() || isJefe()
                            ? "<a class='solo-lectura table-link danger' href='#' title='Eliminar proceso' onclick='eliminar(" + proceso.getId() + ")'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i> </a>"
                            : "<a title='Operacion no permitida' class='table-link danger'><span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i></span></a>")
                    + "\", \"" + addPrint(proceso.getProcesoEstatus().getId(), proceso, currentTerm) + ""
                    + "\"]");
        }
        return SUCCESS_PAGINATED_JSON;
    }

    public String listadoAcumulado() {
        List<Proceso> lista = getDaos().getProcesoDao().procesoByMovPerUa(movimiento, periodo, ua);
        for (Proceso proceso : lista) {
            getJsonResult().add("[\"" + proceso.getTipoProceso().getNombre()
                    + "\", \"" + proceso.getTipoProceso().getDescripcion()
                    + "\", \"" + "<input  type=checkbox value='" + proceso.getId() + "' onclick='transferir(this.checked, this.value);' />"
                    + "\"]");
        }
        return SUCCESS_JSON;
    }

    public String addPrint(BigDecimal estatus_proceso_id, Proceso proceso, Periodo periodoActivo) {
        String print;
        String a = "";
        Integer ax = estatus_proceso_id.intValue();
        Periodo px = proceso.getPeriodo();
        if (ax > 1) {
            a = "<a title='Imprimir resumen del proceso' target='_blank' href='/procesos/resumen/cuadroResumenProceso.action?proceso.id=" + proceso.getId() + "&i=true'>Resumen Proceso(PDF)</a>";
        }
        print = "<div class='btn-group'>"
                + " <button id = 'cuadro' type='button' class='btn btn-primary dropdown-toggle' data-toggle='dropdown' >Cuadro resumen <span class='caret'></span> </button> "
                + " <ul class='dropdown-menu' role='menu'> "
                + " <li>" + a + "</li>"
                + " <li><a title='Resumen del proceso' target='_blank' href='/procesos/resumen/cuadroResumenProceso.action?proceso.id=" + proceso.getId() + "&i=false'>Resumen Proceso(Excel)</a></li> ";
        if (periodoActivo.getId().equals(px.getId())) {
            print += " <li><a title='Resumen del proceso' target='_blank' href='/procesos/resumen/listadoPrelacionProceso.action?proceso.id=" + proceso.getId() + "&i=false'>Listado Prelación(Excel)</a></li> ";
        }
        print += "</ul></div> ";
        return print;
    }

    public String valida() {
        ProcesoBO procesoBO = new ProcesoBO(getDaos());
        Proceso p = getDaos().getProcesoDao().findById(id_proceso);
        ProcesoEstatus procesoEstatus = getDaos().getProcesoEstatusDao().findById(id_estatus);
        p.setProcesoEstatus(procesoEstatus);
        procesoBO.guardaProceso(p);
        procesoBO.guardaBitacoraEstatusProceso(p, procesoEstatus);
        return SUCCESS_JSON;
    }

    public BigDecimal getId_proceso() {
        return id_proceso;
    }

    public void setId_proceso(BigDecimal id_proceso) {
        this.id_proceso = id_proceso;
    }

    public BigDecimal getId_estatus() {
        return id_estatus;
    }

    public void setId_estatus(BigDecimal id_estatus) {
        this.id_estatus = id_estatus;
    }

    public int getSearch_id_periodo() {
        return search_id_periodo;
    }

    public void setSearch_id_periodo(int search_id_periodo) {
        this.search_id_periodo = search_id_periodo;
        if (CampoValidoAJAX(search_id_periodo)) {
            ssu.parametros.put("ep.periodo.id", search_id_periodo);
        }
    }

    public int getSearch_id_proceso() {
        return search_id_proceso;
    }

    public void setSearch_id_proceso(int search_id_proceso) {
        this.search_id_proceso = search_id_proceso;
        if (CampoValidoAJAX(search_id_proceso)) {
            ssu.parametros.put("ep.tipoProceso.id", search_id_proceso);
        }
    }

    public int getSearch_id_ua() {
        return search_id_ua;
    }

    public void setSearch_id_ua(int search_id_ua) {
        this.search_id_ua = search_id_ua;
        if (CampoValidoAJAX(search_id_ua)) {
            ssu.parametros.put("ep.unidadAcademica.id", search_id_ua);
        }
    }

    public int getSearch_id_estatus() {
        return search_id_estatus;
    }

    public void setSearch_id_estatus(int search_id_estatus) {
        this.search_id_estatus = search_id_estatus;
        if (CampoValidoAJAX(search_id_estatus)) {
            ssu.parametros.put("ep.procesoEstatus.id", search_id_estatus);
        }
    }

    /**
     * Acción que obtiene los procesos dada la unidad académcia.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String get() {
        List<Proceso> procesos = getDaos().getProcesoDao().procesosOtorgamientoPorUnidadAcademica(unidadAcademica);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date hoy = new Date();
        if (procesos != null) {
            for (Proceso proceso : procesos) {
                int movimientoInt = proceso.getTipoProceso().getMovimiento().getId().intValue();
                String img;

                switch (movimientoInt) {
                    case 7:
                        img = "/resources/img/asignacionManual/nuevos.png";
                        break;
                    case 3:
                        img = "/resources/img/asignacionManual/nuevos.png";
                        break;
                    case 1:
                        img = "/resources/img/asignacionManual/transferencia.png";
                        break;
                    default:
                        img = "/resources/img/asignacionManual/revalidacion.png";
                        break;
                }

                if (proceso.getProcesoEstatus().getId().intValue() == 1
                        && hoy.after(proceso.getFechaInicial())
                        && hoy.before(proceso.getFechaFinal())) {
                    getJsonResult().add("[\""
                            + "<div class='col-lg-2 col-md-4 col-xs-6 rectangulo' onclick='activo(this)' "
                            + "style='background: #ACC18B url(" + img + ") no-repeat;  "
                            + "data-estatus='" + proceso.getProcesoEstatus().getId() + "' "
                            + "data-activo='1' "
                            + "data-nombre='" + proceso.getTipoProceso().getNombre() + "' "
                            + "data-proceso='" + proceso.getTipoProceso().getMovimiento().getId() + "' "
                            + "data-inicio='" + formato.format(proceso.getFechaInicial()) + "' "
                            + "data-fin='" + formato.format(proceso.getFechaFinal()) + "' "
                            + "data-procesoId='" + proceso.getId() + "'><span>"
                            + proceso.getTipoProceso().getNombre()
                            + "</span></div>"
                            + "\"]"
                    );
                } else {
                    getJsonResult().add("[\""
                            + "<div class='col-lg-2 col-md-4 col-xs-6 rectangulo' onclick='activo(this)'"
                            + "style='background: #DB4D6C url(" + img + ") no-repeat;' "
                            + "data-estatus='" + proceso.getProcesoEstatus().getId() + "' "
                            + "data-activo='0' "
                            + "data-nombre='" + proceso.getTipoProceso().getNombre() + "' "
                            + "data-proceso='" + proceso.getTipoProceso().getMovimiento().getId() + "' "
                            + "data-inicio='" + formato.format(proceso.getFechaInicial()) + "' "
                            + "data-fin='" + formato.format(proceso.getFechaFinal()) + "' "
                            + "data-procesoId='" + proceso.getId() + "'> <span>"
                            + proceso.getTipoProceso().getNombre()
                            + "</span></div>"
                            + "\"]");
                }
            }
        }
        return SUCCESS_JSON;
    }

    public String getBajas() {
        List<Proceso> procesos = getDaos().getProcesoDao().procesosBajasPorUnidadAcademica(unidadAcademica);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date hoy = new Date();
        if (procesos != null) {
            for (Proceso proceso : procesos) {
                int movimientoInt = proceso.getTipoProceso().getMovimiento().getId().intValue();
                String img;

                switch (movimientoInt) {
                    case 4:
                        img = "/resources/img/asignacionManual/bajas-incumplimiento.png";
                        break;
                    case 5:
                        img = "/resources/img/asignacionManual/bajas-pasantia.png";
                        break;
                    default:
                        img = "/resources/img/asignacionManual/bajas-diversas.png";
                        break;
                }

                if (proceso.getProcesoEstatus().getId().intValue() == 1
                        && hoy.after(proceso.getFechaInicial())
                        && hoy.before(proceso.getFechaFinal())) {
                    getJsonResult().add("[\""
                            + "<div class='col-lg-2 col-md-4 col-xs-6 rectangulo' onclick='activo(this)' "
                            + "style='background: #ACC18B url(" + img + ") no-repeat;  "
                            + "data-estatus='" + proceso.getProcesoEstatus().getId() + "' "
                            + "data-activo='1' "
                            + "data-nombre='" + proceso.getTipoProceso().getNombre() + "' "
                            + "data-proceso='" + proceso.getTipoProceso().getMovimiento().getId() + "' "
                            + "data-inicio='" + formato.format(proceso.getFechaInicial()) + "' "
                            + "data-fin='" + formato.format(proceso.getFechaFinal()) + "' "
                            + "data-procesoId='" + proceso.getId() + "'><span>"
                            + proceso.getTipoProceso().getNombre()
                            + "</span></div>"
                            + "\"]"
                    );
                } else {
                    getJsonResult().add("[\""
                            + "<div class='col-lg-2 col-md-4 col-xs-6 rectangulo' onclick='activo(this)'"
                            + "style='background: #DB4D6C url(" + img + ") no-repeat;' "
                            + "data-estatus='" + (hoy.after(proceso.getFechaInicial()) && hoy.before(proceso.getFechaFinal()) ? "" : proceso.getProcesoEstatus().getId()) + "' "
                            + "data-activo='0' "
                            + "data-nombre='" + proceso.getTipoProceso().getNombre() + "' "
                            + "data-proceso='" + proceso.getTipoProceso().getMovimiento().getId() + "' "
                            + "data-inicio='" + formato.format(proceso.getFechaInicial()) + "' "
                            + "data-fin='" + formato.format(proceso.getFechaFinal()) + "' "
                            + "data-procesoId='" + proceso.getId() + "'> <span>"
                            + proceso.getTipoProceso().getNombre()
                            + "</span></div>"
                            + "\"]");
                }
            }
        }
        return SUCCESS_JSON;
    }

    public BigDecimal getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(BigDecimal unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public int getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(int periodoId) {
        this.periodoId = periodoId;
    }

    public int getUaId() {
        return uaId;
    }

    public void setUaId(int uaId) {
        this.uaId = uaId;
    }

    public int getMovimientoId() {
        return movimientoId;
    }

    public void setMovimientoId(int movimientoId) {
        this.movimientoId = movimientoId;
    }

    public int getEstatusId() {
        return estatusId;
    }

    public void setEstatusId(int estatusId) {
        this.estatusId = estatusId;
    }

}

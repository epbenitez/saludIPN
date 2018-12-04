package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.business.EstadisticasBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Estadistica;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.EstadisticaCuestionarios;
import com.becasipn.persistence.model.EstadisticaSolicitudes;
import com.becasipn.persistence.model.Movimiento;
import com.becasipn.persistence.model.UnidadAcademica;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Gustavo A. Alamillo
 */
public class ReportesAjaxAction extends JSONAjaxAction {

    private final EstadisticasBO estadisticasBO;

    public UnidadAcademica unidadAcademica;
    public Beca beca;
    public Periodo periodo;
    public String fechaInicial;
    public String fechaFinal;
    public Cuestionario cuestionario;
    public Nivel nivel;
    public Movimiento movimiento;
    public BigDecimal estatusId;
    public BigDecimal nivelId;
    public BigDecimal uaId;
    public BigDecimal periodoId;
    public BigDecimal tipoBecaPeriodoId;

    public ReportesAjaxAction() {
        this.estadisticasBO = new EstadisticasBO(getDaos());
    }

    public String informacion() {

        Gson gson = new Gson();
        JsonObject estadisticas = new JsonObject();

        List<Estadistica> estadisticaTmp = estadisticasBO.estadistica(Estadistica.Tipo.GENERO, periodo, nivel, unidadAcademica, beca, movimiento);//----------------------OK
        estadisticas.add("genero", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaPendiente(Estadistica.Tipo.GENEROP, periodo, nivel, unidadAcademica, beca, movimiento);
        estadisticas.add("generoP", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaBajas(periodo, nivel, unidadAcademica, beca, movimiento);
        estadisticas.add("generoB", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaMovimiento(periodo, nivel, unidadAcademica, beca, movimiento);//-------------------------------------------------------OK
        estadisticas.add("movimientos", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaMovimientoB(periodo, nivel, unidadAcademica, beca, movimiento);//-------------------------------------------------------OK
        estadisticas.add("movimientosB", gson.toJsonTree(estadisticaTmp));

        if (unidadAcademica != null || unidadAcademica.getId().intValue() != 0) {
            estadisticaTmp = estadisticasBO.estadisticaCarreras(periodo, nivel, unidadAcademica, beca, movimiento);//-----------------------------------------------------OK
            estadisticas.add("carreras", gson.toJsonTree(estadisticaTmp));

            estadisticaTmp = estadisticasBO.estadisticaCarrerasB(periodo, nivel, unidadAcademica, beca, movimiento);//-----------------------------------------------------OK
            estadisticas.add("carrerasB", gson.toJsonTree(estadisticaTmp));
        }

        estadisticaTmp = estadisticasBO.estadisticaProgramaBeca(periodo, nivel, unidadAcademica, beca, movimiento);//-----------------------------------------------------OK
        estadisticas.add("programaBeca", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaProgramaBecaB(periodo, nivel, unidadAcademica, beca, movimiento);//-----------------------------------------------------OK
        estadisticas.add("programaBecaB", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadistica(Estadistica.Tipo.SEMESTRES, periodo, nivel, unidadAcademica, beca, movimiento);//-------------------------------------???
        estadisticas.add("semestres", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaSemestreBajas(periodo, nivel, unidadAcademica, beca, movimiento);//-----------------------------------------------------OK
        estadisticas.add("semestreBaja", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadistica(Estadistica.Tipo.PROMEDIOS, periodo, nivel, unidadAcademica, beca, movimiento);//-------------------------------------???
        estadisticas.add("promedios", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaPromedioBajas(periodo, nivel, unidadAcademica, beca, movimiento);//-----------------------------------------------------OK
        estadisticas.add("promedioBaja", gson.toJsonTree(estadisticaTmp));

        getJsonResult().add(estadisticas.toString());
        return SUCCESS_JSON;
    }

    public String rendimiento() {

        Gson gson = new Gson();
        JsonObject estadisticas = new JsonObject();

        List<Estadistica> estadisticaTmp = estadisticasBO.estadistica(Estadistica.Tipo.GENERO, periodo, nivel, unidadAcademica, beca, movimiento);//----------------------OK
        estadisticas.add("genero", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaPendiente(Estadistica.Tipo.GENEROP, periodo, nivel, unidadAcademica, beca, movimiento);
        estadisticas.add("generoP", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaRendimiento(periodo, nivel, unidadAcademica, beca, movimiento);//------------------------------------------------------OK
        estadisticas.add("otorgamientos", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaBajas(periodo, nivel, unidadAcademica, beca, movimiento);
        estadisticas.add("generoB", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaRendimientoBajas(periodo, nivel, unidadAcademica, beca, movimiento);//-------------------------------------------------OK
        estadisticas.add("bajas", gson.toJsonTree(estadisticaTmp));
        getJsonResult().add(estadisticas.toString());
        return SUCCESS_JSON;
    }

    public String registro() {
        Gson gson = new Gson();
        JsonObject estadisticas = new JsonObject();

        List<Estadistica> estadisticaTmp = estadisticasBO.estadisticaRegistroT(periodo, nivel, unidadAcademica, beca, movimiento);//----------------------OK
        estadisticas.add("genero", gson.toJsonTree(estadisticaTmp));

        estadisticaTmp = estadisticasBO.estadisticaRegistro(periodo, nivel, unidadAcademica, beca, movimiento);//------------------------------------------------------OK
        estadisticas.add("otorgamientos", gson.toJsonTree(estadisticaTmp));

        getJsonResult().add(estadisticas.toString());
        return SUCCESS_JSON;
    }

    public String solicitudes() {
        Gson gson = new Gson();
        JsonObject solicitudes = new JsonObject();

        List<EstadisticaSolicitudes> solicitud = estadisticasBO.estadisticaPreasignadas(periodo, unidadAcademica, nivel, fechaInicial, fechaFinal);
        solicitudes.add("Preasignada", gson.toJsonTree(solicitud));

        solicitud = estadisticasBO.estadisticaSolicitadas(periodo.getId(), unidadAcademica.getId(), nivel.getId(), fechaInicial, fechaFinal);
        solicitudes.add("Solicitada", gson.toJsonTree(solicitud));

        getJsonResult().add(solicitudes.toString());
        return SUCCESS_JSON;
    }

    public String depositos() {
        Gson gson = new Gson();
        JsonObject estadisticas = new JsonObject();

        List<Estadistica> estadisticaTmp = estadisticasBO.estadisticaDepositos(periodo, nivel, unidadAcademica, tipoBecaPeriodoId);//------------------------------------------------------OK
        estadisticas.add("otorgamientos", gson.toJsonTree(estadisticaTmp));

        getJsonResult().add(estadisticas.toString());
        return SUCCESS_JSON;
    }

    public String cuentas() {
        Gson gson = new Gson();
        JsonObject estadisticas = new JsonObject();

        List<Estadistica> estadisticaTmp = estadisticasBO.estadisticaCuentasT(periodo, nivel, unidadAcademica, beca, movimiento);//----------------------OK
        estadisticas.add("genero", gson.toJsonTree(estadisticaTmp));

        getJsonResult().add(estadisticas.toString());
        return SUCCESS_JSON;
    }

    public String tablaCuentas() {
        setSsu();
        setPu(getDaos().getOtorgamientoDao().estadisticaCuentasTabla(periodoId, nivelId, uaId, estatusId, ssu));
        List<Alumno> lista = getPu().getResultados();
        String nombreCorto = "No cuenta con UA";
        if (lista == null) {
            return SUCCESS_JSON;
        }
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
                    + "\", \"<a title='Monitoreo de Cuentas' class='fancybox fancybox.iframe table-link'  href='/tarjeta/bitacoraMonitoreoTarjetaBancaria.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-credit-card fa-stack-1x fa-inverse'></i></span></a>"
                    + " \"]"
            );
        }
        return SUCCESS_PAGINATED_JSON;
    }

    public String cuestionarios() {
        Gson gson = new Gson();
        JsonObject estadisticas = new JsonObject();

        List<EstadisticaCuestionarios> estadisticaTmp = estadisticasBO.estadisticaCuestionarios(periodo, cuestionario);//----------------------OK
        estadisticas.add("Cuestionarios", gson.toJsonTree(estadisticaTmp));

        getJsonResult().add(estadisticas.toString());
        return SUCCESS_JSON;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public Beca getBeca() {
        return beca;
    }

    public void setBeca(Beca beca) {
        this.beca = beca;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public BigDecimal getEstatusId() {
        return estatusId;
    }

    public void setEstatusId(BigDecimal estatusId) {
        this.estatusId = estatusId;
    }

    public BigDecimal getNivelId() {
        return nivelId;
    }

    public void setNivelId(BigDecimal nivelId) {
        this.nivelId = nivelId;
    }

    public BigDecimal getUaId() {
        return uaId;
    }

    public void setUaId(BigDecimal uaId) {
        this.uaId = uaId;
    }

    public BigDecimal getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(BigDecimal periodoId) {
        this.periodoId = periodoId;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public BigDecimal getTipoBecaPeriodoId() {
        return tipoBecaPeriodoId;
    }

    public void setTipoBecaPeriodoId(BigDecimal tipoBecaPeriodoId) {
        this.tipoBecaPeriodoId = tipoBecaPeriodoId;
    }

}

package com.becasipn.actions.ajax;

import com.becasipn.persistence.model.Periodo;
import com.becasipn.util.ProgressBarManagerUtil;
import com.becasipn.util.Util;
import java.math.BigDecimal;

/**
 *
 * @author Othoniel Herrera
 */
public class PreasignacionAjaxAction extends JSONAjaxAction {

    private BigDecimal unidadAcademicaId;
    private BigDecimal nivelId;
    private ProgressBarManagerUtil pbmu;
    private Integer sinc_nivel;
    private Integer sinc_ua;
    private boolean sobreEscribir;

    public String getCifras() {
        Periodo periodoActivo = getDaos().getPeriodoDao().getPeriodoActivo();
        String clave = periodoActivo.getClave();

        BigDecimal totalAlumnosSolicitudPendienteActivo = getDaos().getSolicitudBecaDao().totalAlumnosSolicitudPeriodoEstatusNivelUA(periodoActivo.getId(), null, null, nivelId, unidadAcademicaId, sobreEscribir);
        BigDecimal totalAlumnosSolicitudEsperaActivo = getDaos().getSolicitudBecaDao().totalAlumnosSolicitudPeriodoEstatusNivelUA(periodoActivo.getId(), null, new BigDecimal(3), nivelId, unidadAcademicaId, sobreEscribir);
        BigDecimal totalAlumnosSolicitudEsperaPasado;
        BigDecimal totalAlumnosSolicitudPreasignacion;

        if (clave.charAt(clave.length() - 1) == '1') {
            totalAlumnosSolicitudPreasignacion = totalAlumnosSolicitudPendienteActivo.add(totalAlumnosSolicitudEsperaActivo);
            getJsonResult().add("[\"" + totalAlumnosSolicitudPendienteActivo + " \"]");
            getJsonResult().add("[\"" + totalAlumnosSolicitudEsperaActivo + " \"]");
            getJsonResult().add("[\"" + totalAlumnosSolicitudPreasignacion + " \"]");
        } else {
            totalAlumnosSolicitudEsperaPasado = getDaos().getSolicitudBecaDao().totalAlumnosSolicitudPeriodoEstatusNivelUA(periodoActivo.getId(), periodoActivo.getPeriodoAnterior().getId(), new BigDecimal(3), nivelId, unidadAcademicaId, sobreEscribir);
            totalAlumnosSolicitudPreasignacion = totalAlumnosSolicitudPendienteActivo.add(totalAlumnosSolicitudEsperaActivo).add(totalAlumnosSolicitudEsperaPasado);
            getJsonResult().add("[\"" + totalAlumnosSolicitudPendienteActivo + " \"]");
            getJsonResult().add("[\"" + totalAlumnosSolicitudEsperaActivo + " \"]");
            getJsonResult().add("[\"" + totalAlumnosSolicitudEsperaPasado + " \"]");
            getJsonResult().add("[\"" + totalAlumnosSolicitudPreasignacion + " \"]");
        }

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

    public ProgressBarManagerUtil getPbmu() {
        return pbmu;
    }

    public String setPbmu() {
        this.pbmu = Util.pbmuMap.get(Util.getNivelUAs(getSinc_nivel(), getSinc_ua()));
        if (this.pbmu == null) {
            return ERROR_JSON;
        }
        return SUCCESS_JSON;
    }

    public BigDecimal getUnidadAcademicaId() {
        return unidadAcademicaId;
    }

    public void setUnidadAcademicaId(BigDecimal unidadAcademicaId) {
        this.unidadAcademicaId = unidadAcademicaId;
    }

    public BigDecimal getNivelId() {
        return nivelId;
    }

    public void setNivelId(BigDecimal nivelId) {
        this.nivelId = nivelId;
    }

    public Integer getSinc_nivel() {
        return sinc_nivel;
    }

    public void setSinc_nivel(Integer sinc_nivel) {
        this.sinc_nivel = sinc_nivel;
    }

    public Integer getSinc_ua() {
        return sinc_ua;
    }

    public void setSinc_ua(Integer sinc_ua) {
        this.sinc_ua = sinc_ua;
    }

    public boolean isSobreEscribir() {
        return sobreEscribir;
    }

    public void setSobreEscribir(boolean sobreEscribir) {
        this.sobreEscribir = sobreEscribir;
    }

}

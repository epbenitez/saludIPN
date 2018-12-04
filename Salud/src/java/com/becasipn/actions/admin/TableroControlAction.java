/**
 * SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE SERVICIOS
 * ESTUDIANTILES 2016
 *
 */
package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.TableroControlBO;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author Patricia Benítez
 */
public class TableroControlAction extends BaseAction {

    private BigDecimal totalAlumnosESECompleto;
    private BigDecimal totalAlumnosTransporte;
    private BigDecimal totalAlumnosSalud;
    private BigDecimal totalAlumnosAsignados;
    private BigDecimal totalAlumnosBajas = new BigDecimal(0);
    private BigDecimal bajasOtroPeriodo = new BigDecimal(0);
    private BigDecimal bajasPeriodoActual = new BigDecimal(0);
    private BigDecimal totalAlumnosPreNS;
    private BigDecimal totalAlumnosPreS;
    private BigDecimal totalAlumnosSolicitudAsignada;
    private BigDecimal totalAlumnosSolicitudRechazada;
    private BigDecimal totalAlumnosSolicitudEspera;
    private BigDecimal totalASolicitudPendienteManu;
    private BigDecimal totalASolicitudPendienteElse;
    private BigDecimal totalASolicitudesPendientes;
    private BigDecimal totalAlumnosSolicitud;
    private Boolean isResponsableOrFuncionario;
    private int totalAlumnosBecaUniversal;
    private int totalAlumnosOtrasBecas;

    @Override
    public String execute() {
        TableroControlBO bo = TableroControlBO.getInstance(getDaos());

        if (getIsResponsableOrFuncionario()) {
            Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo pa = getDaos().getPersonalAdministrativoDao().findByUsuario(u.getId());

            if (pa != null) {
                //SE RESTRINGEN EL TABLERO A LA UNIDAD ACADÉMICA DEL USUARIO EN SESIÓN
                BigDecimal uaId = pa.getUnidadAcademica().getId();

                totalAlumnosESECompleto = getDaos().getSolicitudBecaDao().totalAlumnosConSolicitud(uaId, new BigDecimal(1));
                totalAlumnosTransporte = getDaos().getSolicitudBecaDao().totalAlumnosConSolicitud(uaId, new BigDecimal(2));
                totalAlumnosSolicitud = getDaos().getSolicitudBecaDao().totalAlumnosConSolicitud(uaId, null);
                totalAlumnosSolicitudAsignada = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(uaId, new BigDecimal(1), null);
                totalAlumnosSolicitudRechazada = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(uaId, new BigDecimal(2), null);
                totalAlumnosSolicitudEspera = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(uaId, new BigDecimal(3), null);
                totalASolicitudesPendientes = getDaos().getSolicitudBecaDao().totalAlumnosEstatusPendiente(uaId, null);
                totalASolicitudPendienteManu = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(uaId, null, true);
                totalASolicitudPendienteElse = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(uaId, null, false);
                totalAlumnosSalud = getDaos().getSolicitudBecaDao().totalAlumnosCuestionarioCompleto(uaId, new BigDecimal(3));
                totalAlumnosAsignados = getDaos().getOtorgamientoDao().totalAlumnosConOtorgamiento(uaId);
                totalAlumnosPreNS = getDaos().getSolicitudBecaDao().totalAlumnosPreAsignados(null, uaId);
                totalAlumnosPreS = getDaos().getSolicitudBecaDao().totalAlumnosPreAsignados(new BigDecimal("2"), uaId);
                totalAlumnosBecaUniversal = getDaos().getOtorgamientoDao().contadorAlumnoPorBecaPorUA(new BigDecimal("10"), uaId);
                totalAlumnosOtrasBecas = totalAlumnosAsignados.intValue() - totalAlumnosBecaUniversal;

                // Bajas
                Map<String, BigDecimal> kills = bo.getBajas(uaId);
                bajasOtroPeriodo = kills.get("anteriores");
                bajasPeriodoActual = kills.get("actual");
                totalAlumnosBajas = bajasOtroPeriodo.add(bajasPeriodoActual);
            }
            return SUCCESS;
        }
        //SE MUESTRA TODO EL UNIVERSO
        totalAlumnosESECompleto = getDaos().getSolicitudBecaDao().totalAlumnosConSolicitud(null, new BigDecimal(1));
        totalAlumnosTransporte = getDaos().getSolicitudBecaDao().totalAlumnosConSolicitud(null, new BigDecimal(2));
        totalAlumnosSolicitud = getDaos().getSolicitudBecaDao().totalAlumnosConSolicitud(null, null);
        totalAlumnosSolicitudAsignada = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(null, new BigDecimal(1), null);
        totalAlumnosSolicitudRechazada = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(null, new BigDecimal(2), null);
        totalAlumnosSolicitudEspera = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(null, new BigDecimal(3), null);
        totalASolicitudesPendientes = getDaos().getSolicitudBecaDao().totalAlumnosEstatusPendiente(null, null);
        totalASolicitudPendienteManu = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(null, null, true);
        totalASolicitudPendienteElse = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitud(null, null, false);
        totalAlumnosSalud = getDaos().getSolicitudBecaDao().totalAlumnosCuestionarioCompleto(null, new BigDecimal(3));
        totalAlumnosAsignados = getDaos().getOtorgamientoDao().totalAlumnosConOtorgamiento(null);
        totalAlumnosPreNS = getDaos().getSolicitudBecaDao().totalAlumnosPreAsignados(null, null);
        totalAlumnosPreS = getDaos().getSolicitudBecaDao().totalAlumnosPreAsignados(new BigDecimal("2"), null);
        totalAlumnosBecaUniversal = getDaos().getOtorgamientoDao().contadorAlumnoPorBecaPorUA(new BigDecimal("10"), null);
        totalAlumnosOtrasBecas = totalAlumnosAsignados.intValue() - totalAlumnosBecaUniversal;

        // Bajas
        Map<String, BigDecimal> kills = bo.getBajas(null);
        bajasOtroPeriodo = kills.get("anteriores");
        bajasPeriodoActual = kills.get("actual");
        totalAlumnosBajas = bajasOtroPeriodo.add(bajasPeriodoActual);

        return SUCCESS;
    }

    public BigDecimal getTotalAlumnosESECompleto() {
        return totalAlumnosESECompleto;
    }

    public void setTotalAlumnosESECompleto(BigDecimal totalAlumnosESECompleto) {
        this.totalAlumnosESECompleto = totalAlumnosESECompleto;
    }

    public BigDecimal getTotalAlumnosTransporte() {
        return totalAlumnosTransporte;
    }

    public BigDecimal getTotalASolicitudesPendientes() {
        return totalASolicitudesPendientes;
    }

    public void setTotalASolicitudesPendientes(BigDecimal totalASolicitudesPendientes) {
        this.totalASolicitudesPendientes = totalASolicitudesPendientes;
    }

    public void setTotalAlumnosTransporte(BigDecimal totalAlumnosTransporte) {
        this.totalAlumnosTransporte = totalAlumnosTransporte;
    }

    public BigDecimal getTotalAlumnosSalud() {
        return totalAlumnosSalud;
    }

    public void setTotalAlumnosSalud(BigDecimal totalAlumnosSalud) {
        this.totalAlumnosSalud = totalAlumnosSalud;
    }

    public BigDecimal getTotalAlumnosAsignados() {
        return totalAlumnosAsignados;
    }

    public void setTotalAlumnosAsignados(BigDecimal totalAlumnosAsignados) {
        this.totalAlumnosAsignados = totalAlumnosAsignados;
    }

    public BigDecimal getTotalAlumnosBajas() {
        return totalAlumnosBajas;
    }

    public void setTotalAlumnosBajas(BigDecimal totalAlumnosBajas) {
        this.totalAlumnosBajas = totalAlumnosBajas;
    }

    public BigDecimal getTotalAlumnosPreNS() {
        return totalAlumnosPreNS;
    }

    public void setTotalAlumnosPreNS(BigDecimal totalAlumnosPreNS) {
        this.totalAlumnosPreNS = totalAlumnosPreNS;
    }

    public BigDecimal getTotalAlumnosPreS() {
        return totalAlumnosPreS;
    }

    public void setTotalAlumnosPreS(BigDecimal totalAlumnosPreS) {
        this.totalAlumnosPreS = totalAlumnosPreS;
    }

    public Boolean getIsResponsableOrFuncionario() {
        if (isResponsableOrFuncionario == null) {
            setIsResponsableOrFuncionario();
        }
        return isResponsableOrFuncionario;
    }

    public void setIsResponsableOrFuncionario() {
        this.isResponsableOrFuncionario = (isResponsable() || isFuncionario());
    }

    public BigDecimal getTotalAlumnosSolicitudAsignada() {
        return totalAlumnosSolicitudAsignada;
    }

    public void setTotalAlumnosSolicitudAsignada(BigDecimal totalAlumnosSolicitudAsignada) {
        this.totalAlumnosSolicitudAsignada = totalAlumnosSolicitudAsignada;
    }

    public BigDecimal getTotalAlumnosSolicitudRechazada() {
        return totalAlumnosSolicitudRechazada;
    }

    public void setTotalAlumnosSolicitudRechazada(BigDecimal totalAlumnosSolicitudRechazada) {
        this.totalAlumnosSolicitudRechazada = totalAlumnosSolicitudRechazada;
    }

    public BigDecimal getTotalAlumnosSolicitudEspera() {
        return totalAlumnosSolicitudEspera;
    }

    public void setTotalAlumnosSolicitudEspera(BigDecimal totalAlumnosSolicitudEspera) {
        this.totalAlumnosSolicitudEspera = totalAlumnosSolicitudEspera;
    }

    public BigDecimal getTotalASolicitudPendienteManu() {
        return totalASolicitudPendienteManu;
    }

    public void setTotalASolicitudPendienteManu(BigDecimal totalASolicitudPendienteManu) {
        this.totalASolicitudPendienteManu = totalASolicitudPendienteManu;
    }

    public BigDecimal getTotalASolicitudPendienteElse() {
        return totalASolicitudPendienteElse;
    }

    public void setTotalASolicitudPendienteElse(BigDecimal totalASolicitudPendienteElse) {
        this.totalASolicitudPendienteElse = totalASolicitudPendienteElse;
    }

    public BigDecimal getTotalAlumnosSolicitud() {
        return totalAlumnosSolicitud;
    }

    public void setTotalAlumnosSolicitud(BigDecimal totalAlumnosSolicitud) {
        this.totalAlumnosSolicitud = totalAlumnosSolicitud;
    }

    public int getTotalAlumnosBecaUniversal() {
        return totalAlumnosBecaUniversal;
    }

    public void setTotalAlumnosBecaUniversal(int totalAlumnosBecaUniversal) {
        this.totalAlumnosBecaUniversal = totalAlumnosBecaUniversal;
    }

    public int getTotalAlumnosOtrasBecas() {
        return totalAlumnosOtrasBecas;
    }

    public void setTotalAlumnosOtrasBecas(int totalAlumnosOtrasBecas) {
        this.totalAlumnosOtrasBecas = totalAlumnosOtrasBecas;
    }

    public BigDecimal getBajasOtroPeriodo() {
        return bajasOtroPeriodo;
    }

    public void setBajasOtroPeriodo(BigDecimal bajasOtroPeriodo) {
        this.bajasOtroPeriodo = bajasOtroPeriodo;
    }

    public BigDecimal getBajasPeriodoActual() {
        return bajasPeriodoActual;
    }

    public void setBajasPeriodoActual(BigDecimal bajasPeriodoActual) {
        this.bajasPeriodoActual = bajasPeriodoActual;
    }

}

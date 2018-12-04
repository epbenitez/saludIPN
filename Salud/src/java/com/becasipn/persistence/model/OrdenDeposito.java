package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Victor Lozano
 */
@Entity
@Table(name = "ent_orden_deposito")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class OrdenDeposito implements Serializable, BaseEntity {
    @Id
    @GeneratedValue(generator = "entOrdenDepositoSeq")
    @SequenceGenerator(name = "entOrdenDepositoSeq", sequenceName = "ent_orden_deposito_id_seq", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    private int mes;
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAcademica unidadAcademica;
    @ManyToOne(fetch = FetchType.LAZY)
    private Beca programaBeca;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoProceso tipoProceso;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoDeposito tipoDeposito;
    @ManyToOne(fetch = FetchType.LAZY)
    private EstatusDeposito estatusDeposito;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    private String nombreOrdenDeposito;
    private String nombreRespuestaBancaria;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaEjecucion;
    @ManyToOne(fetch = FetchType.LAZY)
    private Nivel nivel;
    private Boolean correspondeIPN;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaGeneracion;
    private Integer formaPago;

    //Variable que permite obtener el tiempo transcurrido desde la creación de la órden
    @Transient
    private String elapsedTime;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public UnidadAcademica getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(UnidadAcademica unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public Beca getProgramaBeca() {
        return programaBeca;
    }

    public void setProgramaBeca(Beca programaBeca) {
        this.programaBeca = programaBeca;
    }

    public TipoProceso getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(TipoProceso tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public TipoDeposito getTipoDeposito() {
        return tipoDeposito;
    }

    public void setTipoDeposito(TipoDeposito tipoDeposito) {
        this.tipoDeposito = tipoDeposito;
    }

    public EstatusDeposito getEstatusDeposito() {
        return estatusDeposito;
    }

    public void setEstatusDeposito(EstatusDeposito estatusDeposito) {
        this.estatusDeposito = estatusDeposito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombreOrdenDeposito() {
        return nombreOrdenDeposito;
    }

    public void setNombreOrdenDeposito(String nombreOrdenDeposito) {
        this.nombreOrdenDeposito = nombreOrdenDeposito;
    }

    public String getNombreRespuestaBancaria() {
        return nombreRespuestaBancaria;
    }

    public void setNombreRespuestaBancaria(String nombreRespuestaBancaria) {
        this.nombreRespuestaBancaria = nombreRespuestaBancaria;
    }

    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Boolean getCorrespondeIPN() {
        return correspondeIPN;
    }

    public void setCorrespondeIPN(Boolean correspondeIPN) {
        this.correspondeIPN = correspondeIPN;
    }

    public String getElapsedTime() {
        long lStartTime = fechaEjecucion.getTime();
        long lEndTime = (new Date()).getTime();

        long millis = lEndTime - lStartTime;
//        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        long days = (millis / (1000 * 60 * 60 * 24)) % 365;
        return String.format("%02dD %02dH %02dM", days, hour, minute);
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @Override
    public String toString() {
        return "OrdenDeposito{" + "id=" + id + ", periodo=" + periodo + ", mes=" + mes + ", unidadAcademica=" + unidadAcademica + ", programaBeca=" + programaBeca + ", tipoProceso=" + tipoProceso + ", tipoDeposito=" + tipoDeposito + ", estatusDeposito=" + estatusDeposito + ", usuario=" + usuario + ", nombreOrdenDeposito=" + nombreOrdenDeposito + ", nombreRespuestaBancaria=" + nombreRespuestaBancaria + ", fechaEjecucion=" + fechaEjecucion + ", nivel=" + nivel + ", correspondeIPN=" + correspondeIPN + '}';
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Integer getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(Integer formaPago) {
        this.formaPago = formaPago;
    }
}
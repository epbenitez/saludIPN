package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Victor Lozano
 */
@Entity
@Table(name = "vw_orden_deposito")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class VWOrdenDeposito implements Serializable, BaseEntity {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigDecimal id;
    private int conteo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    private int mes;
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAcademica unidadAcademica;
    @ManyToOne(fetch = FetchType.LAZY)
    private Nivel nivel;
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
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaEjecucion;
    @Transient
    private String nombreMes;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public int getConteo() {
        return conteo;
    }

    public void setConteo(int conteo) {
        this.conteo = conteo;
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

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
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

    public Date getFechaEjecucion() {
        return fechaEjecucion;
    }

    public void setFechaEjecucion(Date fechaEjecucion) {
        this.fechaEjecucion = fechaEjecucion;
    }

    public String getNombreMes() {
        return nombreMes;
    }

    public void setNombreMes(String nombreMes) {
        this.nombreMes = nombreMes;
    }
}
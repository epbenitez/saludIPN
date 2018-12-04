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
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Mario Márquez
 */
@Entity
@Table(name = "VW_OTORGAMIENTOS")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class VWOtorgamiento implements Serializable, BaseEntity {
    
    @Id
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBecaPeriodo tipoBecaPeriodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Proceso proceso;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    private Boolean alta;
    private Boolean automatico;
    private Boolean asignacionConfirmada;
    private Boolean excluirDeposito;
    @ManyToOne(fetch = FetchType.LAZY)
    private IdentificadorOtorgamiento identificadorOtorgamiento;
    private String boleta;
    private String observaciones;
    private Integer fase;
    @ManyToOne(fetch = FetchType.LAZY)
    private DatosAcademicos datosAcademicos;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private SolicitudBeca solicitudBeca;
    private Integer recursoManutencion;
    
    private VWOtorgamiento() {}
    
    public static VWOtorgamiento getInstance() {
        return new VWOtorgamiento();
    }
    
    public VWOtorgamiento(Alumno alumno, Periodo periodoActivo, TipoBecaPeriodo beca,
             Proceso proceso, Usuario usuario, String observaciones, int fase, DatosAcademicos datos,SolicitudBeca solicitud) {
        this.alumno = alumno;
        this.periodo = periodoActivo;
        this.tipoBecaPeriodo = beca;
        this.proceso = proceso;
        this.usuario = usuario;
        this.fecha = new Date();
        this.alta = true;
        this.automatico = false;
        this.asignacionConfirmada = false;
        this.excluirDeposito = false;
        this.observaciones = observaciones;
        this.fase = fase;
        this.identificadorOtorgamiento = new IdentificadorOtorgamiento(new BigDecimal(12));
        this.datosAcademicos=datos;
        this.solicitudBeca=solicitud;
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public TipoBecaPeriodo getTipoBecaPeriodo() {
        return tipoBecaPeriodo;
    }

    public void setTipoBecaPeriodo(TipoBecaPeriodo tipoBecaPeriodo) {
        this.tipoBecaPeriodo = tipoBecaPeriodo;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public IdentificadorOtorgamiento getIdentificadorOtorgamiento() {
        return identificadorOtorgamiento;
    }

    public void setIdentificadorOtorgamiento(IdentificadorOtorgamiento identificadorOtorgamiento) {
        this.identificadorOtorgamiento = identificadorOtorgamiento;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public Boolean getAutomatico() {
        return automatico;
    }

    public void setAutomatico(Boolean automatico) {
        this.automatico = automatico;
    }

    public Boolean getAsignacionConfirmada() {
        return asignacionConfirmada;
    }

    public void setAsignacionConfirmada(Boolean asignacionConfirmada) {
        this.asignacionConfirmada = asignacionConfirmada;
    }

    public Boolean getExcluirDeposito() {
        return excluirDeposito;
    }

    public void setExcluirDeposito(Boolean excluirDeposito) {
        this.excluirDeposito = excluirDeposito;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getFase() {
        return fase;
    }

    public void setFase(Integer fase) {
        this.fase = fase;
    }

    public DatosAcademicos getDatosAcademicos() {
        return datosAcademicos;
    }

    public void setDatosAcademicos(DatosAcademicos datosAcademicos) {
        this.datosAcademicos = datosAcademicos;
    }

    public SolicitudBeca getSolicitudBeca() {
        return solicitudBeca;
    }

    public void setSolicitudBeca(SolicitudBeca solicitudBeca) {
        this.solicitudBeca = solicitudBeca;
    }

    public Integer getRecursoManutencion() {
        return recursoManutencion;
    }

    public void setRecursoManutencion(Integer recursoManutencion) {
        this.recursoManutencion = recursoManutencion;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }
}

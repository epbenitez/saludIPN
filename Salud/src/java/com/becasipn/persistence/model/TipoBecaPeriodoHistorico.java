package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "ent_bitacora_tipo_beca_periodo")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class TipoBecaPeriodoHistorico implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "TipoBecaPeridoHistSeq")
    @SequenceGenerator(name = "TipoBecaPeridoHistSeq", sequenceName = "ENT_BITACORA_TIPO_BECA_PERIO_1", allocationSize = 1)
    private BigDecimal id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    private Float monto;

    @ManyToOne
    private TipoBecaPeriodo tipoBecaPeriodo;

    @ManyToOne
    private Usuario usuario;

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public TipoBecaPeriodo getTipoBecaPeriodo() {
        return tipoBecaPeriodo;
    }

    public void setTipoBecaPeriodo(TipoBecaPeriodo tipoBecaPeriodo) {
        this.tipoBecaPeriodo = tipoBecaPeriodo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}

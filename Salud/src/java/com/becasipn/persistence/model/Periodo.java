/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "cat_periodo")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class Periodo implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "PerSeq")
    @SequenceGenerator(name = "PerSeq", sequenceName = "CAT_PERIODO_ID_SEQ", allocationSize = 1)
    public BigDecimal id;
    private String descripcion;
    private String clave;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaInicial;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaFinal;
    @OneToOne(fetch = FetchType.LAZY)
    private Periodo periodoAnterior;
    private Boolean estatus;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioModifico;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaModifico;
    @ManyToOne(fetch = FetchType.LAZY)
    private CicloEscolar cicloEscolar;
    
    public Periodo(){
    }

    public Periodo(BigDecimal id) {
        this.id = id;
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Periodo getPeriodoAnterior() {
        return periodoAnterior;
    }

    public void setPeriodoAnterior(Periodo periodoAnterior) {
        this.periodoAnterior = periodoAnterior;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }

    public Usuario getUsuarioModifico() {
        return usuarioModifico;
    }

    public void setUsuarioModifico(Usuario usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    public Date getFechaModifico() {
        return fechaModifico;
    }

    public void setFechaModifico(Date fechaModifico) {
        this.fechaModifico = fechaModifico;
    }

    public CicloEscolar getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(CicloEscolar cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    @Override
    public String toString() {
        return "Periodo{" + "id=" + id + ", descripcion=" + descripcion + ", clave=" + clave + ", fechaInicial=" + fechaInicial + ", fechaFinal=" + fechaFinal + ", periodoAnterior=" + periodoAnterior + ", estatus=" + estatus + ", usuarioModifico=" + usuarioModifico + ", fechaModifico=" + fechaModifico + '}';
    }
    
    public boolean equals(Periodo otroPeriodo) {
        Boolean result = false;
        if (otroPeriodo.getId().equals(id) && otroPeriodo.getClave().equals(clave) && otroPeriodo.getEstatus().equals(estatus)) {
            result = true;
        }
        return result;
    }
}

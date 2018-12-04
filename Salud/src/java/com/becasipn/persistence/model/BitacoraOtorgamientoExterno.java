
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import java.util.Date;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(name = "ENT_BITACORA_OTORGAMIENTOS_EXT")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
public class BitacoraOtorgamientoExterno implements Serializable, BaseEntity {
    @Id
    @GeneratedValue(generator = "BitOtorExt_Sec")
    @SequenceGenerator(name = "BitOtorExt_Sec", sequenceName = "SEQ_ENT_BIT_OTORG_EXT", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne
    private OtorgamientoExterno otorgamientoExterno;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @ManyToOne
    private Usuario usuario;
    private String foliobaja;
    private String comentarios;
    private Boolean activo;
    @ManyToOne
    private UnidadAcademica unidadacademica;
    @ManyToOne
    private Periodo periodo;
    
    public BitacoraOtorgamientoExterno(){
    }
    
    public BitacoraOtorgamientoExterno(BigDecimal id) {
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
    
    public OtorgamientoExterno getOtorgamientoExterno() {
        return otorgamientoExterno;
    }

    public void setOtorgamientoExterno(OtorgamientoExterno otorgamientoExterno) {
        this.otorgamientoExterno = otorgamientoExterno;
    }
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFoliobaja() {
        return foliobaja;
    }

    public void setFoliobaja(String foliobaja) {
        this.foliobaja = foliobaja;
    }
    
    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public UnidadAcademica getUnidadacademica() {
        return unidadacademica;
    }

    public void setUnidadacademica(UnidadAcademica unidadacademica) {
        this.unidadacademica = unidadacademica;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "BitacoraOtorgamientoExterno{" + "id=" + id + ", otorgamientoExterno=" + otorgamientoExterno + ", fecha=" + fecha + ", usuario=" + usuario + ", foliobaja=" + foliobaja + ", comentarios=" + comentarios + ", activo=" + activo + ", unidadacademica=" + unidadacademica + ", periodo=" + periodo + '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        BitacoraOtorgamientoExterno another = (BitacoraOtorgamientoExterno) obj;
        if (another instanceof BitacoraOtorgamientoExterno) {
            return another.getId().equals(id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
}
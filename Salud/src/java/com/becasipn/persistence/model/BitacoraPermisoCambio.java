package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "rmm_bitacora_permiso_cambio")
public class BitacoraPermisoCambio implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "BitacoraPermisoCambioSeq")
    @SequenceGenerator(name = "BitacoraPermisoCambioSeq", sequenceName = "rmm_bitacora_permcamb_id_seq", allocationSize = 1)
    private BigDecimal id;

    @JoinColumn(name = "alumno_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;

    @JoinColumn(name = "usuariomodifico_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuariomodifico;
    
    @JoinColumn(name = "tipopermisocambio_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoPermisoCambio tipoPermisoCambio;
  
    private String cambioCorreoElectronico;

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

    public Usuario getUsuariomodifico() {
        return usuariomodifico;
    }

    public void setUsuariomodifico(Usuario usuariomodifico) {
        this.usuariomodifico = usuariomodifico;
    }

    public TipoPermisoCambio getTipoPermisoCambio() {
        return tipoPermisoCambio;
    }

    public void setTipoPermisoCambio(TipoPermisoCambio tipoPermisoCambio) {
        this.tipoPermisoCambio = tipoPermisoCambio;
    }

    public String getCambioCorreoElectronico() {
        return cambioCorreoElectronico;
    }

    public void setCambioCorreoElectronico(String cambioCorreoElectronico) {
        this.cambioCorreoElectronico = cambioCorreoElectronico;
    }
    
}

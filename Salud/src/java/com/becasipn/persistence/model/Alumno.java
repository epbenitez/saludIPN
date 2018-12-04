/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES 2015
 *
 */
package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.ParameterMode;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author Patricia Benitez
 */
@Entity
@Table(name = "ent_alumno")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
// <editor-fold desc="Mapeos para asignación de alumnos y asignacion de tarjetas bancarias" defaultstate="collapsed" >
@SqlResultSetMappings({
    @SqlResultSetMapping(
            name = "AlumnoAsignacionesMapping",
            entities = {
                @EntityResult(entityClass = Alumno.class) //0
            },
            columns = {
                @ColumnResult(name = "anterior"),    //1 Nombre becaAnterior     
                @ColumnResult(name = "propuestaId"), //2 Id Tipo Beca
                @ColumnResult(name = "propuestaNombre"), //3 Nombre Tipo Beca
                @ColumnResult(name = "tbp_id"), //4 Id Tipo Beca Periodo
                @ColumnResult(name = "oPasado"), //5 Id Otorgamiento anterior 
                @ColumnResult(name = "programaPropuesto")//6 Programa de beca propuesto    
            }),
    @SqlResultSetMapping(
            name = "TarjetaAsignacionesMapping",
            entities = {
                @EntityResult(entityClass = Alumno.class, fields = {
                    @FieldResult(name = "id", column = "alumnoId")
                }),
                @EntityResult(entityClass = UnidadAcademica.class, fields = {
                    @FieldResult(name = "id", column = "unidadAcademicaId")
                })
            },
            columns = {
                @ColumnResult(name = "numTarjetaBancaria")
            })
})
// </editor-fold>
public class Alumno implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(generator = "AluSeq")
    @SequenceGenerator(name = "AluSeq", sequenceName = "ENT_ALUMNO_ID_SEQ", allocationSize = 1)
    private BigDecimal id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String curp;
    @ManyToOne(fetch = FetchType.LAZY)
    private EstadoCivil estadoCivil;
    private String boleta;
    private String preboleta;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaDeNacimiento;
    @ManyToOne(fetch = FetchType.LAZY)
    private Genero genero;
    @ManyToOne(fetch = FetchType.LAZY)
    private EntidadFederativa entidadDeNacimiento;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Direccion direccion;
    private String celular;
    @ManyToOne(fetch = FetchType.LAZY)
    private CompaniaCelular companiaCelular;
    private String correoElectronico;   
    private Boolean beneficiarioOportunidades;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Usuario usuario;
    private Boolean estatus;
    private String correoElectronicoAlterno;
    private String telefonoCasa;
    private Boolean datosBancarios;
    private Boolean permiteingresocuentaexterna;
    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY)
    private Set<SolicitudBeca> cuestionarios;
    @Transient
    private String becaAnterior;
    @Transient
    private TipoBecaPeriodo becaPropuesta;
    @Transient
    private BigDecimal otorgamientoPasadoId;
    @Transient
    private BigDecimal monto;
    @Transient
    private BigDecimal tarjetaBancariaId;
    @Transient
    private Boolean montoVariable;
    @Transient
    private AlumnoDAE origenDAE;
    //    @OneToMany(fetch = FetchType.LAZY, mappedBy="alumno")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alumno", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @OrderBy("id DESC")
    private List<DatosAcademicos> listaDatosAcademicos = new ArrayList();
    @Transient
    private DatosAcademicos datosAcademicos = new DatosAcademicos();
    @Transient
    private Depositos deposito = new Depositos();

    public Alumno() {
    }

    @Override
    public BigDecimal getId() {
        return id;
    }

    @Override
    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public Date getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Date fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public EntidadFederativa getEntidadDeNacimiento() {
        return entidadDeNacimiento;
    }

    public void setEntidadDeNacimiento(EntidadFederativa entidadDeNacimiento) {
        this.entidadDeNacimiento = entidadDeNacimiento;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public CompaniaCelular getCompaniaCelular() {
        return companiaCelular;
    }

    public void setCompaniaCelular(CompaniaCelular companiaCelular) {
        this.companiaCelular = companiaCelular;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Boolean getBeneficiarioOportunidades() {
        return beneficiarioOportunidades;
    }

    public void setBeneficiarioOportunidades(Boolean beneficiarioOportunidades) {
        this.beneficiarioOportunidades = beneficiarioOportunidades;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }

    public String getFullName() {
        return this.apellidoPaterno + " " + this.apellidoMaterno + " " + this.nombre;
    }

    public String getCorreoElectronicoAlterno() {
        return correoElectronicoAlterno;
    }

    public void setCorreoElectronicoAlterno(String correoElectronicoAlterno) {
        this.correoElectronicoAlterno = correoElectronicoAlterno;
    }

    public String getTelefonoCasa() {
        return telefonoCasa;
    }

    public void setTelefonoCasa(String telefonoCasa) {
        this.telefonoCasa = telefonoCasa;
    }

    public String getBecaAnterior() {
        return becaAnterior;
    }

    public void setBecaAnterior(String becaAnterior) {
        this.becaAnterior = becaAnterior;
    }

    public TipoBecaPeriodo getBecaPropuesta() {
        return becaPropuesta;
    }

    public void setBecaPropuesta(TipoBecaPeriodo becaPropuesta) {
        this.becaPropuesta = becaPropuesta;
    }

    public BigDecimal getOtorgamientoPasadoId() {
        return otorgamientoPasadoId;
    }

    public void setOtorgamientoPasadoId(BigDecimal otorgamientoPasadoId) {
        this.otorgamientoPasadoId = otorgamientoPasadoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getTarjetaBancariaId() {
        return tarjetaBancariaId;
    }

    public void setTarjetaBancariaId(BigDecimal tarjetaBancariaId) {
        this.tarjetaBancariaId = tarjetaBancariaId;
    }

    public Boolean getMontoVariable() {
        return montoVariable;
    }

    public void setMontoVariable(Boolean montoVariable) {
        this.montoVariable = montoVariable;
    }

    public AlumnoDAE getOrigenDAE() {
        return origenDAE;
    }

    public void setOrigenDAE(AlumnoDAE origenDAE) {
        this.origenDAE = origenDAE;
    }

    public Set<SolicitudBeca> getCuestionarios() {
        return cuestionarios;
    }

    public void setCuestionarios(Set<SolicitudBeca> cuestionarios) {
        this.cuestionarios = cuestionarios;
    }

    public Boolean getDatosBancarios() {
        return datosBancarios;
    }

    public void setDatosBancarios(Boolean datosBancarios) {
        this.datosBancarios = datosBancarios;
    }

    public Boolean getPermiteingresocuentaexterna() {
        return permiteingresocuentaexterna;
    }

    public void setPermiteingresocuentaexterna(Boolean permiteingresocuentaexterna) {
        this.permiteingresocuentaexterna = permiteingresocuentaexterna;
    }

    public String getPreboleta() {
        return preboleta;
    }

    public void setPreboleta(String preboleta) {
        this.preboleta = preboleta;
    }

    public List<DatosAcademicos> getListaDatosAcademicos() {
        return listaDatosAcademicos;
    }

    public void setListaDatosAcademicos(List<DatosAcademicos> listaDatosAcademicos) {
        this.listaDatosAcademicos = listaDatosAcademicos;
    }

    public void addDatosAcademicos(DatosAcademicos datosAcademicos) {
        this.listaDatosAcademicos.add(datosAcademicos);
        if (datosAcademicos.getAlumno() != this) {
            datosAcademicos.setAlumno(this);
        }
    }

    public DatosAcademicos getDatosAcademicos() {
        return datosAcademicos;
    }

    public void setDatosAcademicos(DatosAcademicos datosAcademicos) {
        this.datosAcademicos = datosAcademicos;
    }

    public Depositos getDeposito() {
        return deposito;
    }

    public void setDeposito(Depositos deposito) {
        this.deposito = deposito;
    }

    @Override
    public String toString() {
        return "Alumno{" + "id=" + id + ", boleta=" + boleta + ", direccion=" + direccion + ", beneficiarioOportunidades=" + beneficiarioOportunidades + ", fechaAlta=" + fechaAlta + ", fechaModificacion=" + fechaModificacion + ", usuario=" + usuario + ", estatus=" + estatus + ", permiteingresocuentaexterna=" + permiteingresocuentaexterna + '}';
    }
}
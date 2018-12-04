package com.becasipn.persistence.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
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
 * @author User-02
 */
@Entity
@Table(name = "ent_otorgamientos")
@Cache(alwaysRefresh = true, type = org.eclipse.persistence.annotations.CacheType.NONE)
// <editor-fold desc="Procedimientos almacenados para baja de becas." defaultstate="collapsed" >
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
            name = "BajaBecasPasantia",
            procedureName = "SP_BAJABECASPASANTIA",
            resultSetMappings = {"BajasMapping"},
            parameters = {
                @StoredProcedureParameter(name = "ALTA_", type = Boolean.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "UNIDADACADEMICA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "TIPOBECA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ALUMNOBOLETA", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "PROCESOID", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ORDENAPOR", type = Integer.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ORDENADIR", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "START_", type = Integer.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "END_", type = Integer.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "resultados", type = Object.class, mode = ParameterMode.REF_CURSOR)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "CuentaBajaBecasPasantia",
            procedureName = "SP_BAJABECASPASANTIA_COUNT",
            parameters = {
                @StoredProcedureParameter(name = "ALTA_", type = Boolean.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "UNIDADACADEMICA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "TIPOBECA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ALUMNOBOLETA", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "PROCESOID", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "cuenta", type = Long.class, mode = ParameterMode.OUT)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "BajaBecasIncumplimiento",
            procedureName = "SP_BAJASINCUMPLIMIENTO",
            resultSetMappings = {"BajasMapping"},
            parameters = {
                @StoredProcedureParameter(name = "ALTA_", type = Boolean.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "UNIDADACADEMICA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "TIPOBECA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ALUMNOBOLETA", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "PROCESOID", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ORDENAPOR", type = Integer.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ORDENADIR", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "START_", type = Integer.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "END_", type = Integer.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "resultados", type = Object.class, mode = ParameterMode.REF_CURSOR)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "CuentaBajaBecasIncumplimiento",
            procedureName = "SP_BAJASINCUMPLIMIENTO_COUNT",
            parameters = {
                @StoredProcedureParameter(name = "ALTA_", type = Boolean.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "UNIDADACADEMICA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "TIPOBECA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ALUMNOBOLETA", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "PROCESOID", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "cuenta", type = Long.class, mode = ParameterMode.OUT)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "BajaBecasDiversas",
            procedureName = "SP_BAJASDIVERSAS",
            resultSetMappings = {"BajasMapping"},
            parameters = {
                @StoredProcedureParameter(name = "ALTA_", type = Boolean.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "UNIDADACADEMICA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "TIPOBECA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ALUMNOBOLETA", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "PROCESOID", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ORDENAPOR", type = Integer.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ORDENADIR", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "START_", type = Integer.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "END_", type = Integer.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "resultados", type = Object.class, mode = ParameterMode.REF_CURSOR)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "CuentaBajaBecasDiversas",
            procedureName = "SP_BAJASDIVERSAS_COUNT",
            parameters = {
                @StoredProcedureParameter(name = "ALTA_", type = Boolean.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "UNIDADACADEMICA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "TIPOBECA", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "ALUMNOBOLETA", type = String.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "PROCESOID", type = BigDecimal.class, mode = ParameterMode.IN),
                @StoredProcedureParameter(name = "cuenta", type = Long.class, mode = ParameterMode.OUT)
            }
    )
})
// </editor-fold>
// <editor-fold desc="Mapeos para baja de becas." defaultstate="collapsed" >
@SqlResultSetMappings({
    @SqlResultSetMapping(
            name = "BajasMapping",
            entities = {
                @EntityResult(entityClass = Otorgamiento.class, fields = {
            @FieldResult(name = "id", column = "otorgamientoId"),
            @FieldResult(name = "semestre", column = "otorgamientoSemestre")
        }),
                @EntityResult(entityClass = Alumno.class, fields = {
            @FieldResult(name = "id", column = "alumnoId"),
            @FieldResult(name = "nombre", column = "alumnoNombre")
        }),
                @EntityResult(entityClass = Carrera.class, fields = {
            @FieldResult(name = "id", column = "carreraId")
        }),
                @EntityResult(entityClass = UnidadAcademica.class, fields = {
            @FieldResult(name = "id", column = "uaId"),
            @FieldResult(name = "nombre", column = "uaNombre")
        }),
                @EntityResult(entityClass = TipoBeca.class, fields = {
            @FieldResult(name = "id", column = "tipoBecaId"),
            @FieldResult(name = "nombre", column = "tipoBecaNombre")
        })
            }
    )
})
// </editor-fold>
public class Otorgamiento implements Serializable, BaseEntity, BaseBitacora {

    @Id
    @GeneratedValue(generator = "entOtorgamientosSeq")
    @SequenceGenerator(name = "entOtorgamientosSeq", sequenceName = "ent_otorgamientos_id_seq", allocationSize = 1)
    private BigDecimal id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Alumno alumno;
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo periodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoBecaPeriodo tipoBecaPeriodo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Proceso proceso;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    @ManyToOne(fetch = FetchType.LAZY)
    private IdentificadorOtorgamiento identificadorOtorgamiento;
    private Boolean alta;
    private Boolean automatico;
    private Boolean asignacionConfirmada;
    @Transient
    private Long alumnosTotal;
    private Boolean excluirDeposito;
    @Transient
    private Double sumaMonto;
    private String observaciones;
    private Integer fase;
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Modalidad modalidad;
    @ManyToOne(fetch = FetchType.LAZY)
    private DatosAcademicos datosAcademicos;
    @ManyToOne(fetch = FetchType.LAZY)
    private SolicitudBeca solicitudBeca;
    private Integer recursoManutencion;

    public Otorgamiento() {
    }

     public Otorgamiento(Alumno alumno, Periodo periodoActivo, TipoBecaPeriodo beca,
             Proceso proceso, Usuario usuario, String observaciones, int fase,DatosAcademicos datos,SolicitudBeca solicitud) {
        this.alumno = alumno;
        this.periodo = periodoActivo;
        this.tipoBecaPeriodo = beca;
        this.proceso = proceso;
        this.usuario = usuario;
        this.fecha = new Date();
//        this.semestre = alumno.getSemestre();
//        this.promedio = alumno.getPromedio();
//        this.regular = alumno.getRegular();
//        this.carrera = alumno.getCarrera();
//        this.modalidad = alumno.getModalidad();
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

    public Long getAlumnosTotal() {
        return alumnosTotal;
    }

    public void setAlumnosTotal(Long alumnosTotal) {
        this.alumnosTotal = alumnosTotal;
    }

    public Boolean getExcluirDeposito() {
        return excluirDeposito;
    }

    public void setExcluirDeposito(Boolean excluirDeposito) {
        this.excluirDeposito = excluirDeposito;
    }

    public Double getSumaMonto() {
        return sumaMonto;
    }

    public void setSumaMonto(Double sumaMonto) {
        this.sumaMonto = sumaMonto;
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

    @Override
    public String toString() {
        return "Otorgamiento{" + "id=" + id + ", alumno=" + alumno.getBoleta() + ", periodo=" + periodo.getId() + ", tipoBecaPeriodo=" + (tipoBecaPeriodo==null?"-":tipoBecaPeriodo.getId()) + ", proceso=" + (proceso==null?"-":proceso.getId()) + ", usuario=" + (usuario==null?"-":usuario.getUsuario()) + ", fecha=" + fecha + ", identificadorOtorgamiento=" + identificadorOtorgamiento + ", alta=" + alta + ", automatico=" + automatico + ", asignacionConfirmada=" + asignacionConfirmada + ", excluirDeposito=" + excluirDeposito + ", observaciones=" + observaciones + ", fase=" + fase + ", datosAcademicos=" + datosAcademicos + '}';
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
}

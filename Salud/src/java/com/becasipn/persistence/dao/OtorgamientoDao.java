package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.domain.BecaPeriodoCount;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.Estadistica;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public interface OtorgamientoDao extends DaoBase<Otorgamiento, BigDecimal> {

    public Boolean existeProcesoAsociado(BigDecimal id);

    public Boolean existeAlumnoAsignado(BigDecimal alumno_id);

    public Boolean existeAlumnoAnterior(BigDecimal alumno_id);

    public Boolean existeAlumnoPreasignadoTransporte(BigDecimal alumno_id);

    public Otorgamiento getPreasignacionTransporte(BigDecimal alumno_id);

    public Boolean incumplimientoPromedio(BigDecimal alumno_id);

    public Boolean incumplimientoSemestre(BigDecimal alumno_id);

    public Object[] incumplimientoIngreso(BigDecimal alumno_id);

    public Object[] incumplimientoIngresoNuevos(BigDecimal alumno_id);

    public PaginateUtil otorgamientosPeriodoAnterior(ServerSideUtil ssu);

    public PaginateUtil otorgamientosIncumplimiento(ServerSideUtil ssu);

    public PaginateUtil otorgamientosPasantia(ServerSideUtil ssu);

    public String getNombreBeca(BigDecimal alumno_id, BigDecimal periodo_id);

    public BigDecimal getIdBeca(BigDecimal alumno_id, BigDecimal periodo_id);

    public List<Otorgamiento> existenOtorgamientosAsociados(BigDecimal periodoId);

    public Otorgamiento getOtorgamientoAlumno(BigDecimal alumno_id, BigDecimal periodo_id);

    public Otorgamiento getOtorgamientoBkUniversalAlumno(BigDecimal alumno_id, BigDecimal periodo_id);

    public Otorgamiento getOtorgamientoTransporteAlumno(BigDecimal alumno_id, BigDecimal periodo_id, int tipo);

    public Otorgamiento getOtorgamientoAlumno(BigDecimal alumno_id, BigDecimal periodo_id, BigDecimal tipoBeca_id);

    public List<Otorgamiento> revalidantesOrdenados();

    public List<Object[]> buscarOrdenDeposito(Boolean groupby, BigDecimal periodoId, BigDecimal mes, Integer origenRecursos, BigDecimal programaBeca, BigDecimal nivelAcademico,
            BigDecimal uAcademica, BigDecimal tipoProceso, BigDecimal idOtorgamiento, String fechaDeposito, BigDecimal formaPago, BigDecimal tipoDeposito, int determinacionRecursos,
            ConvocatoriaSubes convocatoriaSubes);

    public List<Otorgamiento> otorgamientosProceso(BigDecimal procesoId);

    public List<Object[]> prelacionProceso(BigDecimal procesoId, Boolean procesoManutencion);

    public List<Otorgamiento> otorgamientosProcesos(BigDecimal procesoId, String procesos);

    public List<Object[]> otorgamientosPeriodo(Periodo periodo, Boolean alta);

    public List<BecaPeriodoCount> otorgamientosBecariosCount(BigDecimal procesoId);
	
	public List<Object[]> otorgamientosPeriodoAcotado(Periodo periodo, Boolean alta, BigDecimal ua);

    public List<BecaPeriodoCount> otorgamientosBecariosPCount(BigDecimal periodoId, Boolean al);

    public List<BecaPeriodoCount> otorgamientosBecariosCountP(BigDecimal procesoId, String procesos);

    public List<Otorgamiento> alumnosExcluidosPeriodo(BigDecimal periodo);

    public Boolean alumnoExcluidoPeriodo(BigDecimal periodo, BigDecimal alumnoId);

    public List<Otorgamiento> buscarAlumnoPeriodoBoleta(BigDecimal periodo, String boleta);

    public List<Otorgamiento> buscarAlumnoPeriodoNombre(BigDecimal periodo, String nombre, String aPaterno, String aMaterno);

    public List<Otorgamiento> buscarAlumnoPeriodoBoletaNombre(BigDecimal periodo, String boleta, String nombre, String aPaterno, String aMaterno);

    public List<Object[]> estadisticaTodo(BigDecimal periodoId);

    public List<Object[]> estadisticaNivel(BigDecimal periodoId, BigDecimal nivelId);

    public List<Object[]> estadisticaUnidad(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId);

    public List<Object[]> estadisticaNivelGenero(BigDecimal periodoId, BigDecimal nivelId);

    public List<Object[]> estadisticaUnidadGenero(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId);

    public List<Object[]> estadisticaBecaGenero(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId, BigDecimal tipoBecaId);

    public List<Object[]> estadisticaNivelDepositos(BigDecimal periodoId, BigDecimal nivelId);

    public List<Object[]> estadisticaUnidadDepositos(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId);

    public List<Object[]> estadisticaBecaDepositos(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId, BigDecimal tipoBecaId);

    public List<Object[]> estadisticaNivelEstatusT(BigDecimal periodoId, BigDecimal nivelId);

    public List<Object[]> estadisticaUnidadEstatusT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId);

    public List<Object[]> estadisticaBecaEstatusT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadId, BigDecimal tipoBecaId);

    public BigDecimal totalAlumnosConOtorgamiento(BigDecimal unidadAcademicaId);

    public String totalAlumnosConOtorgamientoD(Boolean alta, BigDecimal nivelId);

    public int revertirAsignacion();

    public int getCandidatosRevalidacion(Periodo periodoActivo, Integer nivel);

    public Long getCandidatosNuevos(Periodo periodoActivo, Integer nivel);

    public List<Otorgamiento> revalidantes(BigDecimal unidadID, TipoBecaPeriodo tipoBecaPeriodo, Integer nivel, BigDecimal periodoAnteriorId, BigDecimal periodoActual);

    public int getOtorgamientosEscuela(BigDecimal periodoID, BigDecimal unidadID, Integer nivel);

    public List<Otorgamiento> getOtorgamientosAlumno(BigDecimal alumno_id, BigDecimal periodo_id);

    public List<Otorgamiento> getOtorgamientosAlumno(BigDecimal alumno_id);

    public Otorgamiento getPreasignacion(BigDecimal alumnoId, Beca beca);

    public List<Otorgamiento> getPreasignaciones(BigDecimal alumnoId);

    public void savePreAsignacion(Otorgamiento nuevoOtorgamiento);

    public void saveSolicitud(SolicitudBeca solicitudBeca);

    public Otorgamiento otorgamientoDeposito(BigDecimal alumnoId, BigDecimal periodoId, BigDecimal programaBeca);

    public Boolean mostrarOtorgamiento(BigDecimal alumnoId);

    public Long getEstadistica(Estadistica.Tipo tipo, BigDecimal periodoId, BigDecimal nivelId,
            BigDecimal unidadAcademicaId, BigDecimal tipoBecaId, BigDecimal movimientoId, String genero, Object parametros);

    public Long getEstadisticaPendientes(Estadistica.Tipo tipo, BigDecimal periodoId, BigDecimal nivelId,
            BigDecimal unidadAcademicaId, BigDecimal tipoBecaId, BigDecimal movimientoId, String genero, Object parametros);

    public Boolean tieneOtorgamientoPeriodoActual(BigDecimal alumnoId);
    
    public Boolean tieneOtorgamientoPeriodo(BigDecimal alumnoId, BigDecimal periodoId, Boolean transporte);

    public List<Otorgamiento> getUltimoOtorgamiento(Alumno alumno);

    public Otorgamiento otorgamientoTransporteAlumnoManutencion(BigDecimal periodoId, BigDecimal otorgamientoId);

    public List<Date> getFechasOtorgamientos(BigDecimal periodoId);

    public List<Object[]> estadisticaRendimiento(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal tipoBecaId);

    public List<Object[]> estadisticaRegistro(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal tipoBecaId);

    public List<Object[]> estadisticaRegistroT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal tipoBecaId);

    public List<Object[]> estadisticaDepositosT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId);

    public List<Object[]> estadisticaDepositos(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal programaBecaId);

    public Long estadisticaDepositosTotalO(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal programaBecaId, boolean menosMeses);

    public List<Object[]> estadisticaCuentasT(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId);

    public List<Object[]> estadisticaCuentas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId);

    public PaginateUtil estadisticaCuentasTabla(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal estatusId, ServerSideUtil ssu);

    public List<Object[]> estadisticaRendimientoBajas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal tipoBecaId);

    public List<Object[]> estadisticaProgramaBeca(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);

    public List<Object[]> estadisticaProgramaBecaB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);

    public List<Object[]> estadisticaSemestreBecaB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento, Object parametro);

    public List<Object[]> estadisticaPromedioBecaB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento, Object parametro);

    public List<Object[]> estadisticaBajas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);

    public List<Object[]> estadisticaMovimientoAltas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);

    public List<Object[]> estadisticaMovimientoBajas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);

    public List<Object[]> estadisticaCarreras(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);

    public List<Object[]> estadisticaCarrerasB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);

    public List<Object[]> estadisticaTipoBeca(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);

    public List<Object[]> estadisticaTipoBecaB(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);

    public List<Object[]> otorgamientosPasantiaAuto();

    public List<Object[]> otorgamientosIncumplimientoAuto();

    public List<Object[]> otorgamientosReporteBanamex(String periodoId, String nivelId, String unidadAcademicaId, String boleta, Integer tipo);

    public String countSolicitudes(String periodoId, String nivelId, String unidadAcademicaId, String boleta, Integer tipo);

    public String countSolicitudes(String identificador);

    public List<Object[]> getSolicitudes(String identificador);

    public List<Object[]> listadoSolicitantes(String periodoId, String nivelId, String unidadAcademicaId, String boleta, Integer tipo);

    public Boolean tieneValidacionInscripcion(Periodo periodo, BigDecimal alumnoId, Boolean transporte);
    
    public List<Object[]> resumenBecalos(String periodoId);

    public String nombreProcesoBajaOtorgamiento(BigDecimal otorgamientoId);

    public Otorgamiento getOtorgamientoSolicitudPeriodo(BigDecimal solicitudId, BigDecimal periodoId);

    public Otorgamiento getOtorgamientoAnteriorPrograma(Alumno a, Beca b, Periodo periodo);

    public Boolean tieneMovimientoPeriodoActual(BigDecimal alumnoId);

    public Boolean tieneOtorgamientoDistintoUniversal(BigDecimal alumnoId);

    public Boolean tieneOtorgamientoManutencionPeriodoActual(BigDecimal alumno_id);

    public int contadorAlumnoPorBecaPorUA(BigDecimal becaId, BigDecimal unidadAcademicaId);

    public String totalAlumnosBecaUniversal(BigDecimal nivelId);

    public Boolean tieneOtorgamientoUniversal(BigDecimal alumnoId, BigDecimal periodoId);

    public Boolean tieneOtorgamientoComplementaria(BigDecimal alumnoId, BigDecimal periodoId);
    
    public List<Otorgamiento> getOtorgamientosNoUniversal(BigDecimal alumnoId);
    
    public List<Otorgamiento>  existeDuplicadoCurpConOtorgamiento(Alumno a, Periodo p);
    
    public List<LinkedHashMap<String, Object>> getAlumnosConBaja(BigDecimal unidadAcademicaId);
    
    public List<Object[]> reporteEstatus(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal estatusId);
    
    public Boolean tieneOtorgamientoHistorico(BigDecimal alumno_id);
    
    public List<Object[]>  reporteProceso(BigDecimal periodoId, BigDecimal unidadAcademicaId, BigDecimal becaid);
    
    public Long estadisticaBecarios(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaId, BigDecimal movimiento);
    
    public List<Object[]> reporteTotalBecas(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal becaid, BigDecimal movimiento);
    
    public List<Object[]> reporteTotalBecarios(BigDecimal periodoId, BigDecimal nivelId, BigDecimal unidadAcademicaId, BigDecimal movimiento);
    
    public String getNombreBecaBySolicitud(BigDecimal solicitud_id);

}

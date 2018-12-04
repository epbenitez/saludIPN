package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Victor Lozano
 */
public interface ProcesoDao extends DaoBase<Proceso, BigDecimal> {

    public List<Proceso> procesoEnUso(BigDecimal procesoId);

    public List<Proceso> existenProcesosAsociados(BigDecimal periodoId);

    public List<Proceso> asociadaProceso(BigDecimal unidadAcademicaId);

    public boolean existe(Proceso proceso);

    public Proceso existe(BigDecimal unidadAcademicaId, BigDecimal periodoId, BigDecimal tipoProcesoId);

    public Proceso getProcesoBkUniversal(Periodo periodo, DatosAcademicos datosAcademicos);

    public List<Proceso> procesosOtorgamientoPorUnidadAcademica(BigDecimal unidadAcademicaId);

    public List<Proceso> procesosNuevosOtorgamientosUnidadAcademica(BigDecimal unidadAcademicaId);

    public List<Proceso> procesosBajasPorUnidadAcademica(BigDecimal unidadAcademicaId);

    public Boolean procesosBajaPorUnidadAcademica(String unidadAcademicaId, String proceso);

    public PaginateUtil validacionProcesosList(ServerSideUtil ssu, int search_id_periodo, int search_id_ua, int search_id_proceso, int search_id_estatus);

    public List<Proceso> reportesProcesosList(String listaProcesos);

    public List<Proceso> reportesPeriodoList(Boolean alta, String periodoId);

    public Boolean procesoActivoByUa(BigDecimal uaId);

    public PaginateUtil findAllProcesos(ServerSideUtil ssu);

    public List<Proceso> procesoByMovPerUa(String movimientoId, String periodoId, String uaId);

    public List<BigDecimal> estatusProcesos(int periodoId, int uaId, int movimientoId, int estatusId);

}

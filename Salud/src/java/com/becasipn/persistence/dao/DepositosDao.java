package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.Periodo;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Patricia Benítez
 */
public interface DepositosDao extends DaoBase<Depositos, BigDecimal> {
    public List<Object[]> findByOrdenDeposito(BigDecimal id);
    public List<Object[]> obtenerOrdenDeposito(BigDecimal id, boolean rechazados);
    public Depositos depositoCargaRespuesta (BigDecimal ordenId, String referencia, String tarjeta, Float monto);
    public List<Depositos> depositosRechazados (BigDecimal ordenId);
    /**
     * Devuelve la lista de depositos para un alumno. Opcional se puede especificar 
     * periodo y otorgamiento.
     *
     * @author Mario Márquez
     * @param alumnoId
     * @param periodoId
     * @param otorgamientoId
     * @return List<Depositos>
     */
    public List<Depositos> depositosAlumno (BigDecimal alumnoId, BigDecimal periodoId, BigDecimal otorgamientoId); 
    public void borrarDeposito(BigDecimal ordenDepositoId);
    public Boolean tieneDepositoMesPeriodo(BigDecimal mes, BigDecimal periodoId, BigDecimal otorgamientoId);
    public Integer totalDepositos(BigDecimal ordenId);
    public List<Object[]> ordenDepositoSUBES(BigDecimal ordenId, String folioBeca, String subes, Periodo periodo);
    public List<Object[]> ordenDepositoModalidad (BigDecimal ordenId);
    public Boolean ordenConDepositosEnEspera (BigDecimal ordenId);
    public List<Depositos> depositosAlumnoReferencia(BigDecimal idAlumno, BigDecimal idTarjeta, BigDecimal idPeriodo);
    public List<LinkedHashMap<String, Object>> depositosReferenciados(BigDecimal alumnoId);
    public Boolean tieneDepositosReferenciados(BigDecimal alumnoId);
    public List<Depositos> depositosOtorgamiento( BigDecimal idOtorgamiento);
    public List<Object[]> depositosPorEstatus (BigDecimal ordenId);
    
    public List<Object[]> reporteDepositosMensual(int periodoId, int nivelId, int uAId, int mesId, int pbId);
    
    public List<Object[]> ReportePagos(int periodoId, int nivelId, int unidadAcademicaId, int pbId, String clavePeriodo);
}
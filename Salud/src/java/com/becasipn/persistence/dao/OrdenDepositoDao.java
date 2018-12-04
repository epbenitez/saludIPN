package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.OrdenDeposito;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Victor Lozano
 */
public interface OrdenDepositoDao extends DaoBase<OrdenDeposito, BigDecimal> {
    public List<OrdenDeposito> existenOrdenesDepositoAsociados(BigDecimal periodoId);
    public List<OrdenDeposito> asociadaUnidadAcademicaOrdenDeposito(BigDecimal unidadAcademicaId);
    public OrdenDeposito findByNombre(String nombre);
    public OrdenDeposito getOrdenDepositoBkUniversal(BigDecimal periodoId, BigDecimal nivelId);
    public List<OrdenDeposito> ordenesDepositoPorEstatus(BigDecimal estatus);
    public void borrarOrdenDeposito(BigDecimal ordenDepositoId);
    public List<Object[]> pivotOrdenesPendientesPorEstatus(BigDecimal estatus, String clavesPeriodos);
    public List<LinkedHashMap<String, Object>> ordenesPendientesPorCuenta(Integer cuenta);
}
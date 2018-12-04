package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
public interface BitacoraTarjetaBancariaDao extends DaoBase<BitacoraTarjetaBancaria, BigDecimal> {
    public BitacoraTarjetaBancaria getUltimaTarjetaBitacora(BigDecimal idAlumno);
    public List<BitacoraTarjetaBancaria> monitoreoTarjetaBancaria(BigDecimal numtarjetabancaria);
    public List<BitacoraTarjetaBancaria> todoLote(String lote);
    public BitacoraTarjetaBancaria ultimaBitacoraTarjeta(BigDecimal tarjetaBancariaId);
    public BitacoraTarjetaBancaria datosAsignacionIdentificador(String identificador);
    public int eliminarBitacorAsignacion(String identificador);
    public Long verificarEstatusReversion(String identificador);
}

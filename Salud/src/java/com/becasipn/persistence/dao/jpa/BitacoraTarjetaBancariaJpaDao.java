package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.BitacoraTarjetaBancariaDao;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
public class BitacoraTarjetaBancariaJpaDao extends JpaDaoBase<BitacoraTarjetaBancaria, BigDecimal> implements BitacoraTarjetaBancariaDao {
    
    public BitacoraTarjetaBancariaJpaDao() {
        super(BitacoraTarjetaBancaria.class);
    }
    
    @Override
    public BitacoraTarjetaBancaria getUltimaTarjetaBitacora(BigDecimal idAlumno) {
        String jpql = "SELECT btc FROM BitacoraTarjetaBancaria btc "
                + "JOIN AlumnoTarjetaBancaria atb ON btc.tarjetaBancaria.id = atb.tarjetaBancaria.id "
                + "WHERE atb.alumno.id = ?1 ORDER BY btc.fechaModificacion DESC, atb.id DESC ";
        List<BitacoraTarjetaBancaria> bitacoras = executeQuery(jpql, idAlumno);
        return (bitacoras == null || bitacoras.isEmpty()) ? null : bitacoras.get(0);
    }
    
    @Override
    public List<BitacoraTarjetaBancaria> monitoreoTarjetaBancaria(BigDecimal idAlumno) {
        String jpql = "SELECT btc FROM BitacoraTarjetaBancaria btc"
                + " JOIN AlumnoTarjetaBancaria atb ON btc.tarjetaBancaria.id = atb.tarjetaBancaria.id "
                + "WHERE atb.alumno.id = ?1 ORDER BY btc.fechaModificacion DESC";
        List<BitacoraTarjetaBancaria> bitacora = executeQuery(jpql, idAlumno);
        return bitacora;
    }
    // SQLINJECTION Eliminado
    // public List<BitacoraTarjetaBancaria> monitoreoTarjetaBancariaUA(BigDecimal ua, BigInteger tarjeta,
    // SQLINJECTION Eliminado
    // public List<BitacoraTarjetaBancaria> monitoreoTarjetaBancaria(BigInteger tarjeta,
    
    @Override
    public List<BitacoraTarjetaBancaria> todoLote(String lote) {
        String jpql = "select btb from BitacoraTarjetaBancaria btb, AlumnoTarjetaBancaria b where btb.tarjetaBancaria.lote = ?1"
                + " and btb.tarjetaBancaria.id = b.tarjetaBancaria.id and btb.estatus.id = 3"
                + " and btb.tarjetaBancaria.id not in(select btb.tarjetaBancaria.id from BitacoraTarjetaBancaria btb where btb.estatus.id > 3)";
        List<BitacoraTarjetaBancaria> bitacora = executeQuery(jpql, lote);
        return bitacora;
    }
    
    @Override
    public BitacoraTarjetaBancaria ultimaBitacoraTarjeta(BigDecimal tarjetaBancariaId) {
        String jpql = "SELECT btb FROM BitacoraTarjetaBancaria btb "
                + " WHERE btb.tarjetaBancaria.id = ?1 "
                + " ORDER BY btb.fechaModificacion desc";
        List<BitacoraTarjetaBancaria> bitacora = executeQuery(jpql, tarjetaBancariaId);
        return bitacora == null || bitacora.isEmpty() ? null : bitacora.get(0);
    }
    
    @Override
    public BitacoraTarjetaBancaria datosAsignacionIdentificador(String identificador){
        String jpql = "SELECT btb FROM BitacoraTarjetaBancaria btb "
                + " WHERE btb.observaciones LIKE ?1";
        List<BitacoraTarjetaBancaria> list = executeQuery(jpql, "%"+identificador+"%");
        return list ==null || list.isEmpty() ? null : list.get(0);
    }
    
    @Override
    public int eliminarBitacorAsignacion(String identificador){
        String jpql = "DELETE FROM BitacoraTarjetaBancaria btb WHERE btb.observaciones LIKE ?1";
        return executeUpdate(jpql, "%"+identificador+"%");
    }
    
    @Override
    public Long verificarEstatusReversion(String identificador){
        String jpql = "SELECT count(btc) FROM BitacoraTarjetaBancaria btc join AlumnoTarjetaBancaria atb ON atb.tarjetaBancaria.id = btc.tarjetaBancaria.id " +
            "WHERE EXISTS(SELECT 1 FROM BitacoraTarjetaBancaria btch " +
            "    WHERE btch.tarjetaBancaria.id = btc.tarjetaBancaria.id and btch.estatus.id >3) " +
            "and atb.identificador= ?1";
        return getCountQuery(jpql, identificador);
    }
}

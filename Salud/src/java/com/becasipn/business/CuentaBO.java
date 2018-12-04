package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import com.becasipn.util.ErrorCuenta;
import com.becasipn.util.Tupla;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Sistemas DSE
 */
public class CuentaBO extends XlsLoaderBO {

    public final EstatusTarjetaBancaria liberado = new EstatusTarjetaBancaria(new BigDecimal(13));
    public final EstatusTarjetaBancaria rechazado = new EstatusTarjetaBancaria(new BigDecimal(14));

    public CuentaBO(Service service) {
        super(service);
    }

    public ErrorCuenta liberar(String chequera, String cuenta, String boleta, SolicitudCuentas sc, Usuario usuario, Date fecha) {

        try {
            Alumno alumno = service.getAlumnoDao().findByBoleta(boleta);
            if (alumno == null) {
                alumno = service.getAlumnoDao().findByPreboleta(boleta);
                if (alumno == null) {
                    return ErrorCuenta.NO_ALUMNO;
                }
            }

            AlumnoTarjetaBancaria relacion;
            if (sc != null) {
                relacion = service.getAlumnoTarjetaDao().findBySolicitudCuenta(sc.getId(), alumno.getId());
            } else {
                relacion = service.getAlumnoTarjetaDao().tarjetaAlumnoV2(alumno.getId());
            }

            if (relacion == null) {
                return ErrorCuenta.NO_RELACION;
            }
            if (relacion.getTarjetaBancaria() != null) {
                if (!relacion.getTarjetaBancaria().getNumtarjetabancaria().equals(chequera + cuenta)) {
                    return ErrorCuenta.CUENTA_DIF;
                }
                return ErrorCuenta.CON_CUENTA;
            }
            TarjetaBancaria tj = new TarjetaBancaria(chequera + cuenta, fecha, 1);
            relacion.setTarjetaBancaria(tj);
            if (relacion.getEstatusTarjBanc().getId().equals(new BigDecimal(12))) {
                relacion.setEstatusTarjBanc(liberado);
                relacion = service.getAlumnoTarjetaDao().update(relacion);
//            tj = service.getTarjetaBancariaDao().findByNumeroCuenta(new BigInteger(chequera+cuenta));
                BitacoraTarjetaBancaria btb = new BitacoraTarjetaBancaria(liberado, relacion.getTarjetaBancaria(), usuario, fecha);
                service.getBitacoraTarjetaBancariaDao().save(btb);
            } else {
                return ErrorCuenta.ESTATUS_INVALIDO;
            }
        } catch (Exception e) {
            return ErrorCuenta.EXISTE_CUENTA;
        }
        return null;
    }

    public ErrorCuenta rechazar(String boleta, SolicitudCuentas sc, Usuario usuario, Date fecha) {
        try {
            Alumno alumno = service.getAlumnoDao().findByBoleta(boleta);
            if (alumno == null) {
                alumno = service.getAlumnoDao().findByPreboleta(boleta);
                if (alumno == null) {
                    return ErrorCuenta.NO_ALUMNO;
                }
            }
            AlumnoTarjetaBancaria relacion;
            if (sc != null) {
                relacion = service.getAlumnoTarjetaDao().findBySolicitudCuenta(sc.getId(), alumno.getId());
            } else {
                relacion = service.getAlumnoTarjetaDao().tarjetaAlumnoV2(alumno.getId());
            }
            if (relacion == null) {
                return ErrorCuenta.NO_RELACION;
            }
            if (relacion.getEstatusTarjBanc().getId().equals(new BigDecimal(12))) {
                relacion.setEstatusTarjBanc(rechazado);
                service.getAlumnoTarjetaDao().update(relacion);
            } else {
                return ErrorCuenta.ESTATUS_INVALIDO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ErrorCuenta.CON_DEPOSITOS;
        }
        return null;
    }

    public int finalizar(SolicitudCuentas sc, Usuario usuario) {
        int finalizados;
        if (sc.getFechaFinalizacion() != null) {
            return -1;
        }
        sc.setFechaFinalizacion(new Date());
        sc.setUsuarioCancelacion(usuario);
        try {
            finalizados = service.getAlumnoTarjetaDao().finalizarSolicitudes(sc.getId());
            service.getSolicitudCuentasDao().update(sc);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return finalizados;
    }

    @Override
    public <T> List<T> processFile(Workbook wb, String lote, Date fecha) {
        return null;
    }

    @Override
    public <T> List<T> processFile(Workbook wb) {
        return null;
    }

    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo) {
        return null;
    }

    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo, Integer accion) {
        return null;
    }

    @Override
    public <T> List<T> processFile(Workbook wb, UnidadAcademica unidadAcademica) throws Exception {
        return null;
    }

    public Tupla<Integer[], List<Tupla<String, String>>> processFile(Workbook wb, SolicitudCuentas sc, int tipoCarga, Usuario usuario) throws Exception {
        Date fecha = new Date();
        Sheet sheet = wb.getSheetAt(0);
        List<Tupla<String, String>> errores = new ArrayList<Tupla<String, String>>();
        Integer res[] = new Integer[3];
        for (int i = 0; i < 3; i++) {
            res[i] = new Integer(0);
        }
        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            try {
                DataFormatter formatter = new DataFormatter();
                //String estatus = formatter.formatCellValue(row.getCell(6));
                String boleta = formatter.formatCellValue(row.getCell(1));
                if (tipoCarga == 0) {//Liberar
                    //    if(estatus.equalsIgnoreCase("Entregada")){
                    String chequera = formatter.formatCellValue(row.getCell(4));
                    String cta = formatter.formatCellValue(row.getCell(5));
                    ErrorCuenta error = liberar(chequera, cta, boleta, sc, usuario, fecha);
                    if (error == null) {
                        res[1]++;
                    } else {
                        res[2]++;
                        errores.add(new Tupla<String, String>(boleta, error.getMsg()));
                    }
//                    }else{
//                        res[2]++;
//                        errores.add(new Tupla<String, String>(boleta, "No tiene el tipo de estatus del archivo que se esta subiendo"));
//                    }
                } else {
//                    if(!estatus.equalsIgnoreCase("Entregada")){
                    ErrorCuenta error = rechazar(boleta, sc, usuario, fecha);
                    if (error == null) {
                        res[1]++;
                    } else {
                        res[2]++;
                        errores.add(new Tupla<String, String>(boleta, error.getMsg()));
                    }
//                    }else{
//                        res[2]++;
//                        errores.add(new Tupla<String, String>(boleta, "No tiene el tipo de estatus del archivo que se esta subiendo"));
//                    }
                }

            } catch (Exception e) {
                res[2]++;
                e.printStackTrace();
            }
            res[0]++;
        }
        Tupla<Integer[], List<Tupla<String, String>>> result = new Tupla<>(res, errores);
        return result;
    }

    public List<Tupla<String, String>> processFile(Workbook wb, SolicitudCuentas sc) throws Exception {
        Sheet sheet = wb.getSheetAt(0);
        List<Tupla<String, String>> estatus = new ArrayList<Tupla<String, String>>();
        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            try {
                DataFormatter formatter = new DataFormatter();
                String boleta = formatter.formatCellValue(row.getCell(0));
                Alumno alumno = service.getAlumnoDao().findByBoleta(boleta);
                if (alumno == null) {
                    alumno = service.getAlumnoDao().findByPreboleta(boleta);
                    if (alumno == null) {
                        estatus.add(new Tupla<String, String>(boleta, ErrorCuenta.NO_ALUMNO.getMsg()));
                        continue;
                    }
                }
                AlumnoTarjetaBancaria relacion = service.getAlumnoTarjetaDao().tarjetaAlumnoV2(alumno.getId());
                if (relacion == null) {
                    estatus.add(new Tupla<String, String>(boleta, ErrorCuenta.NO_RELACION.getMsg()));
                    continue;
                }
                if (relacion.getEstatusTarjBanc() == null) {
                    estatus.add(new Tupla<String, String>(boleta, ErrorCuenta.SIN_ESTATUS.getMsg()));
                    continue;
                }
                estatus.add(new Tupla<String, String>(boleta, relacion.getEstatusTarjBanc().getNombre()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return estatus;
    }

    public String graficaEstatusCuentas(BigDecimal solicitudId) {
        String datos = "";
        List<Object[]> estadisticas = service.getAlumnoTarjetaDao().estadisticaSolicitudes(solicitudId);
        for (Object[] c : estadisticas) {
            if (Arrays.equals(c, estadisticas.get(estadisticas.size() - 1))) {
                datos += "['" + ((String) c[0]).replace("á", "\\u00E1").replace("Á", "\\u00C1")
                                                .replace("é", "\\u00E9").replace("É", "\\u00C9")
                                                .replace("í", "\\u00ED").replace("Í", "\\u00CD")
                                                .replace("ó", "\\u00F3").replace("Ó", "\\u00D3")
                                                .replace("ú", "\\u00FA").replace("Ú", "\\u00DA") 
                      + "', " + ((Long) c[1]) + "]";
            } else {
                datos += "['" + ((String) c[0]).replace("á", "\\u00E1").replace("Á", "\\u00C1")
                                                .replace("é", "\\u00E9").replace("É", "\\u00C9")
                                                .replace("í", "\\u00ED").replace("Í", "\\u00CD")
                                                .replace("ó", "\\u00F3").replace("Ó", "\\u00D3")
                                                .replace("ú", "\\u00FA").replace("Ú", "\\u00DA")  
                        + "', " + ((Long) c[1]) + "], ";
            }
        }
        return datos;
    }
}

/**
 * SISTEMA INFORMÁTICO DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE
 * SERVICIOS ESTUDIANTILES Created on : 07-ene-2016, 13:31:53
 *
 */
package com.becasipn.business;

import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import javax.persistence.PersistenceException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Patricia Benitez
 */
public class NumerosTarjetaBancariaBO extends XlsLoaderBO {

    public NumerosTarjetaBancariaBO(Service service) {
        this.service = service;
    }

    @Override
    public List<String> processFile(Workbook wb, String lote, Date fecha) throws Exception {
        List<String> logScreen = new LinkedList<>();

        Sheet sheet = wb.getSheetAt(0);

        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);

            log.info("Procesando la fila: " + cont + " de " + sheet.getLastRowNum());

            try {
                TarjetaBancaria tarjeta;
                if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    String num = row.getCell(0).getNumericCellValue() + "";
                    tarjeta = new TarjetaBancaria(num, lote, fecha);
                } else {
                    tarjeta = new TarjetaBancaria(row.getCell(0).toString(), lote, fecha);
                }

                if (service.getTarjetaBancariaDao().existeTarjeta(tarjeta.getNumtarjetabancaria())) {
                    logScreen.add("<td>" + (cont) + "</td>"
                            + "<td>" + tarjeta.getNumtarjetabancaria() + "</td>"
                            + "<td><span class='label label-danger'>El número de tarjeta ya se ha almacenado previamente</span></td>");
                } else {
                    TarjetaBancaria aux = service.getTarjetaBancariaDao().save(tarjeta);

                    Usuario usuario = service.getUsuarioDao().findById(SecurityContextHolder.getContext().getAuthentication().getName());
                    BitacoraTarjetaBancaria bitacora = new BitacoraTarjetaBancaria(new EstatusTarjetaBancaria(new BigDecimal(1)), aux, usuario, fecha);
                    service.getBitacoraTarjetaBancariaDao().save(bitacora);

                    logScreen.add("<td>" + (cont) + "</td>"
                            + "<td>" + tarjeta.getNumtarjetabancaria() + "</td>"
                            + "<td><span class='label label-success'>OK</span></td>");
                }
            } catch (PersistenceException eee) {
                eee.printStackTrace();
                logScreen.add("<td>" + (cont) + "</td>"
                        + "<td>" + new BigDecimal(row.getCell(0).getNumericCellValue()) + "</td>"
                        + "<td><span class='label label-danger'>El número de tarjeta ya se ha almacenado previamente</span></td>");
            } catch (Exception e) {
                e.printStackTrace();
                logScreen.add("<td>" + (cont) + "</td>"
                        + "<td>" + row.getCell(0) + "</td>"
                        + "<td><span class='label label-danger'>Error en el formato de la columna</span></td>");
            }
        }

        return logScreen;

    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Victor Lozano
     * @param <T>
     * @param wb
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb) throws Exception {
        return null;
    }

    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
     * @param <T>
     * @param wb
     * @param periodo
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo) throws Exception {
        return null;
    }
    
    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
     * @param <T>
     * @param wb
     * @param periodo
     * @param accion
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, BigDecimal periodo, Integer accion) throws Exception {
        return null;
    }
    
    /**
     * Implementa metodo vacio
     *
     * @author Tania G. Sánchez
     * @param <T>
     * @param wb
     * @param unidadAcademica
     * @return lista
     * @throws java.lang.Exception
     */
    @Override
    public <T> List<T> processFile(Workbook wb, UnidadAcademica unidadAcademica) throws Exception {
        return null;
    }
}
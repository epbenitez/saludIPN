package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Mario Márquez
 */
public class EnvioCorreosBO extends BaseBO {
    
    List<Alumno> alumnos = new ArrayList<>();
    List<List<String>> errores = new ArrayList<>();
    
    public static EnvioCorreosBO getInstance(Service service) {
        return new EnvioCorreosBO(service);
    }
    
    private EnvioCorreosBO(Service service) {
        super(service);
    }
    
    public void setAlumnosList(Workbook wb) {
        HashSet<String> boletas = new HashSet<>();

        Sheet sheet = wb.getSheetAt(0);

        for (int cont = 1; cont <= sheet.getLastRowNum(); cont++) {
            Row row = sheet.getRow(cont);
            log.info("Procesando la fila: " + cont + " de " + sheet.getLastRowNum());
            String boleta;

            // Únicamente se toman en cuenta los valores de la primer columna
            if (row.getFirstCellNum() == 0) {
                if (row.getCell(0).getCellType() != Cell.CELL_TYPE_NUMERIC) {
                    boleta = row.getCell(0).toString().trim();
                } else {
                    boleta = new BigDecimal(row.getCell(0).getNumericCellValue()).toPlainString();
                }
                if (!boleta.isEmpty()) {
                    if (!boletas.add(boleta)) {
                        // Repetida
                        List<String> error = new ArrayList<>();
                        error.add(boleta);
                        error.add("Boleta repetida");
                        errores.add(error);
                    }
                }
            }
        }

        List<String> boletasList = new ArrayList<String>(boletas);
        boletas.clear();
        alumnos = service.getAlumnoDao().getAlumnosDesdeBoletas(boletasList);
        // Una vez que tienes las boletas, es posible iniciar el proceso de envío de correos,
        // y de obtención de errores (lo de aquí abajo), en threads separados.
        boletasErronas(alumnos, boletasList);

        for (String boleta : boletasList) {
            List<String> error = new ArrayList<>();
            error.add(boleta);
            error.add("Boleta no encontrada");
            errores.add(error);
        }
    }

    //Pasar al bo del correos
    private void boletasErronas(List<Alumno> alumnos, List<String> boletas) {
        for (Alumno alumno : alumnos) {
            if (boletas.contains(alumno.getBoleta())) {
                boletas.remove(boletas.indexOf(alumno.getBoleta()));
            }
        }
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public List<List<String>> getErrores() {
        return errores;
    }
    
    
}

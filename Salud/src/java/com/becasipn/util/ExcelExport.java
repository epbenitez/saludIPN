/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Patricia Benitez
 */
public class ExcelExport {

    private InputStream excelStream;
    // Opcionales
    String[] encabezado;
    List<Object[]> matriz;
    
    private static final int WINDOWSIZE = 5000;

    /**
     * Obtiene instancia, para crear reporte a partir de encabezado y una matriz
     * de objetos.
     *
     * @param encabezado
     * @param matriz
     * @return ExcelExport
     */
    public static ExcelExport getInstance(String[] encabezado, List<Object[]> matriz) {
        ExcelExport excelExport = new ExcelExport();
        excelExport.setEncabezado(encabezado);
        excelExport.setMatriz(matriz);

        return excelExport;
    }

    public InputStream construyeExcel(String[] encabezado, List<Object[]> matriz) {
        try {
            Integer limite = 6553400;

            int contadorHojas = 0;
            int registros = 0;
            boolean bandera = true;

            XSSFWorkbook libro = new XSSFWorkbook();

            while (true) {
                int renglonNuevaHoja = 1;
                if (registros >= matriz.size()) {
                    break;
                }

                XSSFSheet hoja = libro.createSheet("Hoja " + contadorHojas);

                Row titulos = hoja.createRow(0);

                for (int a = 0; a < encabezado.length; a++) {
                    titulos.createCell(a).setCellValue(encabezado[a]);
                }

                for (int k = registros; k < matriz.size(); k++) {
//                    if (registros != 0 && bandera) {
//                        if (registros % limite == 0) {
//                            contadorHojas++;
//                            bandera = false;
//                            break;
//                        }
//                    }
//                    bandera = true;

                    Row renglon = hoja.createRow(renglonNuevaHoja);
                    Object[] elemento = matriz.get(k);
                    for (int j = 0; j < elemento.length; j++) {
                        renglon.createCell(j).setCellValue(elemento[j] == null ? "" : elemento[j].toString());
                    }
                    registros++;
                    renglonNuevaHoja++;
                }

//                contadorHojas++;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            libro.write(baos);
            excelStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
        }

        return excelStream;
    }

    public String crearExcelPreasignacionesMasivas(String[] encabezado, List<Object[]> matriz) {
        String nombreArchivo = null;

        int contadorHojas = 0;
        int registros = 0;
        int renglonNuevaHoja;

        XSSFWorkbook libro = new XSSFWorkbook();

        while (true) {
            renglonNuevaHoja = 1;
            if (registros >= matriz.size()) {
                break;
            }

            XSSFSheet hoja = libro.createSheet("Hoja " + contadorHojas);

            Row titulos = hoja.createRow(0);

            for (int a = 0; a < encabezado.length; a++) {
                titulos.createCell(a).setCellValue(encabezado[a]);
            }

            Row renglon;
            Object[] elemento;
            for (int k = registros; k < matriz.size(); k++) {
                renglon = hoja.createRow(renglonNuevaHoja);
                elemento = matriz.get(k);
                for (int j = 0; j < elemento.length; j++) {
                    renglon.createCell(j).setCellValue(elemento[j] == null ? "" : elemento[j].toString());
                }
                registros++;
                renglonNuevaHoja++;
            }
        }

        nombreArchivo = "C:\\Reportes\\ReportePreasignacionMasiva";
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        for (String substring : df.format(new Date()).split(" ")) {
            nombreArchivo += "-" + substring;
        }
        nombreArchivo += ".xlsx";

        try {
            File archivo = new File(nombreArchivo);
            FileOutputStream outStream = new FileOutputStream(archivo);
            if (!archivo.getParentFile().exists() && !archivo.getParentFile().mkdirs()) {
                throw new IllegalStateException("No se pudo crear el directorio: " + archivo.getParentFile());
            }
            libro.write(outStream);
            outStream.close();
        } catch (IllegalStateException | IOException e) {
            System.out.println(e.getMessage());
        }

        return nombreArchivo;
    }

    public InputStream construyeExcelDosEncabezados(String[] encabezado, String[] encabezado2, List<Object[]> matriz) {
        try {
            Integer limite = 65534;

            int contadorHojas = 0;
            int registros = 0;
            boolean bandera = true;

            HSSFWorkbook libro = new HSSFWorkbook();

            while (true) {
                int renglonNuevaHoja = 2;
                if (registros >= matriz.size()) {
                    break;
                }

                HSSFSheet hoja = libro.createSheet("Hoja " + contadorHojas);

                HSSFRow titulos = hoja.createRow(0);

                for (int a = 0; a < encabezado.length; a++) {
                    titulos.createCell(a).setCellValue(encabezado[a]);
                }

                titulos = hoja.createRow(1);

                for (int a = 0; a < encabezado2.length; a++) {
                    titulos.createCell(a).setCellValue(encabezado2[a]);
                }

                for (int k = registros; k < matriz.size(); k++) {
                    if (registros != 0 && bandera) {
                        if (registros % limite == 0) {
                            contadorHojas++;
                            bandera = false;
                            break;
                        }
                    }
                    bandera = true;

                    HSSFRow renglon = hoja.createRow(renglonNuevaHoja);
                    Object[] elemento = matriz.get(k);
                    for (int j = 0; j < elemento.length; j++) {
                        renglon.createCell(j).setCellValue(elemento[j] == null ? "" : elemento[j].toString());
                    }
                    registros++;
                    renglonNuevaHoja++;
                }

                contadorHojas++;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            libro.write(baos);
            excelStream = new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            //System.out.println(e.getMessage());
        }

        return excelStream;
    }

    /**
     * Crea el ByteArrayInputStream para regresar al action. Es necesario especificar
     * cómo se llama la hoja del excel. El libro no es necesario, sólo cuando es un excel
     * dinámico.
     *
     * @param libro Con pivot, opcional
     * @param nombreHoja Cómo se llamará la hoja en el archivo excel
     * @return int tamaño de la matriz
     */
    public ByteArrayInputStream getInputStream(XSSFWorkbook libro, String nombreHoja) {
        ZipSecureFile.setMinInflateRatio(0);
        SXSSFWorkbook swb = new SXSSFWorkbook();
        if (libro != null) {
            swb = new SXSSFWorkbook(libro);
        }
        swb.setCompressTempFiles(true);
        
        if (libro != null) {
            swb.getSheet(nombreHoja).setRandomAccessWindowSize(WINDOWSIZE);
            setRenglones(swb.getSheet(nombreHoja));
        } else {
            SXSSFSheet hoja = swb.createSheet(nombreHoja);
            hoja.setRandomAccessWindowSize(WINDOWSIZE);
            setEncabezado(hoja);
            setRenglones(hoja);
        }
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            swb.write(baos);
            swb.close();
            swb.dispose();

            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Puebla el contenido del primer renglón de una XSSFSheet a partir de una
     * arreglo de String.
     *
     * @param hoja    
     */
    public void setEncabezado(Sheet hoja) {
        Row titulos = hoja.createRow(0);
        for (int a = 0; a < encabezado.length; a++) {
            titulos.createCell(a).setCellValue(encabezado[a]);
        }
    }

    /**
     * Puebla el contenido de una XSSFSheet a partir de una matriz de objetos.
     *
     * @param XSSFSheet hoja
     * @return int tamaño de la matriz
     */
    private int setRenglones(Sheet hoja) {
        int size = matriz.size();
        for (int k = 0; k < matriz.size(); k++) {
            Row renglon = hoja.createRow(k + 1);
            Object[] elemento = matriz.get(k);
            for (int j = 0; j < elemento.length; j++) {
                if (elemento[j] == null) {
                    renglon.createCell(j).setCellValue("");
                } else if (elemento[j] instanceof Number) {
                    Double dob = new Double(elemento[j].toString());
                    renglon.createCell(j).setCellValue(dob);
                } else {
                    renglon.createCell(j).setCellValue(elemento[j].toString());
                }
            }
        }
        matriz.clear();

        return size;
    }

    /**
     * Genera una lista de cadenas de texto, para los encabezados del excel En
     * caso de encontrar un '.' en la cadena, se toma como información
     * desechable pues es parte de la consulta SQL Author: Mario Márquez
     *
     * @param map LinkedHashMap<String, Object> Mapa hasheable con cadenas de
     * texto como llaves
     * @return List<String> Lista de cadenas de texto para ser usadas como
     * columnas
     */
    public final List<String> getColumnas(LinkedHashMap<String, Object> map) {
        List<String> columnas = new ArrayList<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String columna = entry.getKey();
            Integer point = columna.indexOf(".");

            // Si no encuentra puntos, entonces es un alias
            if (point == -1) {
                columnas.add(columna);
            }
        }
        return columnas;
    }

    private void setEncabezado(String[] encabezado) {
        this.encabezado = encabezado;
    }

    private void setMatriz(List<Object[]> matriz) {
        this.matriz = matriz;
    }
}

/**
 * SISTEMA DE BECAS DEL INSTITUTO POLITECNICO NACIONAL DIRECCION DE SERVICIOS
 * ESTUDIANTILES 2016
 *
 */
package com.becasipn.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patricia Benítez
 */
public class CSVExport {

    public static InputStream construyeCSV(String[] encabezado, List<Object[]> matriz) {
        StringBuilder sb = new StringBuilder();

        for (String e : encabezado) {
            sb.append(e).append(",");
        }
        sb.replace(sb.lastIndexOf(","),sb.lastIndexOf(",")+1,"");
        sb.append("\n");

        if (matriz != null && !matriz.isEmpty()) {
            for (Object[] o : matriz) {
                Object linea[] = o;
                for (Object w : linea) {
                    sb.append(w==null?"":w.toString()).append(",");
                }
                sb.replace(sb.lastIndexOf(","),sb.lastIndexOf(",")+1,"");
                sb.append("\n");
            }
        }

//        System.out.println("sb: \n" + sb.toString());
        return new ByteArrayInputStream(sb.toString().getBytes());
    }
/*
    public static void main(String[] args) {

        String[] encabezado = new String[]{"Número de Cuenta",
            "Nombre(s)",
            "Apellido Paterno",
            "Apellido Materno",
            "Importe",
            "Ref Num",
            "Ref AlfaNum",
            "Concepto/Pago",
            "Plazo",
            "CURP",
            "folio",
            "Convocatoria"
        };
        List<Object[]> matriz = new ArrayList<Object[]>();
        matriz.add(encabezado);
        matriz.add(encabezado);
        matriz.add(encabezado);

        construyeCSV(encabezado, matriz);
    }
    */
}

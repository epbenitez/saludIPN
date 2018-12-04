package com.becasipn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Métodos de ayuda para el manejo de archivos
 *
 * @author Patricia Benitez
 */
public class UtilFile {

    private static Logger log = LogManager.getLogger("UtilFile");

    public static String getPostFix(String matricula) {

        String postFix = "";
        String consecutivo = "";
        int consInteger = 0;
        int segmento = 0;

        if (matricula.length() > 5) {
            consecutivo = matricula.substring(5);
            log.debug("consecutivo - " + consecutivo);
            consInteger = Integer.parseInt(consecutivo);
            segmento = (consInteger / 20000) + 1;
            NumberFormat formatter = new DecimalFormat("00");
            postFix = formatter.format(segmento);
        }
        return postFix + "/";

    }

    /**
     * Obtiene el arreglo de bytes de un archivo
     *
     * @param file Archivo del cual se obtendrán sus bytes
     * @return Arreglo de bytes del archivo
     */
    public static byte[] getBytesFromFile(File file) {

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        try {
            InputStream is = new FileInputStream(file);

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }

            // Close the input stream and return bytes
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return bytes;
    }

    /**
     * Determina la ruta de los archivos de alumnos
     *
     * @return String que contiene la ruta de los documentos del alumno
     */
    public static String getPath() {
        String path = "";
        path = Parametros.getStringProperty("path.archivos.alumnos");
        boolean success = (new File(path)).mkdirs();
        log.debug("Path: " + path);
        return path;
    }

    /**
     * Determina la ruta a la que pertenece la matricula del alumno
     *
     * @param matricula Matricula del alumno
     * @return String que contiene la ruta de los documentos del alumno
     */
    public static String getPath(String folioAspirantes) {

        String path = "";
        path = Parametros.getStringProperty("path.archivos.aspirantes") + getPostFix(folioAspirantes) + "/";
        boolean success = (new File(path)).mkdirs();
        log.debug("Path: " + path);
        return path;

    }

    public static String getPathAlumnoFiles() {
        String path = "";
        path = Parametros.getStringProperty("path.archivos.alumnos");
        log.debug("Path: " + path);
        return path;

    }

    public static Date strToDate(String fecha) {
        if (fecha == null || fecha.equals("")) {
            return null;

        }
        Date valor = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy hh:mm");

        try {
            valor = fmt.parse(fecha);

        } catch (ParseException ex) {
            log.error(String.format("Util : error%s ", ex));

        }
        return valor;

    }

    public static Date strToDate(String fecha, String formato) {
        if (fecha == null || fecha.equals("")) {
            return null;
        }
        Date valor = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat(formato);

        try {
            valor = fmt.parse(fecha);
        } catch (ParseException ex) {
            log.error(String.format("Util : error%s ", ex));
        }
        return valor;

    }

    public static String dateToString(Date fecha, String formato) {
        if (fecha == null || fecha.equals("")) {
            return null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat(formato);
        String valor = "";

        try {
            valor = fmt.format(fecha);
        } catch (Exception ex) {
            log.error(String.format("Util : error%s ", ex));
        }
        return valor;
    }

    /**
     * Obtiene el path donde se colocarán los archivos QR
     *
     * @return
     */
    public static String getPathQR() {
        String path = "";
        path = Parametros.getStringProperty("path.archivos.alumnos.qr");
        boolean success = (new File(path)).mkdirs();
        log.debug("Path: " + path);
        return path;
    }

    public static Boolean esImagen(String nombreArchivo) {
        if (nombreArchivo.indexOf("jpg") > 0) {
            return Boolean.TRUE;
        } else if (nombreArchivo.indexOf("jpeg") > 0) {
            return Boolean.TRUE;
        } else if (nombreArchivo.indexOf("gif") > 0) {
            return Boolean.TRUE;
        } else if (nombreArchivo.indexOf("png") > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}

package com.becasipn.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Utilerías para manejo de cadenas en el sistema
 *
 * @author Patricia Benitez
 * @since 1.0
 * @version 1.0
 */
public class StringUtil {

    /**
     * Valida que una cadena contenga caracteres válidos
     *
     * @param cadena Cadena a ser validada
     * @param allowNum Bandera que indica si permite valores numericos
     * @return true | false
     */
    public static boolean isTextoValido(String cadena, boolean allowNum) {

        boolean flagFirst = true;
        boolean especial = false;
        CharacterIterator it = new StringCharacterIterator(cadena);
        for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next(), flagFirst = false) {
            especial = false;
            switch (ch) {
                case '\u00C1':
                case '\u00C9':
                case '\u00CD':
                case '\u00D3':
                case '\u00DA':
                case '\u00E1':
                case '\u00E9':
                case '\u00ED':
                case '\u00F3':
                case '\u00FA':
                case '\u00D1':
                case '\u00F1':
                case '\u00FC':
                case '\u00DC':
                    continue;
                case '_':
                case '-':
                case '/':
                case ',':
                case '.':
                case '\'':
                case ' ':
                    if (flagFirst) {
                        return false;
                    }
                    especial = true;
                    continue;

            }

            if (allowNum) {
                if (ch >= '0' && ch <= '9') {
                    continue;
                }
            }

            if (ch < 'A') {
                return false;
            }
            if (ch > 'Z' && ch < 'a') {
                return false;
            }
            if (ch > 'z') {
                return false;
            }
        }
        if (especial) {
            return false;
        }
        return true;
    }

    /**
     * Pone la primer letra de una cadena en mayúsculas
     *
     * @param s cadena a ser modificada
     * @return Cadena modificada
     */
    public static String capitalizeFirstLettersTokenizer(String s) {
        s = s.toLowerCase();
        final StringTokenizer st = new StringTokenizer(s, " ", true);
        final StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            token = String.format("%s%s", Character.toUpperCase(token.charAt(0)), token.substring(1));
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * Genera la clave de asignatura usando tres caracteres alfabéticos.
     *
     * @param tot Número máximo de asignaturas a registrar
     * @return Mapa con las claves de asignaturas generadas
     */
    public static HashMap getAlphaEnumeration(int tot) {
        char array[] = new char[3];
        int i = 0;
        HashMap claves = new HashMap();
        if (tot > 0) {
            for (char c0 = 'A'; c0 <= 'Z'; c0++) {
                array[0] = c0;
                for (char c1 = 'A'; c1 <= 'Z'; c1++) {
                    array[1] = c1;
                    for (char c2 = 'A'; c2 <= 'Z'; c2++) {
                        array[2] = c2;
                        String s = new String(array);
                        claves.put(i, s);
                        i++;
                        if (i > tot) {
                            return claves;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Remueve los acentos de una cadena de texto
     *
     * @param txt
     * @return
     */
    public static String quitaAcentos(String txt) {
        if (txt != null && txt.length() > 0) {
            txt = txt.replaceAll("Á", "A").replaceAll("É", "E").replaceAll("Í", "I").replaceAll("Ó", "O").replaceAll("Ú", "U");
        }
        return txt;
    }

    /**
     * Genera un string del numero de caracteres especificado
     *
     * @param n Número de caracteres del string
     * @return String
     */
    public static String getRandomString(int n) {
        char[] pw = new char[n];
        int c = 'A';
        int r1 = 0;
        for (int i = 0; i < n; i++) {
            r1 = (int) (Math.random() * 3);
            switch (r1) {
                case 0:
                    c = '0' + (int) (Math.random() * 10);
                    break;
                case 1:
                    c = 'a' + (int) (Math.random() * 26);
                    break;
                case 2:
                    c = 'A' + (int) (Math.random() * 26);
                    break;
            }
            pw[i] = (char) c;
        }
        return new String(pw);
    }

    /**
     * Devuelve la cadena no nula de las dos proporcionadas
     *
     * @param a
     * @param b
     * @return
     */
    public static String getValidString(String a, String b) {
        if (a != null && !a.equals("")) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * Valida el formato del CURP
     *
     * @param fieldValue
     * @return
     */
    public static Boolean formatoCorrectoCurp(String fieldValue) {

        //EL MISMO DEL VALIDATOR Curp.java
        if (fieldValue != null && fieldValue.toString().length() > 0) {
            String value = fieldValue.toString();
            if (value.trim().length() == 18) {
                if (!Pattern.matches("^[A-Za-z]{4}[0-9]{6}[HMhm][A-Za-z]{5}[a-zA-Z0-9]{1}[0-9]{1}$", value)) {
                    // Para cuando se definan los ultimos digitos del curp--- if (!Pattern.matches("^[A-Z]{4}[0-9]{6}[HM][A-Z]{5}[A-Z0-9]{1}[0-9]{1}$", value)) {
                    //addFieldError(fieldName, object);
                    return Boolean.FALSE;
                    //}
                }
            } else {
                //addFieldError(fieldName, object);
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    /**
     * Valida que se especifique un solo correo electronico y el formato
     *
     * @param fieldValue
     * @return
     */
    public static Boolean validaCorreoElectronico(String fieldValue) {
        if (fieldValue.split("@").length > 2) {
            return Boolean.FALSE;
        }

        String value = fieldValue.trim();
        if (!Pattern.matches("^[\\w]([_+-\\.]?[\\w][_+-\\.]?)*@[\\w]{1,}([_+-\\.]?[\\w]{2,})\\.[a-zA-Z]{2,4}(\\.[a-zA-Z]{2,4})?$", value)) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * Construye el count de cualquier SQL query, necesario para la paginación
     * del lado del servidor. Author: Jesus Fernandez Flores
     *
     * @param query
     * @param isNative True si la consulta es native
     * @return
     */
    public static String buildCountQuery(String query, Boolean isNative) {
        StringBuilder sb = new StringBuilder();
        if (isNative) {
            sb.append("select count(*) from (").append(query).append(")");
        } else {
            String[] querySplit = query.split("from|FROM", 2);
            String[] alias = querySplit[0].split(" ", 2);
            sb.append("select count(").append(alias[1].split(",")[0].trim()).append(") from").append(querySplit[1]);
        }
        return sb.toString();
    }
    
    /**
     * Método para agregar filtros a un query 
     * Author: Jesus Fernandez Flores
     *
     * @param consulta query al que se le agregarán los parámetros SQL SAFE
     * @param isNew True si la consulta no trae parámetros
     * contenidos en maps
     * @param maps parámetros
     * @return Arreglo de parámetros en orden
     */
    public static Object[] addParameters(StringBuilder consulta, Boolean isNew, Map<String, Object>... maps){
        Object[] values;
        int totalParameters = 0;
        int noParameter = 0;
        
        for (Map<String, Object> map : maps)
            totalParameters += map.size();

        if (isNew && totalParameters > 0)
            consulta.append(" where ");

        values = new Object[totalParameters];
        for (Map<String, Object> map : maps) {
            if (!map.isEmpty()) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    String key = entry.getKey();
                    values[noParameter] = value;
                    
                    if (noParameter > 0 || (!isNew && noParameter == 0))
                        consulta.append(" and ");
                   
                    if (value instanceof String){
                        values[noParameter] = "%" + String.valueOf(values[noParameter]) + "%";
                        consulta.append(key).append(" like ?").append(String.valueOf(++noParameter));                        
                    }else if (value instanceof BigInteger){
                        values[noParameter] = "%" + String.valueOf(values[noParameter]) + "%";
                        consulta.append("cast(").append(key).append(" as varchar(24))").append(" like ?").append(String.valueOf(++noParameter));
                    }else//else if (value instanceof Integer || value instanceof BigDecimal)                        
                        consulta.append(key).append(" = ?").append(String.valueOf(++noParameter));
                }
            }
        }
        return values;
    }

    /**
     * Método que remplaza el texto de un archivo properties para sustituir
     * wildarcards por variables en tiempo de ejecución.<br>
     * <pre> "Este es un mensaje de prueba y debe tener %1 números. </pre> Al
     * momento de invocar el método se debe pasar una lista de parámetros para
     * sustituir en el mensaje, el primer objeto del arreglo se pondrá en %1, el
     * segundo en %2, y así succesivamente.
     *
     * @param message Mensaje extraido del archivo properties.
     * @param params Valores que serán reemplazados en el mensaje original.
     * @return El mensaje con las sustituciones correspondientes.
     */
    public static String formatMessage(String message, Object... params) {
        if (params.length == 0) {
            return message;
        }

        for (int i = 1; i <= params.length; i++) {
            message = message.replaceAll("%" + i + "", String.valueOf(params[i - 1]));
        }
        return message;
    }
}

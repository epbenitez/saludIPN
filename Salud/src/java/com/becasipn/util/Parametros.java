package com.becasipn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * Clase para leer los parámetros de configuración del sistema. Los parámetros
 * son valores constantes que apoyan la implementación de reglas de negocio. El
 * valor de los parámetros se definen en el archivo Parametros .
 *
 */
public class Parametros {

    private static HashMap hmProp = init();

    /**
     * Crea una instancia del objeto Parámetros (Mapa)
     */
    private static HashMap init() {
        HashMap properties = new HashMap();
        try {
            ResourceBundle resource = ResourceBundle.getBundle("Parametros");
            properties.put("path.archivos.alumnos.qr", loadProperty(resource, "path.archivos.alumnos.qr", ""));
            properties.put("path.archivos.ticket", loadProperty(resource, "path.archivos.ticket", ""));
            properties.put("path.archivos.personal.academico", loadProperty(resource, "path.archivos.personal.academico", ""));
            properties.put("path.archivos.alumnos", loadProperty(resource, "path.archivos.alumnos", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * Lee el valor del parámetro del archivo
     *
     * @param resource
     * @param property nombre de la propiedad
     * @param sDefault default string
     * @return valor de la propiedad
     */
    private static String loadProperty(ResourceBundle resource, String property, String sDefault) {
        String value;
        try {
            value = resource.getString(property);
        } catch (Exception e) {
            value = sDefault;
        }
        return value;
    }

    /**
     * Lee la lista de valores de un parámetro separados por |
     *
     * @param resource
     * @param property nombre de la propiedad
     * @return valor de la propiedad
     */
    private ArrayList loadPropertiesList(ResourceBundle resource, String property) {
        ArrayList list = new ArrayList();
        String toSplit = loadProperty(resource, property, null);
        if (toSplit != null) {
            StringTokenizer st = new StringTokenizer(toSplit, "|");
            while (st.hasMoreTokens()) {
                list.add(st.nextToken());
            }
        }
        return list;
    }

    /**
     * Lee el mapa de atributos
     *
     * @param resource
     * @param property nombre de la propiedad
     * @return mapa de propiedades
     */
    private HashMap loadPropertiesMap(ResourceBundle resource, String property) {
        HashMap hm = new HashMap();
        Iterator it = loadPropertiesList(resource, property).iterator();
        while (it != null && it.hasNext()) {
            String str = (String) it.next();
            int equal;
            if (str != null && (equal = str.indexOf("=")) > 0) {
                hm.put(str.substring(0, equal), str.substring(equal + 1));
            }
        }
        return hm;
    }

    /**
     * Obtiene un parámetro que corresponda con el nombre proporcionado
     *
     * @param prop Nombre de la propiedad
     * @return Valor de la propiedad
     */
    public static Object getProperty(String prop) {
        if (hmProp == null) {
            new Parametros();
        }

        return hmProp.get(prop);
    }

    /**
     * Obtiene el valor como cadena de un parámetro que corresponda con el
     * nombre proporcionado
     *
     * @param prop Nombre de la propiedad
     * @return Valor de la propiedad
     */
    public static String getStringProperty(String prop) {
        return (String) getProperty(prop);
    }

    /**
     * Obtiene el valor como entero de un parámetro que corresponda con el
     * nombre proporcionado
     *
     * @param prop Nombre de la propiedad
     * @return Valor de la propiedad
     */
    public static int getIntProperty(String prop) {
        return Integer.valueOf((String) getProperty(prop));
    }

    /**
     * Obtiene el valor como double de un parámetro que corresponda con el
     * nombre proporcionado
     *
     * @param prop
     * @return Valor de la propiedad
     */
    public static double getDoubleProperty(String prop) {
        return Double.valueOf((String) getProperty(prop));
    }

    /**
     * Obtiene el valor como boolean de un parámetro que corresponde con el
     * nombre proporcionado
     *
     * @param prop
     * @return
     */
    public static boolean getBooleanProperty(String prop) {
        return Boolean.valueOf((String) getProperty(prop));
    }

    /**
     * Obtiene el valor como double de un parámetro que corresponda con el
     * nombre proporcionado
     *
     * @param prop
     * @return Valor de la propiedad
     */
    public static long getLongProperty(String prop) {
        return Long.valueOf((String) getProperty(prop));
    }
}

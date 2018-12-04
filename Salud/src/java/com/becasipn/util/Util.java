/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricia Benitez
 */
public class Util {
    
    public static Map<String,ProgressBarManagerUtil> pbmuMap = new HashMap<>();
    
    public static String getNivel(Integer tipomov, Integer nivel) {       
        String nivelString = "";
        if(tipomov == 1)
            nivelString = "NO";
        else if(tipomov == 2)
            nivelString = "REV";
        switch(nivel){
            case 1:
                nivelString += "NMS";
                break;
            case 2:
                nivelString += "NS";
                break;
            case 3:
                nivelString += "POSTGRADO";
                break;            
            default:  
                nivelString += "OTRO";
                break;                    
        }
        return nivelString;
    }
    
    public static String getDescripCorreo(String[] mapData) {
        Integer opcion = Integer.parseInt(mapData[1]);
        String nivel = mapData[2];
        String uA = mapData[3];
        String period = mapData[4];
        String description = "";
        
        switch(opcion){
            case 1:
                description += "Alumnos Registrados en el periodo actual";
                break;
            case 2:
                description += "Alumnos revalidantes que aún no se han registrado";
                break;
            case 3:
                description += "Alumnos registrados sin ESE finalizado";
                break;            
            case 4:
                description += "Alumnos con depósitos pendientes y datos bancarios incorrectos";
                break;
            case 6:
                description += "Filtros";
                break;
            default:  
                description += "Lista";
                break;                    
        }
        return description;
    }
    
    public static String getNivelUAs(Integer nivel, Integer unidadAcademica){
        String nivelUAs = "";
        if(nivel == 1)
            nivelUAs = "NMS";
        else if (nivel == 2)
            nivelUAs = "NS";
        nivelUAs += unidadAcademica;
        
        return nivelUAs; 
    }
    
    public static Date agregaDias(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static String enmascara(String numero) {

        Float calificacion = Float.parseFloat(numero);

        if (calificacion < 1) {
            return "NP";
        } else if (calificacion >= 1 && calificacion <= 59) {
            return "NA";
        } else {
            return calificacion.toString();
        }
    }

    public static String getURL(String url, HashMap<String, String> params) throws UnsupportedEncodingException, IOException {
        String inputText = "";
        HashMap<String, String> map = new HashMap<String, String>();
        URL postUrl = new URL(url);

        try {
            String data = "";

            for (String param : params.keySet()) {

                String valor = params.get(param);

                data += "&" + URLEncoder.encode(param, "UTF-8") + "=" + URLEncoder.encode(valor, "UTF-8");
            }

            URLConnection conn = postUrl.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                inputText += line;
            }

            //System.out.println(inputText);
            wr.close();
            rd.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }

        return inputText;
    }
    
    public static Boolean CampoValidoAJAX(Object value){
        if(value == null) return Boolean.FALSE;
        if (value instanceof String) {
            if(value.equals("undefined") || value.equals("") || value.equals("-Todas-")){
                return Boolean.FALSE;
            }           
        }else if(value instanceof Integer || value instanceof BigDecimal){
            if(value.toString().equals("0")){
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
    
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }

        String objUno = object1.toString();
        String objDos = object2.toString();

        return objUno.equals(objDos);
    }
    
    public static boolean equivaleTexto(String cadena, String cadenaB) {
        boolean result = false;
        
        Collator c = Collator.getInstance();
        c.setStrength(Collator.PRIMARY);
        int compareResult = c.getCollationKey(cadena).compareTo(c.getCollationKey(cadenaB));
        if (compareResult == 0) {
            result = true;
        }
        
        return result;
    }
    
    public static boolean fechaDatos(String fechaDatos) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = formatter.parse(fechaDatos);
        } catch (Exception e) {
            d = null;
        }
        return new Date().after(d);
    }
}

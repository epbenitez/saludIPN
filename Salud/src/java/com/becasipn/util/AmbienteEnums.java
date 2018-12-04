package com.becasipn.util;

import java.util.LinkedHashMap;
import java.util.Map;
import com.becasipn.domain.Genero;

/**
 * Servicio que obtiene los valores de los enums para desplegarlos en selects
 *
 * @author Patricia Benitez
 * @version $Rev: 1208 $
 * @since 1.0
 */
public class AmbienteEnums {

    private static AmbienteEnums service = new AmbienteEnums();

    /**
     * Crea una nueva instancia de <code>AmbienteEnums</code>
     */
    private AmbienteEnums() {
    }

    /**
     * Singleton Pattern, permite un solo objeto del tipo
     *
     * @return AmbienteEnums
     */
    static public AmbienteEnums getInstance() {
        return service;
    }

    /**
     * Regresa la lista de valores espuesta determinante
     *
     * @return Tipos de respuestas (Si/True, No/false)
     */
    public Map<String, String> getRespuestaBoolean() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("false", "No");
        respuestaBoolean.put("true", "Sí");
        return respuestaBoolean;
    }

    /**
     * Regresa la lista de valores espuesta determinante
     *
     * @return Tipos de respuestas (Si/True,No/false)
     */
    public Map<Integer, String> getRespuestaInteger() {
        Map<Integer, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put(1, "Sí");
        respuestaBoolean.put(0, "No");

        return respuestaBoolean;
    }

    public Genero[] getSexo() {
        return Genero.values();
    }

    public Map<String, String> getEstatus() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("true", "Activo");
        respuestaBoolean.put("false", "Inactivo");
        return respuestaBoolean;
    }

    public Map<Integer, String> getBajas() {
        Map<Integer, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put(4, "Incumplimiento");
        respuestaBoolean.put(5, "Pasantia");
        respuestaBoolean.put(6, "Diversas");
        return respuestaBoolean;
    }

    public Map<String, String> getTipoMovimiento() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("1", "Nuevos Otorgamientos");
        respuestaBoolean.put("2", "Revalidantes");
        respuestaBoolean.put("3", "Verificación de Otorgamientos Seleccionados por Alumno");
        return respuestaBoolean;
    }

    public Map<String, String> getMeses() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("1", "Enero");
        respuestaBoolean.put("2", "Febrero");
        respuestaBoolean.put("3", "Marzo");
        respuestaBoolean.put("4", "Abril");
        respuestaBoolean.put("5", "Mayo");
        respuestaBoolean.put("6", "Junio");
        respuestaBoolean.put("7", "Julio");
        respuestaBoolean.put("8", "Agosto");
        respuestaBoolean.put("9", "Septiembre");
        respuestaBoolean.put("10", "Octubre");
        respuestaBoolean.put("11", "Noviembre");
        respuestaBoolean.put("12", "Diciembre");
        respuestaBoolean.put("13", "Enero");
        return respuestaBoolean;
    }

    public Map<String, String> getTipoDeposito() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("1", "Ordinarios");
        respuestaBoolean.put("2", "Rechazos");
        return respuestaBoolean;
    }

    public Map<String, String> getOrigenRecursos() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("1", "Deposita IPN");
        respuestaBoolean.put("0", "Deposita Fundación");
        return respuestaBoolean;
    }

    public Map<String, String> getCarga() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("0", "Debajo de la mínima");
        respuestaBoolean.put("1", "Mínima");
        respuestaBoolean.put("2", "Media");
        respuestaBoolean.put("3", "Máxima");
        return respuestaBoolean;
    }

    public Map<String, String> getFormaPago() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("1", "Cuenta Bancaria");
        respuestaBoolean.put("2", "Referencia Bancaria");
        respuestaBoolean.put("3", "Clabe");
        return respuestaBoolean;
    }

    public Map<String, String> getFormaPagoShort() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("1", "Cuenta Bancaria");
        respuestaBoolean.put("3", "Clabe");
        return respuestaBoolean;
    }

    /**
     * Regresa la lista de valores espuesta determinante
     *
     * @return Tipos de respuestas (Si/True, No/false)
     */
    public Map<String, String> getMontoExpresado() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("false", "Salarios mínimos");
        respuestaBoolean.put("true", "Deciles");
        return respuestaBoolean;
    }

    public Map<String, String> getDeterminacionRecursos() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("1", "Recurso IPN");
        respuestaBoolean.put("2", "Recurso CNBES");
        return respuestaBoolean;
    }

    public Map<String, String> getTurno() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("V", "Vespertino");
        respuestaBoolean.put("M", "Matutino");
        respuestaBoolean.put("X", "Mixto");
        return respuestaBoolean;
    }

    public Map<String, String> getVisible() {
        Map<String, String> respuestaBoolean = new LinkedHashMap<>();
        respuestaBoolean.put("1", "Si");
        respuestaBoolean.put("0", "No");
        return respuestaBoolean;
    }
}

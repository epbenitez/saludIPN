/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.actions.ajax;

import com.becasipn.actions.BaseAction;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Acciones en AJAX
 *
 * @author Patricia Benitez
 */
public class AjaxAction extends BaseAction {

    private static final long serialVersionUID = -8234837273870043185L;
    private static final String SUCCESS_JSON = "success_json";
    private Map<Object, String> resultado;
    private String folio;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;

    /**
     * Inicializa el objeto <code>AjaxAction</code>.
     *
     * @throws Exception
     */
    public AjaxAction() throws Exception {
        resultado = new LinkedHashMap<Object, String>();
    }

    /**
     * Obtiene el valor de resultado.
     *
     * @return el valor de resultado
     */
    public Map<Object, String> getResultado() {
        return resultado;
    }

    /**
     * Establece el valor de resultado.
     *
     * @param resultado nuevo valor de resultado
     */
    public void setResultado(Map<Object, String> resultado) {
        this.resultado = resultado;
    }

    /**
     * Obtiene el valor de nombres
     *
     * @return el valor de la variable nombres
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Establece el valor de la variable nombres
     *
     * @param nombres nuevo valor de la variable nombres
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Obtiene el valor de primerApellido
     *
     * @return el valor de la variable primerApellido
     */
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    /**
     * Establece el valor de la variable primerApellido
     *
     * @param primerApellido nuevo valor de la variable primerApellido
     */
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    /**
     * Obtiene el valor de segundoApellido
     *
     * @return el valor de la variable segundoApellido
     */
    public String getapellidoMaterno() {
        return apellidoMaterno;
    }

    /**
     * Establece el valor de la variable segundoApellido
     *
     * @param segundoApellido nuevo valor de la variable segundoApellido
     */
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    /**
     * Obtiene el valor de matricula
     *
     * @return el valor de matricula
     */
    public String getFolioRodac() {
        return folio;
    }

    /**
     * Establece el valor de matricula
     *
     * @param matricula nuevo valor de matricula
     */
    public void setFolioRodac(String folio) {
        this.folio = folio;
    }

    private List<String> jsonResult;

    public List<String> getJsonResult() {
        return jsonResult;
    }

    /**
     * Reemplaza los caracteres especiales por unicode
     *
     * @param cadena Cadena con caracteres especiales
     * @return Cadena sin caracteres especiales
     */
    public String replaceSpecialCharacters(String cadena) {
        LOG.debug("::replaceSpecialCharacters:: remplazando caracteres especiales");
        String clone = null;
        if (cadena == null) {
            return clone;
        }
        clone = cadena;
        /*clone = clone.replaceAll("Á", "\u00C1");
         clone = clone.replaceAll("É", "\u00C9");
         clone = clone.replaceAll("Í", "\u00CD");
         clone = clone.replaceAll("Ó", "\u00D3");
         clone = clone.replaceAll("Ú", "\u00DA");
         clone = clone.replaceAll("á", "\u00E1");
         clone = clone.replaceAll("é", "\u00E9");
         clone = clone.replaceAll("í", "\u00ED");
         clone = clone.replaceAll("ó", "\u00F3");
         clone = clone.replaceAll("ú", "\u00FA");*/
        clone = clone.replaceAll("Á", "A");
        clone = clone.replaceAll("É", "E");
        clone = clone.replaceAll("Í", "I");
        clone = clone.replaceAll("Ó", "O");
        clone = clone.replaceAll("Ú", "U");
        clone = clone.replaceAll("á", "a");
        clone = clone.replaceAll("é", "e");
        clone = clone.replaceAll("í", "i");
        clone = clone.replaceAll("ó", "o");
        clone = clone.replaceAll("ú", "u");
        return clone;
    }
}

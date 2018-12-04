package com.becasipn.actions.ajax;

import com.becasipn.actions.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Acciones que facilitan la carga de archivos vía AJAX.
 *
 * @author Patricia Benitez
 * @version $Rev: 1165 $
 * @since 1.0
 */
public class UploadAjaxAction extends BaseAction {

    private Map<Object, String> resultado;

    /**
     * Inicializa el objeto <code>UploadAjaxAction</code>.
     */
    public UploadAjaxAction() {
    }

    /**
     * Obtiene el valor de la variable resultado.
     *
     * @return el valor de la variable resultado.
     */
    public Map<Object, String> getResultado() {
        return resultado;
    }

    /**
     * Establece el valor de la variable resultado.
     *
     * @param resultado nuevo valor de la variable resultado.
     */
    public void setResultado(Map<Object, String> resultado) {
        this.resultado = resultado;
    }

    /**
     * Acción que obtiene el nombre del archivo que se cargo.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS.
     */
    public String getFileName() {
        Map<Object, String> mapFileName = (Map<Object, String>) ActionContext.getContext().getSession().get("mapFileName");
        if (mapFileName != null) {
            resultado = mapFileName;
        } else {
            resultado = new HashMap<Object, String>();
        }
        return SUCCESS;
    }

    /**
     * Obtiene el nombre del archivo adjunto del ticket que se esta creando
     *
     * @return
     */
    public String getTicketFileName() {
        String fileName = (String) ActionContext.getContext().getSession().get("uploadAdjuntoFileName");
        Map<Object, String> mapFileName = new HashMap<Object, String>();
        if (fileName != null) {
            mapFileName.put("uploadAdjuntoFileName", fileName);
            resultado = mapFileName;
        } else {
            resultado = new HashMap<Object, String>();
        }
        return SUCCESS;
    }

}

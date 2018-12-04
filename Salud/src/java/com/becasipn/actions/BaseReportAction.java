package com.becasipn.actions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRParameter;

/**
 * Acción base con las propiedades requeridas para la generación de reportes.
 *
 * @author Patricia Benitez
 * @version $Rev: 1038 $
 * @since 1.0
 */
public abstract class BaseReportAction extends BaseAction {

    private Map<String, Object> parametros;

    /**
     * La ejecución de la acción fue satisfactoria, mostrar el reporte al
     * usuario.
     */
    protected final String REPORTE = "reporte";

    /**
     * Inicializa el objeto <code>BaseReportAction</code>.
     */
    public BaseReportAction() {
        if (parametros == null) {
            parametros = new HashMap<String, Object>();
            parametros.put(JRParameter.REPORT_LOCALE, new Locale("es", "MX"));
        }
    }

    /**
     * Obtiene los parámetros del reporte.
     *
     * @return parámentros del reporte.
     */
    public Map<String, Object> getParametros() {
        return parametros;
    }

}

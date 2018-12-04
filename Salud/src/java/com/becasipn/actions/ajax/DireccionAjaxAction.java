package com.becasipn.actions.ajax;

import com.becasipn.persistence.model.EntidadFederativa;
import com.becasipn.persistence.model.InegiLocalidad;
import com.becasipn.persistence.model.LocalidadColonia;
import com.becasipn.persistence.model.MunicipioDelegacion;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Acciones que facilitan la carga de catálogos de dirección vía AJAX.
 *
 * @author Patricia Benitez
 * @version $Rev: 1165 $
 * @since 1.0
 */
public class DireccionAjaxAction extends JSONAjaxAction {

    private Map<Object, String> resultado;
    private BigDecimal pkEstado;
    private BigDecimal pkMunicipio;
    private BigDecimal pkColonia;
    private BigDecimal pkEstadoBanco;
    private BigDecimal pkMunicipioBanco;
    private BigDecimal pkColoniaBanco;

    /**
     * Inicializa el objeto <code>DireccionAjaxAction</code>.
     */
    public DireccionAjaxAction() {
        resultado = new LinkedHashMap<Object, String>();
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
     * Obtiene el valor de la variable pkEstado.
     *
     * @return el valor de la variable pkEstado.
     */
    public BigDecimal getPkEstado() {
        return pkEstado;
    }

    /**
     * Establece el valor de la variable pkEstado.
     *
     * @param pkEstado nuevo valor de la variable pkEstado.
     */
    public void setPkEstado(BigDecimal pkEstado) {
        this.pkEstado = pkEstado;
    }

    /**
     * Obtiene el valor de la variable pkMunicipio.
     *
     * @return el valor de la variable pkMunicipio.
     */
    public BigDecimal getPkMunicipio() {
        return pkMunicipio;
    }

    /**
     * Establece el valor de la variable pkMunicipio.
     *
     * @param pkMunicipio nuevo valor de la variable pkMunicipio.
     */
    public void setPkMunicipio(BigDecimal pkMunicipio) {
        this.pkMunicipio = pkMunicipio;
    }

    /**
     * Obtiene el valor de la variable pkColonia.
     *
     * @return el valor de la variable pkColonia.
     */
    public BigDecimal getPkColonia() {
        return pkColonia;
    }

    /**
     * Establece el valor de la variable pkColonia.
     *
     * @param pkColonia nuevo valor de la variable pkColonia.
     */
    public void setPkColonia(BigDecimal pkColonia) {
        this.pkColonia = pkColonia;
    }

    /**
     * Acción que obtiene los estados de un país.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getEstados() {
        LOG.debug(String.format("%s : getDireccionEstados", getClass().getName()));
        List<EntidadFederativa> tmp = new LinkedList<EntidadFederativa>();
        tmp = getDaos().getEntidadFederativaDao().findAll();
        Collections.sort(tmp);
        int i = 0;
        for (EntidadFederativa EntidadFederativa : tmp) {
            if (i == 2) {
                resultado.put(-1, "---------------------------------");
            }
            resultado.put(EntidadFederativa.getId(), EntidadFederativa.getNombre());
            i++;
        }
        return SUCCESS;
    }

    /**
     * Acción que obtiene los municipios dado un país y un estado.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getMunicipios() {
//        LOG.debug(String.format("%s : getDireccionMunicipios?pkEstado=%d", getClass().getName(), pkEstado));
        List<MunicipioDelegacion> municipios = getDaos().getRelacionGeograficaDao().getMunicipiosByEstado(pkEstado);
        Collections.sort(municipios);

        if (municipios == null) {
        } else {
            for (MunicipioDelegacion mun : municipios) {
                getJsonResult().add("[\"" + mun.getId()
                        + "\", \"" + mun.getNombre()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;

    }

    /**
     * Acción que obtiene las colonias dado un país, estado y municipio.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getColonias() {
//        LOG.debug(String.format("%s : getDireccionColonias?pkMunicipio=%d", getClass().getName(), pkMunicipio));
        List<LocalidadColonia> colonias = getDaos().getRelacionGeograficaDao().getColoniasByMunicipio(pkMunicipio);
        if (colonias != null) {
            Collections.sort(colonias);
            resultado.put(-1, "-- Selecciona un valor --");
        }
        if (colonias == null) {
        } else {
            for (LocalidadColonia col : colonias) {
                getJsonResult().add("[\"" + col.getId()
                        + "\", \"" + col.getNombre()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getLocalidad() {
        List<InegiLocalidad> localidad = null;
        localidad = getDaos().getInegiLocalidadDao().localidadPorEntidadMunicipio(pkEstado, pkMunicipio);
        if (localidad != null) {
            Collections.sort(localidad);
            resultado.put(-1, "-- Selecciona un valor --");
        }
        if (localidad == null) {
        } else {
            for (InegiLocalidad loc : localidad) {
                getJsonResult().add("[\"" + loc.getId()
                        + "\", \"" + loc.getLocalidad()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getColoniasLista() {
        LOG.debug(String.format("%s : getDireccionColonias?pkMunicipio=%d", getClass().getName(), pkMunicipio));
        List<LocalidadColonia> colonias = getDaos().getRelacionGeograficaDao().getColoniasByMunicipio(pkMunicipio);
        Collections.sort(colonias);
        resultado.put(-1, "-- Selecciona un valor --");
        if (colonias == null) {
        } else {
            for (LocalidadColonia col : colonias) {
                getJsonResult().add("[\"" + col.getId()
                        + "\", \"" + col.getNombre()
                        + " </br><a onclick='javascript:editarColonia(" + col.getId() + ");' href='#'><img src='/resources/images/calendarioescolar/eliminar.png' alt=' Eliminar' />  </a>\"]");
            }
        }
        return SUCCESS_JSON;
    }

    public BigDecimal getPkEstadoBanco() {
        return pkEstadoBanco;
    }

    public void setPkEstadoBanco(BigDecimal pkEstadoBanco) {
        this.pkEstadoBanco = pkEstadoBanco;
    }

    public BigDecimal getPkMunicipioBanco() {
        return pkMunicipioBanco;
    }

    public void setPkMunicipioBanco(BigDecimal pkMunicipioBanco) {
        this.pkMunicipioBanco = pkMunicipioBanco;
    }

    public BigDecimal getPkColoniaBanco() {
        return pkColoniaBanco;
    }

    public void setPkColoniaBanco(BigDecimal pkColoniaBanco) {
        this.pkColoniaBanco = pkColoniaBanco;
    }

    /**
     * Acción que obtiene los municipios dado un país y un estado.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getMunicipiosBanco() {
        List<MunicipioDelegacion> municipios = getDaos().getRelacionGeograficaDao().getMunicipiosByEstado(pkEstadoBanco);
        Collections.sort(municipios);

        if (municipios == null) {
        } else {
            for (MunicipioDelegacion mun : municipios) {
                getJsonResult().add("[\"" + mun.getId()
                        + "\", \"" + mun.getNombre()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;

    }

    /**
     * Acción que obtiene las colonias dado un país, estado y municipio.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getColoniasBanco() {
//        LOG.debug(String.format("%s : getDireccionColonias?pkMunicipio=%d", getClass().getName(), pkMunicipio));
        List<LocalidadColonia> colonias = getDaos().getRelacionGeograficaDao().getColoniasByMunicipio(pkMunicipioBanco);
        Collections.sort(colonias);
        resultado.put(-1, "-- Selecciona un valor --");
        if (colonias == null) {
        } else {
            for (LocalidadColonia col : colonias) {
                getJsonResult().add("[\"" + col.getId()
                        + "\", \"" + col.getNombre()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getColoniasListaBanco() {
        List<LocalidadColonia> colonias = getDaos().getRelacionGeograficaDao().getColoniasByMunicipio(pkMunicipioBanco);
        Collections.sort(colonias);
        resultado.put(-1, "-- Selecciona un valor --");
        if (colonias == null) {
        } else {
            for (LocalidadColonia col : colonias) {
                getJsonResult().add("[\"" + col.getId()
                        + "\", \"" + col.getNombre()
                        + " </br><a onclick='javascript:editarColonia(" + col.getId() + ");' href='#'><img src='/resources/images/calendarioescolar/eliminar.png' alt=' Eliminar' />  </a>\"]");
            }
        }
        return SUCCESS_JSON;
    }
}

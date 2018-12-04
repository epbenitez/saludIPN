package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.UnidadAcademica;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Tania G. Sánchez
 */
public class UnidadAcademicaAjaxAction extends JSONAjaxAction {

    private Map<Object, String> resultado;
    private BigDecimal pkNivelAcademico;
    private BigDecimal pkOrigenRecursos;
    private BigDecimal pkProgramaBeca;
    private BigDecimal pkNivel;
    private Boolean uACorto = false;

    /**
     * Inicializa el objeto <code>DireccionAjaxAction</code>.
     */
    public UnidadAcademicaAjaxAction() {
        resultado = new LinkedHashMap<>();
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

    public BigDecimal getPkOrigenRecursos() {
        return pkOrigenRecursos;
    }

    public void setPkOrigenRecursos(BigDecimal pkOrigenRecursos) {
        this.pkOrigenRecursos = pkOrigenRecursos;
    }

    public BigDecimal getPkProgramaBeca() {
        return pkProgramaBeca;
    }

    public void setPkProgramaBeca(BigDecimal pkProgramaBeca) {
        this.pkProgramaBeca = pkProgramaBeca;
    }

    /**
     * Obtiene el valor de la variable pkNivelAcademico.
     *
     * @return el valor de la variable pkNivelAcademico.
     */
    public BigDecimal getPkNivelAcademico() {
        return pkNivelAcademico;
    }

    /**
     * Establece el valor de la variable pkNivelAcademico.
     *
     * @param pkNivelAcademico nuevo valor de la variable pkNivelAcademico.
     */
    public void setPkNivelAcademico(BigDecimal pkNivelAcademico) {
        this.pkNivelAcademico = pkNivelAcademico;
    }

    public String listado() {
        List<UnidadAcademica> list = getDaos().getUnidadAcademicaDao().findAll();
        Boolean mapa;
        for (UnidadAcademica unidadAcademica : list) {
            if (unidadAcademica.getLatitud() == null && unidadAcademica.getLongitud() == null) {
                mapa = Boolean.FALSE;
            } else {
                mapa = Boolean.TRUE;
            }
            getJsonResult().add("[\"" + unidadAcademica.getNombreCorto()
                    + "\", \"" + unidadAcademica.getClave()
                    + "\", \"" + unidadAcademica.getNivel().getNombre()
                    + "\", \"" + unidadAcademica.getAreasConocimiento().getNombre()
                    + "\", \"<a title='Editar Unidad Académica' class='solo-lectura fancybox' href='/catalogos/edicionUnidadAcademica.action?unidadAcademica.id=" + unidadAcademica.getId() + "'> <span class='fa-stack'> <i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"<a title='Eliminar Unidad Académica' class='solo-lectura table-link danger' href='#' onClick='eliminar(" + unidadAcademica.getId() + ");' > <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-trash-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"" + (mapa
                            ? "<a title='Ubicación de la Unidad Académica' class='fancybox fancybox.iframe' href='http://maps.google.com/?q=" + unidadAcademica.getLatitud() + "," + unidadAcademica.getLongitud() + "&output=embed'> <span style='color:#009FFF' class='fa-stack'> <i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-map-marker fa-stack-1x fa-inverse'></i></span></a>"
                            : "<a title='Ubicación no disponible' class='table-link danger'><span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-map-marker fa-stack-1x fa-inverse'></i></span></a>")
                    + " \"]");
        }
        return SUCCESS_JSON;
    }

    public BigDecimal getPkNivel() {
        return pkNivel;
    }

    public void setPkNivel(BigDecimal pkNivel) {
        this.pkNivel = pkNivel;
    }
        
    /**
     * Acción que obtiene la unidad academica dado el nivel académico.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getUnidadAcademica() {
        //LOG.debug(String.format("%s : getUnidadAcademica?pkOrigenRecursos=%d?pkProgramaBeca=%d?pkNivelAcademico=%d", getClass().getName(), pkOrigenRecursos, pkProgramaBeca, pkNivelAcademico));
        List<UnidadAcademica> unidadAcademica = getDaos().getUnidadAcademicaDao()
                .getUnidadAcademicaPorOrigenRecursosProgramaBecaNivel(pkOrigenRecursos, pkProgramaBeca, pkNivelAcademico);
        if (unidadAcademica != null) {
            for (UnidadAcademica ua : unidadAcademica) {
                getJsonResult().add("[\"" + ua.getId()
                        + "\", \"" + ua.getNombreCorto()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    /**
     * Devuelve el listado de unidades académicas de un cierto nivel
     *
     * @author Victor Lozano
     * @return unidades academicas
     */
    public String get() {
        List<UnidadAcademica> unidadAcademica;
        if (pkNivel == null || pkNivel.equals(new BigDecimal(0))) {
            unidadAcademica = getDaos().getUnidadAcademicaDao().findAll();
        } else {
            unidadAcademica = getDaos().getUnidadAcademicaDao().getUnidadAcademicaPorNivel(pkNivel);
        }
        if (pkNivel.equals(new BigDecimal(1))) {
            getJsonResult().add("[\"0\", \"Todas NMS \",\"0\"]");
        }
        if (pkNivel.equals(new BigDecimal(2))) {
            getJsonResult().add("[\"0\", \"Todas NS \",\"0\"]");
        }
        if (pkNivel.equals(new BigDecimal(0))) {
            if (uACorto) {
                getJsonResult().add("[\"0\", \"Todas \",\"0\"]");
            } else {
                getJsonResult().add("[\"0\", \"Todas las U.A. \",\"0\"]");
            }            
        }
        for (UnidadAcademica ua : unidadAcademica) {
            if (uACorto) {
                getJsonResult().add("[\"" + ua.getId()
                    + "\", \"" + ua.getNombreCorto()
                    + " \",\"" + ua.getClave() + "\"]");
            } else {
                getJsonResult().add("[\"" + ua.getId()
                    + "\", \"" + ua.getNombreSemiLargo()
                    + " \",\"" + ua.getClave() + "\"]");
            }
            
        }
        return SUCCESS_JSON;
    }
    
    public Boolean getUACorto() {
        return uACorto;
    }
    
    public void setUACorto(Boolean uACorto) {
        this.uACorto = uACorto;
    }        
}

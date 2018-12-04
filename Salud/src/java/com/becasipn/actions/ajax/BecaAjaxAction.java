package com.becasipn.actions.ajax;

import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.DepositoUnidadAcademica;
import com.becasipn.util.AmbienteEnums;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class BecaAjaxAction extends JSONAjaxAction {

    private BigDecimal pkOrigenRecursos;
    private BigDecimal nivelId;
    private BigDecimal pkPeriodo;
    private BigDecimal pkProgramaBeca;

    /**
     * Acción que obtiene el programa de la beca dado el origen de los recursos.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getProgramaBeca() {
        //LOG.debug(String.format("%s : getProgramaBeca?pkOrigenRecursos=%d", getClass().getName(), pkOrigenRecursos));
        List<Beca> beca = getDaos().getBecaDao().getProgramaBecaPorOrigenRecursos(pkOrigenRecursos);
        if (beca == null) {
        } else {
            for (Beca ua : beca) {
                getJsonResult().add("[\"" + ua.getId()
                        + "\", \"" + ua.getNombre()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }
    
     public String getOrigenRecursos() {
        List<DepositoUnidadAcademica> dua = getDaos().getDepositoUnidadAcademicaDao().getOrigenRecursos(pkProgramaBeca);
        if (dua == null) {
        } else {
            for (DepositoUnidadAcademica or : dua) {
                getJsonResult().add("[\"" + ( or.getCorrespondeIPN()?"1":"0")
                        + "\", \"" + AmbienteEnums.getInstance().getOrigenRecursos().get( or.getCorrespondeIPN()?"1":"0")
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }
    
    
   /*
    Acción que devuelve un listado de los programas de beca existentes exceptuando Pronabes y Proyecto Beca de transporte
    */    
    public String getProgramasBecasAsignaciones() {
        List<BigDecimal> becaIds = new ArrayList<BigDecimal>(Arrays.asList(new BigDecimal(6), new BigDecimal(9)));
        List<Beca> becas = getDaos().getBecaDao().getTodasExceptuando(becaIds);
        if(becas == null){
        }else {
            for(Beca beca : becas)
                getJsonResult().add("[\"" + beca.getId()
                                    + "\", \"" +beca.getNombre()
                                    + " \"]");
        }
        
        return SUCCESS_JSON;
    }
    
    public String getBecasporNivel() {
        List<Beca> becas = new ArrayList<Beca>();
        if (nivelId.equals(new BigDecimal(0)))
            becas = getDaos().getBecaDao().findAll();
        else
            becas = getDaos().getBecaDao().becasPorNivel(nivelId);
        
        getJsonResult().add("[\"0\", \"-Todas- \"]");
        if(becas != null)
            for(Beca beca : becas)
                getJsonResult().add("[\"" + beca.getId()
                        + "\", \"" + beca.getNombre()
                        + " \"]");
        
        return SUCCESS_JSON;
    }
    
    /**
     * Acción que obtiene el programa de la beca dado el origen de los recursos.
     *
     * @return un string representando el resultado lógico de la ejecución de la
     * acción: SUCCESS
     */
    public String getBeca() {
        List<Beca> beca = getDaos().getBecaDao().becasSinTransportePorPeriodoNivel(pkPeriodo, null, Boolean.TRUE);
        if (beca == null) {
        } else {
            for (Beca ua : beca) {
                getJsonResult().add("[\"" + ua.getId()
                        + "\", \"" + ua.getNombre()
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }
    
    public BigDecimal getPkOrigenRecursos() {
        return pkOrigenRecursos;
    }

    public void setPkOrigenRecursos(BigDecimal pkOrigenRecursos) {
        this.pkOrigenRecursos = pkOrigenRecursos;
    }

    public BigDecimal getNivelId() {
        return nivelId;
    }

    public void setNivelId(BigDecimal nivelId) {
        this.nivelId = nivelId;
    }

    public BigDecimal getPkPeriodo() {
        return pkPeriodo;
    }

    public void setPkPeriodo(BigDecimal pkPeriodo) {
        this.pkPeriodo = pkPeriodo;
    }

    public BigDecimal getPkProgramaBeca() {
        return pkProgramaBeca;
    }

    public void setPkProgramaBeca(BigDecimal pkProgramaBeca) {
        this.pkProgramaBeca = pkProgramaBeca;
    }
    
}
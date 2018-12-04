package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.persistence.model.PresupuestoPeriodo;
import com.becasipn.persistence.model.PresupuestoTipoBecaPeriodo;
import com.becasipn.persistence.model.TipoBecaPeriodo;

/**
 *
 * @author User-02
 */
public class AdministracionPresupuestosAction extends BaseAction {

    public static final String FORMULARIO = "formulario";
    private PresupuestoPeriodo periodo = new PresupuestoPeriodo();
    private TipoBecaPeriodo tipoBeca = new TipoBecaPeriodo();
    private PresupuestoTipoBecaPeriodo presupuestoTipoBecaPeriodo = new PresupuestoTipoBecaPeriodo();

    public static final String PERIODO = "periodo";
    public static final String TIPOBECA = "tipobeca";
    public static final String UNIDADACADEMICA = "unidadacademica";

    /**
     * Lista de elementos del formulario
     *
     * @return
     */
    public String lista() {
        if (periodo.getId() == null) {
            //Listado por periodo
            return PERIODO;
        } else {
            if (tipoBeca.getId() == null) {
                //listado por tipo de beca
                return TIPOBECA;
            } else {
                //listado por unidad academica
                return UNIDADACADEMICA;
            }
        }
    }

    public PresupuestoPeriodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PresupuestoPeriodo periodo) {
        this.periodo = periodo;
    }

    public TipoBecaPeriodo getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(TipoBecaPeriodo tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public PresupuestoTipoBecaPeriodo getPresupuestoTipoBecaPeriodo() {
        return presupuestoTipoBecaPeriodo;
    }

    public void setPresupuestoTipoBecaPeriodo(PresupuestoTipoBecaPeriodo presupuestoTipoBecaPeriodo) {
        this.presupuestoTipoBecaPeriodo = presupuestoTipoBecaPeriodo;
    }

}

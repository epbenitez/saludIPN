package com.becasipn.actions.catalogos;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.PeriodoBO;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.util.UtilFile;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tania Sánchez
 */
public class AdministracionPeriodosAction extends BaseAction implements MensajesCatalogos {

    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";
    private Periodo periodo = new Periodo();
    private String fechaInicial;
    private String fechaFinal;
    private Boolean itsActive = false;

    /**
     * Lista de elementos del formulario.
     *
     * @return
     */
    public String lista() {
        return SUCCESS;
    }

    /**
     * Formulario de registro de un nuevo elemento.
     *
     * @return
     */
    public String form() {
        return SUCCESS;
    }

    /**
     * Acciones para el botón Guardar.
     *
     * @return
     */
    public String guarda() {
        PeriodoBO bo = new PeriodoBO(getDaos());
        //System.out.println("::::PERIODO::::" + periodo);
        if (fechaInicial != null && fechaInicial.length() > 0) {
            Date fInicial = UtilFile.strToDate(fechaInicial, "dd/MM/yyyy");
            periodo.setFechaInicial(fInicial);
        } else {
            addActionMessage("Sin fecha inicial");
            return FORMULARIO;
        }
        if (fechaFinal != null && fechaFinal.length() > 0) {
            Date fInicial = UtilFile.strToDate(fechaFinal, "dd/MM/yyyy");
            periodo.setFechaFinal(fInicial);
        } else {
            addActionMessage("Sin fecha final");
            return FORMULARIO;
        }

        //Si es un nuevo periodo el estatus sera Inactivo.
        if (periodo.getId() == null) {
            periodo.setEstatus(Boolean.FALSE);
        }
        //validamos que la clave no se repita
        if (bo.existeClave(periodo)) {
            addActionError(getText("catalogo.guardado.error.clave.repetida"));
            return ERROR;
        }
        //validamos que la fecha final sea postetior a la fecha inicial.
        if (!bo.fechaValida(periodo)) {
            addActionError(getText("catalogo.guardado.error.fecha.invalida"));
            return ERROR;
        }
        //Validamos que no existan periodos activos.
        if (periodo.getEstatus() && periodo.getId() != null && bo.existenPeriodosActivos(periodo.getId())) {
            addActionError(getText("catalogo.guardado.periodos.activos"));
            return FORMULARIO;
        }
        //Validamos que no existan procesos asociados.
        if (periodo.getEstatus() == Boolean.FALSE && periodo.getId() != null && bo.existenProcesosAsociados(periodo.getId())) {
            addActionError(getText("catalogo.guardado.procesos.asociados"));
            return FORMULARIO;
        }
        //Validamos si tiene periodo anterior
        if (periodo.getPeriodoAnterior() == null) {
            addActionError(getText("catalogo.guardado.error.fecha.invalida"));
            return ERROR;
        }
        if (bo.guardaPeriodo(periodo)) {
            try {//Establecemos en nulo la variable del sistema que contiene la lista de periodos
                 //lo que ayudará a recargarla completa una vez que se solicite esta lista en cualquier módulo con el nuevo dato
                getAmbiente().setPeriodoList(null);
            } catch (Exception ex) {
                Logger.getLogger(AdministracionPeriodosAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            addActionMessage(getText("catalogo.guardado.exito"));
            return FORMULARIO;
        } else {
            addActionError(getText("catalogo.guardado.error"));
            return ERROR;
        }
    }

    public String edicion() {
        Periodo periodoAct = getDaos().getPeriodoDao().getPeriodoActivo();

        if (periodo == null || periodo.getId() == null) {
            addActionError(getText("catalogo.guardado.error"));
        }
        PeriodoBO bo = new PeriodoBO(getDaos());
        //Traemos todos los datos de mi objeto
        periodo = bo.getPeriodo(periodo.getId());

        if (periodoAct != null && periodo.getId().equals(periodoAct.getId())) {
            itsActive = true;
        }

        return SUCCESS;
    }

    public String eliminar() {
        PeriodoBO bo = new PeriodoBO(getDaos());
        if (periodo == null || periodo.getId() == null) {
            addActionError(getText("catalogo.eliminado.error"));
        }
        //Traemos todos los datos de mi objeto
        periodo = bo.getPeriodo(periodo.getId());
        //Validamos que no existan procesos asociados
        if ((periodo != null && periodo.getEstatus()) && periodo.getId() != null && bo.existenProcesosAsociados(periodo.getId())) {
            addActionError(getText("catalogo.eliminado.error.procesos.asociados"));
            return LISTA;
        } //Validamos que no existan presupuestos asociados.
        else if (periodo != null && periodo.getId() != null && bo.existenPresupuestosAsociados(periodo.getId())) {
            addActionError(getText("catalogo.eliminado.error.presupuesto.asociados"));
            return LISTA;
        } //Validamos que no existan tipos de beca asociados.
        else if (periodo != null && periodo.getId() != null && bo.existenTiposBecaAsociados(periodo.getId())) {
            addActionError(getText("catalogo.eliminado.error.tipo.beca.asociados"));
            return LISTA;
        } //Validamos que no existan ordenes de deposito asociados.
        else if (periodo != null && periodo.getId() != null && bo.existenOrdenesDepositoAsociados(periodo.getId())) {
            addActionError(getText("catalogo.eliminado.error.deposito.asociados"));
            return LISTA;
        } //Validamos que no existan otorgamientos asociados.
        else if (periodo != null && periodo.getId() != null && bo.existenOtorgamientosAsociados(periodo.getId())) {
            addActionError(getText("catalogo.eliminado.error.otorgamientos.asociados"));
            return LISTA;
        }
        Boolean res = bo.eliminaPeriodo(periodo);
        if (res) {
            try {//Establecemos en nulo la variable del sistema que contiene la lista de periodos
                 //lo que ayudará a recargarla completa una vez que se solicite esta lista en cualquier módulo con el nuevo dato
                getAmbiente().setPeriodoList(null);
            } catch (Exception ex) {
                Logger.getLogger(AdministracionPeriodosAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            addActionMessage(getText("catalogo.eliminado.exito"));
        } else {
            addActionError(getText("catalogo.eliminado.error"));
        }
        return LISTA;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Boolean getItsActive() {
        return itsActive;
    }

    public void setItsActive(Boolean itsActive) {
        this.itsActive = itsActive;
    }
}

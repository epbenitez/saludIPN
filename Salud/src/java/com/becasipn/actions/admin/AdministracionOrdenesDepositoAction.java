package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.DepositosBO;
import com.becasipn.business.OrdenDepositoBO;
import com.becasipn.business.PeriodoBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.OrdenDeposito;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.TipoDeposito;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministracionOrdenesDepositoAction extends BaseAction implements MensajesAdmin {
    Integer numeroAlumnos = 0;
    private BigDecimal periodo;
    private Integer mes;
    private Integer origenRecursos;
    private BigDecimal programaBeca;
    private BigDecimal nivelAcademico;
    private BigDecimal unidadAcademica;
    private BigDecimal tipoProceso;
    private BigDecimal tipoDeposito;
    private BigDecimal formaPago;
    private String nombre;
    private BigDecimal ordenId;
    private BigDecimal idOtorgamiento;
    private int determinacionRecursos;
    private BigDecimal convocatoria;

    public String form() {
        return SUCCESS;
    }

    public String buscar() {
        return SUCCESS;
    }

    public String generar() {
        PeriodoBO pbo = new PeriodoBO(getDaos());
        Periodo periodoActivo = pbo.getPeriodoActivo();
        OrdenDepositoBO ordenesDepositoBO = new OrdenDepositoBO(getDaos());
        DepositosBO depositoBO = new DepositosBO(getDaos());
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        Periodo periodoPago = getDaos().getPeriodoDao().findById(periodo);
        Beca beca = getDaos().getBecaDao().findById(programaBeca);
        Nivel nivel = getDaos().getNivelDao().findById(nivelAcademico);
        ConvocatoriaSubes csubes = null;
        if (beca.getId().equals(new BigDecimal (5)) || beca.getId().equals(new BigDecimal (7))){ //Manutención, Transporte manutención
            csubes = getDaos().getConvocatoriaSubesDao().findById(convocatoria);
        } else {
            determinacionRecursos = 0;
            convocatoria = new BigDecimal(0);
        }
        //Se obtiene si la orden de deposito es Ordinaria (si el periodo de la orden es el mismo que el periodo activo) o Regularización (Si el periodo de la orden es diferente al periodo activo).
        TipoDeposito tDeposito = getDaos().getTipoDepositoDao().findById(periodoActivo.getId().equals(periodoPago.getId()) ? new BigDecimal("1") : new BigDecimal("2"));
        //Creamos el nombre
        nombre = ordenesDepositoBO.nombreOrdenDeposito(mes, origenRecursos, nivel, beca, tDeposito, periodoPago, formaPago, tipoDeposito, determinacionRecursos,csubes);
        //Insertamos en OrdenDeposito
        OrdenDeposito ordenDeposito = ordenesDepositoBO.creaOrdenDeposito(periodoPago, mes, unidadAcademica, beca, tipoProceso, tDeposito, usuario, nombre, nivel, origenRecursos, formaPago);
        ordenId = ordenDeposito.getId();
        //Generamos la fecha del mes a depositar para obtener el monto variable si es que tiene.
        NumberFormat f1 = new DecimalFormat("00");
        String año;
        if (periodoPago.getClave().substring(5, 6).equals("1")) {
            if (mes == 1) {
                año = periodoPago.getClave().substring(0, 4);
            } else {
                Integer nuevoAño = (Integer.parseInt(periodoPago.getClave().substring(0, 4))) - 1;
                año = nuevoAño.toString();
            }
        } else {
            año = periodoPago.getClave().substring(0, 4);
        }
        String fechaDeposito = "01/" + f1.format(mes) + "/" + año;
        OrdenDepositoBO bo = new OrdenDepositoBO(getDaos());
        //Buscamos a los alumnos que se les depositara
        List<Alumno> alumnosLista = new ArrayList();
        alumnosLista = bo.getListaAlumnosOrdenDeposito(periodo, new BigDecimal(mes), origenRecursos, programaBeca, nivelAcademico,
                unidadAcademica, tipoProceso, idOtorgamiento, fechaDeposito, formaPago, tipoDeposito, determinacionRecursos,csubes);
        //Insertamos alumno por alumno en Depositos
        try {
            Boolean insertCorrecto = depositoBO.insertaDeposito(alumnosLista, periodo, ordenDeposito, usuario);
            if (insertCorrecto) {
                String[] parametros = new String[2];
                parametros[0] = "<a href='/becas/descargarOrdenesDeposito.action?ordenId=" + ordenId.toString() +"'>" + nombre + "</a>";
                addActionMessage(getText("admin.generar.ordenes.deposito.exito", parametros)); //Este
            } else {
                getDaos().getOrdenDepositoDao().borrarOrdenDeposito(ordenDeposito.getId());
                addActionError(getText("admin.generar.ordenes.deposito.error.insercion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Se borran los depositos que pudieron insertarse correctamente en la tabla.
            getDaos().getDepositosDao().borrarDeposito(ordenDeposito.getId());
            getDaos().getOrdenDepositoDao().borrarOrdenDeposito(ordenDeposito.getId());
            addActionError(getText("admin.generar.ordenes.deposito.error"));
        }
        return SUCCESS;
    }

    public Integer getNumeroAlumnos() {
        return numeroAlumnos;
    }

    public void setNumeroAlumnos(Integer numeroAlumnos) {
        this.numeroAlumnos = numeroAlumnos;
    }

    public BigDecimal getPeriodo() {
        return periodo;
    }

    public void setPeriodo(BigDecimal periodo) {
        this.periodo = periodo;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getOrigenRecursos() {
        return origenRecursos;
    }

    public void setOrigenRecursos(Integer origenRecursos) {
        this.origenRecursos = origenRecursos;
    }

    public BigDecimal getProgramaBeca() {
        return programaBeca;
    }

    public void setProgramaBeca(BigDecimal programaBeca) {
        this.programaBeca = programaBeca;
    }

    public BigDecimal getNivelAcademico() {
        return nivelAcademico;
    }

    public void setNivelAcademico(BigDecimal nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }

    public BigDecimal getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(BigDecimal unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public BigDecimal getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(BigDecimal tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public BigDecimal getTipoDeposito() {
        return tipoDeposito;
    }

    public void setTipoDeposito(BigDecimal tipoDeposito) {
        this.tipoDeposito = tipoDeposito;
    }

    public BigDecimal getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(BigDecimal formaPago) {
        this.formaPago = formaPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(BigDecimal ordenId) {
        this.ordenId = ordenId;
    }

    public BigDecimal getIdOtorgamiento() {
        return idOtorgamiento;
    }

    public void setIdOtorgamiento(BigDecimal idOtorgamiento) {
        this.idOtorgamiento = idOtorgamiento;
    }

    public int getDeterminacionRecursos() {
        return determinacionRecursos;
    }

    public void setDeterminacionRecursos(int determinacionRecursos) {
        this.determinacionRecursos = determinacionRecursos;
    }

    public BigDecimal getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(BigDecimal convocatoria) {
        this.convocatoria = convocatoria;
    }
    
}

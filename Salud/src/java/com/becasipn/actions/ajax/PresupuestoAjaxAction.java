package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.business.PresupuestoBO;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.PresupuestoPeriodo;
import com.becasipn.persistence.model.PresupuestoTipoBecaPeriodo;
import com.becasipn.persistence.model.PresupuestoUnidadAcademica;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.VWPresupuestoPeriodo;
import com.becasipn.persistence.model.VWPresupuestoTipoBecaPeriodo;
import com.becasipn.persistence.model.VWPresupuestoUnidadAcademica;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author User-02
 */
public class PresupuestoAjaxAction extends JSONAjaxAction {

    private BigDecimal periodoId;
    private BigDecimal tipoBecaId;
    private BigDecimal presupuestoTipoBecaPeriodoId;
    private BigDecimal unidadAcademicaId;
    private String monto;
    private Integer becas;

    public String listadoPeriodo() {

        List<VWPresupuestoPeriodo> list = new ArrayList<VWPresupuestoPeriodo>();//getDaos().getVwPresupuestoPeriodoDao().findAll();
        Periodo periodo = getDaos().getPeriodoDao().getPeriodoActivo();
        VWPresupuestoPeriodo pp = getDaos().getVwPresupuestoPeriodoDao().getPresupuesto(periodo.getId());
        if (pp != null) {
            list.add(pp);
        } else {
            VWPresupuestoPeriodo vwPresupuestoPeriodo = new VWPresupuestoPeriodo();
            vwPresupuestoPeriodo.setPeriodo(periodo);
            list.add(vwPresupuestoPeriodo);
        }
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        for (VWPresupuestoPeriodo pr : list) {
            BigDecimal montoTotal = (pr.getMontoPresupuestado() == null) ? new BigDecimal(0) : pr.getMontoPresupuestado();
            BigDecimal montoAsignado = (pr.getMontoAsignado() == null) ? new BigDecimal(0) : pr.getMontoAsignado();
            BigDecimal montoEjercido = (pr.getMontoEjercido() == null) ? new BigDecimal(0) : pr.getMontoEjercido();

            String link = ((pr.getMontoPresupuestado() == null || pr.getMontoPresupuestado().compareTo(BigDecimal.ZERO) == 0) ? "#listado" : "/admin/listaPresupuesto.action?periodo.id=" + pr.getPeriodo().getId());
            String onClick = (pr.getPeriodo().getEstatus() && (pr.getMontoPresupuestado() == null || pr.getMontoPresupuestado().compareTo(BigDecimal.ZERO) == 0) ? "onclick='asignarPresupuesto(" + pr.getPeriodo().getId() + ");'" : "");
            String color = ((pr.getMontoPresupuestado() == null || pr.getMontoPresupuestado().compareTo(BigDecimal.ZERO) == 0) ? " style='color:red'" : "");

            getJsonResult().add("[\""
                    + pr.getPeriodo().getClave()
                    + "\", \"<div id='div_" + pr.getPeriodo().getId() + "'>$" + df.format(montoTotal) + "</div>"
                    + "<input type='hidden' id='presupuesto_" + pr.getPeriodo().getId() + "' value='" + pr.getId() + "' /> "
                    + "<input type='hidden' id='periodo_" + pr.getPeriodo().getId() + "' value='" + pr.getPeriodo().getId() + "' />"
                    + "<input type='text' id='monto_" + pr.getPeriodo().getId() + "' name='monto_" + pr.getPeriodo().getId() + "' value='" + montoTotal + "' style='display:none;'  /> "
                    + "\", \"<div >$" + df.format(montoAsignado) + "</div>"
                    + "\", \"<div >$" + df.format(montoEjercido) + "</div>"
                    + (pr.getPeriodo().getEstatus() ? "\", \"<a title='Editar presupuesto' id='ver_" + pr.getPeriodo().getId() + "' href='#listado' onclick='showInput(" + pr.getPeriodo().getId() + ");' class='solo-lectura'> <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-edit fa-stack-1x fa-inverse'></i></span> </a>"
                            : "\", \"<a title='Editar presupuesto' id='ver_" + pr.getPeriodo().getId() + "' href='#listado' class='solo-lectura'> <span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-edit fa-stack-1x fa-inverse'></i></span> </a>")
                    + "      <a title='Guardar presupuesto' id='guardar_" + pr.getPeriodo().getId() + "' href='#listado' onclick='save(" + pr.getPeriodo().getId() + ");' style='display:none;'> <span style='color:green' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-save fa-stack-1x fa-inverse'></i></span> </a>"
                    + "\", \"<a title='Asignar presupuesto por tipo de beca' href='" + link + "' " + onClick + " class='solo-lectura'> <span " + color + " class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-cog fa-stack-1x fa-inverse'></i></span>  </a>"
                    + "\"]");
        }

        return SUCCESS_JSON;
    }

    public String guardarPeriodo() {
        if ((periodoId == null || periodoId.toString().equals("")) && (monto == null || monto.equals(""))) {
            addActionError("No se cuenta con los datos necesarios para realizar la operación: {" + periodoId + "," + monto + "}");
            return SUCCESS_JSON;
        }

        PresupuestoBO bo = new PresupuestoBO(getDaos());
        Periodo periodo = getDaos().getPeriodoDao().findById(periodoId);
        if (periodo == null) {
            addActionError("No se encontró el periodo especificado {" + periodoId + "," + monto + "}");
            return SUCCESS_JSON;
        }
        try {
            //Verificamos que el nuevo monto no sea menor al monto asignado.
            VWPresupuestoPeriodo pp = getDaos().getVwPresupuestoPeriodoDao().getPresupuesto(periodoId);
            BigDecimal montoAsignado = (pp == null || pp.getMontoAsignado() == null) ? new BigDecimal(0) : pp.getMontoAsignado();
            Double nuevoMontoPresupuestado = Double.valueOf(monto);
            if (pp != null) {
                if (nuevoMontoPresupuestado >= montoAsignado.doubleValue()) {
                    //Acatualizamos
                    PresupuestoPeriodo p = new PresupuestoPeriodo();
                    p.setId(pp.getId());
                    p.setPeriodo(periodo);
                    p.setMontoPresupuestado(new BigDecimal(nuevoMontoPresupuestado));
                    p.setUsuarioModifico((Usuario) ActionContext.getContext().getSession().get("usuario"));
                    p.setFechaModificacion(new Date());
                    bo.guardaPresupuesto(p);
                } else {
                    getJsonResult().add("\"El monto que ha sido ya asignado ($" + montoAsignado + ") es superior al monto especificado. Por favor, verifique.\"");
                    return SUCCESS_JSON;
                }

            } else {
                //Insertamos
                PresupuestoPeriodo p = new PresupuestoPeriodo();
                p.setId(null);
                p.setPeriodo(periodo);
                p.setMontoPresupuestado(new BigDecimal(nuevoMontoPresupuestado));
                p.setUsuarioModifico((Usuario) ActionContext.getContext().getSession().get("usuario"));
                p.setFechaModificacion(new Date());
                bo.guardaPresupuesto(p);
            }
        } catch (Exception e) {
            getJsonResult().add("\"El formato especificado no es correcto:" + e.getLocalizedMessage().replaceAll("\"", "'") + ". Por favor, verifique.\"");
            return SUCCESS_JSON;
        }

        getJsonResult().add("\"OK\"");
        return SUCCESS_JSON;
    }

    public String datosPeriodo() {
        if (periodoId == null) {
            getJsonResult().add("\"No se encontraron datos para el periodo especificado: " + periodoId + ". Por favor, verifique.\"");
            return SUCCESS_JSON;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        VWPresupuestoPeriodo pr = getDaos().getVwPresupuestoPeriodoDao().getPresupuesto(periodoId);
        BigDecimal montoTotal = (pr == null || pr.getMontoPresupuestado() == null) ? new BigDecimal(0) : pr.getMontoPresupuestado();
        BigDecimal montoAsignado = (pr == null || pr.getMontoAsignado() == null) ? new BigDecimal(0) : pr.getMontoAsignado();
        BigDecimal montoEjercido = (pr == null || pr.getMontoEjercido() == null) ? new BigDecimal(0) : pr.getMontoEjercido();
        String clavePeriodo = (pr == null) ? getDaos().getPeriodoDao().findById(periodoId).getClave() : pr.getPeriodo().getClave();
        getJsonResult().add("[\""
                + clavePeriodo
                + "\", \"<div >$" + df.format(montoTotal) + "</div>"
                + "\", \"<div >$" + df.format(montoAsignado) + "</div>"
                + "\", \"<div >$" + df.format(montoEjercido) + "</div>"
                + "\"]");

        return SUCCESS_JSON;
    }

    public String listadoTipoBeca() {

        if (periodoId == null) {
            getJsonResult().add("\"No se encontraron datos para el periodo especificado: " + periodoId + ". Por favor, verifique.\"");
            return ERROR_JSON;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        List<VWPresupuestoTipoBecaPeriodo> list = getDaos().getVwPresupuestoTipoBecaPeriodoDao().getPresupuesto(periodoId);
        for (VWPresupuestoTipoBecaPeriodo pr : list) {
            BigDecimal montoTotal = (pr.getMontoPresupuestado() == null) ? new BigDecimal(0) : pr.getMontoPresupuestado();
            BigDecimal montoAsignado = (pr.getMontoAsignado() == null) ? new BigDecimal(0) : pr.getMontoAsignado();
            BigDecimal montoEjercido = (pr.getMontoEjercido() == null) ? new BigDecimal(0) : pr.getMontoEjercido();

            String link = ((pr.getMontoPresupuestado() == null || pr.getMontoPresupuestado().compareTo(new BigDecimal(0)) == 0) ? "#listado" : "/admin/listaPresupuesto.action?periodo.id=" + periodoId + "&tipoBeca.id=" + pr.getTipoBecaPeriodo().getId() + "&presupuestoTipoBecaPeriodo.id=" + pr.getId() + "");
            String onClick = (pr.getPresupuestoPeriodo().getPeriodo().getEstatus() && (pr.getMontoPresupuestado() == null || pr.getMontoPresupuestado().compareTo(new BigDecimal(0)) == 0) ? "onclick='asignarPresupuesto(" + periodoId+"," + pr.getTipoBecaPeriodo().getId() + ", "+pr.getId()+");'" : "");
            String color = ((pr.getMontoPresupuestado() == null || pr.getMontoPresupuestado().compareTo(new BigDecimal(0)) == 0) ? " style='color:red'" : "");
            getJsonResult().add("[\""
                    //                    + pr.getId()
                    //                    + "\", \""
                    + pr.getTipoBecaPeriodo().getTipoBeca().getNombre()
                    + "\", \"<div id='div_" + pr.getTipoBecaPeriodo().getId() + "'>$" + df.format(montoTotal) + "</div>"
                    + "<input type='hidden' id='tipoBeca_" + pr.getTipoBecaPeriodo().getId() + "' value='" + pr.getTipoBecaPeriodo().getId() + "' /> "
                    + "<input type='hidden' id='periodo_" + pr.getTipoBecaPeriodo().getId() + "' value='" + periodoId + "' />"
                    + "<input type='text' id='monto_" + pr.getTipoBecaPeriodo().getId() + "' name='monto_" + pr.getTipoBecaPeriodo().getId() + "' value='" + montoTotal + "' style='display:none;' onkeydown = 'if (event.keyCode == 13) save(" + pr.getTipoBecaPeriodo().getId() + ");' /> "
                    + "\", \"<div >$" + df.format(montoAsignado) + "</div>"
                    + "\", \"<div >$" + df.format(montoEjercido) + "</div>"
                    + (pr.getPresupuestoPeriodo().getPeriodo().getEstatus() ? "\", \"<a title='Editar presupuesto' id='ver_" + pr.getTipoBecaPeriodo().getId() + "' href='#listado' onclick='showInput(" + pr.getTipoBecaPeriodo().getId() + ");' > <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-edit fa-stack-1x fa-inverse'></i></span> </a>"
                            : "\", \"<a title='Editar presupuesto' id='ver_" + pr.getTipoBecaPeriodo().getId() + "' href='#listado' > <span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-edit fa-stack-1x fa-inverse'></i></span> </a>")
                    + "      <a title='Guardar presupuesto' id='guardar_" + pr.getTipoBecaPeriodo().getId() + "' href='#listado' onclick='save(" + pr.getTipoBecaPeriodo().getId() + ");' style='display:none;'> <span style='color:green' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-save fa-stack-1x fa-inverse'></i></span> </a>"
                    + "\", \"<a id='href_"+pr.getId()+"'  title='Asignar presupuesto por Unidad Académica' href='" + link + "' " + onClick + "> <span" + color + " class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-cog fa-stack-1x fa-inverse'></i></span>  </a>"
                    + "\"]");
        }

        return SUCCESS_JSON;
    }

    public String guardarTipoBeca() {
        if ((periodoId == null || periodoId.toString().equals("")) || (tipoBecaId == null || tipoBecaId.equals("")) && (monto == null || monto.equals(""))) {
            getJsonResult().add("No se cuenta con los datos necesarios para realizar la operación: {" + periodoId + "," + tipoBecaId + "," + monto + "}");
            return ERROR_JSON;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        PresupuestoBO bo = new PresupuestoBO(getDaos());
        PresupuestoPeriodo periodo = bo.getPresupuestoPeriodo(periodoId);

        if (periodo == null) {
            getJsonResult().add("No se encontró el periodo especificado {" + periodoId + "," + monto + "}");
            return ERROR_JSON;
        }
        try {

            VWPresupuestoTipoBecaPeriodo vtbp = getDaos().getVwPresupuestoTipoBecaPeriodoDao().getPresupuesto(periodo.getId(), tipoBecaId);
            //Verificamos que la suma de los presupuestos, no exceda la del presupuesto por periodo
            VWPresupuestoPeriodo pp = getDaos().getVwPresupuestoPeriodoDao().getPresupuesto(periodoId);
            BigDecimal montoPresupuestado = (vtbp == null || vtbp.getMontoPresupuestado() == null) ? new BigDecimal(0) : vtbp.getMontoPresupuestado();
            BigDecimal montoTotal = ((pp.getMontoAsignado() == null ? new BigDecimal(0) : pp.getMontoAsignado()).subtract(montoPresupuestado).add(new BigDecimal(monto)));
            if (montoTotal.floatValue() > pp.getMontoPresupuestado().floatValue()) {
                getJsonResult().add("\"El monto especificado supera el monto destinado para el periodo $" + df.format(pp.getMontoPresupuestado())
                        + ". Sólo se pueden asignar $" + df.format(pp.getMontoPresupuestado().subtract(pp.getMontoAsignado() == null ? new BigDecimal(0) : pp.getMontoAsignado()).add(montoPresupuestado)) + "\"");
                return SUCCESS_JSON;
            }
            //Verificamos que el nuevo monto no sea menor al monto asignado.
            BigDecimal montoAsignado = (vtbp == null || vtbp.getMontoAsignado() == null) ? new BigDecimal(0) : vtbp.getMontoAsignado();
            Double nuevoMontoPresupuestado = Double.valueOf(monto);
            if (vtbp != null) {
                if (nuevoMontoPresupuestado.floatValue() >= montoAsignado.floatValue()) {
                    //Acatualizamos
                    PresupuestoTipoBecaPeriodo p = new PresupuestoTipoBecaPeriodo();
                    p.setId(vtbp.getId());
                    p.setTipoBecaPeriodo(vtbp.getTipoBecaPeriodo());
                    p.setPresupuestoPeriodo(periodo);
                    p.setMontoPresupuestado(new BigDecimal(nuevoMontoPresupuestado));
                    p.setUsuarioModifico((Usuario) ActionContext.getContext().getSession().get("usuario"));
                    p.setFechaModificacion(new Date());
                    bo.guardaPresupuesto(p);
                } else {
                    getJsonResult().add("\"El monto que ha sido ya asignado ($" + montoAsignado + ") es superior al monto especificado. Por favor, verifique.\"");
                    return SUCCESS_JSON;
                }

            } else {
                TipoBecaPeriodo tipoBecaPeriodo = getDaos().getTipoBecaPeriodoDao().findById(tipoBecaId);
                if (tipoBecaPeriodo == null) {
                    getJsonResult().add("\"No se encuentra el tipo de beca especificado:" + tipoBecaId + ". Por favor, verifique.\"");
                    return SUCCESS_JSON;
                }
                //Insertamos
                PresupuestoTipoBecaPeriodo p = new PresupuestoTipoBecaPeriodo();
                p.setId(null);
                p.setTipoBecaPeriodo(tipoBecaPeriodo);
                p.setPresupuestoPeriodo(periodo);
                p.setMontoPresupuestado(new BigDecimal(nuevoMontoPresupuestado));
                p.setUsuarioModifico((Usuario) ActionContext.getContext().getSession().get("usuario"));
                p.setFechaModificacion(new Date());
                bo.guardaPresupuesto(p);
            }
        } catch (Exception e) {
            getJsonResult().add("\"El formato especificado no es correcto:" + e.getLocalizedMessage().replaceAll("\"", "'") + ". Por favor, verifique.\"");
            return SUCCESS_JSON;
        }

        getJsonResult().add("\"OK\"");
        return SUCCESS_JSON;
    }

    public String datosTipoBeca() {
        if ((periodoId == null || periodoId.toString().equals("")) && (tipoBecaId == null || tipoBecaId.equals("")) && (monto == null || monto.equals(""))) {
            getJsonResult().add("\"No se cuenta con los datos necesarios para realizar la operación: {" + periodoId + "," + tipoBecaId + "," + monto + "}");
            return ERROR_JSON;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        PresupuestoBO bo = new PresupuestoBO(getDaos());
        PresupuestoPeriodo periodo = bo.getPresupuestoPeriodo(periodoId);

        VWPresupuestoTipoBecaPeriodo vtbp = getDaos().getVwPresupuestoTipoBecaPeriodoDao().getPresupuesto(periodo.getId(), tipoBecaId);
        //Verificamos que la suma de los presupuestos, no exceda la del presupuesto por periodo
        BigDecimal montoPresupuestado = (vtbp == null || vtbp.getMontoPresupuestado() == null) ? new BigDecimal(0) : vtbp.getMontoPresupuestado();
        BigDecimal montoAsignado = (vtbp == null || vtbp.getMontoAsignado() == null) ? new BigDecimal(0) : vtbp.getMontoAsignado();
        BigDecimal montoEjercido = (vtbp == null || vtbp.getMontoEjercido() == null) ? new BigDecimal(0) : vtbp.getMontoEjercido();

        getJsonResult().add("["
                + "\"" + vtbp.getTipoBecaPeriodo().getPeriodo().getClave() + ""
                + "\", \"" + ((vtbp == null || vtbp.getTipoBecaPeriodo() == null) ? "" : vtbp.getTipoBecaPeriodo().getTipoBeca().getNombre())
                + "\", \"$" + df.format(vtbp.getTipoBecaPeriodo().getMonto())
                + "\", \"$" + df.format(vtbp.getTipoBecaPeriodo().getMonto().multiply(new BigDecimal(vtbp.getTipoBecaPeriodo().getDuracion())))
                + "\", \"$" + df.format(montoPresupuestado)
                + "\", \"$" + df.format(montoAsignado)
                + "\", \"$" + df.format(montoEjercido)
                + "\"]");

        return SUCCESS_JSON;
    }

    public String listadoUnidadAcademica() {

        if ((periodoId == null || periodoId.toString().equals("")) && (tipoBecaId == null || tipoBecaId.equals(""))) {
            getJsonResult().add("\"No se encontraron datos para el periodo especificado: " + periodoId + "," + tipoBecaId + ". Por favor, verifique.\"");
            return ERROR_JSON;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        DecimalFormat dfEnteros = new DecimalFormat();
        df.setMaximumFractionDigits(0);

        List<VWPresupuestoUnidadAcademica> list = getDaos().getVwPresupuestoUnidadAcademicaDao().getListaPresupuestos(tipoBecaId, presupuestoTipoBecaPeriodoId, periodoId);
        for (VWPresupuestoUnidadAcademica pr : list) {
//            String montoTotal = (pr.getMontoPresupuestado() == null || pr.getMontoPresupuestado().isNaN()) ? "" : pr.getMontoPresupuestado().toString();
            BigDecimal montoAsignado = (pr.getMontoPresupuestado() == null) ? new BigDecimal(0) : pr.getMontoPresupuestado();
            BigDecimal montoEjercido = (pr.getMontoEjercido() == null) ? new BigDecimal(0) : pr.getMontoEjercido();
            BigDecimal becasAsignadas = new BigDecimal(0);
            if (pr.getMontoPresupuestado() != null && pr.getMontoPresupuestado().compareTo(pr.getMontoPorBeca()) >= 0
                    && pr.getMontoPresupuestado().compareTo(new BigDecimal("0")) == 1) {
//                BigDecimal duracionPorTipoBeca = new BigDecimal(pr.getTipoBecaPeriodo().getDuracion());
//                if (duracionPorTipoBeca.compareTo(new BigDecimal("0")) == 1) { //SI ES MAYOR QUE CERO
                becasAsignadas = pr.getMontoPresupuestado().divide(pr.getMontoPorBeca(),2, RoundingMode.HALF_EVEN);
//                }
            }

            BigDecimal becasEjercidas = (pr.getMontoEjercido() == null || pr.getMontoEjercido().compareTo(new BigDecimal(0)) == 0) ? new BigDecimal(0) : pr.getMontoEjercido().divide(pr.getMontoPorBeca());
            getJsonResult().add("[\""
                    + pr.getUnidadAcademica().getNombreCorto()
                    + "\", \"<div id='div_" + pr.getUnidadAcademica().getId() + "'>" + dfEnteros.format(becasAsignadas) + "</div>"
                    + "<input type='hidden' id='unidad_" + pr.getUnidadAcademica().getId() + "' value='" + pr.getUnidadAcademica().getId() + "' />"
                    + "<input type='text' id='becas_" + pr.getUnidadAcademica().getId() + "' name='becas_" + pr.getUnidadAcademica().getId() + "' value='" + dfEnteros.format(becasAsignadas) + "' style='display:none;' onkeydown = 'if (event.keyCode == 13) save(" + pr.getUnidadAcademica().getId() + ");'  /> "
                    + "\", \"$" + df.format(montoAsignado) + ""
                    + "\", \" <div id='ejercidas_"+ pr.getUnidadAcademica().getId() + "'>"  + dfEnteros.format(becasEjercidas) + "</div>"
                    + "\", \"$" + df.format(montoEjercido) + ""
                    + "\", \" <div id='disponibles_" + pr.getUnidadAcademica().getId() + "'>" + df.format(pr.getBecasDisponibles()) + "</div>"
                    + (pr.getPeriodo().getEstatus() ? "\", \"<a title='Editar presupuesto' id='ver_" + pr.getUnidadAcademica().getId() + "' href='#listado' onclick='showInput(" + pr.getUnidadAcademica().getId() + ");' > <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-edit fa-stack-1x fa-inverse'></i></span> </a>"
                            + "      <a title='Guardar presupuesto' id='guardar_" + pr.getUnidadAcademica().getId() + "' href='#listado' onclick='save(" + pr.getUnidadAcademica().getId() + ");' style='display:none;'> <span style='color:green' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-save fa-stack-1x fa-inverse'></i></span> </a>"
                            : "\", \"<a title='Editar presupuesto' id='ver_" + pr.getUnidadAcademica().getId() + "' href='#listado' > <span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-edit fa-stack-1x fa-inverse'></i></span> </a>")
                    + "\"]");
        }

        return SUCCESS_JSON;
    }

    public String guardarUnidadAcademica() {
        if ((unidadAcademicaId == null || unidadAcademicaId.equals(""))
                && (presupuestoTipoBecaPeriodoId == null || presupuestoTipoBecaPeriodoId.equals(""))
                && (becas == null || becas.equals(""))) {
            addActionError("No se cuenta con los datos necesarios para realizar la operación: {" + periodoId + "," + tipoBecaId + "," + unidadAcademicaId + "," + presupuestoTipoBecaPeriodoId + "," + becas + "}");
            return ERROR_JSON;
        }

        PresupuestoBO bo = new PresupuestoBO(getDaos());

        PresupuestoTipoBecaPeriodo ptbp = getDaos().getPresupuestoTipoBecaPeriodoDao().findById(presupuestoTipoBecaPeriodoId);
        UnidadAcademica unidadAcademica = getDaos().getUnidadAcademicaDao().findById(unidadAcademicaId);

        if (ptbp == null || unidadAcademica == null) {
            addActionError("No se encontró el tipo de beca o la Unidad Académica especificada {" + presupuestoTipoBecaPeriodoId + "," + unidadAcademicaId + "}");
            return SUCCESS_JSON;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        try {

            //PRESUPUESTO POR ACTUALIZAR O INSERTAR
            PresupuestoUnidadAcademica presupuesto = getDaos().getPresupuestoUnidadAcademicaDao().getPresupuesto(unidadAcademicaId, presupuestoTipoBecaPeriodoId);
            Boolean existente = presupuesto == null ? Boolean.FALSE : Boolean.TRUE;
            presupuesto = presupuesto == null ? new PresupuestoUnidadAcademica() : presupuesto;
            presupuesto.setPresupuestoTipoBecaPeriodo(ptbp);
            presupuesto.setUnidadAcademica(unidadAcademica);
            BigDecimal duracion = ptbp.getTipoBecaPeriodo().getDuracion() == null ? BigDecimal.ZERO : new BigDecimal(ptbp.getTipoBecaPeriodo().getDuracion());
            BigDecimal montoBecaDuracion = (ptbp.getTipoBecaPeriodo().getMonto() == null ? BigDecimal.ZERO : ptbp.getTipoBecaPeriodo().getMonto()).multiply(duracion);
            BigDecimal montoPresupuestado = new BigDecimal(becas).multiply(montoBecaDuracion);
            //------------------------------------------------------------------
            //TRAEMOS LOS DATOS DE LA VISTA PARA SABER LOS MONTOS PRESUPUESTADO Y ASIGNADO
            VWPresupuestoTipoBecaPeriodo vtbp = getDaos().getVwPresupuestoTipoBecaPeriodoDao().getPresupuesto(ptbp.getPresupuestoPeriodo().getId(), presupuesto.getPresupuestoTipoBecaPeriodo().getTipoBecaPeriodo().getId());
            String nombreBeca = ptbp.getTipoBecaPeriodo().getTipoBeca().getNombre();
            //Verificamos que la suma de los presupuestos, no exceda la del presupuesto por programa
            BigDecimal montoPresupuestadoTbpOriginal = ptbp.getMontoPresupuestado() == null ? BigDecimal.ZERO : ptbp.getMontoPresupuestado();
            //El monto "nuevo" lo obtenemos restandole al monto asignado, el valor actual del monto por Unidad Academica
            // y le sumamos el cálculo del nuevo monto presupuestado especificado en el formulario
            BigDecimal montoPresupuestadoTbpNuevo = (vtbp.getMontoAsignado() == null ? BigDecimal.ZERO : vtbp.getMontoAsignado()).subtract(presupuesto.getMontoPresupuestado() == null ? BigDecimal.ZERO : presupuesto.getMontoPresupuestado()).add(montoPresupuestado);
            BigDecimal becasMaximas = (vtbp.getMontoPresupuestado().subtract(vtbp.getMontoAsignado() == null ? new BigDecimal(0) : vtbp.getMontoAsignado())).divide(montoBecaDuracion, RoundingMode.FLOOR);
            BigDecimal flooredBecasMaximas = becasMaximas.setScale(0, BigDecimal.ROUND_DOWN);
            if (montoPresupuestadoTbpOriginal.compareTo(montoPresupuestadoTbpNuevo) == -1) {    // montoPresupuestadoTbpOriginal < montoPresupuestadoTbpNuevo
                if (becasMaximas.compareTo(BigDecimal.ONE) >= 0) {        // becasMaximas >= 1
                    getJsonResult().add("El monto presupuestado para el programa '" + nombreBeca + "' sólo permite asignar " + flooredBecasMaximas + " becas más.");
                } else {
                    getJsonResult().add("El monto presupuestado para el programa '" + nombreBeca + "' se ha agotado.");
                }
                return ERROR_JSON;
            }
            //------------------------------------------------------------------
            //No poner menos monto del monto ejercido
            VWPresupuestoUnidadAcademica vw = getDaos().getVwPresupuestoUnidadAcademicaDao().getPresupuesto(unidadAcademicaId, presupuestoTipoBecaPeriodoId);
            if (vw != null) {
                BigDecimal montoEjercidoPorUA = vw.getMontoEjercido() == null ? new BigDecimal(0) : vw.getMontoEjercido();
                if (montoPresupuestado.floatValue() < montoEjercidoPorUA.floatValue()) {
                    getJsonResult().add("El monto presupuestado para " + becas + " becas ($" + df.format(montoPresupuestado) + ") no debe ser menor al monto ejercido($" + df.format(vw.getMontoEjercido()) + ")");
                    return ERROR_JSON;
                }
            }

            presupuesto.setMontoPresupuestado(montoPresupuestado);
            presupuesto.setUsuarioModifico((Usuario) ActionContext.getContext().getSession().get("usuario"));
            presupuesto.setFechaModificacion(new Date());
            bo.guardaPresupuesto(existente, presupuesto);
        } catch (Exception e) {
            e.printStackTrace();
            getJsonResult().add("No se pudo actualizar el valor proporcionado. " + (e == null || e.getLocalizedMessage() == null ? "" : e.getLocalizedMessage().replaceAll("\"", "'")) + ". Por favor, verifique.");
            return ERROR_JSON;
        }

        getJsonResult().add("\"OK\"");
        return SUCCESS_JSON;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public BigDecimal getPeriodoId() {
        return periodoId;
    }

    public void setPeriodoId(BigDecimal periodoId) {
        this.periodoId = periodoId;
    }

    public BigDecimal getTipoBecaId() {
        return tipoBecaId;
    }

    public void setTipoBecaId(BigDecimal tipoBecaId) {
        this.tipoBecaId = tipoBecaId;
    }

    public BigDecimal getPresupuestoTipoBecaPeriodoId() {
        return presupuestoTipoBecaPeriodoId;
    }

    public void setPresupuestoTipoBecaPeriodoId(BigDecimal presupuestoTipoBecaPeriodoId) {
        this.presupuestoTipoBecaPeriodoId = presupuestoTipoBecaPeriodoId;
    }

    public Integer getBecas() {
        return becas;
    }

    public void setBecas(Integer becas) {
        this.becas = becas;
    }

    public BigDecimal getUnidadAcademicaId() {
        return unidadAcademicaId;
    }

    public void setUnidadAcademicaId(BigDecimal unidadAcademicaId) {
        this.unidadAcademicaId = unidadAcademicaId;
    }

}

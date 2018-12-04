package com.becasipn.actions.ajax;

import com.becasipn.business.OrdenDepositoBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.OrdenDeposito;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.util.PaginateUtil;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class OrdenesDepositoAjaxAction extends JSONAjaxAction {

    private String ordenDeposito;
    private BigDecimal periodo;
    private BigDecimal mes;
    private Integer origenRecursos;
    private BigDecimal nivelAcademico;
    private BigDecimal unidadAcademica;
    private BigDecimal programaBeca;
    private BigDecimal tipoProceso;
    private BigDecimal tipoDeposito;
    private BigDecimal formaPago;
    private BigDecimal noOrden;
    private BigDecimal idOtorgamiento;
    private String depositosRechazados;
    private int determinacionRecursos;
    private BigDecimal convocatoria;
    // Variables para paginar desde bd    
    PaginateUtil pu;

    /**
     * Devuelve el listado con el número de alumnos a los que se les puede
     * generar orden de deposito.
     *
     * @return SUCCESS_JSON
     */
    public String listado() {
//        if (origenRecursos == 0 && programaBeca.equals(new BigDecimal("3"))) {
//            addActionError(getText("Los depositos de Telmex que realiza la fundación no se administran en el sistema."));
//            return SUCCESS_JSON;
//        }
        //Generamos la fecha del mes a depositar para obtener el monto variable si es que tiene.
        NumberFormat f1 = new DecimalFormat("00");
        Periodo p = getDaos().getPeriodoDao().findById(periodo);
        String año;
        if (p.getClave().substring(5, 6).equals("1")) {
            if (mes.equals(new BigDecimal("1"))) {
                año = p.getClave().substring(0, 4);
            } else {
                Integer nuevoAño = (Integer.parseInt(p.getClave().substring(0, 4))) - 1;
                año = nuevoAño.toString();
            }
        } else {
            año = p.getClave().substring(0, 4);
        }
        if (programaBeca.equals(new BigDecimal(5)) || programaBeca.equals(new BigDecimal(7))) { //Manutención, Transporte manutención
        } else {
            determinacionRecursos = 0;
        }
        String fechaDeposito = "01/" + f1.format(mes) + "/" + año;
        List<Otorgamiento> lista = new ArrayList<>();
        OrdenDepositoBO bo = new OrdenDepositoBO(getDaos());
        System.out.println("convocatoria:: " + convocatoria);
        if (ordenDeposito.equals("0")) {
            ConvocatoriaSubes csubes =  convocatoria==null?null:getDaos().getConvocatoriaSubesDao().findById(convocatoria);
            lista = bo.getPreviewOrdenDeposito(periodo, mes, origenRecursos, programaBeca, nivelAcademico, unidadAcademica, tipoProceso, idOtorgamiento, fechaDeposito, formaPago, tipoDeposito, determinacionRecursos,csubes);
        }
        Long sumaTotalAlumnos = 0L;
        Double sumaMonto = 0D;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        for (Otorgamiento o : lista) {
            DatosAcademicos datosAcademicos = o.getDatosAcademicos();
            if (datosAcademicos == null) {
                getJsonResult().add("[\"" + "Unidad Académica desconocida"
                        + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                        + "\", \"" + o.getAlumnosTotal()
                        + "\", \"" + df.format(o.getSumaMonto())
                        + " \"]");
            } else {
                getJsonResult().add("[\"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
                        + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                        + "\", \"" + o.getAlumnosTotal()
                        + "\", \"" + df.format(o.getSumaMonto())
                        + " \"]");
            }
            sumaTotalAlumnos += o.getAlumnosTotal();
            sumaMonto += o.getSumaMonto();
        }
        getJsonResult().add("[\""
                + "\", \""
                + "\", \"<b>" + df.format(sumaTotalAlumnos)
                + "</b>\", \"<b id='monto'>$" + df.format(sumaMonto)
                + "</b>\"]"
        );
        return SUCCESS_JSON;
    }

    /**
     * Devuelve el listado de las ordenes de deposito generadas en el sistema
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String listadoAdmin() {
//        Gson g = new Gson();
//        try {
//            List<VWOrdenDeposito> lista;
//            Map<String, Object> parametros = new HashMap<>();
//            parametros.put("a.periodo.id", periodo);
//            setPu(getDaos().getVwOrdenDepositoDao().findPaginate(iDisplayStart, iDisplayLength, parametros));
//            lista = getPu().getResultados();
//            SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
//            SimpleDateFormat hora = new SimpleDateFormat("hh:mm a");
//            JsonArray array;
//            for (VWOrdenDeposito orden : lista) {
//                String abre = "";
//                String cierra = "";
//                orden.getProgramaBeca();
//                if (orden.getEstatusDeposito() != null && orden.getEstatusDeposito().getId().compareTo(new BigDecimal(1)) == 0) {
//                    abre = "<b>";
//                    cierra = "</b>";
//                }
//                array = new JsonArray();
//                array.add(new JsonPrimitive("<a title='Ver mas'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-search-plus fa-stack-1x fa-inverse'></i></span></a>"));
//                array.add(new JsonPrimitive(abre + orden.getNombreOrdenDeposito() + cierra));
//                array.add(new JsonPrimitive(abre + (esHoy(orden.getFechaEjecucion()) ? hora.format(orden.getFechaEjecucion()) : fecha.format(orden.getFechaEjecucion())) + cierra));
//                array.add(new JsonPrimitive(orden.getTipoDeposito() == null ? "" : orden.getTipoDeposito().getNombre()));
//                array.add(new JsonPrimitive(orden.getPeriodo().getDescripcion()));
//                array.add(new JsonPrimitive(nombreMes(orden.getMes())));
//                array.add(new JsonPrimitive(orden.getNivel() == null ? "" : orden.getNivel().getNombre()));
//                if (orden.getEstatusDeposito() != null) {
//                    switch (orden.getEstatusDeposito().getId().intValue()) {
//                        case 1:
//                            array.add(new JsonPrimitive("<span class='label label-success'>" + orden.getEstatusDeposito().getNombre() + "</span>"));
//                            break;
//                        case 2:
//                            array.add(new JsonPrimitive("<span class='label label-primary'>" + orden.getEstatusDeposito().getNombre() + "</span>"));
//                            break;
//                        case 3:
//                            array.add(new JsonPrimitive("<span class='label label-info'>" + orden.getEstatusDeposito().getNombre() + "</span>"));
//                            break;
//                        case 4:
//                            array.add(new JsonPrimitive("<span class='label label-warning'>" + orden.getEstatusDeposito().getNombre() + "</span>"));
//                            break;
//                        default:
//                            array.add(new JsonPrimitive("<span class='label label-danger'>" + orden.getEstatusDeposito().getNombre() + "</span>"));
//                            break;
//                    }
//                } else {
//                    array.add(new JsonPrimitive("<span class='label label-danger'> Sin Estatus</span>"));
//                }
//                String SUBES = (orden.getProgramaBeca() == null ? ""
//                        : ((orden.getProgramaBeca().getId().equals(new BigDecimal("5")) || orden.getProgramaBeca().getId().equals(new BigDecimal("7")))
//                                ? "<a title='Descargar la orden de depósito - SUBES' href='/becas/subesOrdenesDeposito.action?ordenId=" + orden.getId() + "'><span class='fa-stack' style='color:red'><i class='fa fa-share-alt'></i></span></a>"
//                                : (orden.getProgramaBeca().getId().equals(new BigDecimal("1"))
//                                        ? "<a title='Descargar la orden de depósito - Modalidad' href='/becas/modalidadOrdenesDeposito.action?ordenId=" + orden.getId() + "'><span class='fa-stack' style='color:purple'><i class='fa fa-download'></i></span></a>"
//                                        : "<a title='Disponible sólo para Manutención y Transporte manutención'><span class='fa-stack' style='color:gray'><i class='fa fa-share-alt'></i></span></a>")));
//                array.add(new JsonPrimitive("<a title='Detalle de la orden de depósito' href='/becas/detalleOrdenesDeposito.action?ordenId=" + orden.getId() + "' class='fancybox fancybox.iframe'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-eye fa-stack-1x fa-inverse'></i></span></a>"));
//                array.add(new JsonPrimitive("<a title='Descargar la orden de depósito' href='/becas/descargarOrdenesDeposito.action?ordenId=" + orden.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-paperclip fa-stack-1x fa-inverse'></i></span></a>"));
//                array.add(new JsonPrimitive("<a title='Cargar la respuesta bancaria' href='/carga/formRespuestaBancaria.action?ordenId=" + orden.getId() + " ' class='fancybox fancybox.iframe'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-arrow-circle-up fa-stack-1x fa-inverse'></i></span></a>"));
//                array.add(new JsonPrimitive(SUBES));
//                array.add(new JsonPrimitive(orden.getUnidadAcademica() == null ? "Todas" : orden.getUnidadAcademica().getNombreCorto()));
//                array.add(new JsonPrimitive(orden.getProgramaBeca() == null ? "Todas" : orden.getProgramaBeca().getNombre()));
//                array.add(new JsonPrimitive(orden.getUsuario() == null ? "" : abre + orden.getUsuario().getUsuario() + cierra));
//                array.add(new JsonPrimitive(orden.getTipoProceso() == null ? "Todos" : orden.getTipoProceso().getNombre()));
//                array.add(new JsonPrimitive(orden.getConteo()));
//                array.add(new JsonPrimitive(orden.getId()));
//                getJsonResult().add(array.toString());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return SUCCESS_PAGINATED_JSON;
        List<OrdenDeposito> lista = getDaos().getOrdenDepositoDao().existenOrdenesDepositoAsociados(periodo);
        SimpleDateFormat fecha = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat hora = new SimpleDateFormat("hh:mm a");
        String estatus;
        for (OrdenDeposito orden : lista) {
            if (orden.getEstatusDeposito() != null) {
                switch (orden.getEstatusDeposito().getId().intValue()) {
                    case 1:
                        estatus = "<span class='label label-success'>" + orden.getEstatusDeposito().getNombre() + "</span>";
                        break;
                    case 2:
                        estatus = "<span class='label label-primary'>" + orden.getEstatusDeposito().getNombre() + "</span>";
                        break;
                    case 3:
                        estatus = "<span class='label label-info'>" + orden.getEstatusDeposito().getNombre() + "</span>";
                        break;
                    case 4:
                        estatus = "<span class='label label-warning'>" + orden.getEstatusDeposito().getNombre() + "</span>";
                        break;
                    default:
                        estatus = "<span class='label label-danger'>" + orden.getEstatusDeposito().getNombre() + "</span>";
                        break;
                }
            } else {
                estatus = "<span class='label label-danger'> Sin Estatus</span>";
            }
            String SUBES = (orden.getProgramaBeca() == null ? ""
                    : ((orden.getProgramaBeca().getId().equals(new BigDecimal("5")) || orden.getProgramaBeca().getId().equals(new BigDecimal("7")))
                            ? "<a title='Descargar la orden de depósito - SUBES' href='/becas/subesOrdenesDeposito.action?ordenId=" + orden.getId() + "'><span class='fa-stack' style='color:red'><i class='fa fa-share-alt'></i></span></a>"
                            : (orden.getProgramaBeca().getId().equals(new BigDecimal("1"))
                                    ? "<a title='Descargar la orden de depósito - Modalidad' href='/becas/modalidadOrdenesDeposito.action?ordenId=" + orden.getId() + "'><span class='fa-stack' style='color:purple'><i class='fa fa-download'></i></span></a>"
                                    : "<a title='Disponible sólo para Manutención y Transporte manutención'><span class='fa-stack' style='color:gray'><i class='fa fa-share-alt'></i></span></a>")));
            getJsonResult().add("[\"" + "<a title='Ver mas'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-search-plus fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"" + orden.getNombreOrdenDeposito()
                    + "\", \"" + (esHoy(orden.getFechaEjecucion()) ? hora.format(orden.getFechaEjecucion()) : fecha.format(orden.getFechaEjecucion()))
                    + "\", \"" + (orden.getTipoDeposito() == null ? "" : orden.getTipoDeposito().getNombre())
                    + "\", \"" + orden.getPeriodo().getDescripcion()
                    + "\", \"" + nombreMes(orden.getMes())
                    + "\", \"" + (orden.getNivel() == null ? "" : orden.getNivel().getNombre())
                    + "\", \"" + estatus
                    + "\", \"" + "<a title='Detalle de la orden de depósito' href='/becas/detalleOrdenesDeposito.action?ordenId=" + orden.getId() + "' class='fancybox fancybox.iframe'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-eye fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"" + "<a title='Descargar la orden de depósito' href='/becas/descargarOrdenesDeposito.action?ordenId=" + orden.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-paperclip fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"" + "<a title='Cargar la respuesta bancaria' href='/carga/formRespuestaBancaria.action?ordenId=" + orden.getId() + " ' class='fancybox fancybox.iframe'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-arrow-circle-up fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"" + SUBES
                    + "\", \"" + (orden.getUnidadAcademica() == null ? "Todas" : orden.getUnidadAcademica().getNombreCorto())
                    + "\", \"" + (orden.getProgramaBeca() == null ? "Todas" : orden.getProgramaBeca().getNombre())
                    + "\", \"" + orden.getUsuario().getUsuario()
                    + "\", \"" + (orden.getTipoProceso() == null ? "Todos" : orden.getTipoProceso().getNombre())
                    + "\", \"" + "Total"
                    + "\", \"" + orden.getId()
                    + "\"]");
        }
        return SUCCESS_JSON;
    }

    /**
     * Devuelve el listado a detalle de los depositos correspondientes a una
     * orden de deposito
     *
     * @author Victor Lozano
     * @return SUCCESS_JSON
     */
    public String detalle() {
        try {
            Long sumaTotalAlumnos = 0L;
            Double sumaMonto = 0D;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            List<Otorgamiento> lista = getDaos().getOtorgamientoDao().detalleByOrdenDeposito(noOrden);

            for (Otorgamiento o : lista) {
                Alumno alumno = o.getAlumno();
                DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
                getJsonResult().add("[\"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
                        + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                        + "\", \"" + df.format(o.getAlumnosTotal())
                        //                        + "\", \"" + o.getAlumnosTotal()
                        + "\", \"$" + df.format(o.getSumaMonto())
                        + "\"]"
                );
                sumaTotalAlumnos += o.getAlumnosTotal();
                sumaMonto += o.getSumaMonto();
            }
            getJsonResult().add("[\""
                    + "\", \""
                    + "\", \"<b>" + df.format(sumaTotalAlumnos)
                    //                        + "\", \"" + o.getAlumnosTotal()
                    + "</b>\", \"<b>$" + df.format(sumaMonto)
                    + "</b>\"]"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS_JSON;
    }

    /**
     * Función para validar si una fecha corresponde al día en curso o no
     *
     * @author Victor Lozano
     * @param fecha a validar
     * @return true la fecha corresponde al día de hoy
     */
    public static boolean esHoy(Date fecha) {
        Calendar aValidar = Calendar.getInstance();
        aValidar.setTime(fecha);
        Calendar referencia = Calendar.getInstance();
        referencia.setTime(Calendar.getInstance().getTime());
        return (aValidar.get(Calendar.ERA) == referencia.get(Calendar.ERA)
                && aValidar.get(Calendar.YEAR) == referencia.get(Calendar.YEAR)
                && aValidar.get(Calendar.DAY_OF_YEAR) == referencia.get(Calendar.DAY_OF_YEAR));
    }

    private String nombreMes(Integer mes) {
        switch (mes) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                return "" + mes;
        }
    }

    public String resumen() {
        Long sumaTotalAlumnos = 0L;
        Double sumaMonto = 0D;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        List<Otorgamiento> lista = getDaos().getOtorgamientoDao().detalleByOrdenDeposito(noOrden);
        for (Otorgamiento o : lista) {
            Alumno alumno = o.getAlumno();
            DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
            getJsonResult().add("[\"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
                    + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                    + "\", \"" + df.format(o.getAlumnosTotal())
                    + "\", \"$" + df.format(o.getSumaMonto())
                    + "\"]"
            );
            sumaTotalAlumnos += o.getAlumnosTotal();
            sumaMonto += o.getSumaMonto();
        }
        getJsonResult().add("[\""
                + "\", \""
                + "\", \"<b>" + df.format(sumaTotalAlumnos)
                + "</b>\", \"<b>$" + df.format(sumaMonto)
                + "</b>\"]"
        );
        return SUCCESS_JSON;
    }

    public String getOrdenDeposito() {
        return ordenDeposito;
    }

    public void setOrdenDeposito(String ordenDeposito) {
        this.ordenDeposito = ordenDeposito;
    }

    public BigDecimal getPeriodo() {
        return periodo;
    }

    public void setPeriodo(BigDecimal periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getMes() {
        return mes;
    }

    public void setMes(BigDecimal mes) {
        this.mes = mes;
    }

    public Integer getOrigenRecursos() {
        return origenRecursos;
    }

    public void setOrigenRecursos(Integer origenRecursos) {
        this.origenRecursos = origenRecursos;
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

    public BigDecimal getProgramaBeca() {
        return programaBeca;
    }

    public void setProgramaBeca(BigDecimal programaBeca) {
        this.programaBeca = programaBeca;
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

    public BigDecimal getNoOrden() {
        return noOrden;
    }

    public void setNoOrden(BigDecimal noOrden) {
        this.noOrden = noOrden;
    }

    public BigDecimal getIdOtorgamiento() {
        return idOtorgamiento;
    }

    public void setIdOtorgamiento(BigDecimal idOtorgamiento) {
        this.idOtorgamiento = idOtorgamiento;
    }

    @Override
    public PaginateUtil getPu() {
        return pu;
    }

    @Override
    public void setPu(PaginateUtil pu) {
        this.pu = pu;
        setJsonTotalRecords(this.pu.getNoResultados());
        setJsonDisplayRecords(this.pu.getNoResultadosFiltrados());
    }

    public String getDepositosRechazados() {
        return depositosRechazados;
    }

    public void setDepositosRechazados(String depositosRechazados) {
        this.depositosRechazados = depositosRechazados;
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

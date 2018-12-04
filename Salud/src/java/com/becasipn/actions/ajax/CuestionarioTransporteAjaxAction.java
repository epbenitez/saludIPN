package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.business.PeriodoBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.TransporteDatosFamiliares;
import com.becasipn.persistence.model.TransporteTraslado;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class CuestionarioTransporteAjaxAction extends JSONAjaxAction {

    private String numeroBoleta;
    private String curp;
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private BigDecimal transporteId;

    public String buscar() {
        List<Alumno> alumnoList;
        PeriodoBO bo = new PeriodoBO(getDaos());
        Periodo periodo = bo.getPeriodoActivo();
        if ("".equals(numeroBoleta)) {
            if ("".equals(curp)) {
                if ("".equals(nombre) && "".equals(aPaterno) && "".equals(aMaterno)) {
                    alumnoList = null;
                } else {
                    //System.out.println("Búsqueda solo por nombre");
                    alumnoList = getDaos().getAlumnoDao().buscarPorNombre(nombre, aPaterno, aMaterno);
                }
            } else {
                if ("".equals(nombre) && "".equals(aPaterno) && "".equals(aMaterno)) {
                    //System.out.println("Búsqueda solo por curp");
                    alumnoList = getDaos().getAlumnoDao().buscarPorCURP(curp);
                } else {
                    //System.out.println("Búsqueda por curp y nombre");
                    alumnoList = getDaos().getAlumnoDao().buscarPorCURPNombre(curp, nombre, aPaterno, aMaterno);
                }
            }
        } else {
            if ("".equals(curp)) {
                if ("".equals(nombre) && "".equals(aPaterno) && "".equals(aMaterno)) {
                    //System.out.println("Búsqueda solo por boleta");
                    alumnoList = getDaos().getAlumnoDao().getByBoleta(numeroBoleta);
                } else {
                    //System.out.println("Búsqueda por boleta y nombre");
                    alumnoList = getDaos().getAlumnoDao().buscarPorBoletaNombre(numeroBoleta, nombre, aPaterno, aMaterno);
                }
            } else {
                if ("".equals(nombre) && "".equals(aPaterno) && "".equals(aMaterno)) {
                    //System.out.println("Búsqueda por boleta y curp");
                    alumnoList = getDaos().getAlumnoDao().buscarPorBoletaCURP(numeroBoleta, curp);
                } else {
                    //System.out.println("Búsqueda por boleta, curp y nombre");
                    alumnoList = getDaos().getAlumnoDao().buscarPorBoletaCURPNombre(numeroBoleta, curp, nombre, aPaterno, aMaterno);
                }
            }
        }
        for (Alumno alumno : alumnoList) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(alumno.getId(), periodo.getId());
            Boolean ese = getDaos().getSolicitudBecaDao().exiteESEPeriodoActivo(alumno.getId(), new BigDecimal("1"), periodo.getId());
            Boolean eset = getDaos().getCuestionarioTransporteDao().tieneEseTransporte(alumno.getUsuario().getId(), periodo.getId());
            getJsonResult().add("[\"" + alumno.getBoleta()
                    + "\", \"" + alumno.getCurp()
                    + "\", \"" + alumno.getFullName()
                    + "\", \"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
                    + "\", \"" + datosAcademicos.getSemestre()
                    + "\", \"" + datosAcademicos.getPromedio()
                    + "\", \"" + (ese
                            ? "<a title='Estudio socioeconómico' class='fancybox fancybox.iframe' href='/admin/eseCuestionario.action?cuestionarioId=1&alumnoId=" + alumno.getId() + "'> <span class='fa-stack'> <i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-money fa-stack-1x fa-inverse'></i></span></a>"
                            : "<a title='Sin estudio socioeconómico' class='table-link danger'><span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-money fa-stack-1x fa-inverse'></i></span></a>")
                    + "\", \"" + (eset
                            ? "<a title='Estudio socioeconómico de transporte' class='fancybox fancybox.iframe' href='/admin/esetCuestionario.action?cuestionarioId=2&alumnoId=" + alumno.getId() + "'> <span class='fa-stack'> <i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-car fa-stack-1x fa-inverse'></i></span></a>"
                            : "<a title='Sin estudio socioeconómico de transporte' class='table-link danger'><span style='color:lightgrey' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-car fa-stack-1x fa-inverse'></i></span></a>")
                    + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String traslado() {
        List<TransporteTraslado> transporteTraslado = getDaos().getTransporteTrasladoDao().respuestasTraslado(transporteId);
        float total = 0;
        for (TransporteTraslado tt : transporteTraslado) {
            total = total + tt.getCosto();
            getJsonResult().add("[\"" + tt.getTransporte().getNombre()
                    + "\", \"" + tt.getPuntopartida()
                    + "\", \"" + tt.getPuntollegada()
                    + "\", \"" + tt.getCosto()
                    + "\", \"" + tt.getTrayecto().getNombre()
                    + " \"]");
        }
        getJsonResult().add("[\"" + ""
                    + "\", \"" + ""
                    + "\", \"" + "Total"
                    + "\", \"" + total
                    + "\", \"" + ""
                    + " \"]");
        return SUCCESS_JSON;
    }

    public String familiar() {
        List<TransporteDatosFamiliares> transporteDatosFamiliares = getDaos().getTransporteDatosFamiliaresDao().respuestasTransporte(transporteId);
        for (TransporteDatosFamiliares tdt : transporteDatosFamiliares) {
            getJsonResult().add("[\"" + tdt.getNombrefamiliar()
                    + "\", \"" + tdt.getParentesco().getNombre()
                    + "\", \"" + tdt.getEdad()
                    + "\", \"" + tdt.getOcupacion()
                    + "\", \"" + tdt.getAportacionmensual()
                    + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String getNumeroBoleta() {
        return numeroBoleta;
    }

    public void setNumeroBoleta(String numeroBoleta) {
        this.numeroBoleta = numeroBoleta;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getaPaterno() {
        return aPaterno;
    }

    public void setaPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getaMaterno() {
        return aMaterno;
    }

    public void setaMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public BigDecimal getTransporteId() {
        return transporteId;
    }

    public void setTransporteId(BigDecimal transporteId) {
        this.transporteId = transporteId;
    }
}

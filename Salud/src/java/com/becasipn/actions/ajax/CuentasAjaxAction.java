package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_PAGINATED_JSON;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.util.PaginateUtil;
import static com.becasipn.util.Util.CampoValidoAJAX;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sistemas DSE
 */
public class CuentasAjaxAction extends JSONAjaxAction {

    PaginateUtil pu;
    private BigDecimal periodo;
    private String numeroDeBoleta;
    private String curp;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private BigDecimal solicitudId;
    
    public String detalle() {
        List<LinkedHashMap<String, Object>> cuentas = getDaos().getTarjetaBancariaDao().findCuentasBySolicitud(solicitudId);
        for(Map<String, Object> cuenta : cuentas){
            getJsonResult().add("[\"" + cuenta.get("nombreCorto")
                    + "\", \"" + cuenta.get("boleta")
                    + "\", \"" + cuenta.get("nombre")
                    + "\", \"" + cuenta.get("tarjeta")
                    + "\", \"" + cuenta.get("tipo")
                    + "\", \"" + cuenta.get("estatus")
                    + "\"]");
        }
    
        return SUCCESS_JSON;
    }

    public String listadoAdminSolicitud() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ssu.setSortCol(2);
        ssu.setSortDir("desc");
        setPu(getDaos().getSolicitudCuentasDao().getListado(ssu));
        List<SolicitudCuentas> lista = getPu().getResultados();
        boolean cancelado;
        for (SolicitudCuentas sc : lista) {
            cancelado = false;
            if (sc.getFechaCancelacion() != null) {
                cancelado = true;
            }
            getJsonResult().add("[\"" + sc.getIdentificador()
                    + "\", \"" + sc.getUsuarioGeneracion().getUsuario()
                    + "\", \"" + sdf.format(sc.getFechaGeneracion())
                    + "\", \"" + "<a title='Estatus' " + (cancelado ? "href= '/admin/estatusOrdenesSolicitudCuentas.action?solicitud.id=" + sc.getId() + "' class='fancybox fancybox.iframe'" : "") + " ><span class=" + (cancelado ? "'label label-danger'" : "'label label-primary'") + ">" + (cancelado ? "Cancelada" : "Activa") + "</span></a> "
                    + "\", \"" + "<a title='Detalle de la solicitud de cuenta' " + (cancelado ? "class='table-link danger'" : "href='/admin/detalleOrdenesSolicitudCuentas.action?solicitud.id=" + sc.getId() + "'") + " class='fancybox fancybox.iframe'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-eye fa-stack-1x fa-inverse'></i></span></a> "
                    + "\", \"" + "<a title='Descargar la solicitud de cuenta' " + (cancelado ? "class='table-link danger'" : "href='/admin/descargarListadoIdntBanamex.action?identificador=" + sc.getIdentificador() + "'") + "><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-paperclip fa-stack-1x fa-inverse'></i></span></a> "
                    + " \"]");
        }
        return SUCCESS_JSON;

    }

    public String listado() {
        if (ssu.parametros.isEmpty()) {
            return SUCCESS_JSON;
        }
        setSsu();
        setPu(getDaos().getAlumnoDao().alumnosConfiguracionCuenta(ssu));
        List<Alumno> lista = pu.getResultados();
        for (Alumno alumno : lista) {
            getJsonResult().add("[\"" + alumno.getBoleta()
                    + "\", \"" + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre()
                    + "\", \"" + alumno.getCurp()
                    + "\", \"" + "<a title='Configuración de Cuenta' class='fancybox fancybox.iframe table-link'  href='/admin/configuracionCuentaBancaria.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + " \"]"
            );
        }
        return SUCCESS_PAGINATED_JSON;
    }

    public BigDecimal getPeriodo() {
        return periodo;
    }

    public void setPeriodo(BigDecimal periodo) {
        this.periodo = periodo;
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

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
        if (CampoValidoAJAX(numeroDeBoleta)) {
            ssu.parametros.put("a.boleta", this.numeroDeBoleta.toUpperCase());
        }
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
        if (CampoValidoAJAX(curp)) {
            ssu.parametros.put("a.curp", this.curp.toUpperCase());
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        if (CampoValidoAJAX(nombre)) {
            ssu.parametros.put("a.nombre", this.nombre.toUpperCase());
        }
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
        if (CampoValidoAJAX(apPaterno)) {
            ssu.parametros.put("a.apellidoPaterno", this.apPaterno.toUpperCase());
        }
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
        if (CampoValidoAJAX(apMaterno)) {
            ssu.parametros.put("a.apellidoMaterno", this.apMaterno.toUpperCase());
        }
    }

    public BigDecimal getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(BigDecimal solicitudId) {
        this.solicitudId = solicitudId;
    }
}

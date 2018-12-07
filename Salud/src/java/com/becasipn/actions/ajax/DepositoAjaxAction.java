package com.becasipn.actions.ajax;

import com.becasipn.business.DepositosBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.Otorgamiento;
import static com.becasipn.util.Util.CampoValidoAJAX;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tania G. Sánchez
 */
public class DepositoAjaxAction extends JSONAjaxAction {

    private BigDecimal ordenId;
    private BigDecimal noIdalumno;
    private String numBoleta;
    private Alumno alum;
    private String curp;
    private String numeroDeBoleta;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private BigDecimal idOtorgamiento;

    public String detalle() {
        List<Depositos> lista = getDaos().getDepositosDao().depositosRechazados(ordenId);
        Otorgamiento o = new Otorgamiento();
        for (Depositos d : lista) {
            Alumno alumno = d.getAlumno();
            o = null;
            DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);
            getJsonResult().add("[\"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
                    + "\", \"" + d.getAlumno().getBoleta()
                    + "\", \"" + d.getAlumno().getFullName()
                    + "\", \"" + o.getTipoBecaPeriodo().getTipoBeca().getNombre()
                    + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String listado() {
        DepositosBO bo = DepositosBO.getInstance(getDaos());
        getJsonResult().add(bo.getListadoJsonResult(noIdalumno, idOtorgamiento));
        
        return SUCCESS_JSON;
    }

    public String buscarListado() {
        alum = getDaos().getAlumnoDao().findByBoleta(numBoleta);
        noIdalumno = alum.getId();
        return listado();
    }

    /**
     * Lista los alumnos que cumplen con los criterios de busqueda
     *
     * @return SUCCESS
     */
    public String buscar() {
        if (ssu.parametros.isEmpty())
            return SUCCESS_JSON;
        setSsu();        
        setPu(getDaos().getAlumnoDao().busquedaAlumnos(ssu));
        List<Alumno> lista = getPu().getResultados();
        for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            if (datosAcademicos == null) {
                getJsonResult().add("[\"" + alumno.getBoleta()
                    + "\", \"" + alumno.getFullName()
                    + "\", \"" + "Sin Unidad Académica"
                    + "\", \"<a title='Depósitos por alumno' class='fancybox fancybox.iframe table-link'  href='/depositos/formAdministraDepositos.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + " \"]");
            } else {
                getJsonResult().add("[\"" + alumno.getBoleta()
                        + "\", \"" + alumno.getFullName()
                        + "\", \"" + datosAcademicos.getUnidadAcademica().getNombreCorto()
                        + "\", \"<a title='Depósitos por alumno' class='fancybox fancybox.iframe table-link'  href='/depositos/formAdministraDepositos.action?numeroDeBoleta=" + alumno.getBoleta() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                        + " \"]");
                
            }
        }
        return SUCCESS_PAGINATED_JSON;
    }

    public String getNumBoleta() {
        return numBoleta;
    }

    public void setNumBoleta(String numBoleta) {
        this.numBoleta = numBoleta;
    }

    public BigDecimal getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(BigDecimal ordenId) {
        this.ordenId = ordenId;
    }

    public BigDecimal getNoIdalumno() {
        return noIdalumno;
    }

    public void setNoIdalumno(BigDecimal noIdalumno) {
        this.noIdalumno = noIdalumno;
    }

    public Alumno getAlum() {
        return alum;
    }

    public void setAlum(Alumno alum) {
        this.alum = alum;
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

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
        if (CampoValidoAJAX(numeroDeBoleta)) {
            ssu.parametros.put("a.boleta", this.numeroDeBoleta.toUpperCase());
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

    public BigDecimal getIdOtorgamiento() {
        return idOtorgamiento;
    }

    public void setIdOtorgamiento(BigDecimal idOtorgamiento) {
        this.idOtorgamiento = idOtorgamiento;
    }
}
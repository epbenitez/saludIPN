package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Periodo;
import java.math.BigDecimal;
import java.util.List;

public class PermisoCambioAjaxAction extends JSONAjaxAction {

    private String numeroDeBoleta;
    private String curp;
    private String nombre;
    private String apPaterno;
    private String apMaterno;

    private BigDecimal ua;
    private String plan;
    private String crr;

    public String listado() {
        if (ssu.parametros.isEmpty()) {
            return SUCCESS_JSON;
        }
        setSsu();
        setPu(getDaos().getAlumnoDao().solicitudAlumnos(ssu));
        List<Alumno> lista = pu.getResultados();
        String nombreCorto = "No cuenta con UA";
        for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId());
            if (datosAcademicos != null) {
                if (datosAcademicos.getUnidadAcademica() != null) {
                    nombreCorto = datosAcademicos.getUnidadAcademica().getNombreCorto();
                }
            }

            getJsonResult().add("[\"" + alumno.getBoleta()
                    + "\", \"" + alumno.getApellidoPaterno() + " " + alumno.getApellidoMaterno() + " " + alumno.getNombre()
                    + "\", \"" + nombreCorto
                    + getSalud(alumno)
                    + " \"]"
            );
        }
        return SUCCESS_PAGINATED_JSON;
    }

    private String getSalud(Alumno alumno) {
        String res = "\", \"";
        Periodo p = getDaos().getPeriodoDao().getPeriodoActivo();

        if (getDaos().getCuestionarioDao().findById(new BigDecimal(3)).getActivo()) {
            Boolean ox = getDaos().getCensoSaludDao().contestoEncuestaSalud(alumno.getId(), p.getId());
            if (ox) {
                res += "<a title='Censo Salud' class='fancybox fancybox.iframe table-link'  href='/misdatos/adminSaludCuestionarioSalud.action?alumnoId=" + alumno.getId() + "'><span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-medkit fa-stack-1x fa-inverse cursor'></i></span></a>";
            } else {
                res += "<a title='Censo de Salud No Contestado' onclick='ESENoActivo()' class='table-link danger'> <span style='color:red' class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-medkit fa-stack-1x fa-inverse cursor'></i></span></a>";
            }
        }
        return res;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
        ssu.parametros.put("a.boleta", this.numeroDeBoleta.toUpperCase());

    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
        ssu.parametros.put("a.curp", this.curp.toUpperCase());

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        ssu.parametros.put("a.nombre", this.nombre.toUpperCase());

    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
        ssu.parametros.put("a.apellidoPaterno", this.apPaterno.toUpperCase());

    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
        ssu.parametros.put("a.apellidoMaterno", this.apMaterno.toUpperCase());

    }

    public BigDecimal getUa() {
        return ua;
    }

    public void setUa(BigDecimal ua) {
        this.ua = ua;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getCrr() {
        return crr;
    }

    public void setCrr(String crr) {
        this.crr = crr;
    }

}

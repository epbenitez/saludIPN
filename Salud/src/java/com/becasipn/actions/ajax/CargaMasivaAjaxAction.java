package com.becasipn.actions.ajax;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.util.List;

/**
 *
 * @author Jesús Fernández
 */
public class CargaMasivaAjaxAction extends JSONAjaxAction {   
    private StringBuilder sbJson;    
        
    public String cargaAlumnos(){
        setUnidadAcademica();
        setSsu();
        setPu(getDaos().getAlumnoDao().findAlumnos(ssu));
        List<Alumno> lista = getPu().getResultados();
        for (Alumno alumno : lista) {
            DatosAcademicos datosAcademicos = alumno.getListaDatosAcademicos().get(0);            
            sbJson = new StringBuilder("[\"");            
            sbJson.append(alumno.getBoleta())
            .append("\", \"").append(alumno.getFullName())
            .append("\", \"").append(datosAcademicos.getCarrera() == null ? "N/A" : datosAcademicos.getCarrera().getCarrera())
            .append("\", \"").append(datosAcademicos.getPromedio())
            .append("\", \"").append(datosAcademicos.getSemestre())
            .append("\", \"").append(labelInscrito(datosAcademicos.getInscrito()))
            .append("\", \"").append(labelRegular(datosAcademicos.getRegular()))
            .append("\"]");
            getJsonResult().add(sbJson.toString());            
        }
        return SUCCESS_PAGINATED_JSON;
    }
    
    private String labelInscrito(Integer inscrito) {
        String res;
        if (inscrito != null && inscrito == 1) {
            res = "<span class='label label-success'>Inscrito</span>";
        } else {
            res = "<span class='label label-danger'>No Inscrito</span>";
        }
        return res;
    }

    private String labelRegular(Integer regular) {
        String res;
        if (regular != null && regular == 1) {
            res = "<span class='label label-success'>Regular</span>";
        } else {
            res = "<span class='label label-danger'>No Regular</span>";
        }
        return res;
    }
    
    private void setUnidadAcademica(){
        Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
        PersonalAdministrativo pa = getDaos().getPersonalAdministrativoDao().findByUsuario(u.getId());
        if(pa != null)        
            ssu.parametrosServidor.put("a.unidadAcademica.id",pa.getUnidadAcademica().getId());
    }
}

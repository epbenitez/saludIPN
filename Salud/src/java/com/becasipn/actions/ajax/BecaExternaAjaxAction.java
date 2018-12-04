package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.persistence.model.OtorgamientoExterno;
import com.becasipn.persistence.model.BitacoraOtorgamientoExterno;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.util.List;
import java.text.SimpleDateFormat;


public class BecaExternaAjaxAction extends JSONAjaxAction{
    
    private String curp;
    private String boleta;
    private String nombre;
    private String apellidop;
    private String apellidom;
    private String becaExterna;
    private String activo;
    private String nombrecompleto;
    private String valordeactivo;
    private String otorextgid;
    
    public String lista() {
        List<OtorgamientoExterno> lista;
        if(!getBoleta().isEmpty()){
            lista = getDaos().getOtorgamientoExternoDao().findBy("boleta", getBoleta(), Boolean.TRUE);
        }else if(!getCurp().isEmpty()){
            lista = getDaos().getOtorgamientoExternoDao().findBy("curp", getCurp(), Boolean.TRUE);
        }else{
            if(!getApellidop().isEmpty()){
                if(!getApellidom().isEmpty()){
                    nombrecompleto = getNombre() + " " + getApellidop() + " " + getApellidom();
                }else{
                    nombrecompleto = getNombre() + " " + getApellidop();
                }
            }else{
                nombrecompleto = getNombre();
            }
            lista = getDaos().getOtorgamientoExternoDao().getOEByName(nombrecompleto);
        }
        if(lista==null){
           return SUCCESS_JSON; 
        }
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        for (OtorgamientoExterno otorgamientosexterno : lista) {
            if (otorgamientosexterno.getActivo()!=null && otorgamientosexterno.getActivo()){
                valordeactivo =  "<span data-toggle='tooltip' data-placement='auto' style='color:green' class='fa-stack' title='Tiene beca externa'><i class='fa fa-check-square fa-stack-2x'></i></span>";
            }else {
                valordeactivo =  "<span data-toggle='tooltip' data-placement='auto' style='color:red' class='fa-stack' title='No tiene beca externa'><i class='fa fa-square fa-stack-2x'></i> <i class='fa fa-times  fa-stack-1x fa-inverse'></i></span>";
            }
            getJsonResult().add("[\"" + (otorgamientosexterno.getBoleta()==null?"Sin información":otorgamientosexterno.getBoleta())
                    + "\", \"" + otorgamientosexterno.getNombre()
                    + "\", \"" + otorgamientosexterno.getCurp()
                    + "\", \"" + (otorgamientosexterno.getUnidadacademica()==null?"Sin información":otorgamientosexterno.getUnidadacademica().getNombreCorto())
                    + "\", \"" + (otorgamientosexterno.getPeriodo()==null?"Sin información":otorgamientosexterno.getPeriodo().getClave())
                    + "\", \"" + valordeactivo
                    + "\", \"<a title='Editar Beca Extrena' class='fancybox fancybox.iframe solo-lectura table-link' href='/becasexternas/formBecaExterna.action?curp=" + otorgamientosexterno.getCurp() + "&usuario=" + usuario.getId() + "&otorg=" + otorgamientosexterno.getId() + "&activ=" + otorgamientosexterno.getActivo() + "'> <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-pencil-square-o fa-stack-1x fa-inverse'></i></span></a>"
                    + "\", \"<a title='Bitacora' class='fancybox fancybox.iframe solo-lectura table-link' href='/becasexternas/bitacoraBecaExterna.action?otorextgid=" + otorgamientosexterno.getId() + "' onClick='' > <span class='fa-stack'><i class='fa fa-square fa-stack-2x'></i><i class='fa fa-clipboard fa-stack-1x fa-inverse'></i></span></a>"
                    + " \"]");
        }
        return SUCCESS_JSON;
    }
    
    public String bitac(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        List<BitacoraOtorgamientoExterno> lista = getDaos().getBitacoraOtorgamientoExternoDao().findBy("otorgamientoExterno.id", getOtorextgid(), Boolean.TRUE);
        for (BitacoraOtorgamientoExterno bitacoraotorgamientoexterno : lista) {
            getJsonResult().add("[\"" + bitacoraotorgamientoexterno.getFoliobaja()
                    + "\", \"" + bitacoraotorgamientoexterno.getUnidadacademica().getNombreCorto()
                    + "\", \"" + bitacoraotorgamientoexterno.getPeriodo().getDescripcion()
                    + "\", \"" + bitacoraotorgamientoexterno.getComentarios()
                    + "\", \"" + bitacoraotorgamientoexterno.getUsuario().getUsuario()
                    + "\", \"" + (bitacoraotorgamientoexterno.getFecha()==null?"Sin información":sdf.format(bitacoraotorgamientoexterno.getFecha()))
                    + " \"]");
        }
        return SUCCESS_JSON;
    }
    
    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBecaExterna() {
        return becaExterna;
    }

    public void setBecaExterna(String becaExterna) {
        this.becaExterna = becaExterna;
    }
    
    public String getActivo() {
        return activo;
    }
    
    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getApellidop() {
        return apellidop;
    }

    public void setApellidop(String apellidop) {
        this.apellidop = apellidop;
    }

    public String getApellidom() {
        return apellidom;
    }

    public void setApellidom(String apellidom) {
        this.apellidom = apellidom;
    }

    public String getOtorextgid() {
        return otorextgid;
    }

    public void setOtorextgid(String otorextgid) {
        this.otorextgid = otorextgid;
    }
    
    
}
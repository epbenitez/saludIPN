package com.becasipn.actions.catalogos;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.BitacoraOtorgamientoExternoBO;
import com.becasipn.business.OtorgamientoBecaExternaBO;
import com.becasipn.persistence.model.BitacoraOtorgamientoExterno;
import com.becasipn.persistence.model.OtorgamientoExterno;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.opensymphony.xwork2.ActionContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

public class AdministracionBecaExternaAction extends BaseAction implements MensajesCatalogos {

    public static final String FORMULARIO = "formulario";
    public static final String LISTA = "lista";
    private BitacoraOtorgamientoExterno bitacoraOtorgamientoExterno = new BitacoraOtorgamientoExterno();
    private Boolean activo;
    private UnidadAcademica unidadacademica;
    private Periodo periodo;
    private String curp;
    private String boleta;
    private String nombre;
    private String apellidop;
    private String apellidom;
    private InputStream excelStream;
    private String contentDisposition;
    private BigDecimal becaid;


    public String lista() {
        return SUCCESS;
    }

    public String form() {
        return SUCCESS;
    }
    
    public String bitacora() {
        return SUCCESS;
    }

    public String guarda() {
        BitacoraOtorgamientoExternoBO bo = new BitacoraOtorgamientoExternoBO(getDaos());
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");

        bitacoraOtorgamientoExterno.setFecha(new Date());
        bitacoraOtorgamientoExterno.setUsuario(usuario);
        if(activo != null){
            bitacoraOtorgamientoExterno.setActivo(activo);
        }
        else{
            bitacoraOtorgamientoExterno.setActivo(Boolean.TRUE);
        }
        OtorgamientoExterno oe = getDaos().getOtorgamientoExternoDao().findById(bitacoraOtorgamientoExterno.getOtorgamientoExterno().getId());
        if (oe != null) {
            oe.setActivo(bitacoraOtorgamientoExterno.getActivo());
            oe.setPeriodo(bitacoraOtorgamientoExterno.getPeriodo());
            oe.setUnidadacademica(bitacoraOtorgamientoExterno.getUnidadacademica());
            getDaos().getOtorgamientoExternoDao().update(oe);
            
        }
        if (bo.guardaBitacoraOtorgamientoExterno(bitacoraOtorgamientoExterno)) {
            addActionMessage(getText("catalogo.guardado.exito"));
        } else {
            addActionError(getText("catalogo.guardado.error"));
        }
        return FORMULARIO;
    }
    
        public String descarga() {
        OtorgamientoBecaExternaBO oBE = OtorgamientoBecaExternaBO.getInstance(getDaos());
        oBE.setPeriodoId(periodo.getId()==null?"":periodo.getId().toString());
        BigDecimal uAId = (unidadacademica.getId() == null || unidadacademica.getId().compareTo(new BigDecimal(0)) == 0) ? new BigDecimal(0) : null;
        oBE.setuAId(uAId);
        
        excelStream = oBE.getInfoExcel(periodo.getId(), unidadacademica.getId(), becaid, unidadacademica.getNombreCorto());
        String titulo = oBE.getTitulo();
        setContentDisposition("attachment; filename="+ titulo);

        return "archivo";
    }
    
    public String reporteEstadistica() {
        return SUCCESS;
    }
    
     public String success() {
        return SUCCESS;
    }

    public BitacoraOtorgamientoExterno getBitacoraOtorgamientoExterno() {
        return bitacoraOtorgamientoExterno;
    }

    public void setBitacoraOtorgamientoExterno(BitacoraOtorgamientoExterno bitacoraOtorgamientoExterno) {
        this.bitacoraOtorgamientoExterno = bitacoraOtorgamientoExterno;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public UnidadAcademica getUnidadacademica() {
        return unidadacademica;
    }

    public void setUnidadacademica(UnidadAcademica unidadacademica) {
        this.unidadacademica = unidadacademica;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
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

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public BigDecimal getBecaid() {
        return becaid;
    }

    public void setBecaid(BigDecimal becaid) {
        this.becaid = becaid;
    }
   
    
}

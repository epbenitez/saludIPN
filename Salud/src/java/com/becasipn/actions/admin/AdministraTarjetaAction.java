package com.becasipn.actions.admin;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.AlumnoTarjetaBancariaBO;
import com.becasipn.business.TarjetaBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.util.ExcelExport;
import com.opensymphony.xwork2.ActionContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

public class AdministraTarjetaAction extends BaseAction implements MensajesAdmin {

    private final String ASIGNACION = "asignacion";

    private AlumnoTarjetaBancaria tarjetaAlumno;
    private String cambiarEstatus;

    private List<String> lote;
    private List<String> identificadores;
    private String lot;
    private String noTarjeta = "";

    private Boolean warning = Boolean.FALSE;

    private List<UnidadAcademica> unidadAcademicaList;
    private List<EstatusTarjetaBancaria> estatus;
    private String identificador;
    private String observaciones;
    private String tarjetasDisponibles;

    private int tipoAsignacion;
    private String boleta;
    private BigDecimal nivel;
    private BigDecimal unidadAcademica;
    private BigDecimal periodo;
    private int tipoArchivo;
    private int operacion;
    private int total;
    private int mal;
    private int bien;

    private InputStream excelStream;
    private String contentDisposition;
    // private String[] documentFormat = {"xlsx", "csv"};

    public String cambioEstatus() {
        clearMessages();
        clearErrors();
        unidadAcademicaList = getDaos().getUnidadAcademicaDao().findAll();
        estatus = getDaos().getEstatusTarjetaBancariaDao().findAll();
        identificadores = getDaos().getAlumnoTarjetaDao().identificadores();
        return SUCCESS;
    }

    public String personalizacion() {
        clearMessages();
        clearErrors();
        identificadores = getDaos().getAlumnoTarjetaDao().identificadores();
        unidadAcademicaList = getDaos().getUnidadAcademicaDao().findAll();
        return SUCCESS;
    }

    public String cambiarEstatus() {
        estatus = getDaos().getEstatusTarjetaBancariaDao().findAll();
        unidadAcademicaList = getDaos().getUnidadAcademicaDao().findAll();
        identificadores = getDaos().getAlumnoTarjetaDao().identificadores();
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        if (cambiarEstatus != null && !cambiarEstatus.trim().isEmpty()) {
            TarjetaBO bo = new TarjetaBO(getDaos());
            Boolean res = bo.cambiarEstatus(cambiarEstatus,usuario);
            if (res) {
                addActionMessage(getText("admin.guardado.exito"));
            } else {
                addActionError(getText("admin.guardado.error"));
            }
        }
        return "estatus";
    }
    
    public String asignacion() {
        clearMessages();
        clearErrors();
        unidadAcademicaList = getDaos().getUnidadAcademicaDao().findAll();
        tarjetasDisponibles = getDaos().getTarjetaBancariaDao().countTarjetasDisponibles();
        return ASIGNACION;
    }

    public String asignar() {
        clearMessages();
        clearErrors();
        AlumnoTarjetaBancariaBO atBO = new AlumnoTarjetaBancariaBO(getDaos());
        Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
        List<AlumnoTarjetaBancaria> alumnos;
        if (tipoAsignacion == 1||tipoAsignacion==3) {
            alumnos = getDaos().getAlumnoTarjetaDao().listadoAlumnoTarjetaRemplazar(boleta, nivel, unidadAcademica, periodo,tipoAsignacion);
        } else {
            alumnos = new ArrayList<>();
            List<Alumno> alumnosResultados = getDaos().getAlumnoDao().listadoAlumnoNueavasTarjetas(boleta, nivel, unidadAcademica);
            for (Alumno alumnosResultado : alumnosResultados) {
                AlumnoTarjetaBancaria nuevo = new AlumnoTarjetaBancaria();
                nuevo.setAlumno(alumnosResultado);
                alumnos.add(nuevo);
            }
        }
        observaciones += "||" + identificador + "||" + periodo;
        List<AlumnoTarjetaBancaria> resultado = atBO.asignarTarjeta(alumnos, usuario, identificador, observaciones);
        StringBuilder mensaje = new StringBuilder();
        if(resultado.size()==alumnos.size()){
            mensaje.append(getText("tarjetas.exito.asignaciones")).append("("+resultado.size()+")");
        }else{
            mensaje.append(getText("tarjetas.error.asignaciones")).append(" ").append(resultado.size())
            .append(" de ").append(alumnos.size());
        }
        mensaje.append("<ul><li><a href='/admin/descargarTarjeta.action?identificador=")
                .append(identificador).append("&periodo=").append(periodo)
                .append("&tipoArchivo=1'>PersonalAdministrativo").append(identificador).append("</a></li>")
                .append("<li><a href='/admin/descargarTarjeta.action?identificador=")
                .append(identificador).append("&periodo=").append(periodo)
                .append("&tipoArchivo=2'>PlantillaPersonalizacion").append(identificador).append("</a></li></ul>");
        addActionMessage(mensaje.toString());
        return ASIGNACION;
    }
    //05/06/18 Actualizacion de reportes excel. Reporte obsoleto,  no se hacen cambios
    public String descargar() {

        AlumnoTarjetaBancariaBO bo = new AlumnoTarjetaBancariaBO(getDaos());
        ExcelExport excelExport = new ExcelExport();
        if (tipoArchivo == 1) {
            String[] encabezado = new String[]{"Secuencia", "No Tarjeta Nueva",
                "No Tarjeta Anterior", "Clave", "Escuela", "CURP", "Boleta", "Nombre", "Beca", "Email"};
            setContentDisposition("attachment; filename=\"Listado Administrativo - " + identificador + ".xlsx\"");
            excelStream = excelExport.construyeExcel(encabezado, bo.imprimirAsignacionTarjeta(identificador, periodo, encabezado, tipoArchivo));
        } else {
            String[] encabezado = new String[]{"Tipo de Registro", "Tipo de Producto",
                "Tipo de Entrega", "Número de Unidad de Trabajo", "Tipo de Persona", "Nombre del Empleado",
                "Apellido Paterno del Empleado", "Apellido Materno del Empleado", "CURP", "Lugar de Nacimiento",
                "Fecha de Nacimiento", "Estado Civil", "Calle y Numero", "Colonia", "Código Postal",
                "Clave de Larga Distancia + Teléfono Domicilio", "Clave de Larga Distancia + Teléfono Oficina",
                "Extensión", "E-mail", "Población o Delegación", "Estado", "Número de Tarjeta", "Forma de Pago",
                "Asignación de Pago", "Actividad u Ocupación", "Sexo", "Nacionalidad", "Datos de Benificiario",
                "Nombre del Benificiario", "Apellido Paterno del Beneiciario", "Apellido Materno del Beneficiario",
                "Parentesco", "Fecha de Nacimiento", "Calle y Numero", "Colonia", "Población o Delegación",
                "Código Postal", "Estado"};
            setContentDisposition("attachment; filename=\"Plantilla - " + identificador + ".xlsx\"");
            excelStream = excelExport.construyeExcel(encabezado, bo.imprimirAsignacionTarjeta(identificador, periodo, encabezado, tipoArchivo));
        }

        return "archivo";
    }

    public String revertirEnviar() {
        clearMessages();
        clearErrors();
        AlumnoTarjetaBancariaBO atBO = new AlumnoTarjetaBancariaBO(getDaos());
        int enviados=0,noenviados=0;
        if (operacion == 0) {
            int reversiones = atBO.revertirAsignaciones(identificador);
            if (reversiones >= 0) {
                addActionMessage(reversiones + " " + getText("tarjetas.exito.reversion"));
            } else {
                addActionError(getText("admin.tarjeta.reversion.error"));
            }
        } else {
            List<AlumnoTarjetaBancaria> asignaciones = getDaos().getAlumnoTarjetaDao().listAsignacionesIdentificador(identificador);
            StringBuilder msg;
            for (AlumnoTarjetaBancaria asignacion : asignaciones) {
                msg = new StringBuilder();
                try {
                    sendEmail(asignacion.getAlumno().getCorreoElectronico(), "Asignación de Tatjeta", msg.toString());
                    enviados++;
                } catch (MessagingException ex) {
                    Logger.getLogger(AdministraTarjetaAction.class.getName()).log(Level.SEVERE, null, ex);
                    noenviados++;
                }
            }
            addActionMessage("<ul><li>Se enviaron "+enviados+" correos de manera correcta</li>"
                    +(noenviados>0?"<li>No se pudieron enviar "+noenviados+" correos</li>":"")+"</ul>");
            
        }
        return ASIGNACION;
    }

    public AlumnoTarjetaBancaria getTarjeta() {
        return tarjetaAlumno;
    }

    public void setTarjeta(AlumnoTarjetaBancaria tarjeta) {
        this.tarjetaAlumno = tarjeta;
    }

    public AlumnoTarjetaBancaria getTarjetaAlumno() {
        return tarjetaAlumno;
    }

    public void setTarjetaAlumno(AlumnoTarjetaBancaria tarjetaAlumno) {
        this.tarjetaAlumno = tarjetaAlumno;
    }

    public Boolean getWarning() {
        return warning;
    }

    public void setWarning(Boolean warning) {
        this.warning = warning;
    }

    public String getCambiarEstatus() {
        return cambiarEstatus;
    }

    public void setCambiarEstatus(String cambiarEstatus) {
        this.cambiarEstatus = cambiarEstatus;
    }

    public List<String> getLote() {
        return lote;
    }

    public void setLote(List<String> lote) {
        this.lote = lote;
    }

    public String getNoTarjeta() {
        return noTarjeta;
    }

    public void setNoTarjeta(String noTarjeta) {
        this.noTarjeta = noTarjeta;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public List<UnidadAcademica> getUnidadAcademicaList() {
        return unidadAcademicaList;
    }

    public void setUnidadAcademicaList(List<UnidadAcademica> unidadAcademicaList) {
        this.unidadAcademicaList = unidadAcademicaList;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTarjetasDisponibles() {
        return tarjetasDisponibles;
    }

    public void setTarjetasDisponibles(String tarjetasDisponibles) {
        this.tarjetasDisponibles = tarjetasDisponibles;
    }

    public int getTipoAsignacion() {
        return tipoAsignacion;
    }

    public void setTipoAsignacion(int tipoAsignacion) {
        this.tipoAsignacion = tipoAsignacion;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public BigDecimal getNivel() {
        return nivel;
    }

    public void setNivel(BigDecimal nivel) {
        this.nivel = nivel;
    }

    public BigDecimal getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(BigDecimal unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMal() {
        return mal;
    }

    public void setMal(int mal) {
        this.mal = mal;
    }

    public int getBien() {
        return bien;
    }

    public void setBien(int bien) {
        this.bien = bien;
    }

    public BigDecimal getPeriodo() {
        return periodo;
    }

    public void setPeriodo(BigDecimal periodo) {
        this.periodo = periodo;
    }

    public int getOperacion() {
        return operacion;
    }

    public void setOperacion(int operacion) {
        this.operacion = operacion;
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

    public int getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(int tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public List<String> getIdentificadores() {
        return identificadores;
    }

    public void setIdentificadores(List<String> identificadores) {
        this.identificadores = identificadores;
    }

    public List<EstatusTarjetaBancaria> getEstatus() {
        return estatus;
    }

    public void setEstatus(List<EstatusTarjetaBancaria> estatus) {
        this.estatus = estatus;
    }

    
}

package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseAction;
import com.becasipn.business.TarjetaBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.PersonalAdministrativo;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import static com.becasipn.util.Util.CampoValidoAJAX;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Patricia Benítez
 */
public class AdministraDepositosAction extends BaseAction implements MensajesAlumno {

    private final String FORM = "form";

    private String boleta;
    private String nombre;
    private String ap_paterno;
    private String ap_materno;
    private String curp;
    private Alumno alumno = new Alumno();
    private AlumnoTarjetaBancaria tarjetaAlumno = new AlumnoTarjetaBancaria();
    private TarjetaBancaria tarjetaBancaria = new TarjetaBancaria();
    private final TarjetaBO tarjetaBO = new TarjetaBO(getDaos());
    private String numero_tarjeta;
    private List<Depositos> depositosAlumno;
    private BigDecimal idAlumno;
    private String numBoleta;
    private int msjError;
    private String textoMensaje;
    public static final String BUSCAR = "buscar";
    public static final String ERROR = "error";
    private final Map<String, Object> parametros = new HashMap<>();
    private BigDecimal noIdalumno;
    private String numeroDeBoleta;
    private BigDecimal idOtorgamiento;
    private Otorgamiento otorgamiento;

    public String ver() {
        if (isAlumno()) {
            boleta = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Alumno> listAlumno = getDaos().getAlumnoDao().getByBoleta(boleta);
            alumno = listAlumno.get(0);
            BigDecimal periodoActivoId = getDaos().getPeriodoDao().getPeriodoActivo().getId();            
            // Variable que sirve para buscar en el JSP mediante Ajax, los depósitos por ID del alumno
            idAlumno = alumno.getId();
            // COnsigue datos académicos del alumno
            DatosAcademicos datosAcademicos = getDaos().getDatosAcademicosDao().datosPorPeriodo(idAlumno, periodoActivoId);
            if (datosAcademicos == null)
                datosAcademicos = getDaos().getDatosAcademicosDao().ultimosDatos(idAlumno);
            alumno.setDatosAcademicos(datosAcademicos);
            // Obtiene la última tarjeta modificada en bitácora del alumno
            tarjetaAlumno = tarjetaBO.obtenerTarjeta(alumno.getUsuario().getId());
            // Condicional para poner el dato de la tarjeta en el cuadro de datos del alumno
            if (tarjetaAlumno == null) {
                return ERROR;
            } else {
                numero_tarjeta = (!tarjetaBO.IsTarjetaActiva(tarjetaAlumno.getTarjetaBancaria().getBitacoraTarjetaBancarias()))
                        ? "NO TIENES TARJETA ACTIVA" : String.valueOf(tarjetaAlumno.getTarjetaBancaria().getNumtarjetabancaria());
            }
            return FORM;
        } else {
            alumno = new Alumno();
            return BUSCAR;

        }
    }

    public String datosAlumno() {
        msjError = 0;
        alumno = null;
        if (parametros.size() > 0) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            PersonalAdministrativo personal = getDaos().getPersonalAdministrativoDao().findByUsuario(usuario.getId());
            if (isResponsable() || isFuncionario()) {
                parametros.put("a.unidadAcademica.id", personal.getUnidadAcademica());
            }
            alumno = getDaos().getAlumnoDao().findByBoleta(parametros);
        }

        if (alumno == null) {
            msjError = 1;
            textoMensaje = "Los datos ingresados no corresponden a algún alumno con beca";
        } else {
            noIdalumno = alumno.getId();
            tarjetaAlumno = tarjetaBO.obtenerTarjeta(alumno.getUsuario().getId());
            if (tarjetaAlumno == null) {
                msjError = 1;
                textoMensaje = "El alumno no cuenta con tarjeta asignada";
//                System.out.println("Entre al error: " + msjError);
            } else {
                if (!tarjetaBO.IsTarjetaActiva(tarjetaAlumno.getTarjetaBancaria().getBitacoraTarjetaBancarias())) {
//                    System.out.println("Numero tarjeta: " + tarjetaBO.IsTarjetaActiva(tarjetaAlumno.getTarjetaBancaria().getBitacoraTarjetaBancarias()));
                    numero_tarjeta = "NO TIENES TARJETA ACTIVA";
                } else {
                    numero_tarjeta = String.valueOf(tarjetaAlumno.getTarjetaBancaria().getNumtarjetabancaria());
                }
            }
        }
        return BUSCAR;
    }

    /**
     * Muestra los depositos para un alumno
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String form() {
        if (isAlumno()) {
            Usuario usuario = (Usuario) ActionContext.getContext().getSession().get("usuario");
            alumno = getDaos().getAlumnoDao().getByUsuario(usuario.getId());
        } else {
            alumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta).get(0);
        }
        alumno.setDatosAcademicos(getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId()));
        tarjetaAlumno = tarjetaBO.obtenerTarjeta(alumno.getUsuario().getId());
        if (tarjetaAlumno != null && tarjetaBO.IsTarjetaActiva(tarjetaAlumno.getTarjetaBancaria().getBitacoraTarjetaBancarias())) {
            numero_tarjeta = String.valueOf(tarjetaAlumno.getTarjetaBancaria().getNumtarjetabancaria());
        } else {
            numero_tarjeta = "No hay una tarjeta activa";
        }
        if(getIdOtorgamiento()!=null){
            otorgamiento = getDaos().getOtorgamientoDao().findById(idOtorgamiento);
        }
        return FORM;
    }

    public String getNumBoleta() {
        return numBoleta;
    }

    public void setNumBoleta(String numBoleta) {
        this.numBoleta = numBoleta;
        if (CampoValidoAJAX(numBoleta)) {
            parametros.put("a.boleta", this.numBoleta);
        }
    }

    public BigDecimal getNoIdalumno() {
        return noIdalumno;
    }

    public void setNoIdalumno(BigDecimal noIdalumno) {
        this.noIdalumno = noIdalumno;
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
        if (CampoValidoAJAX(nombre)) {
            parametros.put("a.nombre", this.nombre.toUpperCase());
        }
    }

    public String getAp_paterno() {
        return ap_paterno;
    }

    public void setAp_paterno(String ap_paterno) {
        this.ap_paterno = ap_paterno;
        if (CampoValidoAJAX(ap_paterno)) {
            parametros.put("a.apellidoPaterno", this.ap_paterno.toUpperCase());
        }
    }

    public String getAp_materno() {
        return ap_materno;
    }

    public void setAp_materno(String ap_materno) {
        this.ap_materno = ap_materno;
        if (CampoValidoAJAX(ap_materno)) {
            parametros.put("a.apellidoMaterno", this.ap_materno.toUpperCase());
        }
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
        if (CampoValidoAJAX(curp)) {
            parametros.put("a.curp", this.curp.toUpperCase());
        }
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public AlumnoTarjetaBancaria getTarjetaAlumno() {
        return tarjetaAlumno;
    }

    public void setTarjetaAlumno(AlumnoTarjetaBancaria tarjetaAlumno) {
        this.tarjetaAlumno = tarjetaAlumno;
    }

    public TarjetaBancaria getTarjetaBancaria() {
        return tarjetaBancaria;
    }

    public void setTarjetaBancaria(TarjetaBancaria tarjetaBancaria) {
        this.tarjetaBancaria = tarjetaBancaria;
    }

    public String getNumero_tarjeta() {
        return numero_tarjeta;
    }

    public void setNumero_tarjeta(String numero_tarjeta) {
        this.numero_tarjeta = numero_tarjeta;
    }

    public List<Depositos> getDepositosAlumno() {
        return depositosAlumno;
    }

    public void setDepositosAlumno(List<Depositos> depositosAlumno) {
        this.depositosAlumno = depositosAlumno;
    }

    public BigDecimal getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(BigDecimal idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getMsjError() {
        return msjError;
    }

    public void setMsjError(int msjError) {
        this.msjError = msjError;
    }

    public String getTextoMensaje() {
        return textoMensaje;
    }

    public void setTextoMensaje(String textoMensaje) {
        this.textoMensaje = textoMensaje;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }

    public BigDecimal getIdOtorgamiento() {
        return idOtorgamiento;
    }

    public void setIdOtorgamiento(BigDecimal idOtorgamiento) {
        this.idOtorgamiento = idOtorgamiento;
    }

    public Otorgamiento getOtorgamiento() {
        return otorgamiento;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
    }
    
}

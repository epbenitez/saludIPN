package com.becasipn.actions.alumno;

import com.becasipn.actions.BaseAction;
import com.becasipn.actions.becas.MensajesBecas;
import com.becasipn.business.TarjetaBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigInteger;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez
 */
public class AdministracionMonitoreoTarjetas extends BaseAction implements MensajesBecas {

    public static final String VER = "ver";
    public static final String DETALLE = "detalle";
    public static final String BUSCAR = "buscar";
    public static final String BITACORA = "bitacora";

    private final TarjetaBO tarjetaBO;

    private Alumno alumno;
    private String mainMsg;
    private BigInteger numeroTarjetaBancaria;
    private AlumnoTarjetaBancaria tarjetaAlumno;
    private Boolean isAccount = false;
    private String msgAccountorCard;
    private String numero_tarjeta;
    private String numeroDeBoleta;
    private Integer stsId;

    public AdministracionMonitoreoTarjetas() {
        tarjetaBO = new TarjetaBO(getDaos());
    }

    public String ver() {
        if (isAlumno()) {
            String numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Alumno> listAlumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta);
            Alumno alumno = listAlumno.get(0);
            Usuario u = (Usuario) ActionContext.getContext().getSession().get("usuario");
            //Último objeto tipo tarjeta asignado.
            AlumnoTarjetaBancaria tarjetaBancaria = tarjetaBO.obtenerTarjetaV2(u.getId());

            if (tarjetaBancaria == null) {
                return ERROR;
            } else {
                EstatusTarjetaBancaria statusCC = tarjetaBancaria.getEstatusTarjBanc();
                Boolean hasSts = statusCC != null;
                if (hasSts) {
                    stsId = statusCC.getId().intValue();
                    Boolean relevantSts = stsId >= 12;

                    if (relevantSts) {
                        isAccount = true;
                        if (stsId == 13) {
                            // Para saber si es cuenta o clabe
                            Integer cuenta = tarjetaBancaria.getTarjetaBancaria().getCuenta();
                            mainMsg = tarjetaBancaria.getTarjetaBancaria().getNumtarjetabancaria();
                            // Subtítulo
                            if (cuenta == 1) {
                                msgAccountorCard = statusCC.getObservaciones();
                            } else {
                                msgAccountorCard = "CLABE interbancaria";
                            }
                        } else {
                            mainMsg = statusCC.getDescripcion();
                            msgAccountorCard = statusCC.getObservaciones();
                        }
                    }
                } else {
                    mainMsg = "Sin cuenta bancaria";
                    msgAccountorCard = "No has solicitado una cuenta";
                }

                return VER;
            }
        } else {
            if (numeroTarjetaBancaria == null) {
                return BUSCAR;
            } else {
                return DETALLE;
            }
        }
    }

    /**
     * Bitacora de tarjeta para un alumno
     *
     * @author Victor Lozano
     * @return SUCCESS
     */
    public String bitacora() {
        alumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta).get(0);
        alumno.setDatosAcademicos(getDaos().getDatosAcademicosDao().ultimosDatos(alumno.getId()));
        TarjetaBO tarjetaBO = new TarjetaBO(getDaos());
        tarjetaAlumno = tarjetaBO.obtenerTarjeta(alumno.getUsuario().getId());
        if (tarjetaAlumno != null && tarjetaBO.IsTarjetaActiva(tarjetaAlumno.getTarjetaBancaria().getBitacoraTarjetaBancarias())) {
            numero_tarjeta = String.valueOf(tarjetaAlumno.getTarjetaBancaria().getNumtarjetabancaria());
        } else {
            numero_tarjeta = "No hay una tarjeta activa";
        }
        return BITACORA;
    }

    public String getMainMsg() {
        return mainMsg;
    }

    public void setMainMsg(String mainMsg) {
        this.mainMsg = mainMsg;
    }

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
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

    public String getNumero_tarjeta() {
        return numero_tarjeta;
    }

    public void setNumero_tarjeta(String numero_tarjeta) {
        this.numero_tarjeta = numero_tarjeta;
    }

    public Boolean getIsAccount() {
        return isAccount;
    }

    public void setIsAccount(Boolean isAccount) {
        this.isAccount = isAccount;
    }

    public String getMsgAccountorCard() {
        return msgAccountorCard;
    }

    public void setMsgAccountorCard(String msgAccountorCard) {
        this.msgAccountorCard = msgAccountorCard;
    }

    public void setStsId(Integer stsId) {
        this.stsId = stsId;
    }

    public Integer getStsId() {
        return stsId;
    }

    public BigInteger getNumeroTarjetaBancaria() {
        return numeroTarjetaBancaria;
    }

    public void setNumeroTarjetaBancaria(BigInteger numeroTarjetaBancaria) {
        this.numeroTarjetaBancaria = numeroTarjetaBancaria;
    }

}

package com.becasipn.actions.ajax;

import com.becasipn.business.TarjetaBO;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Tania G. Sánchez
 */
public class BitacoraAlumnoTarjetaAjaxAction extends JSONAjaxAction {

    private BigInteger numTarjeta;
    private String boleta;
    private String curp;
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private String numeroDeBoleta;

    public String listado() {
        List<BitacoraTarjetaBancaria> list;
        Alumno alumno;
        if (isAlumno()) {
            numeroDeBoleta = SecurityContextHolder.getContext().getAuthentication().getName();
        }
        List<Alumno> listAlumno = getDaos().getAlumnoDao().getByBoleta(numeroDeBoleta);
        if (!listAlumno.isEmpty()) {
            alumno = listAlumno.get(0);
            list = getDaos().getBitacoraTarjetaBancariaDao().monitoreoTarjetaBancaria(alumno.getId());
            java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("dd MMMM yyy", new Locale("es", "ES"));
            java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("hh:mm:ss");

            for (BitacoraTarjetaBancaria monitoreo : list) {

                getJsonResult().add("["
                        + "\"" + (monitoreo.getTarjetaBancaria().getCuenta()==1?"Cuenta Bancaria":(monitoreo.getTarjetaBancaria().getCuenta()==2?"Referencia Bancaria":(monitoreo.getTarjetaBancaria().getCuenta()==3?"CLABE":"Tarjeta")))
                        + "\", \"" +  TarjetaBO.aplicaFormatoNumeroTarjeta(monitoreo.getTarjetaBancaria())
                        + "\", \"" + monitoreo.getEstatus().getNombre()
                        + "\", \"" + sdf1.format(monitoreo.getFechaModificacion())
                        + "\", \"" + sdf2.format(monitoreo.getFechaModificacion())
                        + "\", \"" + (monitoreo.getObservaciones() == null ? "" : monitoreo.getObservaciones().replace("\n", "-"))
                        + "\"]");
            }
        }
        return SUCCESS_JSON;

    }

    public BigInteger getNumTarjeta() {
        return numTarjeta;
    }

    public void setNumTarjeta(BigInteger numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
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

    public String getNumeroDeBoleta() {
        return numeroDeBoleta;
    }

    public void setNumeroDeBoleta(String numeroDeBoleta) {
        this.numeroDeBoleta = numeroDeBoleta;
    }
}

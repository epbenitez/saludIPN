package com.becasipn.business;

import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.Rol;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Gustavo Adolfo Alamillo Medina
 * @version 1.0
 */
public class TarjetaBO extends BaseBO {

    public TarjetaBO(Service service) {
        super(service);
    }

    /**
     * Obtiene la última tarjeta registrada en el sistema con el <b>id</b> del
     * alumno, si no tiene ninguna tarjeta activa devolverá <i>null</i>
     *
     * @param usuarioId Id del alumno en la base de datos
     * @return Instancia de AlumnoTarjetaBancaria de la última tarjeta ó
     * <i>null</i> si no tiene tarjeta activa.
     */
    
    public AlumnoTarjetaBancaria obtenerTarjeta(BigDecimal usuarioId) {
        BigDecimal idAlumno = service.getAlumnoDao().getIdUsuario(usuarioId);
        BitacoraTarjetaBancaria ultimaFilaBitacora
                = service.getBitacoraTarjetaBancariaDao().getUltimaTarjetaBitacora(idAlumno);
        if (null != ultimaFilaBitacora) {
            return service.getAlumnoTarjetaDao().getTarjetaAlumno(idAlumno, ultimaFilaBitacora.getTarjetaBancaria().getId());
        }
        return null;
    }
    
    /**
     * Obtiene la última tarjeta registrada en el sistema con el <b>id</b> del
     * alumno, si no tiene ninguna tarjeta activa devolverá <i>null</i>
     *
     * @param usuarioId Id del alumno en la base de datos
     * @return Instancia de AlumnoTarjetaBancaria de la última tarjeta ó
     * <i>null</i> si no tiene tarjeta activa.
     */
    
    public AlumnoTarjetaBancaria obtenerTarjetaV2(BigDecimal usuarioId) {
        BigDecimal idAlumno = service.getAlumnoDao().getIdUsuario(usuarioId);
        AlumnoTarjetaBancaria ultimaT = service.getAlumnoTarjetaDao().tarjetaAlumnoV2(idAlumno);
                
        return ultimaT == null ? null : ultimaT;
    }
    
    /**
     * Método para validar la activación de una tarjeta, busca en la bitácora si
     * la tarjeta se puede activar (Estado de la tarjeta es <i>Entregado al
     * alumno</i>)
     *
     * @param bitacoraTarjetaBancaria Bitáctora de la tarjeta
     * @return <b>True</b> Si se puede activar <b>False</b> si no se puede
     * activar
     */
    public boolean sePuedeActivar(List<BitacoraTarjetaBancaria> bitacoraTarjetaBancaria) {
        return bitacoraTarjetaBancaria.get(0).getEstatus().getId().compareTo(new BigDecimal(9)) == 0;
    }

    /**
     * Revisa para el usuario dado que la tarjeta activa que tenga se pueda
     * activar.
     *
     * @param id Id del alumno que se quiere activar.
     * @return true o false dependiendo si se puede o no activar
     * @see TarjetaBO
     */
    public boolean sePuedeActivar(BigDecimal id) {
        AlumnoTarjetaBancaria alumnoTarjeta = obtenerTarjeta(id);
        return sePuedeActivar(alumnoTarjeta.getTarjetaBancaria().getBitacoraTarjetaBancarias());
    }

    /**
     * Guarda un nuevo registro en la BD, con el nuevo estatus para la tarjeta
     * activa del alumno. (Pasa al estado <i>Activada por el usuario</i>.
     *
     * @param numTarjetaUsuario El número que el usuario ingreso en la interfaz
     * @param id El id del usuario en la base de datos
     * @return <b>True</b> Si se puedo realizar el cambio de estatus
     * correctamente. <br>
     * <b>False</b> Si no se pudo realizar el cambio (El número ingresado y el
     * de la BD no coinciden, ó la tarjeta ya esta activada).
     */
    public boolean guardarTarjetaAlumno(String numTarjetaUsuario, BigDecimal id) {

        AlumnoTarjetaBancaria original = obtenerTarjeta(id);
        numTarjetaUsuario = numTarjetaUsuario.replaceAll(" ", "");

        if (original.getTarjetaBancaria().getNumtarjetabancaria().substring(11, 16).compareTo(numTarjetaUsuario) == 0
                && sePuedeActivar(original.getTarjetaBancaria().getBitacoraTarjetaBancarias())) {

            EstatusTarjetaBancaria estatus = new EstatusTarjetaBancaria();
            estatus.setId(new BigDecimal(10));

            BitacoraTarjetaBancaria btc = new BitacoraTarjetaBancaria();
            btc.setEstatus(estatus);
            btc.setTarjetaBancaria(original.getTarjetaBancaria());
            btc.setUsuario(original.getAlumno().getUsuario());
            btc.setFechaModificacion(new Date());

            service.getBitacoraTarjetaBancariaDao().save(btc);
            original.setTarjetaActiva(true);
            service.getAlumnoTarjetaDao().update(original);
            return true;
        } else {
            return false;
        }
    }
    
    public Boolean guardaTarjetaBancaria(TarjetaBancaria t) {
        try {
            if (t.getId() == null) {
                service.getTarjetaBancariaDao().save(t);

            } else {
                service.getTarjetaBancariaDao().update(t);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public boolean sePuedeBorrar(List<BitacoraTarjetaBancaria> bitacoraTarjetaBancaria) {
        return bitacoraTarjetaBancaria.get(0).getEstatus().getId().compareTo(new BigDecimal(10)) == 0;
    }

    public boolean sePuedeBorrar(AlumnoTarjetaBancaria tarjetaAlumno) {
        tarjetaAlumno = service.getAlumnoTarjetaDao().findById(tarjetaAlumno.getId());
        return sePuedeBorrar(tarjetaAlumno.getTarjetaBancaria().getBitacoraTarjetaBancarias());
    }

    public boolean suspender(AlumnoTarjetaBancaria tarjetaAlumno, String numReporte, String motivo) {

        numReporte = numReporte;
        tarjetaAlumno = service.getAlumnoTarjetaDao().findById(tarjetaAlumno.getId());

        BitacoraTarjetaBancaria btc = new BitacoraTarjetaBancaria();
        EstatusTarjetaBancaria estatus = new EstatusTarjetaBancaria();
        estatus.setId(new BigDecimal(11));

        btc.setEstatus(estatus);
        btc.setFechaModificacion(new Date());
        btc.setTarjetaBancaria(tarjetaAlumno.getTarjetaBancaria());
        btc.setUsuario(tarjetaAlumno.getAlumno().getUsuario());
        btc.setNumReporteBanco(numReporte);
        btc.setObservaciones(motivo);
        service.getBitacoraTarjetaBancariaDao().save(btc);
        tarjetaAlumno.setTarjetaActiva(false);
        service.getAlumnoTarjetaDao().update(tarjetaAlumno);
        return true;
    }

    public boolean cambiarEstatus(String cambiarEstatus, Usuario usuario) {
        Boolean res = true;

        List<String> tarjetas = Arrays.asList(cambiarEstatus.split("\\|"));
        for (String tarjeta : tarjetas) {
            String[] ids = tarjeta.split(",");
            TarjetaBancaria tar = service.getTarjetaBancariaDao().findById(new BigDecimal(ids[0]));
            EstatusTarjetaBancaria estatus = new EstatusTarjetaBancaria();
            estatus.setId(new BigDecimal(ids[1]));
            if (ids.length > 2 && !ids[2].trim().isEmpty()) {
                res = res & cambiarEstatus(tar, estatus, usuario, ids[2]);
            } else {
                res = res & cambiarEstatus(tar, estatus, usuario);
            }
        }
        if (res) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public boolean cambiarEstatus(TarjetaBancaria tarjeta, EstatusTarjetaBancaria nuevoEstatus, Usuario usuario, String... observaciones) {
        EstatusTarjetaBancaria estatusAnterior = service.getBitacoraTarjetaBancariaDao().
                ultimaBitacoraTarjeta(tarjeta.getId()).getEstatus();
        if (verificaNuevoEstatus(estatusAnterior, nuevoEstatus)) {
            BitacoraTarjetaBancaria nuevaBitacora = new BitacoraTarjetaBancaria(nuevoEstatus, tarjeta, usuario, new Date());
            if (observaciones.length >= 1) {
                nuevaBitacora.setObservaciones(observaciones[0]);
            }
            nuevaBitacora.setEnvioCorreo(Boolean.FALSE);
            return guardaBitacoraTarjetaBancaria(nuevaBitacora);
        }
        return false;
    }

    public Boolean guardaBitacoraTarjetaBancaria(BitacoraTarjetaBancaria btb) {
        AlumnoTarjetaBancariaBO atbo = new AlumnoTarjetaBancariaBO(service);
        try {
            if (!atbo.setVigente(btb.getTarjetaBancaria())) {
                log.warn("No se actualizo el campo vigente de la relacion alumno tarjeta :" + btb.getTarjetaBancaria().getId());
            }
            if (btb.getId() == null) {
                service.getBitacoraTarjetaBancariaDao().save(btb);

            } else {
                service.getBitacoraTarjetaBancariaDao().update(btb);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public boolean personalizar(String liberadas, String rechazadas, Usuario usuario) {
        boolean res = true;
        TarjetaBancaria tarjetaBancaria;
        String observaciones;
        String[] aux;
        if (!liberadas.trim().isEmpty()) {
            List<String> tarjetasLiberadas = Arrays.asList(liberadas.split("\\|"));
            for (String tarjetasLiberada : tarjetasLiberadas) {
                aux = tarjetasLiberada.split("_");
                tarjetaBancaria = service.getTarjetaBancariaDao().findById(new BigDecimal(aux[0]));
                observaciones = aux.length > 1 ? aux[1] : "";
                res = res && cambiarEstatus(tarjetaBancaria, new EstatusTarjetaBancaria(new BigDecimal(6)), usuario, observaciones);
            }
        }
        if (!rechazadas.trim().isEmpty()) {
            List<String> tarjetasRechazadas = Arrays.asList(rechazadas.split("\\|"));
            for (String tarjetasRechazada : tarjetasRechazadas) {
                aux = tarjetasRechazada.split("_");
                tarjetaBancaria = service.getTarjetaBancariaDao().findById(new BigDecimal(aux[0]));
                observaciones = aux.length > 1 ? aux[1] : "";
                res = res && cambiarEstatus(tarjetaBancaria, new EstatusTarjetaBancaria(new BigDecimal(4)), usuario, observaciones);
            }
        }
        return res;
    }

    public boolean IsTarjetaActiva(List<BitacoraTarjetaBancaria> bitacoraTarjetaBancaria) {
        return bitacoraTarjetaBancaria.get(0).getEstatus().getId().intValue() == 10;
    }

    private boolean verificaNuevoEstatus(EstatusTarjetaBancaria estatusAnterior, EstatusTarjetaBancaria nuevoEstatus) {
        Rol rolAlto = getRolAlto();
        List<EstatusTarjetaBancaria> posiblesEstatus = service.getEstatusTarjetaBancariaDao().posiblesEstatus(estatusAnterior, rolAlto);
        if (posiblesEstatus != null && !posiblesEstatus.isEmpty()) {
            return posiblesEstatus.contains(nuevoEstatus);
        }
        return true;
    }
    
    public static String aplicaFormatoNumeroTarjeta(TarjetaBancaria tarjeta) {
        if (tarjeta.getCuenta() != 2) {
            return tarjeta.getNumtarjetabancaria();
        } else {
            NumberFormat nf = new DecimalFormat("000000000000");
            return ("IPN" + nf.format(Long.parseLong(tarjeta.getNumtarjetabancaria())));
        }
    }

}

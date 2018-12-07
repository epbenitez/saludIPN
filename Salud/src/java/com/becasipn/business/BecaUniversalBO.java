package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.AlumnoDatosBancarios;
import com.becasipn.persistence.model.AlumnoTarjetaBancaria;
import com.becasipn.persistence.model.BecaUniversal;
import com.becasipn.persistence.model.BitacoraTarjetaBancaria;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.EstatusDeposito;
import com.becasipn.persistence.model.EstatusTarjetaBancaria;
import com.becasipn.persistence.model.IdentificadorOtorgamiento;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.SolicitudCuentas;
import com.becasipn.persistence.model.TarjetaBancaria;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mario Márquez
 */
public class BecaUniversalBO extends BaseBO {
    
    private final DatosAcademicos datosA;
    private final Periodo periodoActivo;
    private final Alumno alumno;
    private final Date date;
    
    private Otorgamiento otorgamiento = new Otorgamiento();
    private AlumnoTarjetaBancaria aCC = new AlumnoTarjetaBancaria();
    private Map<String, Boolean> resultados = new HashMap<String, Boolean>();
    
    public BecaUniversalBO(Service service, Alumno alumno) {
	super(service);
        this.alumno = alumno;
        date = new Date();
        periodoActivo = service.getPeriodoDao().getPeriodoActivo();        
        datosA = service.getDatosAcademicosDao().datosPorPeriodo(this.alumno.getId(), periodoActivo.getId());
    }
    
    public Map accionesTrasTelon() {        
        creaResultadosDefault();
        
        resultados.put("exitoO", insertaOtorgamiento());
        if (resultados.get("exitoO"))
            resultados.put("exitoACC", insertaACC());
        if (resultados.get("exitoACC"))
            resultados.put("exitoB", insertaBitacora());
        if (resultados.get("exitoB"))
            resultados.put("exitoD", insertaDeposito());
        
        elimina(resultados.get("exitoO"),
                resultados.get("exitoACC"), 
                resultados.get("exitoB"), 
                resultados.get("exitoD")
        );
        
        return resultados;
    }
    
    // Si hubo alguna falla elimina objeto/s anterior/es
    private void elimina(Boolean o, Boolean acc, Boolean b, Boolean d) {
        if (!acc) {
            if (o) service.getOtorgamientoDao().delete(otorgamiento);
        } else if (!b || !d) {
            service.getOtorgamientoDao().delete(otorgamiento);
            service.getAlumnoTarjetaDao().delete(aCC);
        }
    }
    
    private Boolean insertaDeposito() {
        DepositosBO depositBO = new DepositosBO(service);
        Depositos deposit = new Depositos();
        EstatusDeposito stsDeposit = creaEstatusDeposito();
        TarjetaBancaria cc = creaCC();
        Usuario usr = creaUsuario();
        Otorgamiento o = creaOtorgamiento();
        
        if (stsDeposit != null && cc != null && usr != null && o != null) {
            deposit.setEstatusDeposito(stsDeposit);
            deposit.setTarjetaBancaria(cc);
            deposit.setUsuarioModifico(usr);
            deposit.setOtorgamiento(o);
        } else {
            resultados.put("D", Boolean.TRUE);
            return false;
        }
        
        deposit.setAlumno(alumno);
        deposit.setMonto(new BigDecimal(1500));
        deposit.setFechaModificacion(date);
        deposit.setObservaciones("beca universal");
        
        if (!depositBO.guardaDeposito(deposit)) {
            resultados.put("D", Boolean.TRUE);
            return false;
        } else {
            return true;
        }
        
    }
    
    private Otorgamiento creaOtorgamiento() {
        Otorgamiento o = service.getOtorgamientoDao().getOtorgamientoBkUniversalAlumno(alumno.getId(), periodoActivo.getId());
        
        return o;
    }
    
    private EstatusDeposito creaEstatusDeposito() {
        EstatusDeposito stsDeposit = service.getEstatusDepositoDao().findById(BigDecimal.ONE);        
        return stsDeposit;
    }
    
    private Boolean insertaBitacora() {
        TarjetaBO tBO = new TarjetaBO(service);
        List<BitacoraTarjetaBancaria> bitacora = crearBitacora();
        if (bitacora != null) {
            TarjetaBancaria cc = creaCC();
            cc.setBitacoraTarjetaBancarias(bitacora);
            // Si no es guardado correctamente
            if (!tBO.guardaTarjetaBancaria(cc)) {
                resultados.put("B", Boolean.TRUE);
                return false;
            } else {
                return true;
            }
        } else {
            resultados.put("B", Boolean.TRUE);
            return false;
        }
    }
    
    private List<BitacoraTarjetaBancaria> crearBitacora() {
        List<BitacoraTarjetaBancaria> lista = new ArrayList<>();
        BitacoraTarjetaBancaria bitacora = new BitacoraTarjetaBancaria();
        EstatusTarjetaBancaria stsCC = creaEstatusCC();
        TarjetaBancaria cc = creaCC();
        Usuario u = creaUsuario();
        
        if (stsCC != null && cc != null && u != null) {
            bitacora.setEstatus(stsCC);
            bitacora.setTarjetaBancaria(cc);
            bitacora.setUsuario(u);
        } else {
            return null;
        }
        
        bitacora.setFechaModificacion(date);
        bitacora.setObservaciones("beca universal");
        
        lista.add(bitacora);
        
        return lista;
    }
    
    public TarjetaBancaria creaCC() {
        TarjetaBancaria cc = service.getTarjetaBancariaDao().findByNumeroCuenta(creaNumeroTarjeta());
        
        return cc;
    }
    
    private Boolean insertaACC() {
        AlumnoTarjetaBancariaBO aCCBO = new AlumnoTarjetaBancariaBO(service);        
        AlumnoDatosBancarios datosB = creaDatosB();
        TarjetaBancaria cc = armaCC();
        EstatusTarjetaBancaria stsCC = creaEstatusCC();
        SolicitudCuentas sC = creaSolicitudC();
        
        if (cc != null && stsCC != null && sC != null) {
            aCC.setTarjetaBancaria(cc);
            aCC.setEstatusTarjBanc(stsCC);
            aCC.setSolicitudCuentas(sC);
        } else {
            resultados.put("ACC", Boolean.TRUE);
            return false;
        }
        
        aCC.setAlumno(alumno);
        aCC.setTarjetaActiva(false);
        aCC.setVigente(false);
        if (datosB != null) aCC.setDatosBancarios(datosB);
        if (!aCCBO.guardaTarjeta(aCC)) {
            resultados.put("ACC", Boolean.TRUE);
            return false;
        } else {
            return true;
        }
    }
    
    private SolicitudCuentas creaSolicitudC() {
        SolicitudCuentas sC = service.getSolicitudCuentasDao().findById(new BigDecimal(65));
        return sC;
    }
    
    private EstatusTarjetaBancaria creaEstatusCC() {
        EstatusTarjetaBancaria stsCC = service.getEstatusTarjetaBancariaDao().findById(new BigDecimal(17));
        return stsCC;
    }
    
    private TarjetaBancaria armaCC() {        
        TarjetaBancaria cc = new TarjetaBancaria();
        
        String ccN = creaNumeroTarjeta();
        if (ccN == null)
            return null;
        cc.setNumtarjetabancaria(ccN);
        cc.setCuenta(2);
        cc.setFechaLote(date);        
        
        return cc;
    }
    
    private String creaNumeroTarjeta() {        
        BecaUniversal becaUni = service.getBecaUniversalDao().getByBoleta(alumno.getBoleta());
        if (becaUni == null)
            return null;
	    
        String reference = becaUni.getReferencia();
        int indexOf = reference.indexOf("IPN", 0);
        String creditN = reference.substring(indexOf+3);
        
        return creditN;
    }
    
    private AlumnoDatosBancarios creaDatosB() {
        List<AlumnoDatosBancarios> listDatosB = service.getAlumnoDatosBancariosDao().listaDatosBancarios(alumno.getId());
        if (listDatosB == null || listDatosB.isEmpty())
            return null;
        else
            return listDatosB.get(0);
    }
    
    // Crea un otorgamiento nuevo
    private Boolean insertaOtorgamiento() {
        // Busca por ototrgamientos actuales
        List<Otorgamiento> otrosOtorgamientos = service.getOtorgamientoDao().getOtorgamientosNoUniversal(alumno.getId());
        TipoBecaPeriodo tbp = creaBecaP();
        
        // Para asignar o no asignar en caso de que ya tenga otorgamientos
        if (otrosOtorgamientos != null && !otrosOtorgamientos.isEmpty()) {
            BigDecimal montoT = new BigDecimal(0);
            List<Depositos> depositos = new ArrayList();
            for (Otorgamiento otroOtorgamiento : otrosOtorgamientos) {
                TipoBecaPeriodo tbpOtroOtorgamiento = otroOtorgamiento.getTipoBecaPeriodo();
                // Se suman los montos y los depósitos, únicamente de las becas no complementarias
                if (tbpOtroOtorgamiento.getVisible()) {
                    BigDecimal montoOtroOtorg = otroOtorgamiento.getTipoBecaPeriodo().getMonto();
                    BigDecimal duracionOtroOtorg = new BigDecimal(otroOtorgamiento.getTipoBecaPeriodo().getDuracion());
                    montoT = montoT.add(montoOtroOtorg.multiply(duracionOtroOtorg));

                    depositos.addAll(service.getDepositosDao().depositosOtorgamiento(otroOtorgamiento.getId()));
                }
            }
            // Si el monto total es menor al monto de la universal y no tiene depósitos, 
            // hay que poner la complementaria, y permitir el resto de acciones
            // si no, entonces cancelar el proceso
            if (montoT.compareTo(tbp.getMonto()) < 0 && !depositos.isEmpty()) {
                for (Otorgamiento otroOtorgamiento : otrosOtorgamientos) {
                    TipoBecaPeriodo tbpOtroOtorgamiento = otroOtorgamiento.getTipoBecaPeriodo();
                    // Sacar el tipo beca periodo complementaria correspondiente y asignarla
                    otroOtorgamiento.setTipoBecaPeriodo(service.getTipoBecaPeriodoDao().getComplementaria(tbpOtroOtorgamiento.getId()));
                    service.getOtorgamientoDao().save(otroOtorgamiento);
                }
            } else {
                resultados.put("otroO", Boolean.TRUE);
                return false;
            }
        }
        OtorgamientoBO oBO = new OtorgamientoBO(service);
        
        Proceso p = creaProceso();
        Usuario u = creaUsuario();
        IdentificadorOtorgamiento idO = creaIdentificador();
        
        if (tbp != null && p != null && u != null && idO != null && datosA != null) {
            otorgamiento.setTipoBecaPeriodo(tbp);
            otorgamiento.setProceso(p);
            otorgamiento.setUsuario(u);
            otorgamiento.setIdentificadorOtorgamiento(idO);
            otorgamiento.setDatosAcademicos(datosA);
        } else {
            resultados.put("O", Boolean.TRUE);
            return false;
        }
        
        otorgamiento.setAlumno(alumno);
        otorgamiento.setPeriodo(periodoActivo);
        otorgamiento.setFecha(date);
        otorgamiento.setAlta(Boolean.TRUE);
        otorgamiento.setAutomatico(Boolean.FALSE);
        otorgamiento.setAsignacionConfirmada(Boolean.FALSE);
        otorgamiento.setExcluirDeposito(Boolean.FALSE);
        otorgamiento.setObservaciones("beca universal");
        otorgamiento.setFase(1);
        
        if (!oBO.guardaOtorgamiento(otorgamiento)) {
            resultados.put("O", Boolean.TRUE);
            return false;
        } else {
            return true;
        }
        
    }
    
    private TipoBecaPeriodo creaBecaP() {
        TipoBecaPeriodo tBecaP = service.getTipoBecaPeriodoDao().getBkUniversalPorNivel(periodoActivo, datosA);
        return tBecaP;
    }
    
    private Proceso creaProceso() {
        Proceso proceso = service.getProcesoDao().getProcesoBkUniversal(periodoActivo, datosA);
        return proceso;
    }
    
    private Usuario creaUsuario() {        
        Usuario usr = service.getUsuarioDao().findById(new BigDecimal(160891));
        return usr;
    }
    
    private IdentificadorOtorgamiento creaIdentificador() {
        IdentificadorOtorgamiento idO = service.getIdentificadorOtorgamientoDao().findById(new BigDecimal(17));
        return idO;
    }
    
    private void creaResultadosDefault() {
        resultados.put("exitoO", false);
        resultados.put("exitoACC", false);
        resultados.put("exitoB", false);
        resultados.put("exitoD", false);
    }
}

package com.becasipn.business;

import com.becasipn.domain.Egresos;
import com.becasipn.domain.IngresosEgresos;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.persistence.model.CuestionarioTransporte;
import com.becasipn.persistence.model.Documentos;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Victor Lozano
 */
public class DocumentosBO extends BaseBO {

    public DocumentosBO(Service service) {
        super(service);
    }

    /**
     * Actualiza la validación de documentos de un alumno
     *
     * @author Victor Lozano
     * @param documentos
     * @param alumno
     * @return true si la operación se realizó exitosamente
     */
    public Boolean validaDocumentos(Documentos documentos) {
        try {
            if (documentos.getId() == null) {
                documentos = service.getDocumentosDao().save(documentos);
            } else {
                service.getDocumentosDao().update(documentos);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Obtiene la información de la solicitud ordinaria o de transporte de los
     * egresos del alumno
     *
     * @param alumno
     * @param periodo
     * @return
     */
    public IngresosEgresos datosEgresos(Alumno alumno, Periodo periodo) {
        IngresosEgresos ie = new IngresosEgresos();
        List<Egresos> egresos = new ArrayList<Egresos>();
        Boolean tieneSolicitudTransporte = service.getCuestionarioTransporteDao().tieneEseTransporte(alumno.getUsuario().getId(), periodo.getId());
        SolicitudBeca ordinaria = service.getSolicitudBecaDao().getESEAlumno(alumno.getId(), periodo.getId());
        Boolean tieneSolicitudOrdinaria = (ordinaria == null) ? Boolean.FALSE : Boolean.TRUE;

        ie.setTieneSolicitudOrdinaria(tieneSolicitudOrdinaria);
        ie.setTieneSolicitudTransporte(tieneSolicitudTransporte);
        
        if (tieneSolicitudTransporte) {
            ie.setFuenteDeInformacion("<b>SOLICITUD DE TRANSPORTE</b>");
            //Obtenemos gastos
            CuestionarioTransporte datosTransporte = service.getCuestionarioTransporteDao().respuestasTransporte(alumno.getUsuario().getId(), periodo.getId());
            egresos.add(agregaEgreso("Alimentación", datosTransporte.getAlimentacion()));
            egresos.add(agregaEgreso("Renta", datosTransporte.getRenta()));
            egresos.add(agregaEgreso("Escolares", datosTransporte.getEscolares()));
            egresos.add(agregaEgreso("Salud", datosTransporte.getSalud()));
            egresos.add(agregaEgreso("Transporte", datosTransporte.getTransporte()));
            egresos.add(agregaEgreso("Actividades", datosTransporte.getActividades()));
            egresos.add(agregaEgreso("Vivienda", datosTransporte.getVivienda()));
            egresos.add(agregaEgreso("Otros", datosTransporte.getOtros()));
            ie.setEgresos(egresos);

            //Obtenemos ingresos y total de integrantes de la familia
            HashMap<String, Float> hm = service.getTransporteDatosFamiliaresDao().ingesoEIntegrantes(datosTransporte.getId());
            ie.setIngresosPerCapita(hm.get("ingresoPerCapita"));
            ie.setTotalIntegrantes(hm.get("integrantes"));
            ie.setTotalIngresos(hm.get("ingresoMensual"));

            return ie;
        } else if (tieneSolicitudOrdinaria) {
            ie.setFuenteDeInformacion("<b>SOLICITUD ORDINARIA</b>");
            List<CuestionarioRespuestasUsuario> gastos = service.getCuestionarioRespuestasUsuarioDao().getEgresos(alumno.getUsuario(), periodo);
            if (gastos != null) {
                String totalIntegrantes = null;
                String ingresos = null;
                for (CuestionarioRespuestasUsuario res : gastos) {
                    if (res.getPregunta().getId().compareTo(new BigDecimal("167")) == 0) {
                        ingresos = res.getRespuestaAbierta();
                    } else if (res.getPregunta().getId().compareTo(new BigDecimal("168")) == 0) {
                        totalIntegrantes = res.getRespuestaAbierta();
                    } else {
                        egresos.add(agregaEgreso(res.getPregunta().getNombre(), res.getRespuesta().getNombre()));
                    }
                }
                Float ipc = totalIntegrantes == null || ingresos == null ? 0F : (new Float(ingresos) / new Float(totalIntegrantes));
                BigDecimal bd = new BigDecimal(Float.toString(ipc));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

                ie.setTotalIntegrantes(totalIntegrantes == null ? 0F : new Float(totalIntegrantes));
                ie.setIngresosPerCapita(bd.floatValue());
                ie.setTotalIngresos(ingresos == null?0F:new Float(ingresos));
                ie.setEgresos(egresos);
                return ie;
            }
        }
        return null;
    }

    private Egresos agregaEgreso(String nombre, Object monto) {
        Egresos e = new Egresos();
        e.setNombre(nombre);
        if (monto == null) {
            e.setMonto("0.0");
        } else {
            e.setMonto(monto.toString());
        }
        return e;
    }
}

package com.becasipn.domain;

import com.becasipn.business.TipoBecaPeriodoBO;
import com.becasipn.business.OtorgamientoBO;
import com.becasipn.actions.BaseAction;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.service.Service;
import com.becasipn.persistence.model.PadronSubes;
import com.becasipn.persistence.model.SolicitudBeca;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Mario Márquez
 */
public class RequisitosPrograma {

    private final String nombre;
    private final String clave;
    private final String estatus;
    private final String motivo;
    private final int estatusId;
    private final List<List<Requisito>> becaRequisitos;
    private final SolicitudBeca solicitud;
    private final String beca;
    private final boolean otorgamientoDefinitivo;

    public static class Builder {

        // Parámetros requeridos
        private final String nombre;
        private final String clave;
        private final SolicitudBeca solicitud;

        // Parámetros opcionales, default
        private String estatus = "";
        private String motivo = "";
        private int estatusId = -1;
        private List<List<Requisito>> becaRequisitos = new ArrayList<>();
        private String beca = "";
        private boolean otorgamientoDefinitivo;

        public Builder(SolicitudBeca solicitud) {
            this.solicitud = solicitud;
            this.nombre = solicitud.getProgramaBecaSolicitada().getNombre();
            this.clave = solicitud.getProgramaBecaSolicitada().getClave();
        }

        // Depende de que el objeto solicitud ya haya sido asignado
        public Builder estatus() {
            if (solicitud.getClasificacionSolicitud() != null) {
                estatus = solicitud.getClasificacionSolicitud().getNombre();
            } else {
                estatus = "Pendiente";
            }
            return this;
        }

        public Builder motivo(PadronSubes padronSbs) {
            if (solicitud.getClasificacionSolicitud() != null && solicitud.getClasificacionSolicitud().getId().intValue() == 2) {
                BigDecimal cuestId = solicitud.getCuestionario().getId();
                Boolean fivOrFour = cuestId.equals(new BigDecimal(5)) || cuestId.equals(new BigDecimal(4));
                // este if busca el detalle del rechazo en la tabla de ENT_PADRON_SUBES
                if (solicitud.getMotivoRechazo().getId().equals(new BigDecimal(35)) && fivOrFour) {
                    String errorPadron = "";

                    if (cuestId.equals(new BigDecimal(5))) {
                        errorPadron = padronSbs.getErrorAsignacion();
                    } else if (cuestId.equals(new BigDecimal(4))) {
                        errorPadron = padronSbs.getErrorAsignacionTransporte();
                    }
                    if (errorPadron == null || errorPadron.isEmpty()) {
                        errorPadron = solicitud.getMotivoRechazo().getNombre();
                    }
                    motivo = errorPadron;
                } else {
                    motivo = solicitud.getMotivoRechazo().getNombre();
                }
            } else {
                motivo = "";
            }
            return this;
        }

        // Depende de que el objeto solicitud ya haya sido asignado
        public Builder estatusId() {
            if (solicitud.getClasificacionSolicitud() != null) {
                estatusId = solicitud.getClasificacionSolicitud().getId().intValue();
            } else {
                estatusId = -1;
            }
            return this;
        }

        public Builder becaRequisitos(List<List<Requisito>> val) {
            becaRequisitos = val;
            return this;
        }
        
        public Builder beca(String becaotorgada){
            beca = becaotorgada;
            return this;
        }
        
        public Builder otorgamientoDefinitivo(boolean val) {
            otorgamientoDefinitivo = val;
            return this;
        }

        public RequisitosPrograma build() {
            return new RequisitosPrograma(this);
        }
    }

    private RequisitosPrograma(Builder builder) {
        nombre = builder.nombre;
        clave = builder.clave;
        estatus = builder.estatus;
        motivo = builder.motivo;
        estatusId = builder.estatusId;
        becaRequisitos = builder.becaRequisitos;
        solicitud = builder.solicitud;
        beca = builder.beca;
        otorgamientoDefinitivo = builder.otorgamientoDefinitivo;
    }

    public JsonObject toJson() {
        JsonObjectBuilder jOB = Json.createObjectBuilder()
                .add("nombre", nombre)
                .add("clave", clave);
        if (!estatus.isEmpty()) { jOB.add("estatus", estatus); }
        if (estatusId != -1) { jOB.add("estatusId", estatusId); }
        if (!motivo.isEmpty()) { jOB.add("motivo", motivo); }
        if (!beca.isEmpty()) { jOB.add("beca", beca); }
        jOB.add("otorgamientoDefinitivo", otorgamientoDefinitivo);
        
        JsonArrayBuilder jOBArray = Json.createArrayBuilder();
        for (List<Requisito> becaRequisito : becaRequisitos) {
            JsonArrayBuilder jOBInnerArray = Json.createArrayBuilder();
            for (Requisito requisito : becaRequisito) {
                jOBInnerArray.add(requisito.toJson());
            }
            jOBArray.add(jOBInnerArray);
        }
        jOB.add("becaRequisitos", jOBArray);
        
        return jOB.build();
    }

    public String getNombre() {
        return nombre;
    }

    public List<List<Requisito>> getBecaRequisitos() {
        return becaRequisitos;
    }

    public String getClave() {
        return clave;
    }

    public int getEstatuId() {
        return estatusId;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getBeca() {
        return beca;
    }

    public boolean isOtorgamientoDefinitivo() {
        return otorgamientoDefinitivo;
    }
}
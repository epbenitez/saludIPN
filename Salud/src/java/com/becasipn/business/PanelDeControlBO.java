package com.becasipn.business;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.SolicitudBeca;
import com.becasipn.service.Service;
import com.becasipn.util.Util;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author SISTEMAS
 */
public class PanelDeControlBO extends BaseBO {

    private Alumno alumno;
    private Otorgamiento otorgamiento;

    // Texto sección Beca
    private String textoPrincipalBeca;
    private String textoSecundarioBeca;
    private String textoEnlaceBeca = "Para más información sobre tu historial de becas da click aquí";
    private String enlaceBeca = "/misdatos/mostrarHistorialBecas.action";
    private boolean hasOtorgamientoPeriodoActual;

    public PanelDeControlBO(Service service) {
        super(service);
    }

    public static PanelDeControlBO getInstance(Service service, Alumno alumno, Otorgamiento otorgamiento) {
        PanelDeControlBO result = new PanelDeControlBO(service);
        result.setAlumno(alumno);
        result.setOtorgamiento(otorgamiento);

        return result;
    }

    /**
     * Este método comienza con la creación de los campos correspondientes a la 
     * sección Beca, del panel de control alumno.
     * Autor: Mario Márquez
     *
     * @param stsId Estatus de la cuenta bancaria
     *
     */
    public void setSeccionBeca(int stsId) {
        boolean definitivamenteAsignado = service.getOtorgamientoDao().existeAlumnoAsignado(alumno.getId());
        setInfoSeccionBeca(definitivamenteAsignado, stsId == 13);
    }

    /**
     * Este método rellena los campos de la sección beca, del panel de control alumno.
     * Autor: Mario Márquez
     *
     * @param definitivamenteAsignado Si el otorgamiento es definitivo
     * @param cuentaLiberada Si la ceunta está en estatus 13
     *
     */
    private void setInfoSeccionBeca(boolean definitivamenteAsignado, boolean cuentaLiberada) {
        Periodo periodoActual = service.getPeriodoDao().getPeriodoActivo();
        // Si el otorgamiento es del periodo actual, y su proceso fue validado, entonces ya tiene beca
        if (definitivamenteAsignado) {
            hasOtorgamientoPeriodoActual = true;
            textoPrincipalBeca = otorgamiento.getTipoBecaPeriodo().getTipoBeca().getNombre();
            // Si la cuenta ya fue liberada
            if (cuentaLiberada) {
                textoSecundarioBeca = "<span class=\"headline\">Beca otorgada | <span class=\"texto-beca\">Periodo actual</span></span>";
            } else {
                textoSecundarioBeca = "Beca otorgada | Ingresa tu cuenta bancaria";
                textoEnlaceBeca = "Para agregar una cuenta, haz clic aquí";
                enlaceBeca = "/misdatos/configuracionCuentaBancaria.action";
            }
        } else {
            textoPrincipalBeca = "Sin Beca";
            textoSecundarioBeca = "No tienes beca en el periodo actual";

            AlumnoBO abo = new AlumnoBO(service);
            if (abo.solicitudActiva(alumno, periodoActual)) { // Si es posible rellenar una solicitud
                textoEnlaceBeca = "Solicita tu beca para este periodo";
                enlaceBeca = "/misdatos/resultadoEstudioSocioeconomico.action?cuestionarioId=1";
            }
        }
    }

    /**
     * Este método valida si es tiempo de publicar los resultados.
     * Una vez esto es cierto, buscará las solicitudes del alumno, correspondientes
     * al periodo, y generará la información del front
     * Autor: Mario Márquez
     *
     * @return List<List<String>> Lista de cadenas para mostrar directamente en el front
     *
     */
    public List<List<String>> getInfoSolicitudes() {
        // Sólo si ya es tiempo de publicar resultados
        if (Util.fechaDatos((String) ActionContext.getContext().getApplication().get("PublicacionResultadosOtorgamientos"))) {
            // Obtener lista de solicitudes
            SolicitudBecasBO solicitudBecasBO = SolicitudBecasBO.getInstance(service);
            List<SolicitudBeca> solicitudes = solicitudBecasBO.getSolicitudesAlumnoPeriodo(alumno, null);
            if (solicitudes != null) {
                // Arma lista de resultados según reglas de negocio
                List<List<String>> result = new ArrayList<List<String>>();
                for (SolicitudBeca solicitud : solicitudes) {
                    result.add(getInfoSolicitud(solicitud));
                }
                return result;
            }
        }
        return null;
    }

    /**
     * Este método crea una lista con el nombre de la solicitud y su estatus. 
     * El estatus "asignada" es generado según si el otorgamiento correspondiente 
     * a una solicitud asignada es definitivo. Las reglas para armar esta información, 
     * deben coincidir con las que arman la sección del estatus de la solicitud, 
     * en el módulo "Estatus de mi solicitud".
     * Autor: Mario Márquez
     *
     * @param solicitud Con la que se armará la info
     * @return List<String> 
     *
     */
    private List<String> getInfoSolicitud(SolicitudBeca solicitud) {
        int clasifId = -1;
        String estatus = "Pendiente";
        
        List<String> solicitudInfo = new ArrayList<>();
        
        solicitudInfo.add(solicitud.getCuestionario().getNombre());
        if (solicitud.getClasificacionSolicitud() != null) {
            if (solicitud.getClasificacionSolicitud().getId().intValue() == 1) {
                Otorgamiento solicitudO = service.getOtorgamientoDao()
                        .getOtorgamientoSolicitudPeriodo(solicitud.getId(), solicitud.getPeriodo().getId());
                // Sólo si es definitivo se muestra como asignado
                if (OtorgamientoBO.isOtorgamientoActualDefinitivo(solicitudO)) {
                    clasifId = solicitud.getClasificacionSolicitud().getId().intValue();
                    estatus = solicitud.getClasificacionSolicitud().getNombre();
                }
            } else {
                clasifId = solicitud.getClasificacionSolicitud().getId().intValue();
                estatus = solicitud.getClasificacionSolicitud().getNombre();
            }
        }
        solicitudInfo.add(getEtiquetaEstatus(clasifId, estatus));

        return solicitudInfo;
    }

    /**
     * Este método crea una cadena con el html para mostrar un color adecuado
     * a la clasificación de la solicitud
     * Autor: Mario Márquez
     *
     * @param id De la clasificación de la solicitud
     * @param clasificacion Texto descriptivo sobre el estatus de la solicitud
     * @return String
     *
     */
    private String getEtiquetaEstatus(int id, String clasificacion) {
        StringBuilder sb = new StringBuilder();
        switch (id) {
            case 1:
                sb.append("<span class=\"label label-success\">");
                break;
            case 2:
                sb.append("<span class=\"label label-danger\">");
                break;
            case 3:
                sb.append("<span class=\"label label-warning\">");
                break;
            default:
                sb.append("<span class=\"label label-info\">");
                break;
        }
        sb.append(clasificacion).append("</span>");
        
        return sb.toString();
    }

    private static Map getCamposFecha(Date date) {
        Locale spanish = new Locale("es", "ES");
        Map campos = new HashMap();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, spanish);
        Integer year = cal.get(Calendar.YEAR);
        Integer day = cal.get(Calendar.DATE);

        campos.put("y", year.toString());
        campos.put("m", month);
        campos.put("d", day.toString());

        return campos;
    }

    // Arma un mapa con la información que contendrá el mensaje
    public static final Map creaModelo(Date startDate, Date endDate) {

        Map model = new HashMap();
        Map start = getCamposFecha(startDate);
        Map end = getCamposFecha(endDate);

        model.put("d", start.get("d"));
        model.put("endD", end.get("d"));
        model.put("m", end.get("m"));
        model.put("y", end.get("y"));

        return model;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public void setOtorgamiento(Otorgamiento otorgamiento) {
        this.otorgamiento = otorgamiento;
    }

    public String getTextoPrincipalBeca() {
        return textoPrincipalBeca;
    }

    public String getTextoSecundarioBeca() {
        return textoSecundarioBeca;
    }

    public String getTextoEnlaceBeca() {
        return textoEnlaceBeca;
    }

    public String getEnlaceBeca() {
        return enlaceBeca;
    }

    public boolean isHasOtorgamientoPeriodoActual() {
        return hasOtorgamientoPeriodoActual;
    }
}

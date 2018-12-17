package com.becasipn.business;

import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.EstadisticaCuestionarios;
import com.becasipn.service.Service;
import com.becasipn.util.AmbienteEnums;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EstadisticasBO extends BaseBO {

    // total de otorgamientos para los que no aplica el sexto depósito
    private Long otorgamientosMenosMeses;

    private AmbienteEnums enums = AmbienteEnums.getInstance();
    private Map<String, String> meses = enums.getMeses();

    public EstadisticasBO(Service service) {
        super(service);
    }
    
//----------------------------------------------------------------------------------------------------------------
    public List<EstadisticaCuestionarios> estadisticaCuestionarios(Periodo periodo, Cuestionario cuestionario) {
        List<EstadisticaCuestionarios> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getCuestionarioPreguntaRespuestaDao().estadisticaCuestionario(periodo.getId(), cuestionario.getId());
        if (ax != null) {
            estadisticas.addAll(setDataCuestionarios(ax, cuestionario));
        }
        return estadisticas;
    }

    public List<EstadisticaCuestionarios> setDataCuestionarios(List<Object[]> ax, Cuestionario cuestionario) {
        List<EstadisticaCuestionarios> estadisticas = new ArrayList<>();
        for (Object[] x : ax) {
            EstadisticaCuestionarios ex = new EstadisticaCuestionarios();
            ex.setPreguntaId((BigDecimal) x[0]);
            ex.setPregunta(x[1].toString());
            ex.setRespuestaId((BigDecimal) x[2]);
            ex.setRespuesta(x[3].toString());
            BigDecimal aux = (BigDecimal) x[4];
            ex.setTotalRespuesta(aux.longValue());
            ex.setSeccion((BigDecimal) x[5]);
            estadisticas.add(ex);
        }

        return estadisticas;
    }
}

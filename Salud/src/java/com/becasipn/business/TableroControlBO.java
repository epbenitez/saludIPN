package com.becasipn.business;

import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mario Márquez
 */
public class TableroControlBO extends BaseBO {
    
    private TableroControlBO(Service service) {
	super(service);
    }
    
    public static TableroControlBO getInstance(Service service) {
        return new TableroControlBO(service);
    }
    
    public Map<String, BigDecimal> getBajas(BigDecimal uaId) {
        Map<String, BigDecimal> result = new HashMap();
        result.put("actual", new BigDecimal(0));
        result.put("anteriores", new BigDecimal(0));
        
        BigDecimal activePeriodID = service.getPeriodoDao().getPeriodoActivo().getId();
        List<LinkedHashMap<String,Object>> casualtiesXPeriod = service.getOtorgamientoDao().getAlumnosConBaja(uaId);
        if (casualtiesXPeriod != null) {
            for (Map<String, Object> casualityXPeriod : casualtiesXPeriod) {
                BigDecimal periodID = (BigDecimal) casualityXPeriod.get("periodo");
                BigDecimal casuality = (BigDecimal) casualityXPeriod.get("bajas");
                // Para periodos anteriores se suma al total
                if (periodID.compareTo(activePeriodID) != 0) {
                    result.put("anteriores", result.get("anteriores").add(casuality));
                } 
                // Para el periodo actual, se guarda tal cual (rima sin esfuerzo)
                else {
                    result.put("actual", casuality);
                }
            }
        }
        
        return result;
    }
    
}

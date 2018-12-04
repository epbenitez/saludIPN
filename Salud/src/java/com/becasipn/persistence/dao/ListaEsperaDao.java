package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.ListaEspera;
import java.math.BigDecimal;

public interface ListaEsperaDao extends DaoBase<ListaEspera, BigDecimal> {

    public ListaEspera findBySolicitudESE(BigDecimal solicitudId);
    
    public ListaEspera findBySolicitud(BigDecimal solicitudId, boolean vigente);
    
    public int quitarVigente(BigDecimal solicitudId);
    
    public ListaEspera buscarUltimoTurno(BigDecimal periodoId, BigDecimal unidadAcademicaId);
    
    public ListaEspera findBySolicitudProceso(BigDecimal solicitudId,BigDecimal procesoId);
}

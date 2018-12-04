package com.becasipn.persistence.dao;

import java.math.BigDecimal;

import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.PadronProspera;

public interface PadronProsperaDao extends DaoBase<PadronProspera, BigDecimal> {

    public Boolean existeAlumnoPeriodo(Alumno alumno);

}

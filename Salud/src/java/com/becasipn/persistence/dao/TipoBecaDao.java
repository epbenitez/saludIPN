package com.becasipn.persistence.dao;

import com.becasipn.persistence.model.PadronSubes;
import java.math.BigDecimal;

import com.becasipn.persistence.model.TipoBeca;
import java.util.List;

/**
 *
 * @author Usre-05
 */
public interface TipoBecaDao extends DaoBase<TipoBeca, BigDecimal> {

    public List<TipoBeca> tipoBecaEnUso(BigDecimal tipoBeca);

    public List<TipoBeca> findByNivel(BigDecimal nivelId);

    public TipoBeca getByNombre(String tipoBeca);

    public TipoBeca siguienteTipoBeca(TipoBeca anterior, PadronSubes padronSubes);

    public List<TipoBeca> getTipoBecaPorBeca(BigDecimal becaId);

}

package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.TipoInconformidadReconsideracionDao;
import com.becasipn.persistence.model.TipoInconformidadReconsideracion;
import java.math.BigDecimal;

/**
 *
 * @author Sistemas DSE
 */
public class TipoInconformidadReconsideracionJpaDao extends JpaDaoBase<TipoInconformidadReconsideracion, BigDecimal> implements TipoInconformidadReconsideracionDao{

    public TipoInconformidadReconsideracionJpaDao() {
        super(TipoInconformidadReconsideracion.class);
    }

}

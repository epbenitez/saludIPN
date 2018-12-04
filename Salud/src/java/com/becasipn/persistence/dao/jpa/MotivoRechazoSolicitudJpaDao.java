package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.MotivoRechazoSolicitudDao;
import com.becasipn.persistence.model.MotivoRechazoSolicitud;
import java.math.BigDecimal;
import java.util.List;

public class MotivoRechazoSolicitudJpaDao extends JpaDaoBase<MotivoRechazoSolicitud, BigDecimal> implements MotivoRechazoSolicitudDao {

    public MotivoRechazoSolicitudJpaDao() {
        super(MotivoRechazoSolicitud.class);
    }

    @Override
    public List<MotivoRechazoSolicitud> getMotivosVisibles() {
        String jpql = "SELECT mrs FROM MotivoRechazoSolicitud mrs WHERE mrs.visible = 1";
        
        return executeQuery(jpql);
    }

}

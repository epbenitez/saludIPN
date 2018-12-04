package com.becasipn.business;

import com.becasipn.persistence.model.TipoBeca;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.List;

public class TipoBecaBO extends BaseBO {

    public TipoBecaBO(Service service) {
        super(service);
    }

    public Boolean guardaTipoBeca(TipoBeca tipoBeca) {
        try {
            if (tipoBeca.getId() == null) {
                service.getTipoBecaDao().save(tipoBeca);
            } else {
                service.getTipoBecaDao().update(tipoBeca);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public TipoBeca getTipoBeca(BigDecimal id) {
        TipoBeca tipoBeca = service.getTipoBecaDao().findById(id);
        return tipoBeca;
    }

    public Boolean eliminaTipoBeca(TipoBeca tipoBeca) {
        try {
            service.getTipoBecaDao().delete(tipoBeca);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    public Boolean tipoBecaEnUso(BigDecimal tipoBeca) {
        List<TipoBeca> lista = service.getTipoBecaDao().tipoBecaEnUso(tipoBeca);
        return lista == null || lista.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
    }
}

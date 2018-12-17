package com.becasipn.business;

import com.becasipn.persistence.model.CuestionarioRespuestasUsuario;
import com.becasipn.service.Service;

/**
 *
 * @author Tania Gabriela Sánchez Manilla
 */
public class CuestionarioPreguntasRespuestasBO extends BaseBO {

    public CuestionarioPreguntasRespuestasBO(Service service) {
        this.service = service;
    }

    public Boolean guardaCRU(CuestionarioRespuestasUsuario cru) {
        try {
            if (cru.getId() == null) {
                service.getCuestionarioRespuestasUsuarioDao().save(cru);
            } else {
                service.getCuestionarioRespuestasUsuarioDao().update(cru);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}

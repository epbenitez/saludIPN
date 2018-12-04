/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.persistence.dao.jpa;

import com.becasipn.persistence.dao.CuestionarioRespuestasDao;
import com.becasipn.persistence.model.CuestionarioRespuestas;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public class CuestionarioRespuestasJpaDao extends JpaDaoBase<CuestionarioRespuestas, BigDecimal> implements CuestionarioRespuestasDao {

    public CuestionarioRespuestasJpaDao() {
        super(CuestionarioRespuestas.class);
    }
    
    @Override
    public List<CuestionarioRespuestas> respuestasPregunta(BigDecimal preguntaId) {
        String sql = "select r.* from ENT_CUESTIONARIO_RESPUESTA r join ENT_CUESTIONARIO_PREGUNTA_RESP c on C.RESPUESTA_ID=r.id and C.PREGUNTA_ID=?1 order by C.ORDEN";
        List<Object[]> respuestasObjetos = executeNativeQuery(sql,preguntaId);
        List<CuestionarioRespuestas> respuestas = new ArrayList<>();
        for (Object[] respuesta : respuestasObjetos) {
            CuestionarioRespuestas resp = new CuestionarioRespuestas();
            resp.setId((BigDecimal)respuesta[0]);
            resp.setNombre((String)respuesta[1]);
            respuestas.add(resp);
        }
        return respuestas;
    }
    
    @Override
    public CuestionarioRespuestas respuesta(BigDecimal preguntaId,String respuesta){
        String sql = "select r.* from ENT_CUESTIONARIO_RESPUESTA r "
                + " join ENT_CUESTIONARIO_PREGUNTA_RESP c on C.RESPUESTA_ID=r.id and C.PREGUNTA_ID=?1 and r.nombre=?2 order by C.ORDEN";
        List<Object[]> respuestas = executeNativeQuery(sql,preguntaId,respuesta);
        CuestionarioRespuestas resp = new CuestionarioRespuestas();
        if(respuestas!=null && !respuestas.isEmpty()){
            resp.setId((BigDecimal)respuestas.get(0)[0]);
            resp.setNombre((String)respuestas.get(0)[1]);
        }else{
            //System.out.println(preguntaId+" "+respuesta);
        }
        return resp;
    }

}

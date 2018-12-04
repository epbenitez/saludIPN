/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.becasipn.business;

import com.becasipn.persistence.model.Configuracion;
import com.becasipn.service.Service;
import com.becasipn.util.Ambiente;
import com.opensymphony.xwork2.ActionContext;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Patricia Benitez
 */
public class ConfiguracionAppBO extends BaseBO {

    public ConfiguracionAppBO(Service service) {
        super(service);
    }

    public void actualizaConfiguracion(String[] config, Service service) {
        if (config != null && config.length > 0) {
            for (String c : config) {
                String[] datos = c.split("-");
                BigDecimal id = new BigDecimal(datos[1]);
                Configuracion configuracion = service.getConfiguracionDao().findById(id);
                configuracion.setValor(datos[0].trim());
                service.getConfiguracionDao().update(configuracion);
            }
        }
    }

    public void actualizaConfiguracionEnMemoria(Service service) {
        //System.out.println("::::: ACTUALIZACION DE VALORES EN MEMORIA ::::");
        List<Configuracion> configuracionList = service.getConfiguracionDao().findAll();

        Ambiente ambiente = new Ambiente();
        //ambiente.setConfiguracionList(configuracionList);
        for (Configuracion c : configuracionList) {
            //System.out.println(c.getPropiedad() + "," + c.getValor());
            ActionContext.getContext().getSession().get(c.getPropiedad());
            ActionContext.getContext().getSession().put(c.getPropiedad(), c.getValor());
        }
    }

    public void guardaConfiguracion(BigDecimal id, String tip, String valor) {
        Configuracion config = service.getConfiguracionDao().findById(id);
        config.setTip(tip);
        config.setValor(valor);
        service.getConfiguracionDao().update(config);
    }
}

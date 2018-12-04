package com.becasipn.util;

import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.service.Service;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author SISTEMAS
 */
public class ExcelTitulo {

    private final Service service;
    private final Date hoy;
    private final int mesId;
    private final String tipo;
    private final int total;
    private final BigDecimal periodoId;
    private final BigDecimal nivelId;
    private final BigDecimal uAId;
    
    private String titulo;

    public static class Builder {

        // Requeridos
        private final Service service;
        private final String tipo;
        private final int total;
        
        // Opcionales
        private Date hoy = new Date();
        private int mesId = 0;
        private BigDecimal periodoId = BigDecimal.ZERO;
        private BigDecimal nivelId = BigDecimal.ZERO;
        private BigDecimal uAId = BigDecimal.ZERO;
        
        public Builder(Service service, String tipo, int total) {
            this.service = service;
            this.tipo = tipo;
            this.total = total;
        }
        
        public Builder mes(int val) {
            mesId = val;
            return this;
        }
        public Builder periodoId(BigDecimal val) {
            if (val != null) {
                periodoId = val;
            }
            return this;
        }
        public Builder nivelId(BigDecimal val) {
            if (val != null) {
                nivelId = val;
            }
            return this;
        }
        public Builder uAId(BigDecimal val) {
            if (val != null) {
                uAId = val;
            }
            return this;
        }
        
        public ExcelTitulo build() {
            return new ExcelTitulo(this);
        }
    }
    
    private ExcelTitulo(Builder builder) {
        service = builder.service;
        hoy = builder.hoy;
        mesId = builder.mesId;
        tipo = builder.tipo;
        total = builder.total;
        periodoId = builder.periodoId;
        nivelId = builder.nivelId;
        uAId = builder.uAId;
        setTitulo();
    }

    private void setTitulo() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append(tipo).append(" ");
        
        if (mesId != 0) {
            AmbienteEnums enums = AmbienteEnums.getInstance();
            sb.append(enums.getMeses().get(String.valueOf(mesId)));
        }
        
        sb.append(" (").append(UtilFile.dateToString(hoy, "yyyyMMdd"));
        sb.append(" ").append(UtilFile.dateToString(hoy, "HHmm")).append("hrs)");
        sb.append(" (").append(total).append(")");
        sb.append(" ").append(service.getPeriodoDao().findById(periodoId).getClave());
        if (!nivelId.equals(BigDecimal.ZERO)) {
            sb.append(" ").append(service.getNivelDao().findById(nivelId).getClave());
        }
        if (!uAId.equals(BigDecimal.ZERO)) {
            UnidadAcademica ua = service.getUnidadAcademicaDao().findById(uAId);
            if (nivelId.equals(BigDecimal.ZERO)) {
                sb.append(" ").append(ua.getNivel().getClave());
            }
            sb.append("-");
            sb.append(ua.getNombreCorto());
        }
        sb.append(".xlsx\"");
        
        this.titulo = sb.toString();
    }
    
    public String getTitulo() {
        return titulo;
    }
}
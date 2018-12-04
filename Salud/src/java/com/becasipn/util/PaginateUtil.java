package com.becasipn.util;

import java.util.List;

/**
 * Clase para paginación de BD.
 * @author Jesús Fernández
 */
public class PaginateUtil<T>{
    private List<T> Resultados;
    private Long NoResultados;
    private Long NoResultadosFiltrados;

    public PaginateUtil(){        
    }
    
    public PaginateUtil(List<T> Resultados, Long NoResultados, Long NoResultadosFiltrados){
        this.Resultados = Resultados;
        this.NoResultados = NoResultados;
        this.NoResultadosFiltrados = NoResultadosFiltrados;
    }
    
    public List<T> getResultados() {
        return Resultados;
    }

    public void setResultados(List<T> Resultados) {
        this.Resultados = Resultados;
    }

    public Long getNoResultados() {
        return NoResultados;
    }

    public void setNoResultados(Long NoResultados) {
        this.NoResultados = NoResultados;
    }

    public Long getNoResultadosFiltrados() {
        return NoResultadosFiltrados;
    }

    public void setNoResultadosFiltrados(Long NoResultadosFiltrados) {
        this.NoResultadosFiltrados = NoResultadosFiltrados;
    }
    
}

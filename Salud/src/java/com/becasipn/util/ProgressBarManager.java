package com.becasipn.util;

import com.becasipn.persistence.model.TipoBecaPeriodo;

/**
 *
 * @author SISTEMAS
 */
public class ProgressBarManager {

    private ProgressBarManagerUtil progressManager;
    private long asignados;
    private long noAsignados;
    private int nivel;
    private int ua;
    private int tipoMov;

    public void setTotalDeElementos(int totalCandidatos, int totalBecas, int nivel, int tipoMov) {
        this.nivel = nivel;
        this.tipoMov = tipoMov;
        progressManager = new ProgressBarManagerUtil(totalCandidatos, totalBecas);
    }
    
    public void setTotalDeElementosCorreos(int totalCorreos) {
        progressManager = new ProgressBarManagerUtil();
        progressManager.setTotalGlobal(totalCorreos);
    }

    public void setTotales(int totalSolicitudes, int totalUAs, int ua, int nivel) {
        this.nivel = nivel;
        this.ua = ua;
        progressManager = new ProgressBarManagerUtil(totalSolicitudes, totalUAs);
    }

    public void cambiarElemento(TipoBecaPeriodo tipoBecaPeriodoAnterior, long totalPorBeca, long revalidantesReales) {
        Long tmp = totalPorBeca - revalidantesReales;
        asignados = 0;
        noAsignados = 0;
        progressManager.setPbu(totalPorBeca, tipoBecaPeriodoAnterior.getTipoBeca().getNombre());
        Util.pbmuMap.put(Util.getNivel(tipoMov, nivel), progressManager);
        for (int i = 0; i < tmp; i++) {
            aumentarRevalidacionNoAsignada();
        }
    }
    
    public boolean cambiarElementoCorreo(long totalCorreos, String[] mapData) {  
        asignados = 0;
        noAsignados = 0;
        progressManager.setPbu(totalCorreos, Util.getDescripCorreo(mapData));
        // Se crea la clave a partir del id del usuario y la opción
        if (Util.pbmuMap.containsKey(mapData[0]+mapData[1])) {
            return false;
        } else {
            Util.pbmuMap.put(mapData[0]+mapData[1], progressManager);
            return true;
        }
    }
    
    public void borraBarraCorreo(String[] mapData) {
        Util.pbmuMap.remove(mapData[0]+mapData[1]);
    }

    public void cambiarUnidadAcademica(long subtotal, String uaCorto) {
        progressManager.setPbu(subtotal, uaCorto);
        Util.pbmuMap.put(Util.getNivelUAs(nivel, ua), progressManager);
    }

    public void aumentarPreasignada() {
        progressManager.setProcessedSuccess();
        progressManager.setProcessedGlobal();
    }
    
    public void aumentarNoPreasignada() {
        progressManager.setProcessedError();
        progressManager.setProcessedGlobal();
    }

    public void borraBarra() {
        Util.pbmuMap.remove(Util.getNivel(tipoMov, this.nivel));
    }

    public void aumentarRevalidacionAsignada() {
        asignados++;
        // System.out.println("aumento");
        progressManager.setProcessedSuccess();
        progressManager.setProcessedGlobal();
    }

    public void aumentarRevalidacionNoAsignada() {
        noAsignados++;
        progressManager.setProcessedError();
        progressManager.setProcessedGlobal();
    }

    public Long getRevalidacionesAsignadas() {
        return asignados;
    }

    public Long getRevalidacionesNoAsignadas() {
        return noAsignados;
    }

}

package com.becasipn.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author Jesús Alejandro Fernández Flores
 */
public class ProgressBarUtil {

    private static final DecimalFormat DFORMAT = new DecimalFormat("#.##");
    private long eta;  
    private float pbarPercentage;    
    private long processed;
    private long processedError;
    private long processedSuccess;
    private long total;
    private long timeElapsed;
    private int nbar;
    private String info;

    public ProgressBarUtil() {
        this.DFORMAT.setRoundingMode(RoundingMode.CEILING);
        this.eta = 0;
        this.pbarPercentage = 0;        
        this.processed = 0;
        this.processedError = 0;
        this.processedSuccess = 0;
        this.total = 0;
        this.timeElapsed = System.nanoTime();
        this.nbar = 0;
        this.info = "";        
    }
    
    /**
     * Constructor que será llamado por ProgressBarManager
     * @param nbar número de barra que llama
     * @param total total de elementos que procesará la barra
     * @param info texto que se mostrará en la barra de progreso
     */
    public ProgressBarUtil(int nbar, long total, String info){
        this.DFORMAT.setRoundingMode(RoundingMode.CEILING);
        this.eta = 0;
        this.pbarPercentage = 0;        
        this.processed = 0;
        this.processedError = 0;
        this.processedSuccess = 0;        
        this.total = total;
        this.timeElapsed = System.nanoTime();
        this.nbar = nbar;        
        this.info = info;
    }
        
    public long getEta() {
        return eta;
    }

    public void setEta(long eta) {
        this.eta = eta ;
    }

    public float getPbarPercentage() {
        return pbarPercentage;
    }

    public void setPbarPercentage() {                
        this.pbarPercentage = Float.parseFloat(DFORMAT.format((100F*this.processed)/this.total));        
    }

    public long getProcessed() {
        return processed;
    }

    public void setProcessed() {
        this.processed++;
        setPbarPercentage();
        setEta((((System.nanoTime() - this.timeElapsed) / 1000000) * (this.total - this.processed))/this.processed);
    }
        
    public long getProcessedError() {
        return processedError;
    }

    public void setProcessedError() {
        this.processedError++;
    }  
    
    public long getProcessedSuccess() {
        return processedSuccess;
    }

    public void setProcessedSuccess() {
        this.processedSuccess++;
    }
  
    public long getTotal() {
        return total;
    }

    public int getNbar() {
        return nbar;
    }

    public void setNbar(int nbar) {
        this.nbar = nbar;
    }
    
    public String getInfo() {
        return info;
    }  
}

package com.becasipn.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jesús Alejandro Fernández Flores
 */
public class ProgressBarManagerUtil {

    private static final DecimalFormat DFORMAT = new DecimalFormat("#.##");
    private float pbarPercentageGlobal;
    private long etaGlobal;
    private long processedGlobal;
    private long processedError;
    private long processedSuccess;
    private long totalGlobal;
    private long timeElapsed;
    private int nbar;
    private int nbarTotal;
    private ProgressBarUtil pbu;

    public ProgressBarManagerUtil() {
        this.DFORMAT.setRoundingMode(RoundingMode.CEILING);
        this.pbarPercentageGlobal = 0;
        this.etaGlobal = 0;
        this.processedGlobal = 0;
        this.processedError = 0;
        this.processedSuccess = 0;
        this.totalGlobal = 0;
        this.timeElapsed = System.nanoTime();
        this.nbar = 0;
        this.nbarTotal = 0;
        this.pbu = new ProgressBarUtil();
    }

    /**
     * Constructor para manejar mas de 1 barra de progreso
     *
     * @param totalGlobal total de elementos que se procesarán
     * @param nbarTotal total de barras de progreso que se mostrarán en la vista
     */
    public ProgressBarManagerUtil(long totalGlobal, int nbarTotal) {
        this.DFORMAT.setRoundingMode(RoundingMode.CEILING);
        this.etaGlobal = 0;
        this.pbarPercentageGlobal = 0;
        this.processedGlobal = 0;
        this.processedError = 0;
        this.processedSuccess = 0;
        this.totalGlobal = totalGlobal;
        this.timeElapsed = System.nanoTime();
        this.nbar = 0;
        this.nbarTotal = nbarTotal;
    }

    public float getPbarPercentageGlobal() {
        return pbarPercentageGlobal;
    }

    public void setPbarPercentageGlobal() {
        this.pbarPercentageGlobal = Float.parseFloat(DFORMAT.format((100F * this.processedGlobal) / this.totalGlobal));
    }

    public long getEtaGlobal() {
        return etaGlobal;
    }

    public void setEtaGlobal(long etaGlobal) {
        this.etaGlobal = etaGlobal;
    }

    public long getProcessedGlobal() {
        return processedGlobal;
    }

    public void setProcessedGlobal() {
        try {
            this.processedGlobal++;
            if (this.pbu != null) {
                getPbu().setProcessed();
            }
            setPbarPercentageGlobal();
            setEtaGlobal((((System.nanoTime() - this.timeElapsed) / 1000000) * (this.totalGlobal - this.processedGlobal)) / this.processedGlobal);
        } catch (Exception e) {
            System.out.println("> E" + (((System.nanoTime() - this.timeElapsed) / 1000000) * (this.totalGlobal - this.processedGlobal)) / this.processedGlobal);
        }

    }

    public long getProcessedError() {
        return processedError;
    }

    public void setProcessedError() {
        this.processedError++;
        getPbu().setProcessedError();
    }

    public long getProcessedSuccess() {
        return processedSuccess;
    }

    public void setProcessedSuccess() {
        this.processedSuccess++;
        getPbu().setProcessedSuccess();
    }

    public long getTotalGlobal() {
        return totalGlobal;
    }

    public void setTotalGlobal(long totalGlobal) {
        this.totalGlobal = totalGlobal;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public int getNbarTotal() {
        return nbarTotal;
    }

    public void setNbarTotal(int nbarTotal) {
        this.nbarTotal = nbarTotal;
    }

    public ProgressBarUtil getPbu() {
        return pbu;
    }

    public void setPbu(long total, String info) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProgressBarManagerUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.pbu = new ProgressBarUtil(this.nbar++, total, info);
    }
}

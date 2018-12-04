package com.becasipn.domain;

import com.becasipn.persistence.model.Proceso;
import com.becasipn.persistence.model.Otorgamiento;
import java.util.ArrayList;
import java.util.List;

public class Resumenes {

    private List<Otorgamiento> otorgamientoList = new ArrayList<>();
    private List<BecaPeriodoCount> otorgamientosBecariosCount = new ArrayList<>();
    private Proceso proceso = new Proceso();

    private String otorgamientoListStr;
    private String otorgamientosBecariosCountStr;

    public List<Otorgamiento> getOtorgamientoList() {
        return otorgamientoList;
    }

    public void setOtorgamientoList(List<Otorgamiento> otorgamientoList) {
        this.otorgamientoList = otorgamientoList;
    }

    public List<BecaPeriodoCount> getOtorgamientosBecariosCount() {
        return otorgamientosBecariosCount;
    }

    public void setOtorgamientosBecariosCount(List<BecaPeriodoCount> otorgamientosBecariosCount) {
        this.otorgamientosBecariosCount = otorgamientosBecariosCount;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public String getOtorgamientoListStr() {
        return otorgamientoListStr;
    }

    public void setOtorgamientoListStr(String otorgamientoListStr) {
        this.otorgamientoListStr = otorgamientoListStr;
    }

    public String getOtorgamientosBecariosCountStr() {
        return otorgamientosBecariosCountStr;
    }

    public void setOtorgamientosBecariosCountStr(String otorgamientosBecariosCountStr) {
        this.otorgamientosBecariosCountStr = otorgamientosBecariosCountStr;
    }
}

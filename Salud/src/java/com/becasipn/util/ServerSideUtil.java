package com.becasipn.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jesus Fernandez
 */
public class ServerSideUtil {
    public final Map<String, Object> parametros = new HashMap<>();
    public final Map<String, Object> parametrosServidor = new HashMap<>();
    private int start;
    private int length;
    private int sortCol;
    private String sortDir;
    private String filterBox;
    
    public ServerSideUtil(){
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSortCol() {
        return sortCol;
    }

    public void setSortCol(int sortCol) {
        this.sortCol = sortCol;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public String getFilterBox() {
        return filterBox;
    }

    public void setFilterBox(String filterBox) {
        this.filterBox = filterBox;
    }
}

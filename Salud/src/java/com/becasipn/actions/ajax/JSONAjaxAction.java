package com.becasipn.actions.ajax;

import com.becasipn.actions.BaseAction;
import com.becasipn.util.PaginateUtil;
import com.becasipn.util.ServerSideUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Accion base con las propiedades requeridas para completar acciones con uso de
 * JSON.
 *
 * @author Patricia Benitez
 */
public abstract class JSONAjaxAction extends BaseAction {

    private List<String> jsonResult;
    private long jsonTotalRecords;
    private long jsonDisplayRecords;
    private String jsonDraw;
    //Variables que se obtendrán en caso de usar server side processing
    protected String sEcho;
    protected int iDisplayStart;
    protected int iDisplayLength;
    protected String sSearch;
    protected int iColumns;
    protected int iSortCol_0;
    protected String sSortDir_0;
    //Objeto para mandar parametros al Dao
    protected ServerSideUtil ssu;
    //Objeto para paginar del lado del servidor
    protected PaginateUtil pu;

    /**
     * La ejecución de la acción fue satisfactoria, mostrar el resultado JSON.
     */
    protected static final String SUCCESS_JSON = "success_json";
    protected static final String ERROR_JSON = "error_json";
    protected static final String SUCCESS_PAGINATED_JSON = "success_paginated_json";

    /**
     * Inicializa el objeto <code>JSONAjaxAction</code>.
     */
    public JSONAjaxAction() {
        jsonResult = new ArrayList<String>();
        jsonTotalRecords = 0;
        jsonDisplayRecords = 0;
        jsonDraw = new String();
        ssu = new ServerSideUtil();
    }

    /**
     * Obtiene el valor de jsonResult
     *
     * @return el valor de jsonResult
     */
    public List<String> getJsonResult() {
        return jsonResult;
    }

    /**
     * Establece el valor de jsonResult
     *
     * @param jsonResult nuevo valor de jsonResult
     */
    public void setJsonResult(List<String> jsonResult) {
        this.jsonResult = jsonResult;
    }

    public long getJsonTotalRecords() {
        return jsonTotalRecords;
    }

    public void setJsonTotalRecords(long jsonTotalRecords) {
        this.jsonTotalRecords = jsonTotalRecords;
    }

    public long getJsonDisplayRecords() {
        return jsonDisplayRecords;
    }

    public void setJsonDisplayRecords(long jsonDisplayRecords) {
        this.jsonDisplayRecords = jsonDisplayRecords;
    }

    public String getJsonDraw() {
        return jsonDraw;
    }

    public void setJsonDraw(String jsonDraw) {
        this.jsonDraw = jsonDraw;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
        this.jsonDraw = sEcho;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
        this.jsonDisplayRecords = iDisplayLength;
    }

    public String getsSearch() {
        return sSearch;
    }

    public void setsSearch(String sSearch) {
        this.sSearch = sSearch;
    }

    public int getiColumns() {
        return iColumns;
    }

    public void setiColumns(int iColumns) {
        this.iColumns = iColumns;
    }

    public int getiSortCol_0() {
        return iSortCol_0;
    }

    public void setiSortCol_0(int iSortCol_0) {
        this.iSortCol_0 = iSortCol_0;
    }

    public String getsSortDir_0() {
        return sSortDir_0;
    }

    public void setsSortDir_0(String sSortDir_0) {
        this.sSortDir_0 = sSortDir_0;
    }

    protected PaginateUtil getPu() {
        return pu;
    }

    protected void setPu(PaginateUtil pu) {
        this.pu = pu;
        setJsonTotalRecords(this.pu.getNoResultados());
        setJsonDisplayRecords(this.pu.getNoResultadosFiltrados());
    }

    protected void setSsu() {
        ssu.setStart(iDisplayStart);
        ssu.setLength(iDisplayLength);
        ssu.setSortCol(iSortCol_0);
        ssu.setSortDir(sSortDir_0);
        ssu.setFilterBox(sSearch);
    }
}

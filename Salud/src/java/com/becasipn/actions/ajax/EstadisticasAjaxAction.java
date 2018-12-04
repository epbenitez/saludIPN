package com.becasipn.actions.ajax;

import static com.becasipn.actions.ajax.JSONAjaxAction.SUCCESS_JSON;
import com.becasipn.business.EstadisticasBO;
import com.becasipn.business.OtorgamientoBO;
import java.math.BigDecimal;

public class EstadisticasAjaxAction extends JSONAjaxAction {

    private String est;
    private String periodo;
    private String nivel;
    private String ua;
    private String tipoBeca;
    private String total;
//----------------------------------------------------------------------------------------------------------------

    public String listadoTodo() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaTodo(new BigDecimal(periodo));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                String nvl = findNivel(val[0]);
                String ex;
                if (est.equals("1")) {
                    ex = "[\"<a style=\'cursor: pointer;\' href='/admin/formGenero1Estadisticas.action?periodo=" + periodo + "&nivel=" + nvl + "&est=" + est + "'>" + val[0] + "</a>"
                            + "\", \"<a style=\'cursor: pointer;\' onclick='recarga(" + periodo + "," + nvl + ")'>" + val[1] + "</a>"
                            + " \"]";
                } else if (est.equals("2")) {
                    ex = "[\"<a style=\'cursor: pointer;\' href='/admin/formDepositos1Estadisticas.action?periodo=" + periodo + "&nivel=" + nvl + "&est=" + est + "'>" + val[0] + "</a>"
                            + "\", \"<a style=\'cursor: pointer;\' onclick='recarga(" + periodo + "," + nvl + "," + val[1] + ")'>" + val[1] + "</a>"
                            + " \"]";
                } else if (est.equals("3")) {
                    ex = "[\"<a style=\'cursor: pointer;\' href='/admin/formEstatusT1Estadisticas.action?periodo=" + periodo + "&nivel=" + nvl + "&est=" + est + "'>" + val[0] + "</a>"
                            + "\", \"<a style=\'cursor: pointer;\' onclick='recarga(" + periodo + "," + nvl + ")'>" + val[1] + "</a>"
                            + " \"]";
                } else {
                    ex = "[\"" + val[0]
                            + "\", \"" + val[1]
                            + " \"]";
                }
                getJsonResult().add(ex);
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosTodo() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaTodo(new BigDecimal(periodo));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoNivel() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaNivel(new BigDecimal(periodo), new BigDecimal(nivel));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                String un = findUA(val[0]);
                String ex;
                if (est.equals("1")) {
                    ex = "[\"<a style=\'cursor: pointer;\' href='/admin/formGenero2Estadisticas.action?periodo=" + periodo + "&nivel=" + nivel + "&ua=" + un + "&est=" + est + "'>" + val[0] + "</a>"
                            + "\", \"<a style=\'cursor: pointer;\' onclick='recarga(" + periodo + "," + nivel + "," + un + ")'>" + val[1] + "</a>"
                            + " \"]";
                } else if (est.equals("2")) {
                    ex = "[\"<a style=\'cursor: pointer;\' href='/admin/formDepositos2Estadisticas.action?periodo=" + periodo + "&nivel=" + nivel + "&ua=" + un + "&est=" + est + "'>" + val[0] + "</a>"
                            + "\", \"<a style=\'cursor: pointer;\' onclick='recarga(" + periodo + "," + nivel + "," + un + "," + val[1] + ")'>" + val[1] + "</a>"
                            + " \"]";
                } else if (est.equals("3")) {
                    ex = "[\"<a style=\'cursor: pointer;\' href='/admin/formEstatusT2Estadisticas.action?periodo=" + periodo + "&nivel=" + nivel + "&ua=" + un + "&est=" + est + "'>" + val[0] + "</a>"
                            + "\", \"<a style=\'cursor: pointer;\' onclick='recarga(" + periodo + "," + nivel + "," + un + ")'>" + val[1] + "</a>"
                            + " \"]";
                } else {
                    ex = "[\"" + val[0]
                            + "\", \"" + val[1]
                            + " \"]";
                }
                getJsonResult().add(ex);
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosNivel() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaNivel(new BigDecimal(periodo), new BigDecimal(nivel));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoUnidad() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaUnidad(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                String tb = findTB(val[0]);
                String ex;
                if (est.equals("1")) {
                    ex = "[\"" + val[0]
                            + "\", \"<a style=\'cursor: pointer;\' onclick='recarga(" + periodo + "," + nivel + "," + ua + "," + tb + ")'>" + val[1] + "</a>" + " \"]";
                } else if (est.equals("2")) {
                    ex = "[\"" + val[0]
                            + "\", \"<a style=\'cursor: pointer;\' onclick='recarga(" + periodo + "," + nivel + "," + ua + "," + tb + "," + val[1] + ")'>" + val[1] + "</a>"
                            + " \"]";
                } else if (est.equals("3")) {
                    ex = "[\"" + val[0]
                            + "\", \"<a style=\'cursor: pointer;\' onclick='recarga(" + periodo + "," + nivel + "," + ua + "," + tb + ")'>" + val[1] + "</a>" + " \"]";
                } else {
                    ex = "[\"" + val[0]
                            + "\", \"" + val[1]
                            + " \"]";
                }
                getJsonResult().add(ex);
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosUnidad() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaUnidad(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }
//----------------------------------------------------------------------------------------------------------------

    public String listadoNivelGenero() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaNivelGenero(new BigDecimal(periodo), new BigDecimal(nivel));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                getJsonResult().add("[\"" + val[0]
                        + "\", \"" + val[1]
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosNivelGenero() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaNivelGenero(new BigDecimal(periodo), new BigDecimal(nivel));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoUnidadGenero() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaUnidadGenero(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                getJsonResult().add("[\"" + val[0]
                        + "\", \"" + val[1]
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosUnidadGenero() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaUnidadGenero(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoBecaGenero() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaBecaGenero(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua), new BigDecimal(tipoBeca));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                getJsonResult().add("[\"" + val[0]
                        + "\", \"" + val[1]
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosBecaGenero() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaBecaGenero(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua), new BigDecimal(tipoBeca));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }
//----------------------------------------------------------------------------------------------------------------

    public String getSum(String uno, String dos) {
        Integer ax = Integer.valueOf(uno) - Integer.valueOf(dos);
        return ax.toString();
    }

    public String listadoNivelDepositos() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaNivelDepositos(new BigDecimal(periodo), new BigDecimal(nivel));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                getJsonResult().add("[\"" + val[0]
                        + "\", \"" + val[1]
                        + "\", \"" + getSum(total, val[1])
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosNivelDepositos() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaNivelDepositos(new BigDecimal(periodo), new BigDecimal(nivel));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoUnidadDepositos() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaUnidadDepositos(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                getJsonResult().add("[\"" + val[0]
                        + "\", \"" + val[1]
                        + "\", \"" + getSum(total, val[1])
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosUnidadDepositos() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaUnidadDepositos(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoBecaDepositos() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaBecaDepositos(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua), new BigDecimal(tipoBeca));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                getJsonResult().add("[\"" + val[0]
                        + "\", \"" + val[1]
                        + "\", \"" + getSum(total, val[1])
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosBecaDepositos() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaBecaDepositos(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua), new BigDecimal(tipoBeca));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }

    //----------------------------------------------------------------------------------------------------------------
    public String listadoNivelEstatusT() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaNivelEstatusT(new BigDecimal(periodo), new BigDecimal(nivel));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                getJsonResult().add("[\"" + val[0]
                        + "\", \"" + val[1]
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosNivelEstatusT() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaNivelEstatusT(new BigDecimal(periodo), new BigDecimal(nivel));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoUnidadEstatusT() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaUnidadEstatusT(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                getJsonResult().add("[\"" + val[0]
                        + "\", \"" + val[1]
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosUnidadEstatusT() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaUnidadEstatusT(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }

    public String listadoBecaEstatusT() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaBecaEstatusT(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua), new BigDecimal(tipoBeca));
        if (aux != null) {
            String[] ls = aux.split("/");
            for (String ax : ls) {
                String[] val = ax.split(",");
                getJsonResult().add("[\"" + val[0]
                        + "\", \"" + val[1]
                        + " \"]");
            }
        }
        return SUCCESS_JSON;
    }

    public String getDatosBecaEstatusT() {
        EstadisticasBO bo = new EstadisticasBO(getDaos());
        String aux = bo.estadisticaBecaEstatusT(new BigDecimal(periodo), new BigDecimal(nivel), new BigDecimal(ua), new BigDecimal(tipoBeca));
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
        }
        return SUCCESS_JSON;
    }
    //----------------------------------------------------------------------------------------------------------------

    public String getDatosInscritos() {
        String aux = "";
        switch (est) {
            case "1"://GRAFICA DE ALUMNOS REGISTRADOS
                aux = getDaos().getAlumnoDao().totalAlumnosRegistradosD(new BigDecimal(nivel));
                break;

            case "2"://GRAFICA DE TOTAL DE SOLICITUDES
                aux = getDaos().getSolicitudBecaDao().totalAlumnosConSolicitudD(new BigDecimal(nivel), null);
                break;

            case "3"://GRAFICA DE TOTAL DE SOLICITUDES NORMALES
                aux = getDaos().getSolicitudBecaDao().totalAlumnosConSolicitudD(new BigDecimal(nivel), new BigDecimal(1));
                break;

            case "4"://GRAFICA DE TOTAL DE SOLICITUDES DE TRANSPORTE
                aux = getDaos().getSolicitudBecaDao().totalAlumnosConSolicitudD(new BigDecimal(nivel), new BigDecimal(2));
                break;

            case "5"://GRAFICA DE TOTAL DE SOLICITUDES PENDIENTES
                aux = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitudD(new BigDecimal(nivel), null);
                break;

            case "6"://GRAFICA DE TOTAL DE SOLICITUDES EN ESPERA
                aux = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitudD(new BigDecimal(nivel), new BigDecimal(3));
                break;

            case "7"://GRAFICA DE TOTAL DE SOLICITUDES RECHAZADAS
                aux = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitudD(new BigDecimal(nivel), new BigDecimal(2));
                break;

            case "8"://GRAFICA DE TOTAL DE SOLICITUDES DE ASIGNADAS
                aux = getDaos().getSolicitudBecaDao().totalAlumnosEstatusSolicitudD(new BigDecimal(nivel), new BigDecimal(1));
                break;

            case "9"://GRAFICA DE TOTAL DE CUESTIONARIOS SALUD
                aux = getDaos().getSolicitudBecaDao().totalAlumnosCuestionarioCompletoD(new BigDecimal(nivel), new BigDecimal(3));
                break;

            case "10"://GRAFICA DE ALUMNOS CON OTORGAMIENTO
                aux = getDaos().getOtorgamientoDao().totalAlumnosConOtorgamientoD(Boolean.TRUE, new BigDecimal(nivel));
                break;

            case "11"://GRAFICA DE ALUMNOS CON BAJA
                aux = getDaos().getOtorgamientoDao().totalAlumnosConOtorgamientoD(Boolean.FALSE, new BigDecimal(nivel));
                break;

            case "12"://GRAFICA DE ALUMNOS CON BECA UNIVERSAL
                aux = getDaos().getOtorgamientoDao().totalAlumnosBecaUniversal(new BigDecimal(nivel));
                break;

            case "13"://GRAFICA DE ALUMNOS CON SOLICITUDES DE MANUTENCIÓN PENDIENTES
                aux = getDaos().getSolicitudBecaDao().totalAlumnosBecaPendienteManutencion(new BigDecimal(nivel));
                break;

            case "14"://GRAFICA DE ALUMNOS CON SOLICITUDES PENDIENTES DE OTRAS BECAS
                aux = getDaos().getSolicitudBecaDao().totalAlumnosBecaPendienteDiferenteManutencion(new BigDecimal(nivel));
                break;

            default:
                aux = getDaos().getAlumnoDao().totalAlumnosRegistradosD(new BigDecimal(nivel));
                break;
        }
        if (aux != null) {
            getJsonResult().add("[\"" + aux + " \"]");
            return SUCCESS_JSON;
        } else {
            return ERROR_JSON;
        }
    }

//----------------------------------------------------------------------------------------------------------------
    public String findNivel(String nivel) {
        return getDaos().getNivelDao().findByNombre(nivel).getId().toString();
    }

    public String findUA(String ua) {
        return getDaos().getUnidadAcademicaDao().getByNombreCorto(ua).getId().toString();
    }

    public String findTB(String tipoBeca) {
        return getDaos().getTipoBecaDao().getByNombre(tipoBeca).getId().toString();
    }

    public String getEst() {
        return est;
    }

    public void setEst(String est) {
        this.est = est;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}

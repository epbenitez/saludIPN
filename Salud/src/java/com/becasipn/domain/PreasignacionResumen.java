package com.becasipn.domain;

/**
 *
 * @author Othoniel Herrera
 */
public class PreasignacionResumen {

    private String unidadAcademica;
    private long solicitudesPreasignadasUA;
    private long solicitudesNoPreasignadasUA;
    private long solicitudesProcesadasUA;

    public String getUnidadAcademica() {
        return unidadAcademica;
    }

    public void setUnidadAcademica(String unidadAcademica) {
        this.unidadAcademica = unidadAcademica;
    }

    public long getSolicitudesPreasignadasUA() {
        return solicitudesPreasignadasUA;
    }

    public void setSolicitudesPreasignadasUA(long solicitudesPreasignadasUA) {
        this.solicitudesPreasignadasUA = solicitudesPreasignadasUA;
    }

    public long getSolicitudesNoPreasignadasUA() {
        return solicitudesNoPreasignadasUA;
    }

    public void setSolicitudesNoPreasignadasUA(long solicitudesNoPreasignadasUA) {
        this.solicitudesNoPreasignadasUA = solicitudesNoPreasignadasUA;
    }

    public long getSolicitudesProcesadasUA() {
        return solicitudesProcesadasUA;
    }

    public void setSolicitudesProcesadasUA(long solicitudesProcesadasUA) {
        this.solicitudesProcesadasUA = solicitudesProcesadasUA;
    }

    @Override
    public String toString() {
        return "PreasignacionResumen{" + "unidadAcademica=" + unidadAcademica + "solicitudesProcesadasUA=" + solicitudesProcesadasUA + "solicitudesPreasignadasUA=" + solicitudesPreasignadasUA + ", solicitudesNoPreasignadasUA=" + solicitudesNoPreasignadasUA + '}';
    }
}

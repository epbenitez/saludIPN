package com.becasipn.business;

import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.Carrera;
import com.becasipn.persistence.model.Estadistica;
import com.becasipn.persistence.model.Movimiento;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Cuestionario;
import com.becasipn.persistence.model.EstadisticaCuestionarios;
import com.becasipn.persistence.model.EstadisticaSolicitudes;
import com.becasipn.service.Service;
import com.becasipn.util.AmbienteEnums;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EstadisticasBO extends BaseBO {

    // total de otorgamientos para los que no aplica el sexto depósito
    private Long otorgamientosMenosMeses;

    private AmbienteEnums enums = AmbienteEnums.getInstance();
    private Map<String, String> meses = enums.getMeses();

    public EstadisticasBO(Service service) {
        super(service);
    }

//----------------------------------------------------------------------------------------------------------------
    public String estadisticaTodo(BigDecimal periodo) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaTodo(periodo);
        return acomodar(ax);
    }

    public String estadisticaNivel(BigDecimal periodo, BigDecimal nivel) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaNivel(periodo, nivel);
        return acomodar(ax);
    }

    public String estadisticaUnidad(BigDecimal periodo, BigDecimal nivel, BigDecimal ua) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaUnidad(periodo, nivel, ua);
        return acomodar(ax);
    }
//----------------------------------------------------------------------------------------------------------------    

    public String estadisticaNivelGenero(BigDecimal periodo, BigDecimal nivelId) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaNivelGenero(periodo, nivelId);
        return acomodar(ax);
    }

    public String estadisticaUnidadGenero(BigDecimal periodo, BigDecimal nivelId, BigDecimal unidadId) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaUnidadGenero(periodo, nivelId, unidadId);
        return acomodar(ax);
    }

    public String estadisticaBecaGenero(BigDecimal periodo, BigDecimal nivelId, BigDecimal unidadId, BigDecimal tipoBecaId) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaBecaGenero(periodo, nivelId, unidadId, tipoBecaId);
        return acomodar(ax);
    }

//----------------------------------------------------------------------------------------------------------------
    public static String getMes(String mes) {
        switch (Integer.valueOf(mes)) {
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Septiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                return "X";
        }
    }

    public String estadisticaNivelDepositos(BigDecimal periodo, BigDecimal nivelId) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaNivelDepositos(periodo, nivelId);
        String s = "";
        for (Object[] res : ax) {
            BigDecimal aux = (BigDecimal) res[0];
            s = s + getMes(aux.toString()) + "," + res[1] + "/";
        }
        if (s.length() > 1) {
            return s;
        } else {
            return null;
        }
    }

    public String estadisticaUnidadDepositos(BigDecimal periodo, BigDecimal nivelId, BigDecimal unidadId) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaUnidadDepositos(periodo, nivelId, unidadId);
        String s = "";
        for (Object[] res : ax) {
            BigDecimal aux = (BigDecimal) res[0];
            s = s + getMes(aux.toString()) + "," + res[1] + "/";
        }
        if (s.length() > 1) {
            return s;
        } else {
            return null;
        }
    }

    public String estadisticaBecaDepositos(BigDecimal periodo, BigDecimal nivelId, BigDecimal unidadId, BigDecimal tipoBecaId) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaBecaDepositos(periodo, nivelId, unidadId, tipoBecaId);
        String s = "";
        for (Object[] res : ax) {
            BigDecimal aux = (BigDecimal) res[0];
            s = s + getMes(aux.toString()) + "," + res[1] + "/";
        }
        if (s.length() > 1) {
            return s;
        } else {
            return null;
        }
    }

//----------------------------------------------------------------------------------------------------------------
    public String getEstatusT(String tarId) {
        return service.getEstatusTarjetaBancariaDao().getEstatusTarjeta(tarId);
    }

    public String estadisticaNivelEstatusT(BigDecimal periodo, BigDecimal nivelId) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaNivelEstatusT(periodo, nivelId);
        return acomodar(ax);
    }

    public String estadisticaUnidadEstatusT(BigDecimal periodo, BigDecimal nivelId, BigDecimal unidadId) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaUnidadEstatusT(periodo, nivelId, unidadId);
        String s = "";
        return acomodar(ax);
    }

    public String estadisticaBecaEstatusT(BigDecimal periodo, BigDecimal nivelId, BigDecimal unidadId, BigDecimal tipoBecaId) {
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaBecaEstatusT(periodo, nivelId, unidadId, tipoBecaId);
        return acomodar(ax);
    }

//----------------------------------------------------------------------------------------------------------------
    private String acomodar(List<Object[]> ax) {
        String s = "";
        for (Object[] res : ax) {
            s = s + res[0] + "," + res[1] + "/";
        }
        if (s.length() > 1) {
            return s;
        } else {
            return null;
        }
    }

//----------------------------------------------------------------------------------------------------------------
    public List<Estadistica> estadistica(Estadistica.Tipo tipo, Periodo periodo, Nivel nivel,
            UnidadAcademica unidadAcademica, Beca beca, Movimiento movimiento) {

        List<Estadistica> estadisticas = new ArrayList<>();

        BigDecimal nivelId = nivel.getId().equals(new BigDecimal(0)) ? null : nivel.getId();
        BigDecimal unidadAcademicaId = unidadAcademica.getId().equals(new BigDecimal(0)) ? null : unidadAcademica.getId();
        BigDecimal becaId = beca.getId().equals(new BigDecimal(0)) ? null : beca.getId();
        BigDecimal movimientoId = movimiento.getId().equals(new BigDecimal(0)) ? null : movimiento.getId();

        List parametros = new ArrayList();

        switch (tipo) {
            case MOVIMIENTOS:
                parametros = service.getMovimientoDao().findAll();
                break;
            case CARRERA:
                if (unidadAcademicaId != null) {
                    parametros = service.getCarreraDao().findByUnidadAacademica(unidadAcademicaId);
                } else {
                    parametros.add(null);
                }
                break;
            case PROGRAMA_BECA:
                parametros = service.getBecaDao().findAll();
                break;
            case PROMEDIOS:
                parametros = Arrays.asList(new String[]{"BETWEEN 0 AND 5.99", "BETWEEN 6 AND 6.99", "BETWEEN 7 AND 7.99", "BETWEEN 8 AND 8.99", "BETWEEN 9 AND 10"});
                break;
            case SEMESTRES:
                parametros = Arrays.asList(new String[]{"IN(1,2)", "IN(3,4)", "IN(5,6)", "IN(7,8)", " >= 9"});
                break;
            case GENERO:
                parametros.add(" ");
                break;
            case OTORGAMIENTOS:
                parametros = service.getOtorgamientoDao().getFechasOtorgamientos(periodo.getId());
                break;
        }

        for (Object parametro : parametros) {
            Long mujeres = service.getOtorgamientoDao().getEstadistica(tipo, periodo.getId(), nivelId,
                    unidadAcademicaId, becaId, movimientoId, "F", parametro);
            Long hombres = service.getOtorgamientoDao().getEstadistica(tipo, periodo.getId(), nivelId,
                    unidadAcademicaId, becaId, movimientoId, "M", parametro);
            Long total = mujeres + hombres;

            Long becarios = service.getOtorgamientoDao().estadisticaBecarios(periodo.getId(), nivel.getId(), unidadAcademica.getId(), beca.getId(), movimiento.getId());

            String name;
            if (parametro == null) {
                name = "No aplica";
            } else if (parametro instanceof Carrera) {
                name = ((Carrera) parametro).getCarrera();
            } else if (parametro instanceof Movimiento) {
                name = ((Movimiento) parametro).getNombre();
            } else if (parametro instanceof Beca) {
                name = ((Beca) parametro).getNombre();
            } else if (parametro instanceof Date) {
                name = ((Date) parametro).toString();
            } else {
                name = parametro.toString();
                if (name.contains("BETWEEN")) {
                    name = name.replaceAll("BETWEEN", "ENTRE").replaceAll("AND", "Y");
                } else if (name.contains("IN")) {
                    name = name.replaceAll("IN\\(", "").replaceAll(",", "° y ").replaceAll("\\)", "°");
                } else if (name.contains(">=")) {
                    name = name.replaceAll(">=", "").concat("°");
                }
            }

            estadisticas.add(new Estadistica(tipo, name, total, becarios, hombres, mujeres));
        }

        return estadisticas;
    }

    public List<Estadistica> estadisticaPendiente(Estadistica.Tipo tipo, Periodo periodo, Nivel nivel,
            UnidadAcademica unidadAcademica, Beca beca, Movimiento movimiento) {

        List<Estadistica> estadisticas = new ArrayList<>();

        BigDecimal nivelId = nivel.getId().equals(new BigDecimal(0)) ? null : nivel.getId();
        BigDecimal unidadAcademicaId = unidadAcademica.getId().equals(new BigDecimal(0)) ? null : unidadAcademica.getId();
        BigDecimal becaId = beca.getId().equals(new BigDecimal(0)) ? null : beca.getId();
        BigDecimal movimientoId = movimiento.getId().equals(new BigDecimal(0)) ? null : movimiento.getId();

        List parametros = new ArrayList();
        parametros.add(" ");

        for (Object parametro : parametros) {
            Long mujeres = service.getOtorgamientoDao().getEstadisticaPendientes(tipo, periodo.getId(), nivelId,
                    unidadAcademicaId, becaId, movimientoId, "F", parametro);
            Long hombres = service.getOtorgamientoDao().getEstadisticaPendientes(tipo, periodo.getId(), nivelId,
                    unidadAcademicaId, becaId, movimientoId, "M", parametro);
            Long total = mujeres + hombres;

            Long becarios = service.getOtorgamientoDao().estadisticaBecarios(periodo.getId(), nivel.getId(), unidadAcademica.getId(), beca.getId(), movimiento.getId());

            String name = "No aplica";

            estadisticas.add(new Estadistica(tipo, name, total, becarios, hombres, mujeres));
        }

        return estadisticas;
    }

    public List<Estadistica> estadisticaRendimiento(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaRendimiento(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId());
        if (ax != null) {
            estadisticas.addAll(setElementos(ax, Estadistica.Tipo.OTORGAMIENTOS));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaRegistro(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaRegistro(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId());
        if (ax != null) {
            estadisticas.addAll(setElementos(ax, Estadistica.Tipo.REGISTRO));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaRegistroT(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaRegistroT(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId());
        if (ax != null) {
            estadisticas.addAll(setElementos(ax, Estadistica.Tipo.GENERO));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaRendimientoBajas(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaRendimientoBajas(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId());
        if (ax != null) {
            estadisticas.addAll(setElementos(ax, Estadistica.Tipo.BAJAS));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaBajas(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaBajas(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId(), movimiento.getId());
        if (ax != null) {
            estadisticas.addAll(setElementosB(ax, Estadistica.Tipo.GENEROB));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaProgramaBeca(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaProgramaBeca(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId(), movimiento.getId());
        if (tipoBeca.getId().intValue() != 0) {
            ax = service.getOtorgamientoDao().estadisticaTipoBeca(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId(), movimiento.getId());
        }
        if (ax != null) {
            estadisticas.addAll(setElementos(ax, Estadistica.Tipo.PROGRAMA_BECA));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaProgramaBecaB(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaProgramaBecaB(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId(), movimiento.getId());
        if (tipoBeca.getId().intValue() != 0) {
            ax = service.getOtorgamientoDao().estadisticaTipoBecaB(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId(), movimiento.getId());
        }
        if (ax != null) {
            estadisticas.addAll(setElementos(ax, Estadistica.Tipo.PROGRAMA_BECAB));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaMovimiento(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaMovimientoAltas(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId(), movimiento.getId());

        if (ax != null) {
            estadisticas.addAll(setElementos(ax, Estadistica.Tipo.MOVIMIENTOS));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaMovimientoB(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> axx = service.getOtorgamientoDao().estadisticaMovimientoBajas(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId(), movimiento.getId());

        if (axx != null) {
            estadisticas.addAll(setElementos(axx, Estadistica.Tipo.MOVIMIENTOS));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaSemestreBajas(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List parametros = new ArrayList();

        parametros = Arrays.asList(new String[]{"IN(1,2)", "IN(3,4)", "IN(5,6)", "IN(7,8)", " >= 9"});

        for (Object parametro : parametros) {
            List<Object[]> axx = service.getOtorgamientoDao()
                    .estadisticaSemestreBecaB(periodo.getId(),
                            nivel.getId(),
                            unidadAcademica.getId(),
                            tipoBeca.getId(),
                            movimiento.getId(),
                            parametro
                    );

            if (axx != null) {
                estadisticas.addAll(setElementos(axx, Estadistica.Tipo.SEMESTRES));
            }
        }

        return estadisticas;
    }

    public List<Estadistica> estadisticaPromedioBajas(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List parametros = new ArrayList();

        parametros = Arrays.asList(new String[]{"BETWEEN 0 AND 5.99", "BETWEEN 6 AND 6.99", "BETWEEN 7 AND 7.99", "BETWEEN 8 AND 8.99", "BETWEEN 9 AND 10"});

        for (Object parametro : parametros) {
            List<Object[]> axx = service.getOtorgamientoDao()
                    .estadisticaPromedioBecaB(periodo.getId(),
                            nivel.getId(),
                            unidadAcademica.getId(),
                            tipoBeca.getId(),
                            movimiento.getId(),
                            parametro
                    );

            if (axx != null) {
                estadisticas.addAll(setElementos(axx, Estadistica.Tipo.PROMEDIOB));
            }
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaCarreras(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaCarreras(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId(), movimiento.getId());
        if (ax != null) {
            estadisticas.addAll(setElementos(ax, Estadistica.Tipo.CARRERA));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaCarrerasB(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaCarrerasB(periodo.getId(), nivel.getId(), unidadAcademica.getId(), tipoBeca.getId(), movimiento.getId());
        if (ax != null) {
            estadisticas.addAll(setElementos(ax, Estadistica.Tipo.CARRERA));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaDepositosT(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        Long t = service.getOtorgamientoDao().estadisticaDepositosTotalO(periodo.getId(), nivel.getId(), unidadAcademica.getId(), new BigDecimal(0), false);
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaDepositosT(periodo.getId(), nivel.getId(), unidadAcademica.getId());
        if (ax != null) {
            estadisticas.addAll(setElementosC(ax, Estadistica.Tipo.GENERO, t));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaDepositos(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, BigDecimal programaBecaId) {
        List<Estadistica> estadisticas = new ArrayList<>();
        Long t = service.getOtorgamientoDao().estadisticaDepositosTotalO(periodo.getId(), nivel.getId(), unidadAcademica.getId(), programaBecaId, false);
        otorgamientosMenosMeses = service.getOtorgamientoDao().estadisticaDepositosTotalO(periodo.getId(), nivel.getId(), unidadAcademica.getId(), programaBecaId, true);
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaDepositos(periodo.getId(), nivel.getId(), unidadAcademica.getId(), programaBecaId);
        if (ax != null) {
            estadisticas.addAll(setElementosC(ax, Estadistica.Tipo.DEPOSITOS, t));
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaCuentasT(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaCuentasT(periodo.getId(), nivel.getId(), unidadAcademica.getId());
        if (ax != null) {
            estadisticas.addAll(setDataEstatusCuenta(ax, Estadistica.Tipo.GENERO, null));
        }
        return estadisticas;
    }

    public List<EstadisticaCuestionarios> estadisticaCuestionarios(Periodo periodo, Cuestionario cuestionario) {
        List<EstadisticaCuestionarios> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getCuestionarioPreguntaRespuestaDao().estadisticaCuestionario(periodo.getId(), cuestionario.getId());
        if (ax != null) {
            estadisticas.addAll(setDataCuestionarios(ax, cuestionario));
        }
        return estadisticas;
    }

    public List<EstadisticaSolicitudes> estadisticaPreasignadas(Periodo periodo, UnidadAcademica ua, Nivel nivel, String fechaInicial, String fechaFinal) {
        List<EstadisticaSolicitudes> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getSolicitudBecaDao().estadisticaPreasignadasData(periodo.getId(), ua.getId(), nivel.getId(), fechaInicial, fechaFinal);
        long total = 0;
        if (ax != null) {
            for (Object[] x : ax) {
                EstadisticaSolicitudes ex = new EstadisticaSolicitudes();
                ex.setNombre(x[0].toString());
                BigDecimal aux = (BigDecimal) x[1];
                ex.setTotal(aux.longValue());
                total += aux.longValue();
                ex.setTotalSolicitudes(total);
                estadisticas.add(ex);

            }
        }
        return estadisticas;
    }

    public List<EstadisticaSolicitudes> estadisticaSolicitadas(BigDecimal periodoId, BigDecimal unidadAcademicaId, BigDecimal nivelId, String fechaInicial, String fechaFinal) {
        List<EstadisticaSolicitudes> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getSolicitudBecaDao().estadisticaSolicitadasData(periodoId, unidadAcademicaId, nivelId, fechaInicial, fechaFinal);
        long total = 0;
        if (ax != null) {
            if (ax != null) {
                for (Object[] x : ax) {
                    EstadisticaSolicitudes ex = new EstadisticaSolicitudes();
                    ex.setNombre(x[0].toString());
                    BigDecimal aux = (BigDecimal) x[1];
                    ex.setTotal(aux.longValue());
                    total += aux.longValue();
                    ex.setTotalSolicitudes(total);
                    estadisticas.add(ex);
                }
            }
        }
        return estadisticas;
    }

    public List<Estadistica> estadisticaCuentas(Periodo periodo, Nivel nivel, UnidadAcademica unidadAcademica, Beca tipoBeca, Movimiento movimiento) {
        List<Estadistica> estadisticas = new ArrayList<>();
        List<Object[]> ax = service.getOtorgamientoDao().estadisticaCuentas(periodo.getId(), nivel.getId(), unidadAcademica.getId());
        if (ax != null) {
            estadisticas.addAll(setElementosC(ax, Estadistica.Tipo.DEPOSITOS, null));
        }
        return estadisticas;
    }

    public List<Estadistica> setElementos(List<Object[]> ax, Estadistica.Tipo tipo) {
        List<Estadistica> estadisticas = new ArrayList<>();
        for (Object[] x : ax) {
            Estadistica ex = new Estadistica();
            ex.setTipo(tipo);
            if (tipo == Estadistica.Tipo.OTORGAMIENTOS || tipo == Estadistica.Tipo.REGISTRO || tipo == Estadistica.Tipo.BAJAS) {
                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                Date d = (Date) x[0];
                ex.setNombre(d == null ? "-" : formater.format(x[0]));
            } else {
                ex.setNombre("" + x[0] + "");
            }
            BigDecimal l = (BigDecimal) x[1];
            ex.setTotalHombre(l == null ? 0 : l.longValue());
            l = (BigDecimal) x[2];
            ex.setTotalMujer(l == null ? 0 : l.longValue());
            ex.setTotal(ex.getTotalMujer() + ex.getTotalHombre());
            estadisticas.add(ex);
        }
        return estadisticas;
    }

    public List<Estadistica> setElementosB(List<Object[]> ax, Estadistica.Tipo tipo) {
        List<Estadistica> estadisticas = new ArrayList<>();
        for (Object[] x : ax) {
            Estadistica ex = new Estadistica();
            ex.setTipo(tipo);

            BigDecimal l = (BigDecimal) x[0];
            ex.setTotalHombre(l == null ? 0 : l.longValue());
            l = (BigDecimal) x[1];
            ex.setTotalMujer(l == null ? 0 : l.longValue());
            ex.setTotal(ex.getTotalMujer() + ex.getTotalHombre());
            estadisticas.add(ex);
        }
        return estadisticas;
    }

    public List<Estadistica> setElementosC(List<Object[]> ax, Estadistica.Tipo tipo, Long total) {
        List<Estadistica> estadisticas = new ArrayList<>();

        for (Object[] x : ax) {
            String mesBD = x[0].toString();
            Estadistica ex = new Estadistica();
            ex.setTipo(tipo);
            if (tipo == Estadistica.Tipo.DEPOSITOS) {
                // Busca el nombre del mes, según el resultado de la BD
                if (meses.containsKey(mesBD)) {
                    ex.setNombre(meses.get(mesBD));
                } else {
                    switch (mesBD) {
                        case "13":
                            ex.setNombre("Enero");
                            break;
                        default:
                            ex.setNombre(mesBD);
                            break;
                    }
                }
            } else {
                ex.setNombre(mesBD);
            }
            // Extrae la info de la BD y la asigna al lugar correspondiente, 
            // hasta la posición 8 pues son 8 las columnas del pivot. La 9 y 10 tiene trato especial
            for (int i = 1; i <= 8; i++) {
                BigDecimal aux = (BigDecimal) x[i];
                if (aux != null) {
                    ex.setAx(i, aux.longValue());
                }
            }
            ex.setTotal(ex.getAx1() + ex.getAx2() + ex.getAx3() + ex.getAx4() + ex.getAx5() + ex.getAx6() + ex.getAx7() + ex.getAx8());
            if (total != null) {
                if (tipo == Estadistica.Tipo.DEPOSITOS) {
                    // Si no es el último mes
                    if (!mesBD.equals("13") && !mesBD.equals("7")) {
                        ex.setAx9(total - ex.getTotal());//Faltantes de un total sumado
                    } else {
                        // Si es el último mes, entonces restar los otorgamientos que no pagan los 6 meses
                        ex.setAx9(total - ex.getTotal() - otorgamientosMenosMeses);//Faltantes de un total sumado
                        ex.setAx10(otorgamientosMenosMeses);
                    }
                }
                ex.setTotal(total);
            }
            estadisticas.add(ex);
        }

        estadisticas.addAll(getMesesVacios(estadisticas.get(estadisticas.size() - 1).getNombre()));

        return estadisticas;
    }

    public List<Estadistica> setDataEstatusCuenta(List<Object[]> ax, Estadistica.Tipo tipo, Long total) {
        List<Estadistica> estadisticas = new ArrayList<>();
        for (Object[] x : ax) {
            Estadistica ex = new Estadistica();
            // Extrae la info de la BD y la asigna al lugar correspondiente, 
            // hasta la posición 7 pues no hay más resultados. La 8 y 9 tiene trato especial
            for (int i = 0; i < x.length; i++) {
                BigDecimal aux = (BigDecimal) x[i];
                if (aux != null) {
                    ex.setAx(i, aux.longValue());
                }
            }
            estadisticas.add(ex);
        }

        return estadisticas;
    }

    public List<EstadisticaCuestionarios> setDataCuestionarios(List<Object[]> ax, Cuestionario cuestionario) {
        List<EstadisticaCuestionarios> estadisticas = new ArrayList<>();
        for (Object[] x : ax) {
            EstadisticaCuestionarios ex = new EstadisticaCuestionarios();
            ex.setPreguntaId((BigDecimal) x[0]);
            ex.setPregunta(x[1].toString());
            ex.setRespuestaId((BigDecimal) x[2]);
            ex.setRespuesta(x[3].toString());
            BigDecimal aux = (BigDecimal) x[4];
            ex.setTotalRespuesta(aux.longValue());
            ex.setSeccion((BigDecimal) x[5]);
            estadisticas.add(ex);
        }

        return estadisticas;
    }

    /**
     * Crea una lista de objetos Estadistica con los meses restantes del
     * semestre, a partir del último mes
     *
     * @author Mario Márquez
     * @param ultimoMes Nombre del último mes (límite inferior).
     * @return List<Estadistica> Meses restantes del semestre
     */
    private List<Estadistica> getMesesVacios(String ultimoMes) {
        String lastMonthKey = "";
        List<Estadistica> estadisticasVacias = new ArrayList<>();

        for (Map.Entry<String, String> entry : meses.entrySet()) {
            if (entry.getValue().equals(ultimoMes)) {
                lastMonthKey = entry.getKey();
                if (lastMonthKey.equals("1")) {
                    lastMonthKey = "13";
                }
                break;
            }
        }
        int floor = Integer.parseInt(lastMonthKey);
        int ceiling;
        if (floor > 7) {
            ceiling = 13;
        } else {
            ceiling = 7;
        }

        for (int i = floor + 1; i <= ceiling; i++) {
            Estadistica ex = new Estadistica();
            ex.setTipo(Estadistica.Tipo.DEPOSITOS);
            ex.setNombre(meses.get(String.valueOf(i)));
            estadisticasVacias.add(ex);
        }

        return estadisticasVacias;
    }
}

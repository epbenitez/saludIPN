package com.becasipn.business;

import com.becasipn.domain.OrdenDepositoPivot;
import com.becasipn.persistence.model.Alumno;
import com.becasipn.persistence.model.Beca;
import com.becasipn.persistence.model.ConvocatoriaSubes;
import com.becasipn.persistence.model.DatosAcademicos;
import com.becasipn.persistence.model.Depositos;
import com.becasipn.persistence.model.EstatusDeposito;
import com.becasipn.persistence.model.Nivel;
import com.becasipn.persistence.model.OrdenDeposito;
import com.becasipn.persistence.model.Otorgamiento;
import com.becasipn.persistence.model.Periodo;
import com.becasipn.persistence.model.TipoBeca;
import com.becasipn.persistence.model.TipoBecaPeriodo;
import com.becasipn.persistence.model.TipoDeposito;
import com.becasipn.persistence.model.TipoProceso;
import com.becasipn.persistence.model.UnidadAcademica;
import com.becasipn.persistence.model.Usuario;
import com.becasipn.service.Service;
import static java.lang.Math.round;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Victor Lozano
 */
public class OrdenDepositoBO extends BaseBO {

    public OrdenDepositoBO(Service service) {
        super(service);
    }

    /**
     * Almacena la información de la orden de deposito
     *
     * @author Victor Lozano
     * @param orden
     * @return true si la operación se realizó exitosamente
     */
    public Boolean guardaOrden(OrdenDeposito orden) {
        try {
            if (orden.getId() == null) {
                service.getOrdenDepositoDao().save(orden);
            } else {
                service.getOrdenDepositoDao().update(orden);
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Crea el nombre de una nueva orden de deposito según los parametros
     * seleccionados. El nombre de la dispersión se generará bajo el siguiente
     * formato:
     * Nivel_de_estudios|Programa_de_la_Beca|Tipo_de_Dispersion|Mes|Periodo_de_Pago|Fecha_de_Ejecución||ID
     * Ejemplo: NMS_PRO_ORD_FEB_20151_061014_01
     *
     * @author Tania G. Sánchez
     * @param mes
     * @param origenRecursos
     * @param nivel
     * @param beca
     * @param tDeposito
     * @param periodoPago
     * @param formaPago
     * @param tipoDeposito
     * @param determinacionRecursos
     * @param convocatoriaSubes
     * @return
     */
    public String nombreOrdenDeposito(Integer mes, Integer origenRecursos, Nivel nivel, Beca beca, TipoDeposito tDeposito,
            Periodo periodoPago, BigDecimal formaPago, BigDecimal tipoDeposito, int determinacionRecursos, ConvocatoriaSubes convocatoriaSubes) {
        String mS = "";
        switch (mes) {
            case 1:
                mS = "_ENE";
                break;
            case 2:
                mS = "_FEB";
                break;
            case 3:
                mS = "_MAR";
                break;
            case 4:
                mS = "_ABR";
                break;
            case 5:
                mS = "_MAY";
                break;
            case 6:
                mS = "_JUN";
                break;
            case 7:
                mS = "_JUL";
                break;
            case 8:
                mS = "_AGO";
                break;
            case 9:
                mS = "_SEP";
                break;
            case 10:
                mS = "_OCT";
                break;
            case 11:
                mS = "_NOV";
                break;
            case 12:
                mS = "_DIC";
                break;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        String date = sdf.format(new Date());
        SimpleDateFormat sdfId = new SimpleDateFormat("HHmmss");
        String id = sdfId.format(new Date());
        if (origenRecursos == 0) {
            mS = mS + "_FUN";
        }
        String cuenta = "";
        if (formaPago.equals(new BigDecimal(1))) {
            cuenta = "_CU";
        } else if (formaPago.equals(new BigDecimal(2))) {
            cuenta = "_RE";
        } else if (formaPago.equals(new BigDecimal(3))) {
            cuenta = "_CB";
        }
        String recursos = determinacionRecursos == 1 ? "_IPN" : determinacionRecursos == 2 ? "_CNBES" : "";
        String depositoTipo = tipoDeposito.equals(new BigDecimal("1")) ? "_TDO_" : "_TDR_";
        String subes = convocatoriaSubes==null?"": "_CONV-" + convocatoriaSubes.getClave() + "_";
        String nombre = nivel.getClave() + "_" + beca.getClavedisp() + "_" + tDeposito.getClave() + mS + cuenta + depositoTipo + periodoPago.getClave() + recursos + subes + "_" + date + "_" + id;
        return nombre;
    }

    /**
     * Crea una nueva orden de deposito según los parametros seleccionados.
     *
     * @author Tania G. Sánchez
     * @param periodoPago
     * @param mes
     * @param unidadAcademica
     * @param beca
     * @param tipoProceso
     * @param tDeposito
     * @param usuario
     * @param nombre
     * @param nivel
     * @param origenRecursos
     * @param formaPago
     * @return
     */
    public OrdenDeposito creaOrdenDeposito(Periodo periodoPago, Integer mes, BigDecimal unidadAcademica, Beca beca, BigDecimal tipoProceso, TipoDeposito tDeposito,
            Usuario usuario, String nombre, Nivel nivel, Integer origenRecursos, BigDecimal formaPago) {
        OrdenDeposito ordenDeposito = new OrdenDeposito();
        ordenDeposito.setPeriodo(periodoPago);
        ordenDeposito.setMes(mes);
        if (unidadAcademica.intValue() == 0) {
            //Si unidadAcademica es = a 0 es porque se selecciono la opción todas y en bd se sabe que se selecciono esa opción cuando el valor es null.
            ordenDeposito.setUnidadAcademica(null);
        } else {
            UnidadAcademica uAcademica = service.getUnidadAcademicaDao().findById(unidadAcademica);
            ordenDeposito.setUnidadAcademica(uAcademica);
        }
        //Programa de Beca
        ordenDeposito.setProgramaBeca(beca);
        if (tipoProceso.intValue() == 0) {
            //Si tipoDeposito es = a 0 es porque se selecciono la opción todos y en bd se sabe que se selecciono esa opción cuando el valor es null.
            ordenDeposito.setTipoProceso(null);
        } else {
            TipoProceso tProceso = service.getTipoProcesoDao().findById(tipoProceso);
            ordenDeposito.setTipoProceso(tProceso);
        }
        ordenDeposito.setTipoDeposito(tDeposito);
        //Se le asigna el estatusDeposito 1 que es En Espera.
        EstatusDeposito estatusDeposito = new EstatusDeposito();
        estatusDeposito.setId(new BigDecimal(1));
        ordenDeposito.setEstatusDeposito(estatusDeposito);
        ordenDeposito.setUsuario(usuario);
        ordenDeposito.setNombreOrdenDeposito(nombre);
        ordenDeposito.setNombreRespuestaBancaria("");
        ordenDeposito.setFechaEjecucion(new Date());
        ordenDeposito.setNivel(nivel);
        if (origenRecursos == 0) {
            ordenDeposito.setCorrespondeIPN(Boolean.FALSE);
        } else {
            ordenDeposito.setCorrespondeIPN(Boolean.TRUE);
        }
        ordenDeposito.setFormaPago(formaPago.intValue());
        ordenDeposito.setFechaGeneracion(new Date());
        service.getOrdenDepositoDao().save(ordenDeposito);
        return ordenDeposito;
    }

    public List<OrdenDeposito> obtieneOrdenesDepositoPorEstatus(BigDecimal estatus) {
        List<OrdenDeposito> odList = service.getOrdenDepositoDao().ordenesDepositoPorEstatus(estatus);
        return odList == null || odList.isEmpty() ? null : odList;
    }

    public List<Otorgamiento> getPreviewOrdenDeposito(BigDecimal periodoId, BigDecimal mes, Integer origenRecursos, BigDecimal programaBeca, BigDecimal nivelAcademico,
            BigDecimal uAcademica, BigDecimal tipoProceso, BigDecimal idOtorgamiento, String fechaDeposito, BigDecimal formaPago, BigDecimal tipoDeposito, int determinacionRecursos,
            ConvocatoriaSubes convocatoriaSubes) {
        List<Object[]> lista = service.getOtorgamientoDao().buscarOrdenDeposito(Boolean.TRUE, periodoId, mes, origenRecursos,
                programaBeca, nivelAcademico, uAcademica, tipoProceso, idOtorgamiento, fechaDeposito, formaPago, tipoDeposito, determinacionRecursos, convocatoriaSubes);
        List<Otorgamiento> lo = new ArrayList<>();
        BigDecimal montoVariable = new BigDecimal(0);
        BigDecimal totalAlumnos = new BigDecimal(0);
        BigDecimal sumaMonto = new BigDecimal(0);
        for (Object[] res : lista) {
            Otorgamiento o = new Otorgamiento();
            Alumno a = new Alumno();
            DatosAcademicos datosAcademicos = new DatosAcademicos();
            UnidadAcademica ua = new UnidadAcademica();
            ua.setId((BigDecimal) res[0]);
            ua.setNombreCorto((String) res[1]);
            datosAcademicos.setAlumno(a);
            datosAcademicos.setUnidadAcademica(ua);
            o.setAlumno(a);
            o.setDatosAcademicos(datosAcademicos);
            TipoBeca tb = new TipoBeca();
            tb.setId((BigDecimal) res[2]);
            tb.setNombre((String) res[3]);
            TipoBecaPeriodo tbp = new TipoBecaPeriodo();
            tbp.setTipoBeca(tb);
            o.setTipoBecaPeriodo(tbp);
            totalAlumnos = res[4] == null ? new BigDecimal(0) : (BigDecimal) res[4];
            o.setAlumnosTotal(totalAlumnos.longValue());
            sumaMonto = res[5] == null ? new BigDecimal(0) : (BigDecimal) res[5];
            o.setSumaMonto(sumaMonto.doubleValue());
            montoVariable = res[6] == null ? null : (BigDecimal) res[6];
            //Si hay datos en montoVariable, es que este manda
            if (montoVariable != null && montoVariable.compareTo(new BigDecimal(0)) != 0 //&& o.getSumaMonto()==null
                    ) {
                o.setSumaMonto(montoVariable.doubleValue());
            }
            lo.add(o);
        }
        return lo;
    }

    public List<Alumno> getListaAlumnosOrdenDeposito(BigDecimal periodoId, BigDecimal mes, Integer origenRecursos, BigDecimal programaBeca, BigDecimal nivelAcademico,
            BigDecimal uAcademica, BigDecimal tipoProceso, BigDecimal idOtorgamiento, String fechaDeposito, BigDecimal formaPago, BigDecimal tipoDeposito, int determinacionRecursos,ConvocatoriaSubes convocatoriaSubes) {
        List<Object[]> lista = service.getOtorgamientoDao().buscarOrdenDeposito(Boolean.FALSE, periodoId, mes, origenRecursos,
                programaBeca, nivelAcademico, uAcademica, tipoProceso, idOtorgamiento, fechaDeposito, formaPago, tipoDeposito, determinacionRecursos,convocatoriaSubes);
        List<Alumno> la = new ArrayList<>();
        EstatusDeposito ed = service.getEstatusDepositoDao().findById(new BigDecimal(10));
        for (Object[] res : lista) {
            Alumno a = new Alumno();
            a.setId((BigDecimal) res[1]);
            a.setOtorgamientoPasadoId((BigDecimal) res[0]);
            a.setTarjetaBancariaId((BigDecimal) res[10]);
            a.setMontoVariable((res[6] == null || Integer.parseInt(res[6].toString()) == 0) ? Boolean.FALSE : Boolean.TRUE);
            if (a.getMontoVariable()) {
                if (res[5] == null) {
                    a.setMonto((BigDecimal) res[5]);
                } else {
                    a.setMonto((BigDecimal) res[7]);
                }
            } else {
                a.setMonto((BigDecimal) res[5]);
            }
            if (res[11] != null) {
                Depositos d = service.getDepositosDao().findById((BigDecimal) res[11]);
                d.setEstatusDeposito(ed);
                a.setDeposito(d);
            }
            la.add(a);
        }
        return la;
    }
    
    public final String getFileNamePendientes(Integer cuenta) {
        String cuentaStr = "";
        switch (cuenta) {
            case 0:
                cuentaStr = "Pendientes Tarjeta";
                break;
            case 1:
                cuentaStr = "Pendientes Cuenta";
                break;
            case 2:
                cuentaStr = "Pendientes Referencia";
                break;
            case 3:
                cuentaStr = "Pendientes CLABE";
                break;
        }
        
        return cuentaStr;
    }
    
    public final List<Object[]> getInfoExcelPendientes(Integer cuenta) {
        // el parametro viene del front
        List<LinkedHashMap<String, Object>> results = service.getOrdenDepositoDao().ordenesPendientesPorCuenta(cuenta);
        List<Object[]> info = new ArrayList<>();
        // si el resuultado no esta vacío
        for (Map<String, Object> result : results) {
            Object[] row = new Object[5];
            String periodo = (String) result.get("periodo");
            String nombreOrdn = (String) result.get("nombreOrden");
            Long total = (Long) result.get("total");
            Long espera = (Long) result.get("enEspera");
            Integer faltantes = round((espera * 100) / total);
            String faltantesPercnt = faltantes.toString() + '%';
            
            row[0] = periodo;
            row[1] = nombreOrdn;
            row[2] = total;
            row[3] = espera;
            row[4] = faltantesPercnt;
            
            info.add(row);
        }
        
        return info;
    }

    public List<OrdenDepositoPivot> informacionPivotOrdenesDeposito(BigDecimal estatus) {
        List<Periodo> periodoLst = service.getPeriodoDao().findAll(10);
        if (periodoLst != null && !periodoLst.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for(Periodo p: periodoLst){
                sb.append("'").append(p.getClave()).append("',");
            }
            
            String periodos = sb.toString().substring(0, sb.toString().lastIndexOf(","));
            
            List<Object[]> odList = service.getOrdenDepositoDao().pivotOrdenesPendientesPorEstatus(estatus, periodos);
            List<OrdenDepositoPivot> odp = new ArrayList<OrdenDepositoPivot>();
            if(odList!=null && !odList.isEmpty()){
                for(Object[] o: odList) {
                    OrdenDepositoPivot op = new OrdenDepositoPivot();
                    op.setNombre(o[0]==null?"":o[0].toString());
                    Integer valores[] = new Integer[o.length-1];
                    for(Integer i=2; i< o.length; i++){
                        valores[i-1] = (o[i]==null?0:new Integer(o[i].toString()));
                    }
                    op.setValoresStr(arraytoString(valores));
                    op.setValoresStrEjeX(formatXvalues(periodos));
                    BigDecimal cuenta = o[1]==null?(new BigDecimal(0)):((BigDecimal) o[1]);
                    op.setCuentaId(cuenta.intValue());
                    odp.add(op);
                }
            }
            return odp.isEmpty() ? null : odp;
        }
        return null;
    }    
    
    
    public String arraytoString(Integer[] valores) {
        StringBuilder val = new StringBuilder();
        if (valores != null && valores.length > 0) {
            for (Integer v : valores) {
                val.append(v).append(",");
            }
            
        }
        return val.toString().length()<1?val.toString():val.substring(0, val.lastIndexOf(","));
    }
    
    public String formatXvalues(String x){
        if(x==null){
            return "";
        }
        StringBuilder res = new StringBuilder();
        String[] arr = x.split(",");
        for(int i=0; i<arr.length; i++){
            res.append(i+1).append(":").append(arr[i]).append(",");
        }
        res.deleteCharAt(res.length()-1);
        return res.toString();
    }
}

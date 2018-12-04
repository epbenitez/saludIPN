package com.becasipn.domain;

import com.becasipn.persistence.model.AreasConocimiento;
import com.becasipn.persistence.model.TransporteTraslado;
import java.math.BigDecimal;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Mario Márquez
 */
public class Requisito {

    private final Boolean cumple;
    private final Boolean cadena;
    private final String texto;

    public static class Builder {

        private boolean cumple;
        private boolean cadena;
        private String texto = "No hay datos academicos para el periodo en curso";

        public Builder() {
        }

        public Builder beca(String nombreBeca) {
            texto = nombreBeca;
            cadena = true;
            return this;
        }

        public Builder ingresos(Float ingresosBeca, Float ingresosSolicitud) {
            StringBuilder sb = new StringBuilder();

            if (ingresosBeca != null) {
                if (ingresosSolicitud <= ingresosBeca) {
                    cumple = true;
                    sb.append("Los ingresos per cápita son los adecuados (son menores a ");
                    sb.append(ingresosBeca);
                    sb.append(")");
                } else {
                    sb.append("Los ingresos per cápita son de ");
                    sb.append(ingresosSolicitud);
                    sb.append(", y están fuera del límite (son mayores a ");
                    sb.append(ingresosBeca);
                    sb.append(")");
                }
            } else {
                cumple = true;
                sb.append("El requisito no aplica para esta beca");
            }
            texto = sb.toString();
            return this;
        }

        public Builder transporte() {
            texto = "El alumno no ha respondido su cuestionario de transporte.";
            return this;
        }

        public Builder noTransporte(boolean faltaSolicitud) {
            if (faltaSolicitud) {
                cumple = false;
                texto = "La solicitud no es de transporte";
            } else {
                cumple = true;
                texto = "Esta beca no es de transporte institucional";
            }
            
            return this;
        }

        public Builder transporte(Float gastoMinimo, List<TransporteTraslado> traslados) {
            StringBuilder sb = new StringBuilder();
            float costo = 0f;
            for (TransporteTraslado traslado : traslados) {
                costo += traslado.getCosto() * 21.5f;
            }
            if (costo >= gastoMinimo) {
                cumple = true;
                sb.append("El gasto en transporte es mayor a ");
                sb.append(gastoMinimo);
            } else {
                sb.append("El gasto en transporte es de ");
                sb.append(costo);
                sb.append(" (menor al mínimo ");
                sb.append(gastoMinimo);
                sb.append(")");
            }
            texto = sb.toString();

            return this;
        }

        public Builder vulnera(Boolean vulneraBeca, Boolean vulneraSubes) {
            if (vulneraBeca != null) {
                if (vulneraBeca.equals(vulneraSubes)) {
                    cumple = true;
                    texto = "Sí";
                } else {
                    texto = "No";
                }
            } else {
                cumple = true;
                texto = "El requisito no aplica para esta beca";
            }
            return this;
        }

        public Builder regular(Integer regularAlumno, Integer regularBeca) {
            if (regularBeca != null) {
                if (regularAlumno >= regularBeca) {
                    cumple = true;
                    texto = "Cumple con el requisito de regularidad";
                } else {
                    texto = "No cumple con el requisito de regularidad";
                }
            } else {
                cumple = true;
                texto = "La regularidad no es requisito para esta beca";
            }
            return this;
        }

        public Builder inscrito(Integer inscrito) {
            if (inscrito == 1) {
                cumple = true;
                texto = "Alumno inscrito";
            } else {
                texto = "Alumno no inscrito";
            }
            return this;
        }

        public Builder promedio(Float min, Double promedioAlumno) {
            StringBuilder sb = new StringBuilder();
            Boolean up = promedioAlumno >= min;

            if (up) {
                cumple = true;
                sb.append("Promedio mayor o igual al mínimo (");
                sb.append(min);
                sb.append(")");
            } else {
                sb.append("Promedio menor al mínimo (");
                sb.append(min);
                sb.append(")");
            }
            texto = sb.toString();

            return this;
        }

        public Builder semestre(Integer minBeca, Integer maxBeca, Integer alumno) {
            StringBuilder sb = new StringBuilder();

            Boolean up = alumno >= minBeca;
            Boolean down = alumno <= maxBeca;
            if (up && down) {
                cumple = true;
                sb.append("Semestre dentro de los límites (");
                sb.append(minBeca);
                sb.append(" - ");
                sb.append(maxBeca);
                sb.append(")");
            } else {
                sb.append("Semestre fuera de los límites (");
                sb.append(minBeca);
                sb.append(" - ");
                sb.append(maxBeca);
                sb.append(")");
            }
            texto = sb.toString();

            return this;
        }

        public Builder carga(Integer becaCarga, Integer alumnoCarga) {
            StringBuilder sb = new StringBuilder();

            if (becaCarga != null) {
                String cargaNecesaria = textoCarga(becaCarga);
                if (becaCarga <= alumnoCarga) {
                    cumple = true;
                    sb.append("El alumno cuenta con ");
                    sb.append(cargaNecesaria);
                    sb.append(" o más");
                } else {
                    String cargaAlumno = textoCarga(alumnoCarga);
                    sb.append("El alumno cuenta con ");
                    sb.append(cargaAlumno);
                    sb.append(" (la beca solicita ");
                    sb.append(cargaNecesaria);
                    sb.append(")");
                }
            } else {
                cumple = true;
                sb.append("El requisito no aplica para esta beca");
            }
            texto = sb.toString();

            return this;
        }

        public Builder areasConocimiento(AreasConocimiento areasUA, AreasConocimiento areasBeca) {
            StringBuilder sb = new StringBuilder();

            if (areasBeca == null || areasBeca.equals(areasUA) || areasBeca.getId().equals(new BigDecimal(4))) {
                cumple = true;
                sb.append("Las áreas de conocimiento son compatibles");
            } else {
                sb.append("Las áreas de conocimiento no son compatibles");
            }
            texto = sb.toString();

            return this;
        }

        private String textoCarga(Integer becaCarga) {
            String cargaNecesaria = "";

            switch (becaCarga) {
                case 0:
                    cargaNecesaria = "Sin carga mínima";
                    break;
                case 1:
                    cargaNecesaria = "Carga mínima";
                    break;
                case 2:
                    cargaNecesaria = "Carga media";
                    break;
                case 3:
                    cargaNecesaria = "Carga máxima";
                    break;
            }

            return cargaNecesaria;
        }

        public Requisito build() {
            return new Requisito(this);
        }
    }

    private Requisito(Builder builder) {
        cumple = builder.cumple;
        cadena = builder.cadena;
        texto = builder.texto;
    }
    
    public JsonObject toJson() {
        JsonObjectBuilder jOB = Json.createObjectBuilder()
                .add("cumple", cumple)
                .add("cadena", cadena)
                .add("texto", texto);
        
        return jOB.build();
    }
    
    public Boolean getCumple() {
        return cumple;
    }

    public String getTexto() {
        return texto;
    }

    public Boolean getCadena() {
        return cadena;
    }
}

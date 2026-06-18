package controlador;

import java.util.ArrayList;
import java.util.Comparator;
import modelo.Estudiante;
import modelo.Materia;
import modelo.InscripcionMateria;
import dao.EstudianteDAOJDBC; // Restaurado a tu versión original
import dao.MateriaDAO;
import dao.InscripcionMateriaDAO;

public class ControladorAcademico {

    private final ArrayList<Materia> materias;
    private final ArrayList<Estudiante> estudiantes;

    private Estudiante estudianteActivo;

    private final MateriaDAO materiaDAO;
    private final EstudianteDAOJDBC estudianteDAO; // Restaurado a tu versión original

    public ControladorAcademico() {
        this.materiaDAO = new MateriaDAO();
        this.estudianteDAO = new EstudianteDAOJDBC(); // Restaurado a tu versión original

        this.materias = materiaDAO.cargar();
        this.estudiantes = estudianteDAO.cargar();

        this.estudianteActivo = null;
    }

    public boolean iniciarSesion(String legajo) {
        Estudiante e = buscarEstudiantePorLegajo(legajo);
        if (e == null) {
            return false;
        }
        this.estudianteActivo = e;
        cargarInscripcionesDelActivo();
        return true;
    }

    public void registrarEstudianteYLoguear(String nombre, String legajo, String carrera, int anioIngreso) {
        registrarEstudiante(nombre, legajo, carrera, anioIngreso);
        this.estudianteActivo = buscarEstudiantePorLegajo(legajo);
        cargarInscripcionesDelActivo();
    }

    public boolean haySesionActiva() {
        return estudianteActivo != null;
    }

    private void cargarInscripcionesDelActivo() {
        if (estudianteActivo == null) {
            return;
        }
        InscripcionMateriaDAO dao = new InscripcionMateriaDAO(rutaInscripciones(estudianteActivo.getLegajo()));
        ArrayList<InscripcionMateria> inscripciones = dao.cargar(materias);
        estudianteActivo.getMaterias().clear();
        estudianteActivo.getMaterias().addAll(inscripciones);
    }

    private void guardarInscripcionesDelActivo() {
        if (estudianteActivo == null) {
            return;
        }
        InscripcionMateriaDAO dao = new InscripcionMateriaDAO(rutaInscripciones(estudianteActivo.getLegajo()));
        dao.guardar(estudianteActivo.getMaterias());
    }

    private String rutaInscripciones(String legajo) {
        return "inscripciones_" + legajo + ".txt";
    }

    public void registrarMateria(String codigo, String nombre, int cuatrimestre, int anio) {
        Materia nuevaMateria = new Materia(nombre, codigo, cuatrimestre, anio);

        for (Materia m : materias) {
            if (m.getCodigo().equalsIgnoreCase(codigo)) {
                throw new IllegalArgumentException("Ya existe una materia con el código: " + codigo);
            }
        }

        materias.add(nuevaMateria);
        materiaDAO.guardar(materias); 
    }

    public void registrarEstudiante(String nombre, String legajo, String carrera, int anioIngreso) {
        if (legajo == null || legajo.trim().isEmpty()) {
            throw new IllegalArgumentException("El legajo no puede estar vacío.");
        }

        for (Estudiante e : estudiantes) {
            if (e.getLegajo().equalsIgnoreCase(legajo)) {
                throw new IllegalArgumentException("Ya existe un estudiante con el legajo: " + legajo);
            }
        }

        Estudiante nuevoEstudiante = new Estudiante(nombre, legajo, carrera, anioIngreso);
        estudiantes.add(nuevoEstudiante);
        estudianteDAO.guardar(nuevoEstudiante); 
    }

    public void procesarInscripcion(String codigo, String nombre, int cuatrimestre, int anio) {
        requiereSesion();

        Materia materiaExistente = buscarMateriaPorCodigo(codigo);
        
        if (materiaExistente == null) {
            registrarMateria(codigo, nombre, cuatrimestre, anio);
        }

        inscribirEstudianteActivo(codigo);
    }

    private void inscribirEstudianteActivo(String codigoMateria) {
        requiereSesion();
        Materia materia = buscarMateriaPorCodigo(codigoMateria);
        if (materia == null) {
            throw new IllegalArgumentException("Materia no encontrada.");
        }

        if (estudianteActivo.getInscripcion(codigoMateria) != null) {
            throw new IllegalArgumentException(
                    "Ya estás inscripto en la materia: " + materia.getNombre());
        }

        estudianteActivo.inscribirse(materia);
        guardarInscripcionesDelActivo();
    }

    public void darDeBajaActivo(String codigoMateria) {
        requiereSesion();
        InscripcionMateria insc = estudianteActivo.getInscripcion(codigoMateria);
        if (insc == null) {
            throw new IllegalArgumentException("El estudiante no está inscripto en esa materia.");
        }
        estudianteActivo.darDeBaja(codigoMateria);
        guardarInscripcionesDelActivo();
    }

    public void registrarAsistenciaActivo(String codigoMateria, boolean presente) {
        requiereSesion();
        InscripcionMateria inscripcion = estudianteActivo.getInscripcion(codigoMateria);

        if (inscripcion == null) {
            throw new IllegalArgumentException("El estudiante no está inscripto en esta materia.");
        }

        inscripcion.registrarAsistencia(presente);
        guardarInscripcionesDelActivo();
    }

    public void registrarNotaActivo(String codigoMateria, double nota) {
        requiereSesion();
        InscripcionMateria inscripcion = estudianteActivo.getInscripcion(codigoMateria);

        if (inscripcion == null) {
            throw new IllegalArgumentException("El estudiante no está inscripto en esta materia.");
        }

        inscripcion.agregarNota(nota); 
        guardarInscripcionesDelActivo();
    }

    public boolean asistenciaEnRiesgo(String codigoMateria) {
        requiereSesion();
        InscripcionMateria inscripcion = estudianteActivo.getInscripcion(codigoMateria);
        if (inscripcion == null) {
            return false;
        }
        return inscripcion.getPorcentajeAsistencia() < 75.0;
    }

    public String[] obtenerDatosPerfilActivo() {
        if (estudianteActivo == null) {
            return null;
        }
        
        double promedio = estudianteActivo.getPromedioGeneral();
        // El Controlador define la regla de negocio para la alerta
        String alerta = (promedio > 0 && promedio < 6) ? "Atención: tu promedio general está por debajo de 6." : " ";

        return new String[]{
            estudianteActivo.getNombre(),
            estudianteActivo.getLegajo(),
            estudianteActivo.getCarrera(),
            String.valueOf(estudianteActivo.getAnioIngreso()),
            String.valueOf(promedio),
            alerta // Enviamos la alerta ya resuelta a la Vista
        };
    }

    public Object[][] obtenerDatosInscripcionesActivo() {
        if (estudianteActivo == null || estudianteActivo.getMaterias() == null) {
            return new Object[0][5];
        }

        ArrayList<InscripcionMateria> lista = estudianteActivo.getMaterias();
        Object[][] matrizDatos = new Object[lista.size()][5];

        for (int i = 0; i < lista.size(); i++) {
            InscripcionMateria ins = lista.get(i);
            matrizDatos[i][0] = ins.getMateria().getCodigo();
            matrizDatos[i][1] = ins.getMateria().getNombre();
            matrizDatos[i][2] = ins.getCondicion();
            matrizDatos[i][3] = String.format("%.1f%%", ins.getPorcentajeAsistencia());
            matrizDatos[i][4] = String.format("%.2f", ins.getPromedio());
        }

        return matrizDatos;
    }

    public String[] obtenerMateriasEnRiesgoActivo() {
        if (estudianteActivo == null) {
            return new String[0];
        }
        ArrayList<InscripcionMateria> criticas = estudianteActivo.getMateriasCriticas();
        String[] resultado = new String[criticas.size()];
        for (int i = 0; i < criticas.size(); i++) {
            InscripcionMateria insc = criticas.get(i);
            resultado[i] = String.format("%s (%.1f%% asistencia)",
                    insc.getMateria().getNombre(), insc.getPorcentajeAsistencia());
        }
        return resultado;
    }

    public String[] obtenerMateriasAprobadasActivo() {
        if (estudianteActivo == null) {
            return new String[0];
        }
        ArrayList<InscripcionMateria> aprobadas = new ArrayList<>();
        for (InscripcionMateria insc : estudianteActivo.getMaterias()) {
            if (insc.estaAprobada()) {
                aprobadas.add(insc);
            }
        }
        String[] resultado = new String[aprobadas.size()];
        for (int i = 0; i < aprobadas.size(); i++) {
            InscripcionMateria insc = aprobadas.get(i);
            resultado[i] = String.format("%s (Promedio: %.2f)",
                    insc.getMateria().getNombre(), insc.getPromedio());
        }
        return resultado;
    }

    public String generarReporteSituacionGeneral() {
        requiereSesion();
        StringBuilder sb = new StringBuilder();
        sb.append("Estudiante: ").append(estudianteActivo.getNombre()).append("\n");
        sb.append("Legajo: ").append(estudianteActivo.getLegajo()).append("\n");
        sb.append("Carrera: ").append(estudianteActivo.getCarrera()).append("\n");
        sb.append("Año de ingreso: ").append(estudianteActivo.getAnioIngreso()).append("\n");
        sb.append("Materias inscriptas: ").append(estudianteActivo.getMaterias().size()).append("\n");
        sb.append(String.format("Promedio general: %.2f\n", estudianteActivo.getPromedioGeneral()));

        int aprobadas = 0;
        int libres = 0;
        for (InscripcionMateria insc : estudianteActivo.getMaterias()) {
            if (insc.estaAprobada()) {
                aprobadas++;
            }
            if ("Libre".equals(insc.getCondicion())) {
                libres++;
            }
        }
        sb.append("Materias aprobadas: ").append(aprobadas).append("\n");
        sb.append("Materias en condición Libre: ").append(libres);

        return sb.toString();
    }

    public String generarReporteMateriasEnRiesgo() {
        requiereSesion();
        ArrayList<InscripcionMateria> criticas = new ArrayList<>(estudianteActivo.getMateriasCriticas());
        criticas.sort(Comparator.comparingDouble(InscripcionMateria::getPorcentajeAsistencia));

        if (criticas.isEmpty()) {
            return "No hay materias en riesgo (asistencia entre 75% y 85%).";
        }

        StringBuilder sb = new StringBuilder("Materias en riesgo (orden ascendente por asistencia):\n\n");
        for (InscripcionMateria insc : criticas) {
            sb.append(String.format("- %s: %.1f%% de asistencia\n",
                    insc.getMateria().getNombre(), insc.getPorcentajeAsistencia()));
        }
        return sb.toString();
    }

    public String generarReporteMateriasAprobadas() {
        requiereSesion();
        ArrayList<InscripcionMateria> aprobadas = new ArrayList<>();
        for (InscripcionMateria insc : estudianteActivo.getMaterias()) {
            if (insc.estaAprobada()) {
                aprobadas.add(insc);
            }
        }

        if (aprobadas.isEmpty()) {
            return "Todavía no hay materias aprobadas.";
        }

        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        double suma = 0;
        StringBuilder detalle = new StringBuilder();

        for (InscripcionMateria insc : aprobadas) {
            double prom = insc.getPromedio();
            max = Math.max(max, prom);
            min = Math.min(min, prom);
            suma += prom;
            detalle.append(String.format("- %s: %.2f\n", insc.getMateria().getNombre(), prom));
        }

        StringBuilder sb = new StringBuilder("Materias aprobadas:\n\n");
        sb.append(detalle);
        sb.append(String.format("\nPromedio más alto: %.2f\n", max));
        sb.append(String.format("Promedio más bajo: %.2f\n", min));
        sb.append(String.format("Promedio del conjunto: %.2f", suma / aprobadas.size()));
        return sb.toString();
    }

    public ArrayList<String> buscarInscripcionesPorTexto(String texto) {
        requiereSesion();
        ArrayList<String> codigosEncontrados = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) {
            return codigosEncontrados;
        }
        String t = texto.trim().toLowerCase();

        for (InscripcionMateria insc : estudianteActivo.getMaterias()) {
            Materia m = insc.getMateria();
            if (m.getCodigo().toLowerCase().contains(t) || m.getNombre().toLowerCase().contains(t)) {
                codigosEncontrados.add(m.getCodigo());
            }
        }
        return codigosEncontrados;
    }

    private void requiereSesion() {
        if (estudianteActivo == null) {
            throw new IllegalStateException("No hay un estudiante activo en la sesión.");
        }
    }

    private Estudiante buscarEstudiantePorLegajo(String legajo) {
        for (Estudiante e : estudiantes) {
            if (e.getLegajo().equalsIgnoreCase(legajo)) {
                return e;
            }
        }
        return null;
    }

    private Materia buscarMateriaPorCodigo(String codigo) {
        for (Materia m : materias) {
            if (m.getCodigo().equalsIgnoreCase(codigo)) {
                return m;
            }
        }
        return null;
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.ArrayList;
import java.util.Comparator;
import modelo.Estudiante;
import modelo.Materia;
import modelo.InscripcionMateria;
import dao.EstudianteDAOJDBC;
import dao.MateriaDAO;
import dao.InscripcionMateriaDAO;

/**
 * Controlador de la capa MVC.
 * Recibe las acciones de la Vista, aplica las validaciones/lógica del Modelo,
 * llama al DAO correspondiente para persistir y devuelve datos ya armados
 * (Object[][], String[], etc.) para que la Vista solo los muestre.
 *
 * IMPORTANTE: esta clase NO tiene ningún import de javax.swing ni
 * referencias a componentes gráficos, tal como exige la
 * consigna de arquitectura MVC+DAO.
 *
 * @author Valen453
 */
public class ControladorAcademico {

    private ArrayList<Materia> materias;
    private ArrayList<Estudiante> estudiantes;

    // Estudiante actualmente "logueado" en la sesión de autogestión
    private Estudiante estudianteActivo;

    // Instancias de los DAOs reales
    private MateriaDAO materiaDAO;
    private EstudianteDAOJDBC estudianteDAO;
    // Nota: InscripcionMateriaDAO se instancia puntualmente por estudiante
    // (cada legajo tiene su propio archivo de inscripciones), por eso no
    // se guarda como campo fijo acá.

    public ControladorAcademico() {
        this.materiaDAO = new MateriaDAO();
        this.estudianteDAO = new EstudianteDAOJDBC();

        // 1. Cargar catálogo de materias (texto plano)
        this.materias = materiaDAO.cargar();
        // 2. Cargar estudiantes (JDBC / MySQL)
        this.estudiantes = estudianteDAO.cargar();

        this.estudianteActivo = null;
    }

    // ==========================================
    // 0. SESIÓN / LOGIN DEL ESTUDIANTE
    // ==========================================

    /**
     * Busca un estudiante por legajo y lo deja como estudiante activo de la
     * sesión. Si lo encuentra, carga además sus inscripciones desde el
     * archivo de texto correspondiente.
     *
     * @return true si encontró y activó al estudiante, false si no existe.
     */
    public boolean iniciarSesion(String legajo) {
        Estudiante e = buscarEstudiantePorLegajo(legajo);
        if (e == null) {
            return false;
        }
        this.estudianteActivo = e;
        cargarInscripcionesDelActivo();
        return true;
    }

    /**
     * Da de alta un estudiante nuevo (vía JDBC) y lo deja como activo.
     */
    public void registrarEstudianteYLoguear(String nombre, String legajo, String carrera, int anioIngreso) {
        registrarEstudiante(nombre, legajo, carrera, anioIngreso);
        this.estudianteActivo = buscarEstudiantePorLegajo(legajo);
        cargarInscripcionesDelActivo();
    }

    public boolean haySesionActiva() {
        return estudianteActivo != null;
    }

    public Estudiante getEstudianteActivo() {
        return estudianteActivo;
    }

    // Cada estudiante tiene su propio archivo de inscripciones: inscripciones_<legajo>.txt
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

    // ==========================================
    // 1. MÉTODOS DE ENTRADA (altas)
    // ==========================================

    public void registrarMateria(String codigo, String nombre, int cuatrimestre, int anio) {
        // Validaciones delegadas a la clase Materia al instanciar
        Materia nuevaMateria = new Materia(nombre, codigo, cuatrimestre, anio);

        for (Materia m : materias) {
            if (m.getCodigo().equalsIgnoreCase(codigo)) {
                throw new IllegalArgumentException("Ya existe una materia con el código: " + codigo);
            }
        }

        materias.add(nuevaMateria);
        materiaDAO.guardar(materias); // Guarda en materias.txt
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
        estudianteDAO.guardar(nuevoEstudiante); // INSERT vía JDBC
    }

    /**
     * Inscribe al estudiante ACTIVO de la sesión en la materia indicada.
     */
    public void inscribirEstudianteActivo(String codigoMateria) {
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

    /**
     * Da de baja la materia indicada para el estudiante activo.
     * La confirmación previa con el usuario es responsabilidad de la Vista.
     */
    public void darDeBajaActivo(String codigoMateria) {
        requiereSesion();
        InscripcionMateria insc = estudianteActivo.getInscripcion(codigoMateria);
        if (insc == null) {
            throw new IllegalArgumentException("El estudiante no está inscripto en esa materia.");
        }
        estudianteActivo.darDeBaja(codigoMateria);
        guardarInscripcionesDelActivo();
    }

    // ==========================================
    // 2. MÉTODOS DE CARGA OPERATIVA (sobre el activo)
    // ==========================================

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

        inscripcion.agregarNota(nota); // Lanza error hacia la vista si no cumple 0-10 o tope de 5
        guardarInscripcionesDelActivo();
    }

    /**
     * Indica si, tras registrar la última asistencia, la condición del
     * estudiante en esa materia quedó por debajo del 75%. Útil para que la
     * Vista muestre la alerta correspondiente al usuario.
     */
    public boolean asistenciaEnRiesgo(String codigoMateria) {
        requiereSesion();
        InscripcionMateria inscripcion = estudianteActivo.getInscripcion(codigoMateria);
        if (inscripcion == null) {
            return false;
        }
        return inscripcion.getPorcentajeAsistencia() < 75.0;
    }

    // ==========================================
    // 3. MÉTODOS DE SALIDA (Para la Vista)
    // ==========================================

    /**
     * Arma la matriz de datos para la JTable de materias inscriptas del
     * estudiante activo. Columnas: nombre, condición, asistencia %, promedio.
     */
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

    /**
     * Devuelve, como arreglo de Strings ya formateados, las materias en
     * condición de riesgo (asistencia entre 75% y 85%) para poblar el JList.
     */
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

    /**
     * Devuelve, como arreglo de Strings ya formateados, las materias
     * aprobadas del estudiante activo para poblar el JList.
     */
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

    /**
     * Reporte de texto con la situación académica general del estudiante
     * activo, para mostrar en un JLabel/JTextArea desde el menú Reportes.
     */
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

    /**
     * Reporte de materias en riesgo, ordenado ascendentemente por
     * porcentaje de asistencia.
     */
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

    /**
     * Reporte de materias aprobadas con nota máxima, mínima y promedio
     * del conjunto de promedios.
     */
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

    /**
     * Busca materias inscriptas del activo por código o nombre (parcial,
     * sin distinguir mayúsculas/minúsculas). Devuelve los códigos que
     * matchean, para que la Vista resalte esas filas en la JTable.
     */
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

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

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

    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.ArrayList;
import modelo.Estudiante;
import modelo.Materia;
import modelo.InscripcionMateria;
import dao.EstudianteDAO;
import dao.MateriaDAO;
import dao.InscripcionMateriaDAO;


/**
 *
 * @author Valen453
 */
public class ControladorAcademico {

    private ArrayList<Materia> materias;
    private ArrayList<Estudiante> estudiantes;

    // Instancias de tus DAOs reales
    private MateriaDAO materiaDAO;
    private EstudianteDAO estudianteDAO;
    private InscripcionMateriaDAO inscripcionDAO;
    
    public ControladorAcademico() {
        this.materiaDAO = new MateriaDAO();
        this.estudianteDAO = new EstudianteDAO();
        this.inscripcionDAO = new InscripcionMateriaDAO();
        
        // 1. Cargar materias base
        this.materias = materiaDAO.cargar();
        // 2. Cargar estudiantes
        this.estudiantes = estudianteDAO.cargar();
    }

    // ==========================================
    // 1. MÉTODOS DE ENTRADA
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
        estudianteDAO.guardar(estudiantes); // Guarda en estudiantes.txt
    }

    public void inscribirEstudiante(String legajo, String codigoMateria) {
        Estudiante estudiante = buscarEstudiantePorLegajo(legajo);
        Materia materia = buscarMateriaPorCodigo(codigoMateria);

        if (estudiante == null) throw new IllegalArgumentException("Estudiante no encontrado.");
        if (materia == null) throw new IllegalArgumentException("Materia no encontrada.");

        estudiante.inscribirse(materia);
        
        // Guardar cambios: Asumiendo que las inscripciones se guardan globalmente
        inscripcionDAO.guardar(obtenerTodasLasInscripciones()); 
    }

    // ==========================================
    // 2. MÉTODOS DE CARGA OPERATIVA
    // ==========================================

    public void registrarAsistencia(String legajo, String codigoMateria, boolean presente) {
        InscripcionMateria inscripcion = buscarInscripcion(legajo, codigoMateria);
        
        if (inscripcion == null) {
            throw new IllegalArgumentException("El estudiante no está inscripto en esta materia.");
        }

        inscripcion.registrarAsistencia(presente);
        inscripcionDAO.guardar(obtenerTodasLasInscripciones()); // Actualiza archivo
    }

    public void registrarNota(String legajo, String codigoMateria, double nota) {
        InscripcionMateria inscripcion = buscarInscripcion(legajo, codigoMateria);
        
        if (inscripcion == null) {
            throw new IllegalArgumentException("El estudiante no está inscripto en esta materia.");
        }

        inscripcion.agregarNota(nota); // Lanza error hacia la vista si no cumple 0-10 o tope de 5
        inscripcionDAO.guardar(obtenerTodasLasInscripciones()); // Actualiza archivo
    }

    // ==========================================
    // 3. MÉTODOS DE SALIDA (Para la Vista)
    // ==========================================

    public Object[][] obtenerDatosInscripciones(String legajo) {
        Estudiante e = buscarEstudiantePorLegajo(legajo);
        
        if (e == null || e.getMaterias() == null) {
            return new Object[0][4]; 
        }

        ArrayList<InscripcionMateria> lista = e.getMaterias();
        Object[][] matrizDatos = new Object[lista.size()][4];

        for (int i = 0; i < lista.size(); i++) {
            InscripcionMateria ins = lista.get(i);
            
            // Cálculo seguro de porcentaje de asistencia
            double porcAsistencia = 0.0;
            // Para obtener el porcentaje, la vista necesitará un cálculo si no existe el getter
            // Asumimos que usa getCondicion, getPromedio de Evaluable
            matrizDatos[i][0] = ins.getMateria().getNombre();
            matrizDatos[i][1] = ins.getCondicion();       
            matrizDatos[i][2] = ins.getPromedio(); // Reemplazá si tenés un método explícito para % asistencia
            matrizDatos[i][3] = ins.estaAprobada() ? "Aprobada" : "No Aprobada";        
        }

        return matrizDatos;
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    private Estudiante buscarEstudiantePorLegajo(String legajo) {
        for (Estudiante e : estudiantes) {
            if (e.getLegajo().equalsIgnoreCase(legajo)) return e;
        }
        return null;
    }

    private Materia buscarMateriaPorCodigo(String codigo) {
        for (Materia m : materias) {
            if (m.getCodigo().equalsIgnoreCase(codigo)) return m;
        }
        return null;
    }

    private InscripcionMateria buscarInscripcion(String legajo, String codigoMateria) {
        Estudiante e = buscarEstudiantePorLegajo(legajo);
        if (e != null && e.getMaterias() != null) {
            for (InscripcionMateria ins : e.getMaterias()) {
                if (ins.getMateria().getCodigo().equalsIgnoreCase(codigoMateria)) {
                    return ins;
                }
            }
        }
        return null;
    }
    
    // Extrae todas las inscripciones del sistema para el DAO
    private ArrayList<InscripcionMateria> obtenerTodasLasInscripciones() {
        ArrayList<InscripcionMateria> todas = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            if (e.getMaterias() != null) {
                todas.addAll(e.getMaterias());
            }
        }
        return todas;
    }

    public ArrayList<Materia> getMaterias() { return materias; }
    public ArrayList<Estudiante> getEstudiantes() { return estudiantes; }
    
}
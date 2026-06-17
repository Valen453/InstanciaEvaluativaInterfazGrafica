/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.util.ArrayList;
import modelo.Materia;
import modelo.Estudiante;
import modelo.InscripcionMateria;


/**
 *
 * @author Valen453
 */
public class ControladorAcademico {

    private ArrayList<Materia> materias;
    private ArrayList<Estudiante> estudiantes;

    public ControladorAcademico() {
        this.materias = new ArrayList<>();
        this.estudiantes = new ArrayList<>();
        // Al iniciar, carga los datos si existen:
    }

    // ==========================================
    // 1. MÉTODOS DE ENTRADA
    // ==========================================

    /**
     * Registra una nueva Materia original.
     */
    public void registrarMateria(String codigo, String nombre, int cuatrimestre, int anio) {
        if (codigo == null || codigo.length() < 3 || codigo.length() > 10) {
            throw new IllegalArgumentException("El código debe tener entre 3 y 10 caracteres.");
        }
        if (cuatrimestre < 1 || cuatrimestre > 2) {
            throw new IllegalArgumentException("El cuatrimestre debe ser 1 o 2.");
        }
        
        // Validación de código
        for (Materia m : materias) {
            if (m.getCodigo().equalsIgnoreCase(codigo)) {
                throw new IllegalArgumentException("Ya existe una materia con el código: " + codigo);
            }
        }

        Materia nuevaMateria = new Materia(nombre, codigo, cuatrimestre, anio);
        materias.add(nuevaMateria);
    }

    /**
     * Registra un nuevo Estudiante (hereda de PersonaAcademica).
     */
    public void registrarEstudiante(String nombre, String legajo, String carrera, int anioIngreso) {
        if (legajo == null || legajo.trim().isEmpty()) {
            throw new IllegalArgumentException("El legajo no puede estar vacío.");
        }
        
        // Validar legajo
        for (Estudiante e : estudiantes) {
            if (e.getLegajo().equalsIgnoreCase(legajo)) {
                throw new IllegalArgumentException("Ya existe un estudiante con el legajo: " + legajo);
            }
        }

        Estudiante nuevoEstudiante = new Estudiante(nombre, legajo, carrera, anioIngreso);
        estudiantes.add(nuevoEstudiante);

        // Guardado persistente
    }

    /**
     * Busca el estudiante y la materia en memoria. Si existen, invoca inscribirse(Materia m).
     */
    public void inscribirEstudiante(String legajo, String codigoMateria) {
        Estudiante estudiante = buscarEstudiantePorLegajo(legajo);
        Materia materia = buscarMateriaPorCodigo(codigoMateria);

        if (estudiante == null) {
            throw new IllegalArgumentException("No se encontró ningún estudiante con el legajo: " + legajo);
        }
        if (materia == null) {
            throw new IllegalArgumentException("No se encontró ninguna materia con el código: " + codigoMateria);
        }

        // Llama al método interno de estudiantye
        estudiante.inscribirse(materia);

    }

    // ==========================================
    // 2. MÉTODOS DE CARGA OPERATIVA
    // ==========================================

    /**
     * Busca la inscripción del alumno y registra la asistencia del día.
     */
    public void registrarAsistencia(String legajo, String codigoMateria, boolean presente) {
        InscripcionMateria inscripcion = buscarInscripcion(legajo, codigoMateria);
        
        if (inscripcion == null) {
            throw new IllegalArgumentException("El estudiante no está inscripto en esta materia.");
        }

        // Llama a la lógica existente inscripcion materia
        inscripcion.registrarAsistencia(presente);

        // Guardado persistente automático tras la modificación
        // academicoDAO.guardarEstudiantes(estudiantes);
    }

    public void registrarNota(String legajo, String codigoMateria, double nota) {
        InscripcionMateria inscripcion = buscarInscripcion(legajo, codigoMateria);
        
        if (inscripcion == null) {
            throw new IllegalArgumentException("El estudiante no está inscripto en esta materia.");
        }

        inscripcion.agregarNota(nota);
    }

    // ==========================================
    // 3. MÉTODOS DE SALIDA
    // ==========================================

    /**
     * Recorre las inscripciones del estudiante y ejecuta los métodos de Evaluable y Consultable.
     * Retorna una matriz pura para que la Vista arme el DefaultTableModel.
     */
    
    public Object[][] obtenerDatosInscripciones(String legajo) {
        Estudiante e = buscarEstudiantePorLegajo(legajo);
        if (e == null || e.getMateriasCursando() == null) {
            return new Object[0][4]; // Retorna matriz vacía si no existe o no tiene materias
        }

        ArrayList<InscripcionMateria> lista = e.getMateriasCursando();
        Object[][] matrizDatos = new Object[lista.size()][4];

        for (int i = 0; i < lista.size(); i++) {
            InscripcionMateria ins = lista.get(i);
            
            // Llenamos las columnas requeridas: Nombre, Condición, Asistencia %, Promedio
            matrizDatos[i][0] = ins.getMateria().getNombre();
            matrizDatos[i][1] = ins.getCondicion();    
            matrizDatos[i][2] = ins.getPorcentajeAsistencia() + "%"; 
            matrizDatos[i][3] = ins.getPromedio();    
        }

        return matrizDatos;
    }

    // ==========================================
    // MÉTODOS AUXILIARES DE BÚSQUEDA INTERNA
    // ==========================================

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

    private InscripcionMateria buscarInscripcion(String legajo, String codigoMateria) {
        Estudiante e = buscarEstudiantePorLegajo(legajo);
        if (e != null && e.getMateriasCursando() != null) {
            for (InscripcionMateria ins : e.getMateriasCursando()) {
                if (ins.getMateria().getCodigo().equalsIgnoreCase(codigoMateria)) {
                    return ins;
                }
            }
        }
        return null;
    }

    // Getters operativos para la Vista si requiere llenar ComboBoxes o listados directos
    public ArrayList<Materia> getMaterias() { return materias; }
    public ArrayList<Estudiante> getEstudiantes() { return estudiantes; }
}

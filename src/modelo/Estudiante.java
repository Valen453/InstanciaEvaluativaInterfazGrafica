/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author baldo
 */
import java.util.ArrayList;
import java.util.List;

public class Estudiante extends PersonaAcademica implements Consultable {

    private String carrera;
    private int anioIngreso;
    private ArrayList<InscripcionMateria> materias;

    public Estudiante(String nombre, String legajo, String carrera, int anioIngreso) {
        super(nombre, legajo);
        this.carrera = carrera;
        this.anioIngreso = anioIngreso;
        this.materias = new ArrayList<>();
    }

    @Override
    public void mostrarResumen() {
        System.out.println("--- PERFIL DEL ESTUDIANTE ---");
        // getters de la clase madre
        System.out.println("Nombre: " + getNombre());
        System.out.println("Legajo: " + getLegajo());
        System.out.println("Carrera: " + carrera);
        System.out.println("Año de Ingreso: " + anioIngreso);
        System.out.println("Materias Inscriptas: " + materias.size());
    }

    public void inscribirse(Materia m) {
        //  validar si ya está inscripto (requisito no duplicados)

        for (InscripcionMateria insc : materias) {
            if (insc.getMateria().getCodigo().equals(m.getCodigo())) {
                System.out.println("Error: Ya se encuentra inscripto en la materia: " + m.getNombre());
                return;
            }
        }

        // si pasa la validacion, se crea nueva inscripción
        InscripcionMateria nuevaInscripcion = new InscripcionMateria(m);

        // se agrega a la lista  
        this.materias.add(nuevaInscripcion);

        System.out.println("Inscripción exitosa a: " + m.getNombre());
    }

    public double getPromedioGeneral() {
        if (materias.isEmpty()) {   // si no tiene materias el promedio es 0
            return 0.0;
        }

        double sumaDePromedios = 0;

        for (InscripcionMateria insc : materias) {
            sumaDePromedios += insc.getPromedio();
        }

        return sumaDePromedios / materias.size();  // promedio final 
    }

    public InscripcionMateria getInscripcion(String codigoMateria) {        // Retorna la inscripcion o null si no la encuentra
        for (InscripcionMateria insc : materias) {
            if (insc.getMateria().getCodigo().equals(codigoMateria)) {
                return insc;
            }
        }
        return null;
    }

    public void darDeBaja(String codigoMateria) {    // elimina la inscripcion si existe
        InscripcionMateria aEliminar = getInscripcion(codigoMateria);
        
        if (aEliminar != null) {
            materias.remove(aEliminar);
            System.out.println("Materia con código " + codigoMateria + " dada de baja.");
        } else {
            System.out.println("No se encontró la materia con ese código.");
        }
    }
    
    public ArrayList<InscripcionMateria> getMateriasCriticas() {
        ArrayList<InscripcionMateria> criticas = new ArrayList<>();

        for (InscripcionMateria insc : materias) {
            double asistencia = insc.getPorcentajeAsistencia();
            if (asistencia >= 75 && asistencia <= 85) {
                criticas.add(insc);
            }
        }
        return criticas;
    }
    
    public String getCarrera() { 
        return carrera; 
    }

    public int getAnioIngreso() { 
        return anioIngreso; 
    }
    
        // --- SOBRECARGA ---
    
    // busqueda por CÓDIGO ----> getInscripcion 
    public InscripcionMateria buscarMateria(String codigo) {
        for (InscripcionMateria insc : materias) {
            if (insc.getMateria().getCodigo().equalsIgnoreCase(codigo)) {
                return insc;
            }
        }
        return null;
    }

    // busqueda por NOMBRE 
    // uso de ArrayList porque puede haber materias con nombres similares 
    public ArrayList<InscripcionMateria> buscarMateria(int cuatrimestre) {
        ArrayList<InscripcionMateria> resultados = new ArrayList<>();
        for (InscripcionMateria insc : materias) {
            if (insc.getMateria().getCuatrimestre() == cuatrimestre) {
                resultados.add(insc);
            }
        }
        return resultados;
    }
    
    // polimorfismo
    public static void generarReporteConsultable(List<Consultable> lista) {
        System.out.println("======= REPORTE DETALLADO =======");
        
        for (Consultable item : lista) {
               item.mostrarResumen(); 
            System.out.println("---------------------------------");
        }
    }
    
    public String toTexto() {
        return getNombre() + ";" + getLegajo() + ";" + carrera + ";" + anioIngreso;
    }
    
    public static Estudiante fromTexto(String linea) {
        String[] partes = linea.split(";");
        return new Estudiante(partes[0], partes[1], partes[2], Integer.parseInt(partes[3]));
    }

    public ArrayList<InscripcionMateria> getMaterias() {
        return this.materias;
    }
}

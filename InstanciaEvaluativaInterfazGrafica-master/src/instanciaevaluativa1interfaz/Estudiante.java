/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instanciaevaluativa1interfaz;
import java.util.ArrayList; // 1. ¡SIEMPRE va el import arriba de todo!
/**
 *
 * @author Valen453
 */
public class Estudiante extends PersonaAcademica implements Consultable{
    private String carrera;
    private int anioIngreso;
    private ArrayList<InscripcionMateria> materias;
    
    
    public Estudiante(String nombre, String legajo, String carrera, int anioIngreso) {
        super(nombre, legajo);
        this.carrera = carrera;
        this.anioIngreso = anioIngreso;
        this.materias = new ArrayList<>();
    }
    
    
    
 // Metodos
   
    //Metodo inscribirse
   public void inscribirse(Materia m) {
    if (m == null) {
        System.out.println("Error: Materia inválida.");
        return;
    }
    
    // Se aseguyra que no se duplique
    if (getInscripcion(m.getCodigo()) != null) {
        System.out.println("Error: El estudiante ya está inscripto en la materia con código " + m.getCodigo());
        return;
    }
    
    // Se agrega en caso de no estar duplicado
    this.materias.add(new InscripcionMateria(m));
    System.out.println("Inscripción exitosa a: " + m.getNombre());
}

public InscripcionMateria getInscripcion(String codigoMateria) {
    // Recorremos la lista de inscripciones del estudiante
    for (InscripcionMateria mat : this.materias) {
        // Si el código de la materia coincide
        if (mat.getMateria().getCodigo().equals(codigoMateria)) {
            return mat; // Devolvemos la inscripción encontrada
        }
    }
    return null; // Si termina el bucle y no la encontró, devuelve null
}

public void darDeBaja(String codigoMateria) {
    // Buscar inscripcion por codigo
    InscripcionMateria encontrada = getInscripcion(codigoMateria);
    
        // Eliminar del array
    if (encontrada != null) {
            this.materias.remove(encontrada);
            System.out.println("Se dio de baja la materia con código: " + codigoMateria);
        } 
    else {
            // Si no esta ionscripto lo avisa
            System.out.println("Error: El estudiante no está inscripto en la materia: " + codigoMateria);
        }
    }

    public double getPromedioGeneral() { //
            if (this.materias.isEmpty()) {
                //Devuelve 0 en caso de que no haya notas
                return 0.0;
            }

            double sumaPromedios = 0.0;

            for (InscripcionMateria ins : this.materias) {
                sumaPromedios += ins.getPromedio(); // Suma el promedio individual de cada cursada
            }

            return sumaPromedios / this.materias.size(); //
        }
    
    public ArrayList<InscripcionMateria> getMateriasCriticas() { //
        ArrayList<InscripcionMateria> criticas = new ArrayList<>(); //
        
        for (InscripcionMateria ins : this.materias) {
            double asistencia = ins.getPorcentajeAsistencia();

            // Caso en el que se agregan las materias a criticas
            if (asistencia >= 75.0 && asistencia <= 85.0) {
                criticas.add(ins); //
            }
        }
        return criticas; //
    }
    
    
    //Sobrecarga de metodos, 
    public InscripcionMateria buscarMateria(String codigo) {
        for (InscripcionMateria ins : this.materias) {
            // Compara el codigo de la materia
            if (ins.getMateria().getCodigo().equalsIgnoreCase(codigo)) {
                return ins; 
            }
        }
        return null;
    }
    
    public ArrayList<InscripcionMateria> buscarMateria(int cuatrimestre) {
        ArrayList<InscripcionMateria> filtradas = new ArrayList<>();
        for (InscripcionMateria ins : this.materias) {
            //compara e cuatrimestre y lo agrega a un array
            if (ins.getMateria().getCuatrimestre() == cuatrimestre) {
                filtradas.add(ins);
            }
        }
        return filtradas;
    }
    
    //Sobreesctibe resumen de consultable
    @Override
    public void mostrarResumen() {
            System.out.println("\n=========================================");
            System.out.println("         PERFIL DEL ESTUDIANTE          ");
            System.out.println("=========================================");
            System.out.println("Legajo: " + getLegajo());
            System.out.println("Estudiante: " + getNombre());
            System.out.println("Carrera: " + this.carrera);
            System.out.println("Año de Ingreso: " + this.anioIngreso);
            System.out.println("Materias Inscriptas: " + this.materias.size());
            System.out.printf("Promedio General: " + getPromedioGeneral());
            System.out.println("=========================================");
    }
}

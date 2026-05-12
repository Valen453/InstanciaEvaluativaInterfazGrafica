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
public class Estudiante extends PersonaAcademica{
    private String carrera;
    private int anioIngreso;
    private ArrayList<Materia> materias;
    
    
    public Estudiante(String nombre, String legajo, String carrera, int anioIngreso) {
        super(nombre, legajo);
        this.carrera = carrera;
        this.anioIngreso = anioIngreso;
        this.materias = new ArrayList<>();
    }
    
public void inscribirse(Materia m) {
    if (m == null) {
        System.out.println("Error: Materia inválida.");
        return;
    }
    
    // AGREGÁ ESTA LÍNEA para guardar la materia en el ArrayList:
    this.materias.add(m);
    System.out.println("Inscripción exitosa a: " + m.getNombre());
}

    @Override
    public void mostrarResumen() {
        System.out.println("Perfil Estudiantil:");
        System.out.println("Legajo: " + getLegajo());
        System.out.println("Estudiante: " + getNombre());
        System.out.println("Carrera: " + this.carrera);
    }
}

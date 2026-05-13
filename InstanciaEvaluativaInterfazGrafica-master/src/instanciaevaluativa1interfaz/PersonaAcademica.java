/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instanciaevaluativa1interfaz;

/**
 *
 * @author Valen453
 */
public abstract class PersonaAcademica {
    private String nombre;
    private String legajo;
    
    PersonaAcademica(String nombre,String legajo) 
    {
        this.nombre = nombre;
        this.legajo = legajo;

    }
    
    
    // Getters con validación
    
    public String getNombre() {
        // Validación: El nombre no puede ser null ni estar vacío
        if (this.nombre == null || this.nombre.trim().isEmpty()) {
            return "Nombre no especificado";
        }
        return this.nombre;
    }

    public String getLegajo() {
        // Validación: El legajo no puede ser null
        if (this.legajo == null) {
            return "Legajo no asignado";
        }
        return this.legajo;
    }

    // Funcion abstracta
    public abstract void mostrarResumen();
    
}

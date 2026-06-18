/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author baldo
 */
public  abstract class PersonaAcademica {
    private String nombre;
    private String legajo;
    
    public PersonaAcademica(String nombre, String legajo) {
        this.nombre = nombre;
        this.legajo = legajo;
    }
    
    public abstract void mostrarResumen();
    
    public String getNombre() {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Nombre no válido";
        }
        return nombre;
    }

    public String getLegajo() {
        if (legajo == null) {
            return "Legajo no asignado";
        }
        return legajo;
    }
}

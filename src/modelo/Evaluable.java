/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo;

/**
 *
 * @author baldo
 */
public interface Evaluable {
    String getCondicion();
    double getPromedio();
    boolean estaAprobada();
    
    default void mostrar_EstadoAcademico() {
        System.out.println("Condicion: " + getCondicion());
        System.out.println("Promedio: " + getPromedio());
    }
}

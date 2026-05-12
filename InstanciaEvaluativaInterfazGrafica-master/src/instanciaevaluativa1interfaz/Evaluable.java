/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package instanciaevaluativa1interfaz;

/**
 *
 * @author Valen453
 */
public interface Evaluable {
    
    String getCondicion();
    double getPromedio();
    boolean estaAprobada();
    
    default void mostrarEstadoAcademico() {
        System.out.println("Condición: " + getCondicion());
        System.out.println("Promedio:" + getPromedio());
        if (estaAprobada()) {
            System.out.println("Usted esta APROBADO");
        } 
        else {
            System.out.println("Usted esta DESAPROBADO");
        }
    }
}

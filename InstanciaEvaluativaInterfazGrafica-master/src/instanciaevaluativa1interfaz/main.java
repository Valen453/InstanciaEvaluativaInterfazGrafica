/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package instanciaevaluativa1interfaz;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Valen453
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Crear estudiante
        Estudiante estudiante = new Estudiante("Juan", "L123456", 
                "Analista de Sistemas", 2025);
        
        Materia m1 = new Materia("Programación Orientada a Objetos", "POO-102", 1, 2026);
        Materia m2 = new Materia("Programacion 2", "LP1-205", 2, 2026);
        Materia m3 = new Materia("Arquitectura de Computadoras", "ACO-501", 1, 2026);
        Materia m4 = new Materia("Interfaz Grafica", "IG-302", 1, 2026);
                    // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        // Usamos string para evitar que el programa falle en caso de que se ponga una letra    
        String opcion;
        //Menu principal de opciones
        System.out.println("""
                        ===============================
                                   Autogestion
                        ===============================
                        1- Ver perfil del estudiante
                        2- Gestion de materias
                        3- Registrar asistencia
                        4- Registrar calificacion
                        5- Ver reportes
                        0- Salir
                        """);
            do {
                System.out.print("\nElegi a que espacio queres acceder: ");
                opcion = sc.nextLine();

                // Usamos un switch para procesar la opción seleccionada
                switch (opcion) {
                    case "1" -> estudiante.mostrarResumen();
                    case "2" -> {
                        //SubMenu Materias
                        // Usamos string para evitar que el programa falle  
                        String opcionsm;
                        //gestion de materias
                        System.out.println("""
                            ===============================
                                 Gestion de materias
                            ===============================
                            1- Inscribirse a una materia
                            2- Darse de baja de una materia
                            3- Listar las materias inscriptas
                            4- Buscar materia
                            0- Salir
                            """);
                        do {
                            System.out.print("\nElegi a que espacio queres acceder: ");
                            opcionsm = sc.nextLine();

                            // Usamos un switch para procesar la opción seleccionada
                            // Gestion de materias
                            switch (opcionsm) {
                                case "1" -> //Inscribirse
                                    System.out.println("Elegiste la opción 1.");
                                case "2" -> //Darse de baja
                                    System.out.println("");
                                case "3" -> //Listar materias
                                    System.out.println("Llegaste al límite: Opción 3.");
                                case "4" -> //Buscar materias
                                    System.out.println("Llegaste al límite: Opción 3.");
                                case "0" -> System.out.println("Saliendo del sistema...");
                                default -> System.out.println("Opción inválida, reintentá.");
                            }
                        
                        // El bucle se repite mientras la opción NO sea "0"
                        } while (opcionsm.compareTo("0") != 0);
                        // Final subMenu
                }
                    case "3" -> System.out.println("Llegaste al límite: Opción 3.");
                    case "4" -> System.out.println("Llegaste al límite: Opción 3.");
                    case "5" -> System.out.println("Opción especial (4 o 5).");
                    case "0" -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opción inválida, reintentá.");
                }

            // El bucle se repite mientras la opción NO sea "0"
            } while (opcion.compareTo("0") != 0);
    }
    
    
}



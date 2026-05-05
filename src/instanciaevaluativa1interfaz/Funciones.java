/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package instanciaevaluativa1interfaz;
import java.util.Scanner;
/**
 *
 * @author Valen453
 */
public class Funciones {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }
    
    public static void menuPrincipal() {
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
                    case "1":
                        System.out.println("Elegiste la opción 1.");
                        break;
                    case "2":
                        subMenu();
                        break;
                    case "3":
                        System.out.println("Llegaste al límite: Opción 3.");
                        break;
                    case "4":
                        System.out.println("Llegaste al límite: Opción 3.");
                        break;
                    case "5":
                        System.out.println("Opción especial (4 o 5).");
                        break;
                    case "0":
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción inválida, reintentá.");
                        break;
                }

            // El bucle se repite mientras la opción NO sea "0"
            } while (opcion.compareTo("0") != 0);
    }
    
    public static void subMenu(){
            Scanner sc = new Scanner(System.in);
            // Usamos string para evitar que el programa falle en caso de que se ponga una letra    
            String opcion;
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
            opcion = sc.nextLine();

            // Usamos un switch para procesar la opción seleccionada
            // Gestion de materias
            switch (opcion) {
                case "1":
                    //Inscribirse
                    System.out.println("Elegiste la opción 1.");
                    break;
                case "2":
                    //Darse de baja
                    System.out.println("");
                    break;
                case "3":
                    //Listar materias
                    System.out.println("Llegaste al límite: Opción 3.");
                    break;
                case "4":
                    //Buscar materias
                    System.out.println("Llegaste al límite: Opción 3.");
                    break;
                case "0":
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida, reintentá.");
                    break;
            }

        // El bucle se repite mientras la opción NO sea "0"
        } while (opcion.compareTo("0") != 0);
        // Final subMenu
    }
    
}

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
        
        ArrayList<Materia> poolMaterias = new ArrayList<>();
        poolMaterias.add(new Materia("Programación Orientada a Objetos", "POO-102", 1, 2026));
        poolMaterias.add(new Materia("Programacion 2", "LP1-205", 2, 2026));
        poolMaterias.add(new Materia("Arquitectura de Computadoras", "ACO-501", 1, 2026));
        poolMaterias.add(new Materia("Interfaz Grafica", "IG-302", 1, 2026));
                    // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        // Usamos string para evitar que el programa falle en caso de que se ponga una letra    
        String opcion;

            do {
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
                System.out.print("\nElegi a que espacio queres acceder: ");
                opcion = sc.nextLine();

                // Usamos un switch para procesar la opción seleccionada
                switch (opcion) {
                    case "1" -> estudiante.mostrarResumen();
                    case "2" -> {
                        //SubMenu Materias
                        // Usamos string para evitar que el programa falle  
                        String opcionsm;

                        do {
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
                            System.out.print("\nElegi a que espacio queres acceder: ");
                            opcionsm = sc.nextLine();

                            // Usamos un switch para procesar la opción seleccionada
                            // Gestion de materias
                            switch (opcionsm) {
                                case "1" -> {
                                    System.out.println("\nMaterias Disponibles:");
                                    for (Materia m : poolMaterias) {
                                        // Muestra la informacion de la materia a inscribirse
                                        System.out.println("- [" + m.getCodigo() + "] " + m.getNombre());
                                    }
                                    System.out.print("Ingrese el código de la materia para inscribirse: ");
                                    String codIns = sc.nextLine();
                                    Materia mseleccionada = null;
                                    for (Materia m : poolMaterias) {
                                        if (m.getCodigo().equalsIgnoreCase(codIns)) {
                                            mseleccionada = m;
                                            break;
                                        }
                                    }
                                    if (mseleccionada != null) {
                                        estudiante.inscribirse(mseleccionada);
                                    } else {
                                        System.out.println("Código inválido. Materia no encontrada.");
                                    }
                                }
                                case "2" -> //Darse de baja
                                {
                                    for (Materia m : poolMaterias) {
                                    // Muestra la informacion de la materia
                                    System.out.println("- [" + m.getCodigo() + "] " + m.getNombre());
                                    }
                                    System.out.print("Ingrese el código de la materia a dar de baja: ");
                                    String codBaja = sc.nextLine();
                                    estudiante.darDeBaja(codBaja);
                                }
                                case "3" -> {
                                List<Consultable> listaPolimorfica = new ArrayList<>();
                                listaPolimorfica.add(estudiante);
                                for (InscripcionMateria im : estudiante.getMateriasCursando()) {
                                    listaPolimorfica.add(im.getMateria());
                                }
                                // Recorrido polimórfico puro
                                for (Consultable c : listaPolimorfica) {
                                    c.mostrarResumen();
                                }
                            }
                                case "4" -> {
                                System.out.print("Ingrese el código de la materia a buscar: ");
                                String codBusq = sc.nextLine();
                                // Uso de la funcion buscarMateria por codigo
                                InscripcionMateria insc = estudiante.buscarMateria(codBusq);
                                if (insc != null) {
                                    System.out.println("¡Materia Encontrada!");
                                    insc.getMateria().mostrarResumen();
                                    insc.mostrarEstadoAcademico();
                                } else {
                                    System.out.println("No se registra cursada para ese código.");
                                }
                            }
                                case "0" -> System.out.println("Saliendo del sistema...");
                                default -> System.out.println("Opción inválida, reintentá.");
                            }
                        
                        // El bucle se repite mientras la opción NO sea "0"
                        } while (opcionsm.compareTo("0") != 0);
                        // Final subMenu
                }
                    case "3" -> { // Registrar Asistencia
                                for (Materia m : poolMaterias) {
                                    // Muestra la informacion de la materia a inscribirse
                                    System.out.println("- [" + m.getCodigo() + "] " + m.getNombre());
                                    }
                                System.out.print("\nIngrese el código de la materia para marcar la asistencia: ");
                                String codMat = sc.nextLine();

                        InscripcionMateria ins = estudiante.getInscripcion(codMat);
                        if (ins != null) {
                            System.out.print("¿El estudiante asistió a la clase hoy? (S/N): ");
                            boolean presente = sc.nextLine().equalsIgnoreCase("S");

                            ins.registrarAsistencia(presente);
                            System.out.println("¡Asistencia computada!");

                            // Enviar alertas en caso de haber ingresado
                            double porc = ins.getPorcentajeAsistencia();
                            System.out.printf("Porcentaje actual de asistencia: %.1f%%\n", porc);

                            if (porc < 75.0) {
                                System.out.println("La asistencia se encuentra por debajo del 75%.");
                                System.out.println("El alumno ha quedado LIBRE en esta asignatura");
                            } else if (porc < 80.0) {
                                System.out.println("La asistencia cayó por debajo del 80%.");
                                System.out.println("La materia entró en ZONA DE RIESGO de perder regularidad.");
                            }
                        } else {
                            System.out.println("Error: El estudiante no se encuentra cursando la materia especificada.");
                        }
                    }
                    case "4" -> { // Registrar Calificación
                        System.out.print("Ingrese el código de la materia para cargar la nota: ");
                        String codMat = sc.nextLine();

                        InscripcionMateria ins = estudiante.getInscripcion(codMat);
                        if (ins != null) {
                            System.out.print("Ingrese la calificación obtenida (1.00 a 10.00): ");
                            double nota = Double.parseDouble(sc.nextLine());

                            if (nota >= 1.0 && nota <= 10.0) {
                                ins.agregarNota(nota);
                                System.out.println("Nota guardada correctamente en el legajo virtual.");
                            } 
                            else {
                                System.out.println("Error: Escala de notas no válida (Debe ser entre 1 y 10).");
                            }
                        } else {
                            System.out.println("Error: El alumno no registra inscripción en dicha materia.");
                        }
                    }
                    case "5" -> {
                        // Llamada directa a la función unificada pasando el estudiante cargado
                        generarReporteUnicoConsolidado(estudiante);
                    }
                    case "0" -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opción inválida, reintentá.");
                }

            // El bucle se repite mientras la opción NO sea "0"
            } while (opcion.compareTo("0") != 0);
    }
    
    public static void generarReporteUnicoConsolidado(Estudiante estudiante) {
    // 1. Cabecera e Información del Usuario cargado
    System.out.println("\n======================================================================");
    System.out.println("                 SISTEMA DE AUTOGESTIÓN ESTUDIANTIL");
    System.out.println("              REPORTE GENERAL DE SITUACIÓN ACADÉMICA");
    System.out.println("======================================================================");
    
    System.out.println("DATOS DEL ESTUDIANTE:");
    System.out.println("> Nombre Completo: " + estudiante.getNombre());
    System.out.println("> Legajo Virtual:  " + estudiante.getLegajo());
    System.out.println("----------------------------------------------------------------------");

    // Validamos si el alumno tiene materias inscriptas para evitar errores
    if (estudiante.getMateriasCursando().isEmpty()) {
        System.out.println("El estudiante no registra inscripciones a ninguna asignatura actualmente.");
        System.out.println("======================================================================");
        return;
    }

    // Inicializamos contadores para las métricas finales
    int cantidadRegulares = 0;
    int cantidadEnRiesgo = 0;
    int cantidadLibres = 0;

    System.out.println("DETALLE DE ASIGNATURAS INSCRIPTAS:");
    System.out.println("----------------------------------------------------------------------");

    // 2. Recorrido de las materias con toda la info técnica y de negocio
    for (InscripcionMateria ins : estudiante.getMateriasCursando()) {
        Materia m = ins.getMateria();
        double asistencia = ins.getPorcentajeAsistencia();
        double promedioMateria = ins.getPromedio();
        
        // Determinar el Estado Académico del alumno en esta materia específica
        String estadoFinal = "En curso";
        if (ins.estaAprobada()) {
            estadoFinal = "APROBADA ✔";
        } else if (ins.getCondicion().equals("Libre")) {
            estadoFinal = "LIBRE 🚨";
        }

        // Clasificación para las métricas globales (Consigna Punto 5.1)
        if (ins.getCondicion().equals("Libre")) {
            cantidadLibres++;
        } else if (asistencia >= 75.0 && asistencia <= 85.0) {
            cantidadEnRiesgo++;
        } else {
            cantidadRegulares++;
        }

        // Imprimir toda la información estructurada de la materia actual
        System.out.printf("• Asignatura: %s [%s]\n", m.getNombre(), m.getCodigo());
        System.out.printf("  > Planificación:   Cuatrimestre %d | Año %d\n", m.getCuatrimestre(), m.getAnio());
        System.out.printf("  > Clases Totales:  %d | Clases Asistidas: %d\n", ins.getTotalClases(), ins.getClasesAsistidas());
        System.out.printf("  > Evaluaciones:    %s\n", ins.getNotas().isEmpty() ? "Sin calificaciones cargadas" : ins.getNotas().toString());
        System.out.printf("  > Promedio Parcial:%.2f\n", promedioMateria);
        System.out.printf("  > Cursada actual:  %s\n", ins.getCondicion());
        System.out.printf("  > ESTADO FINAL:    %s\n", estadoFinal);
        
    }

    // 3. Bloque Final: Métricas Consolidadas y Cierre del Reporte
    System.out.println("MÉTRICAS DE RENDIMIENTO GLOBAL:");
    System.out.println("----------------------------------------------------------------------");
    // Llama a tu función recursiva o directa para obtener el promedio general
    System.out.printf("> Promedio General del Estudiante: %.2f\n", estudiante.getPromedioGeneral());
    System.out.println("> Resumen de Cursadas:");
    System.out.println("  - Materias Regulares / Estables: " + cantidadRegulares);
    System.out.println("  - Materias en Zona de Riesgo:    " + cantidadEnRiesgo + " (Asistencia entre 75% y 85%)");
    System.out.println("  - Materias en Condición Libre:   " + cantidadLibres);
    System.out.println("======================================================================\n");
}
}



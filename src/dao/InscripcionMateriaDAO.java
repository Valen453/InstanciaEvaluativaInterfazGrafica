/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author baldo
 */
import java.io.*;
import java.util.ArrayList;
import modelo.InscripcionMateria;
import modelo.Materia;

public class InscripcionMateriaDAO {
    // nombre del archivo donde se persisten los datos (por defecto, global)
    private static final String ARCHIVO_DEFAULT = "inscripciones.txt";

    // archivo efectivo que usa esta instancia del DAO
    private final String archivo;

    // Constructor por defecto: mantiene el comportamiento original (un solo archivo global)
    public InscripcionMateriaDAO() {
        this.archivo = ARCHIVO_DEFAULT;
    }

    // Constructor con archivo configurable: permite, por ejemplo, un
    // archivo de inscripciones por estudiante (inscripciones_<legajo>.txt)
    public InscripcionMateriaDAO(String nombreArchivo) {
        this.archivo = nombreArchivo;
    }

    // pasar  lista de materias para q sepa cual es cada una
    public ArrayList<InscripcionMateria> cargar(ArrayList<Materia> materiasDisponibles) {
        ArrayList<InscripcionMateria> lista = new ArrayList<>();
        
        File archivoFile = new File(this.archivo);
        if (!archivoFile.exists()) return lista; // si no existe, lista vacía

        try (BufferedReader br = new BufferedReader(new FileReader(archivoFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                  String codigoMateria = linea.split(",")[0];
                    Materia materiaEncontrada = null;
                    for (Materia m : materiasDisponibles) {
                    if (m.getCodigo().equals(codigoMateria)) {
                    materiaEncontrada = m;
                    break;
    }
}
if (materiaEncontrada != null) {
    lista.add(InscripcionMateria.fromTexto(linea, materiaEncontrada));
}
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer inscripciones: " + e.getMessage());
        }
        return lista;
    }
    
    // GUARDAR EN EL ARCHIVO
    public void guardar(ArrayList<InscripcionMateria> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.archivo))) {
            for (InscripcionMateria im : lista) {
                bw.write(im.toTexto());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar inscripciones: " + e.getMessage());
        }
    }
}


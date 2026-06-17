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
    // nombre del archivo donde se persisten los datos
    private static final String ARCHIVO = "inscripciones.txt";

    // pasar  lista de materias para q sepa cual es cada una
    public ArrayList<InscripcionMateria> cargar(ArrayList<Materia> materiasDisponibles) {
        ArrayList<InscripcionMateria> lista = new ArrayList<>();
        
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return lista; // si no existe, lista vacía

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (InscripcionMateria im : lista) {
                bw.write(im.toTexto());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar inscripciones: " + e.getMessage());
        }
    }
}


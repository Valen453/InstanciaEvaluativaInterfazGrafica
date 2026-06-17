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
import modelo.Estudiante;

public class EstudianteDAO {
        // nombre del archivo donde se persisten los datos
    private static final String ARCHIVO = "estudiantes.txt";

        // MÉTODO PARA LEER EL ARCHIVO
    public ArrayList<Estudiante> cargar() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return lista; // si no existe, lista vacía

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lista.add(Estudiante.fromTexto(linea)); // llama al modelo
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer estudiantes: " + e.getMessage());
        }
        return lista;
    }

        // GUARDAR EN EL ARCHIVO
    public void guardar(ArrayList<Estudiante> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Estudiante e : lista) {
                bw.write(e.toTexto());   // llama al Modelo
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar estudiantes: " + e.getMessage());
        }
    }
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instanciaevaluativa1interfaz;

/**
 *
 * @author MATEO
 */


import java.util.ArrayList;

// Representa la cursada de un estudiante en una materia especifica
// Implementa Evaluable porque el trabajo lo exige para esta clase
public class InscripcionMateria implements Evaluable {

    // Referencia a la materia que se esta cursando
    private Materia materia;
    
    // Contadores para calcular el porcentaje de asistencia
    private int totalClases;
    private int clasesAsistidas;
    
    // Lista de notas, maximo 5
    private ArrayList<Double> notas;

    // Constructor: recibe la materia y arranca todos los contadores en 0
    public InscripcionMateria(Materia materia) {
        this.materia = materia;
        this.totalClases = 0;
        this.clasesAsistidas = 0;
        this.notas = new ArrayList<>();
    }

    public Materia getMateria() { return materia; }

    // Registra una clase: siempre suma al total
    // Solo suma a asistidas si el alumno estuvo presente
    public void registrarAsistencia(boolean presente) {
        totalClases++;
        if (presente) clasesAsistidas++;
    }

    // Agrega una nota validando que este en rango 0-10
    // y que no se superen las 5 notas permitidas
    public void agregarNota(double nota) {
        if (nota < 0 || nota > 10)
            throw new IllegalArgumentException("La nota debe estar entre 0 y 10.");
        if (notas.size() >= 5)
            throw new IllegalStateException("No se pueden agregar más de 5 notas.");
        notas.add(nota);
    }

    // Calcula el porcentaje de asistencia
    // Si no hubo ninguna clase todavia devuelve 100 para no penalizar al alumno
    public double getPorcentajeAsistencia() {
        if (totalClases == 0) return 100.0;
        return (clasesAsistidas * 100.0) / totalClases;
    }

    // Metodo de Evaluable
    // Devuelve "Regular" si la asistencia es 75% o mas, "Libre" si no
    @Override
    public String getCondicion() {
        return getPorcentajeAsistencia() >= 75 ? "Regular" : "Libre";
    }

    // Metodo de Evaluable
    // Calcula el promedio sumando todas las notas y dividiendo por la cantidad
    // Si no hay notas devuelve 0 para evitar division por cero
    @Override
    public double getPromedio() {
        if (notas.isEmpty()) return 0;
        double suma = 0;
        for (double n : notas) suma += n;
        return suma / notas.size();
    }

    // Metodo de Evaluable
    // La materia esta aprobada solo si se cumplen LAS DOS condiciones:
    // promedio >= 6 Y condicion Regular
    @Override
    public boolean estaAprobada() {
        return getPromedio() >= 6 && getCondicion().equals("Regular");
    }

    // Getters de los atributos restantes
    public ArrayList<Double> getNotas() { return notas; }
    public int getTotalClases() { return totalClases; }
    public int getClasesAsistidas() { return clasesAsistidas; }
}

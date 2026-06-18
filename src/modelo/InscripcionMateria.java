/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

// Representa la cursada de un estudiante en una materia especifica
// Implementa Evaluable porque el trabajo lo exige para esta clase
public class InscripcionMateria implements Evaluable {

    private Materia materia;
    // Contadores para calcular el porcentaje de asistencia
    private int totalClases;
    private int clasesAsistidas;
    // Lista de notas, maximo 5
    private ArrayList<Double> notas;

    // Constructor: arranca todos los contadores en 0
    public InscripcionMateria(Materia materia) {
        this.materia = materia;
        this.totalClases = 0;
        this.clasesAsistidas = 0;
        this.notas = new ArrayList<>();
    }

    public Materia getMateria() { return materia; }

    // Siempre suma al total, solo suma a asistidas si el alumno estuvo presente
    public void registrarAsistencia(boolean presente) {
        totalClases++;
        if (presente) clasesAsistidas++;
    }

    // Valida rango 0-10 y que no se superen las 5 notas permitidas
    public void agregarNota(double nota) {
        if (nota < 0 || nota > 10)
            throw new IllegalArgumentException("La nota debe estar entre 0 y 10.");
        if (notas.size() >= 5)
            throw new IllegalStateException("No se pueden agregar más de 5 notas.");
        notas.add(nota);
    }

    // Si no hubo clases devuelve 100 para no penalizar al alumno
    public double getPorcentajeAsistencia() {
        if (totalClases == 0) return 100.0;
        return (clasesAsistidas * 100.0) / totalClases;
    }

    // Metodo de Evaluable: Regular si asistencia >= 75%, Libre si no
    @Override
    public String getCondicion() {
        return getPorcentajeAsistencia() >= 75 ? "Regular" : "Libre";
    }

    // Metodo de Evaluable: devuelve 0 si no hay notas para evitar division por cero
    @Override
    public double getPromedio() {
        if (notas.isEmpty()) return 0;
        double suma = 0;
        for (double n : notas) suma += n;
        return suma / notas.size();
    }

    // Metodo de Evaluable: aprobada solo si promedio >= 6 Y condicion Regular
    @Override
    public boolean estaAprobada() {
        return getPromedio() >= 6 && getCondicion().equals("Regular");
    }

    // Formato: codigoMateria,totalClases,clasesAsistidas,nota1;nota2;nota3
    // Las notas se separan con ; para no confundirse con la coma principal
    public String toTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append(materia.getCodigo()).append(",");
        sb.append(totalClases).append(",");
        sb.append(clasesAsistidas).append(",");
        for (int i = 0; i < notas.size(); i++) {
            sb.append(notas.get(i));
            if (i < notas.size() - 1) sb.append(";");
        }
        return sb.toString();
    }

    // Reconstruye el objeto desde una línea del archivo
    // Necesita la Materia ya cargada para poder armar la inscripcion
    public static InscripcionMateria fromTexto(String linea, Materia materia) {
        String[] partes = linea.split(",");
        InscripcionMateria ins = new InscripcionMateria(materia);
        ins.totalClases = Integer.parseInt(partes[1]);
        ins.clasesAsistidas = Integer.parseInt(partes[2]);
        // Si hay notas las recorre y las agrega una por una
        if (partes.length > 3 && !partes[3].isEmpty()) {
            for (String nota : partes[3].split(";")) {
                ins.notas.add(Double.parseDouble(nota));
            }
        }
        return ins;
    }

    public ArrayList<Double> getNotas() { return notas; }
    public int getTotalClases() { return totalClases; }
    public int getClasesAsistidas() { return clasesAsistidas; }
}

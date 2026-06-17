/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instanciaevaluativa1interfaz;

/**
 *
 * @author MATEO
 */


// Implementa Consultable porque el trabajo lo exige para Materia
public class Materia implements Consultable {

    // Atributos privados - encapsulamiento
    private String nombre;
    private String codigo;
    private int cuatrimestre;
    private int anio;

    // Constructor: recibe los 4 datos y llama a los setters
    // para que las validaciones se ejecuten desde el principio
    public Materia(String nombre, String codigo, int cuatrimestre, int anio) {
        setNombre(nombre);
        setCodigo(codigo);
        setCuatrimestre(cuatrimestre);
        this.anio = anio; // anio no tiene validacion especial
    }

    // Getters y setters de nombre (sin validacion especial)
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Getter y setter de codigo
    // Validacion: el codigo debe tener entre 3 y 10 caracteres
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.length() < 3 || codigo.length() > 10)
            throw new IllegalArgumentException("El código debe tener entre 3 y 10 caracteres.");
        this.codigo = codigo;
    }

    // Getter y setter de cuatrimestre
    // Validacion: solo puede ser 1 o 2
    public int getCuatrimestre() { return cuatrimestre; }
    public void setCuatrimestre(int cuatrimestre) {
        if (cuatrimestre != 1 && cuatrimestre != 2)
            throw new IllegalArgumentException("El cuatrimestre debe ser 1 o 2.");
        this.cuatrimestre = cuatrimestre;
    }

    // Getter y setter de anio (sin validacion especial)
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    // Metodo obligatorio de la interface Consultable
    // Muestra por consola los datos principales de la materia
    @Override
    public void mostrarResumen() {
        System.out.println("Materia: " + nombre + " | Código: " + codigo
                + " | Cuatrimestre: " + cuatrimestre + " | Año: " + anio);
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

// Implementa Consultable porque el trabajo lo exige para Materia
public class Materia implements Consultable {

    // Atributos privados - encapsulamiento
    private String nombre;
    private String codigo;
    private int cuatrimestre;
    private int anio;

    // Constructor: llama a los setters para que las validaciones
    // se ejecuten desde el momento en que se crea el objeto
    public Materia(String nombre, String codigo, int cuatrimestre, int anio) {
        setNombre(nombre);
        setCodigo(codigo);
        setCuatrimestre(cuatrimestre);
        this.anio = anio;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Validacion: el codigo debe tener entre 3 y 10 caracteres
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.length() < 3 || codigo.length() > 10)
            throw new IllegalArgumentException("El código debe tener entre 3 y 10 caracteres.");
        this.codigo = codigo;
    }

    // Validacion: solo puede ser 1 o 2
    public int getCuatrimestre() { return cuatrimestre; }
    public void setCuatrimestre(int cuatrimestre) {
        if (cuatrimestre != 1 && cuatrimestre != 2)
            throw new IllegalArgumentException("El cuatrimestre debe ser 1 o 2.");
        this.cuatrimestre = cuatrimestre;
    }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    // Convierte el objeto a texto separado por comas para guardar en archivo
    public String toTexto() {
        return nombre + "," + codigo + "," + cuatrimestre + "," + anio;
    }

    // Lee una línea del archivo, la separa por comas y reconstruye el objeto
    public static Materia fromTexto(String linea) {
        String[] partes = linea.split(",");
        return new Materia(partes[0], partes[1],
                Integer.parseInt(partes[2]),
                Integer.parseInt(partes[3]));
    }

    // Metodo obligatorio de la interface Consultable
    @Override
    public void mostrarResumen() {
        System.out.println("Materia: " + nombre + " | Código: " + codigo
                + " | Cuatrimestre: " + cuatrimestre + " | Año: " + anio);
    }
}

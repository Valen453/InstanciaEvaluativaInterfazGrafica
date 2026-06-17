/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author baldo
 */
import java.sql.*;
import java.util.ArrayList;
import modelo.Estudiante;

public class EstudianteDAOJDBC {
    
    // MÉTODO CARGAR (SELECT)
    public ArrayList<Estudiante> cargar() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM estudiantes";

        // try-with-resources cierra la conexión automáticamente
        try (Connection cn = ConexionDB.getConexion(); 
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estudiante e = new Estudiante(
                    rs.getString("nombre"),
                    rs.getString("legajo"),
                    rs.getString("carrera"),
                    rs.getInt("anio_ingreso")
                );
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar: " + ex.getMessage());
        }
        return lista;
    }

    // MÉTODO GUARDAR (INSERT)
    public void guardar(Estudiante e) {
        String sql = "INSERT INTO estudiantes (nombre, legajo, carrera, anio_ingreso) VALUES (?, ?, ?, ?)";
        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, e.getNombre()); 
            ps.setString(2, e.getLegajo());
            ps.setString(3, e.getCarrera());
            ps.setInt(4, e.getAnioIngreso());
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println("Error al guardar: " + ex.getMessage());
        }
    }
    
        // MÉTODO ELIMINAR (DELETE)
    public void eliminar(String legajo) {
        String sql = "DELETE FROM estudiantes WHERE legajo = ?";
        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setString(1, legajo);
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println("Error al eliminar: " + ex.getMessage());
        }
    }

    // MÉTODO ACTUALIZAR (UPDATE)
    public void actualizar(Estudiante e) {
        String sql = "UPDATE estudiantes SET nombre = ?, carrera = ?, anio_ingreso = ? WHERE legajo = ?";
        try (Connection cn = ConexionDB.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCarrera());
            ps.setInt(3, e.getAnioIngreso());
            ps.setString(4, e.getLegajo());  // legajo --> clave para buscar cuál editar
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println("Error al actualizar: " + ex.getMessage());
        }
    }                                    
}

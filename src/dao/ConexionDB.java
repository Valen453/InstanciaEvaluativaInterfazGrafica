/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author baldo
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    // Datos de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/autogestion_db";
    private static final String USER = "root";
    private static final String PASS = "";

    // abre la conexión con la base de datos
    // devuelve la conexión al DAO que la pidió
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

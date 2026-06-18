/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instanciaevaluativa1interfaz;

import controlador.ControladorAcademico;
import vista.LoginFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación.
 * Crea el Controlador (que carga materias y estudiantes) y muestra la
 * pantalla de login para que el estudiante acceda a su perfil.
 *
 * @author Valen453
 */
public class Main {

    public static void main(String[] args) {
        // Look and feel del sistema operativo, si está disponible
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // si falla, se usa el look and feel por defecto de Swing
        }

        SwingUtilities.invokeLater(() -> {
            ControladorAcademico controlador = new ControladorAcademico();
            LoginFrame login = new LoginFrame(controlador);
            login.setVisible(true);
        });
    }
}

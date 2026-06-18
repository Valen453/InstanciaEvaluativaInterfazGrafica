/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControladorAcademico;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla inicial del sistema de autogestión.
 * El estudiante ingresa su legajo para acceder a su perfil, o se registra
 * si todavía no existe en la base de datos.
 *
 * Esta clase SÍ puede usar Swing y JOptionPane (es la Vista), pero todas
 * las validaciones y el acceso a datos quedan delegados al Controlador.
 *
 * @author Paloma
 */
public class LoginFrame extends JFrame {

    private final ControladorAcademico controlador;

    private JTextField txtLegajo;
    private JButton btnIngresar;
    private JButton btnRegistrarme;

    public LoginFrame(ControladorAcademico controlador) {
        this.controlador = controlador;
        initComponents();
    }

    private void initComponents() {
        setTitle("Autogestión Estudiantil - Acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Sistema de Autogestión Estudiantil", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 8, 8));
        panelForm.add(new JLabel("Ingresá tu legajo:"));
        txtLegajo = new JTextField();
        panelForm.add(txtLegajo);

        panelPrincipal.add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnIngresar = new JButton("Ingresar");
        btnRegistrarme = new JButton("Registrarme");
        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrarme);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        btnIngresar.addActionListener(e -> ingresar());
        btnRegistrarme.addActionListener(e -> abrirRegistro());

        // Permitir Enter en el campo de texto para ingresar directamente
        txtLegajo.addActionListener(e -> ingresar());
    }

    private void ingresar() {
        String legajo = txtLegajo.getText().trim();

        if (legajo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingresá tu legajo.",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean encontrado = controlador.iniciarSesion(legajo);

        if (!encontrado) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró ningún estudiante con el legajo: " + legajo
                    + "\nPodés registrarte con el botón 'Registrarme'.",
                    "Estudiante no encontrado",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        abrirVentanaPrincipal();
    }

    private void abrirRegistro() {
        JDialog dialogo = new JDialog(this, "Registro de Estudiante", true);
        dialogo.setSize(380, 280);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 8, 8));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField txtNombre = new JTextField();
        JTextField txtLegajoNuevo = new JTextField();
        JTextField txtCarrera = new JTextField();
        JTextField txtAnio = new JTextField();

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Legajo:"));
        panelForm.add(txtLegajoNuevo);
        panelForm.add(new JLabel("Carrera:"));
        panelForm.add(txtCarrera);
        panelForm.add(new JLabel("Año de ingreso:"));
        panelForm.add(txtAnio);

        dialogo.add(panelForm, BorderLayout.CENTER);

        JButton btnConfirmar = new JButton("Crear cuenta");
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.add(btnConfirmar);
        dialogo.add(panelSur, BorderLayout.SOUTH);

        btnConfirmar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String legajoNuevo = txtLegajoNuevo.getText().trim();
                String carrera = txtCarrera.getText().trim();
                int anio = Integer.parseInt(txtAnio.getText().trim());

                controlador.registrarEstudianteYLoguear(nombre, legajoNuevo, carrera, anio);

                JOptionPane.showMessageDialog(dialogo,
                        "¡Cuenta creada con éxito! Bienvenido/a " + nombre + ".",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

                dialogo.dispose();
                abrirVentanaPrincipal();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo,
                        "El año de ingreso debe ser un número entero.",
                        "Dato inválido",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialogo,
                        ex.getMessage(),
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        dialogo.setVisible(true);
    }

    private void abrirVentanaPrincipal() {
        VentanaPrincipal principal = new VentanaPrincipal(controlador);
        principal.setVisible(true);
        this.dispose();
    }
}
